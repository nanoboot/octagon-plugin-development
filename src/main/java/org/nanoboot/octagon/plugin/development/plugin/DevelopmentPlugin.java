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

package org.nanoboot.octagon.plugin.development.plugin;

import java.util.Properties;
import org.nanoboot.octagon.plugin.api.core.Plugin;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.core.PluginStubImpl;
import org.nanoboot.octagon.plugin.development.classes.*;
import org.nanoboot.octagon.plugin.development.persistence.impl.mappers.*;
import org.nanoboot.octagon.plugin.development.persistence.impl.repos.*;
import org.nanoboot.octagon.plugin.development.persistence.impl.typehandlers.GitlabRepoStatusTypeHandler;
import org.nanoboot.octagon.plugin.development.persistence.impl.typehandlers.GitlabRepoTypeTypeHandler;
import lombok.Getter;
import org.nanoboot.octagon.plugin.development.batches.AutofillNumberPerProjectAndAliasBatch;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class DevelopmentPlugin implements Plugin {
    
    private static final String DEVELOPMENT = "development";
    @Getter
    private PluginStub pluginStub = new PluginStubImpl();
    
    @Override
    public String getGroup() {
        return DEVELOPMENT;
    }
    
    @Override
    public String getId() {
        return DEVELOPMENT;
    }
    
    @Override
    public String getVersion() {
        return "0.0.0";
    }
    
    @Override
    public String init(Properties propertiesConfiguration) {
        for (Object objectKey : propertiesConfiguration.keySet()) {
            String key = (String) objectKey;
            String value = propertiesConfiguration.getProperty(key);
            System.out.println("Found configuration entry for plugin development: " + key + "=" + value);
        }
        pluginStub.registerEntityGroup(DEVELOPMENT, 20);
        
        int sortkeyInGroup = 10;
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Server.class,
                        ServerMapper.class,
                        ServerRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductGroup.class,
                        ProductGroupMapper.class,
                        ProductGroupRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Product.class,
                        ProductMapper.class,
                        ProductRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductModule.class,
                        ProductModuleMapper.class,
                        ProductModuleRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductComponent.class,
                        ProductComponentMapper.class,
                        ProductComponentRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductMilestone.class,
                        ProductMilestoneMapper.class,
                        ProductMilestoneRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductRelease.class,
                        ProductReleaseMapper.class,
                        ProductReleaseRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProductVersion.class,
                        ProductVersionMapper.class,
                        ProductVersionRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        GitlabRepo.class,
                        GitlabRepoMapper.class,
                        GitlabRepoRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Epic.class,
                        EpicMapper.class,
                        EpicRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Story.class,
                        StoryMapper.class,
                        StoryRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        DevTask.class,
                        DevTaskMapper.class,
                        DevTaskRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        DevSubTask.class,
                        DevSubTaskMapper.class,
                        DevSubTaskRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);

////
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Proposal.class,
                        ProposalMapper.class,
                        ProposalRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        NewFeature.class,
                        NewFeatureMapper.class,
                        NewFeatureRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Enhancement.class,
                        EnhancementMapper.class,
                        EnhancementRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Bug.class,
                        BugMapper.class,
                        BugRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Incident.class,
                        IncidentMapper.class,
                        IncidentRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Problem.class,
                        ProblemMapper.class,
                        ProblemRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++, true);
        
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        ProjectGroup.class,
                        ProjectGroupMapper.class,
                        ProjectGroupRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        pluginStub
                .registerEntity(
                        DEVELOPMENT,
                        Project.class,
                        ProjectMapper.class,
                        ProjectRepositoryImplSQLiteMyBatis.class, sortkeyInGroup++);
        
        pluginStub.registerTypeHandler(GitlabRepoStatusTypeHandler.class);
        pluginStub.registerTypeHandler(GitlabRepoTypeTypeHandler.class);
        //
        if (propertiesConfiguration.getProperty("octagon.plugins.development.batches.autofill-number-and-alias.enabled", "false").equals("true")) {
            pluginStub.registerBatch(AutofillNumberPerProjectAndAliasBatch.class.getName());
        }
        return null;
    }
    
    @Override
    public String getDependsOn() {
        return "task";
    }
    
    @Override
    public boolean hasMigrationSchema() {
        return true;
    }
}
