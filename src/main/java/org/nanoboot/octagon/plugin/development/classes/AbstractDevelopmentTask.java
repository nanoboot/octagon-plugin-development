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

package org.nanoboot.octagon.plugin.development.classes;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.octagon.plugin.task.AbstractTask;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.actionlog.classes.ActionLog;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data()
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public sealed abstract class AbstractDevelopmentTask extends AbstractTask permits Epic, Story, DevTask, DevSubTask, Bug, Enhancement, Incident, NewFeature, Problem, Proposal, SimpleTask {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(AbstractDevelopmentTask.class);
    private UUID projectId;
    private Integer numberPerProject;
    private String alias;
    private UUID productId;
    private UUID componentId;
    private UUID moduleId;
    private UUID versionId;
    private UUID targetMilestoneId;
    //Year, when the work on this bug is expected.
    private Integer budgetYear;

    @Override
    public void validate() {
        
        super.validate();
        if (budgetYear != null && budgetYear < 2020) {
            String msg = "Budget year is wrong: " + budgetYear + this.toString();
            throw new OctagonException(msg);
        }
        if (projectId == null) {
            throw new OctagonException("Project is null.");
        }
//        if (numberPerProject == null) {
//            throw new OctagonException("Number per project is null.");
//        }
        if (numberPerProject != null && numberPerProject <= 0) {
            throw new OctagonException("Number per project must be more than zero, but it is " + numberPerProject + ".");
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        super.loadFromMap(map);

        setProjectId(getUuidParam("projectId", map));
        setNumberPerProject(getIntegerParam("numberPerProject", map));
        setAlias(getStringParam("alias", map));
        setProductId(getUuidParam("productId", map));
        setComponentId(getUuidParam("componentId", map));
        setModuleId(getUuidParam("moduleId", map));
        setVersionId(getUuidParam("versionId", map));
        setTargetMilestoneId(getUuidParam("targetMilestoneId", map));
        setBudgetYear(getIntegerParam("budgetYear", map));
    }

    @Override
    public String[] toStringArray() {
        List<String> list = new ArrayList<>();
        for (String s : super.toStringArray()) {
            list.add(s);
        }
        list.add(projectId == null ? "" : projectId.toString());
        list.add(numberPerProject == null ? "" : numberPerProject.toString());
        list.add(alias == null ? "" : alias);
        list.add(productId == null ? "" : productId.toString());
        list.add(componentId == null ? "" : componentId.toString());
        list.add(moduleId == null ? "" : moduleId.toString());
        list.add(versionId == null ? "" : versionId.toString());
        list.add(targetMilestoneId == null ? "" : targetMilestoneId.toString());
        list.add(budgetYear == null ? "" : budgetYear.toString());
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    protected List<EntityAttribute> createAbstractSchema() {
        List<EntityAttribute> SCHEMA = EntityAttribute.copy(super.createAbstractSchema());
        SCHEMA.add(new EntityAttribute("projectId", "project", "getProjects").withReadonly(true).withMandatory(true));
        SCHEMA.add(new EntityAttribute("numberPerProject", EntityAttributeType.INTEGER).withReadonly(true).withAutofill(true));
        SCHEMA.add(new EntityAttribute("alias", EntityAttributeType.TEXT).withReadonly(true).withAutofill(true));
        //
        SCHEMA.add(new EntityAttribute("productId", "product", "getProducts").withReadonly(true).withPreselectionProperty(true));
        SCHEMA.add(new EntityAttribute("componentId", "productComponent", "getProductComponentsForProduct", "productId"));
        SCHEMA.add(new EntityAttribute("moduleId", "productModule", "getProductModulesForProduct", "productId"));

        SCHEMA.add(new EntityAttribute("versionId", "productVersion", "getVersionsForProduct", "productId"));

        SCHEMA.add(new EntityAttribute("targetMilestoneId", "productMilestone", "getProductMilestonesForProduct", "productId"));
        SCHEMA.add(new EntityAttribute("budgetYear", EntityAttributeType.INTEGER));

        return SCHEMA;
    }

    @Override
    public void autofillIfNeeded(Entity entity, RepositoryRegistry repositoryRegistry) {
        AbstractDevelopmentTask adt = (AbstractDevelopmentTask) entity;
        if (adt.getNumberPerProject() == null) {
//            Repository<Product> productRepository = repositoryRegistry.find(Product.class.getSimpleName());
//            Repository<Project> projectRepository = repositoryRegistry.find(Project.class.getSimpleName());
//            Product product = productRepository.read(adt.getProductId().toString());
//            UUID defaultProjectId = product.getDefaultProjectId();
//            if (defaultProjectId == null) {
//                String msg = "Cannot autofill number per project for task " + adt.getName() + ", because defaultProjectId is null.";
//                LOG.error(msg);
//                throw new OctagonException(msg);
//            }
//            Project project = projectRepository.read(defaultProjectId.toString());
//
//            Integer nextNumberPerProject = project.getNextNumberPerProject();
//            project.setNextNumberPerProject(nextNumberPerProject + 1);
//            projectRepository.update(project);
            //
            Repository<Project> projectRepository = repositoryRegistry.find(Project.class.getSimpleName());
            Repository<ActionLog> actionLogRepository = repositoryRegistry.find(ActionLog.class.getSimpleName());
            Project projectBefore = projectRepository.read(adt.getProjectId().toString());
            final String projectKey = projectBefore.getKey();
            if (projectKey == null) {
                String msg = "Cannot autofill number per project for project " + projectBefore.getName() + ", because its key is not set.";
                LOG.error(msg);
                throw new OctagonException(msg);
            }
            if (adt.getAlias() != null) {
                String msg = "Cannot autofill alias for task " + adt.getName() + ", because it is already set.";
                LOG.error(msg);
                throw new OctagonException(msg);
            }
            Project project = projectRepository.read(adt.getProjectId().toString());
            Integer nextNumberPerProject = project.getNextNumberPerProject();
            project.setNextNumberPerProject(nextNumberPerProject + 1);
            projectRepository.update(project);
            ((ActionLogRepository) actionLogRepository).persistActionLog(ActionType.UPDATE, projectBefore, project, project, null);
            adt.setNumberPerProject(nextNumberPerProject);
            adt.setAlias(projectKey + '-' + nextNumberPerProject);
        }
    }

    public void setAutofillPropertiesToNull() {
        this.numberPerProject = null;
        this.alias = null;
    }

    abstract String getForeignKeyNameForParent();

    public void setPreselectionProperty(String value) {
        this.setProductId(value != null ? UUID.fromString(value.trim()) : null);
    }
    
    
    
    
//    activity_type,
//    study,
//    specification,
//    analysis,
//    development,
//    testing,
//    deployment,
//    misc

}

