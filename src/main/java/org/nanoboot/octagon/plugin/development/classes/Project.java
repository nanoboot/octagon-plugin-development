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
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.octagon.plugin.task.TaskResolution;
import org.nanoboot.octagon.plugin.task.TaskStatus;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Project implements Entity {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Project.class);

    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private String name;
    private UUID projectGroupId;
    private TaskStatus status;
    private TaskResolution resolution;
    private String key;
    private Integer sortkey;
    private String description;
    private Integer nextNumberPerProject;

    public void validate() {
        if (name == null) {
            String msg = "This product has missing at least one of the mandatory properties (name)" + this.toString();
            LOG.error(msg);
            throw new OctagonException(msg);
        }
        validateKey(key);
    }

    private void validateKey(String key) {
        if (key == null) {
            return;
        }
        int i = 0;
        boolean previousCharWasDash = false;
        for (char ch : key.toCharArray()) {
            boolean isUpperCase = Character.isLetter(ch) && Character.isUpperCase(ch);
            boolean isNumber = Character.isDigit(ch);
            boolean isDash = ch == '-';
            boolean keyIsValid = isUpperCase || isNumber || isDash;
            if (!keyIsValid) {
                String msg
                        = "Product key " + key
                        + " does contain an invalid character " + ch
                        + " at position " + i
                        + ", which is not one of the allowed: uppercase letters, numbers or dashes.";
                LOG.error(msg);
                throw new OctagonException(msg);
            }
            if (isDash && previousCharWasDash) {
                String msg = "Product key " + key
                        + " does contain an invalid character " + ch
                        + " at position " + i
                        + ". Two dashes follows consecutively.";
                LOG.error(msg);
                throw new OctagonException(msg);
            }
            previousCharWasDash = isDash;
            i++;
        }

        if (key.startsWith("-")) {
            String msg = "Product key " + key
                    + " must not start with -";
            LOG.error(msg);
            throw new OctagonException(msg);
        }
        if (key.endsWith("-")) {
            String msg = "Product key " + key
                    + " must not end with -";
            LOG.error(msg);
            throw new OctagonException(msg);
        }

        if (Character.isDigit(key.charAt(0))) {
            String msg = "Product key " + key
                    + " must not start with a number";
            LOG.error(msg);
            throw new OctagonException(msg);
        }

    }

    @Override
    public void validateCreate() {
        if (this.getNextNumberPerProject() == null || this.getNextNumberPerProject() != 1) {
            throw new OctagonException("Next number per project is " + nextNumberPerProject + ", but initial value must be 1.");
        }
    }

    @Override
    public void validateUpdate(Entity updatedEntity) {
        Project oldProject = this;
        Project updatedProject = (Project) updatedEntity;
        String oldKey = oldProject.getKey();
        String newKey = updatedProject.getKey();
        boolean oldKeyIsValid = true;
        try {
            validateKey(oldKey);
        } catch (OctagonException e) {
            oldKeyIsValid = false;
        }
        if (oldKey != null && newKey != null) {
            if (!oldKey.equals(newKey) && oldKeyIsValid) {
                String msg = "Cannot change (valid) key from " + oldKey + " to " + newKey + ". It is readonly.";

                LOG.error(msg);
                throw new OctagonException(msg);
            }
        }
        if (oldKey != null && newKey == null) {
            String msg = "Cannot change key to null.";

            LOG.error(msg);
            throw new OctagonException(msg);
        }
        Integer oldNextNumberPerProject = oldProject.getNextNumberPerProject();
        Integer newNextNumberPerProject = updatedProject.getNextNumberPerProject();
        if(newNextNumberPerProject == null) {
            final String msg = "Cannot change next number per project to null.";
            LOG.error(msg);
            throw new OctagonException(msg);
        }
        Boolean nextNumberPerProjectWasChanged = oldNextNumberPerProject.intValue() != newNextNumberPerProject.intValue();
        Integer expectedNewNextNumberPerProject = oldNextNumberPerProject + 1;
        if(nextNumberPerProjectWasChanged && newNextNumberPerProject.intValue() != expectedNewNextNumberPerProject.intValue()) {
            String msg = "Next number per project was changed (old value=" + oldNextNumberPerProject+") to " + newNextNumberPerProject + ", but " + expectedNewNextNumberPerProject + " was expected (+ 1).";
            LOG.error(msg);
            throw new OctagonException(msg);
        }
    }

    public void loadFromMap(Map<String, String> map) {
        setName(getStringParam("name", map));
        setProjectGroupId(getUuidParam("projectGroupId", map));

        String statusParam = getStringParam("status", map);
        setStatus(statusParam == null ? null : TaskStatus.valueOf(statusParam));

        String resolutionParam = getStringParam("resolution", map);
        setResolution(resolutionParam == null ? null : TaskResolution.valueOf(resolutionParam));

        setKey(getStringParam("key", map));
        setSortkey(getIntegerParam("sortkey", map));
        setDescription(getStringParam("description", map));
        setNextNumberPerProject(getIntegerParam("nextNumberPerProject", map));
    }

    @Override
    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
            id == null ? "" : id.toString(),
            name == null ? "" : name,
            projectGroupId == null ? "" : projectGroupId.toString(),
            status == null ? "" : status.name(),
            resolution == null ? "" : resolution.name(),
            key == null ? "" : key,
            sortkey == null ? "" : sortkey.toString(),
            description == null ? "" : description,
            nextNumberPerProject == null ? "" : nextNumberPerProject.toString(),};
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("projectGroupId", "projectGroup", "getProjectGroups"));

            SCHEMA.add(new EntityAttribute("status", Arrays.asList(TaskStatus.values()).stream().map(TaskStatus::name).collect(Collectors.toList())).withDefaultValue(TaskStatus.UNCONFIRMED.name()).withMandatory(true));
            SCHEMA.add(new EntityAttribute("resolution", Arrays.asList(TaskResolution.values()).stream().map(TaskResolution::name).collect(Collectors.toList())));

            SCHEMA.add(new EntityAttribute("key", EntityAttributeType.TEXT));
            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER));
            SCHEMA.add(new EntityAttribute("description"));
            SCHEMA.add(new EntityAttribute("nextNumberPerProject", EntityAttributeType.INTEGER).withDefaultValue("1"));
        }
        return SCHEMA;
    }

    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{};
    }

    public String[] getRelatedActionsForEntity() {
        return new String[]{};
    }
}
