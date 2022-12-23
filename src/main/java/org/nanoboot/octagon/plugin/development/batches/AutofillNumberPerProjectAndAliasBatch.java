///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon Plugin Development: Development plugin for Octagon application.
// Copyright (C) 2021-2022 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////

package org.nanoboot.octagon.plugin.development.batches;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.nanoboot.octagon.core.utils.RegistryImpl;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.actionlog.classes.ActionLog;
import org.nanoboot.octagon.plugin.api.core.BatchBase;
import org.nanoboot.octagon.plugin.development.classes.Product;
import org.nanoboot.octagon.plugin.development.classes.*;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class AutofillNumberPerProjectAndAliasBatch extends BatchBase {

    private RegistryImpl<Repository> repositoryRegistry;
    private ActionLogRepository actionLogRepository;

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AutofillNumberPerProjectAndAliasBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.repositoryRegistry = (RegistryImpl<Repository>) getObjectDependency(RepositoryRegistry.class);
        this.actionLogRepository = (ActionLogRepository) getObjectDependency(ActionLogRepository.class);
        Repository<Product> productRepository = repositoryRegistry.find(Product.class.getSimpleName());
        Repository<Project> projectRepository = repositoryRegistry.find(Project.class.getSimpleName());
        //
        Repository<Epic> epicRepository = repositoryRegistry.find(Epic.class.getSimpleName());
        Repository<Story> storyRepository = repositoryRegistry.find(Story.class.getSimpleName());
        Repository<DevTask> devTaskRepository = repositoryRegistry.find(DevTask.class.getSimpleName());
        Repository<DevSubTask> devSubTaskRepository = repositoryRegistry.find(DevSubTask.class.getSimpleName());
        Repository<Bug> bugRepository = repositoryRegistry.find(Bug.class.getSimpleName());
        //
        Repository<Enhancement> enhancementRepository = repositoryRegistry.find(Enhancement.class.getSimpleName());
        Repository<NewFeature> newFeatureRepository = repositoryRegistry.find(NewFeature.class.getSimpleName());
        Repository<Proposal> proposalRepository = repositoryRegistry.find(Proposal.class.getSimpleName());
        Repository<Problem> problemRepository = repositoryRegistry.find(Problem.class.getSimpleName());
        Repository<Incident> incidentRepository = repositoryRegistry.find(Incident.class.getSimpleName());

        List<Repository<? extends AbstractDevelopmentTask>> ticketRepositoryRegistries = new ArrayList<>();

        ticketRepositoryRegistries.add(epicRepository);
        ticketRepositoryRegistries.add(storyRepository);
        ticketRepositoryRegistries.add(devTaskRepository);
        ticketRepositoryRegistries.add(devSubTaskRepository);
        ticketRepositoryRegistries.add(bugRepository);
        //
        ticketRepositoryRegistries.add(enhancementRepository);
        ticketRepositoryRegistries.add(newFeatureRepository);
        ticketRepositoryRegistries.add(proposalRepository);
        ticketRepositoryRegistries.add(problemRepository);
        ticketRepositoryRegistries.add(incidentRepository);
        //
        @Data
        @AllArgsConstructor
        class Ticket implements Comparable<Ticket> {

            private Class clazz;
            private AbstractDevelopmentTask task;
            private UniversalDateTime createdOn;

            @Override
            public String toString() {
                return "Ticket(clazz=" + clazz.getSimpleName() + " task=" + task.getName() + " createdOn=" + createdOn.toLong();
            }

            @Override
            public int compareTo(Ticket o) {
                Long thisCreatedOn = this.createdOn.toLong();
                Long oCreatedOn = o.createdOn.toLong();
                return thisCreatedOn.compareTo(oCreatedOn);
            }
        }
        Map<String, ActionLog> actionLogsMap = new HashMap<>();
        for (ActionLog al : actionLogRepository.list(" ACTION_TYPE = 'CREATE' ")) {
            actionLogsMap.put(al.getObjectId().toString(), al);
        }
        List<Ticket> tickets = new ArrayList<>();

        for (Repository<? extends AbstractDevelopmentTask> repo : ticketRepositoryRegistries) {
            List<AbstractDevelopmentTask> tasks = (List<AbstractDevelopmentTask>) repo.list();
            for (AbstractDevelopmentTask t : tasks) {
                if (t.getProjectId() != null) {
                    continue;
                }
                Ticket ticket = new Ticket(repo.getClassOfType(), t, findCreateDate(t.getId().toString(), actionLogsMap));
                System.out.println("Found ticket: " + ticket.toString());
                tickets.add(ticket);
            }
        }
        Collections.sort(tickets);
        Map<String, List<Ticket>> mapOfTicketsPerProjectId = new HashMap<>();
        for (Ticket t : tickets) {
            String productId = t.task.getProductId().toString();
            Product product = productRepository.read(productId);
            if (product.getDefaultProjectId() == null) {
                System.err.println("Ticket belongs to product, which has set no default project id (skipping this ticket): " + product.getName());
                continue;
            }
            String projectId = product.getDefaultProjectId().toString();
            String key = projectId;
            if (!mapOfTicketsPerProjectId.containsKey(key)) {
                mapOfTicketsPerProjectId.put(key, new ArrayList<Ticket>());
            }
            List<Ticket> list = mapOfTicketsPerProjectId.get(key);
            list.add(t);
        }

        for (String key : mapOfTicketsPerProjectId.keySet()) {
            Project project = projectRepository.read(key);
            System.out.println("AutofillNumberPerProjectAndAliasBatch : checking project " + project.getName());
            List<Ticket> ticketsPerProject = mapOfTicketsPerProjectId.get(key);
            Collections.sort(ticketsPerProject);
            String projectKey = project.getKey();
            for (Ticket ttt : ticketsPerProject) {
                AbstractDevelopmentTask adt = ttt.getTask();

                System.out.println("Assigning alias to ticket: " + ttt.toString());

                adt.setProjectId(project.getId());
                Repository taskRepo = repositoryRegistry.find(ttt.getClazz().getSimpleName());
                AbstractDevelopmentTask adtBefore = (AbstractDevelopmentTask) taskRepo.read(adt.getId().toString());
                taskRepo.updateWithoutValidatioon(adt);

                actionLogRepository.persistActionLog(ActionType.UPDATE, adtBefore, adt, adt, null);
            }
        }

    }

    private UniversalDateTime findCreateDate(String objectId, Map<String, ActionLog> actionLogs) {

        return actionLogs.get(objectId).getUniversalDateTime();

    }

}
