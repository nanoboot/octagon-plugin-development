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


import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.octagon.plugin.task.TaskResolution;
import org.nanoboot.octagon.plugin.task.TaskStatus;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class ProductGroup implements Entity {
    private static List<EntityAttribute> SCHEMA;

    private UUID id;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskResolution resolution;
    private Integer sortkey;

    public void validate() {

    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setName(getStringParam("name", map));
        setDescription(getStringParam("description", map));

        String statusParam = getStringParam("status", map);
        setStatus(statusParam == null ? null : TaskStatus.valueOf(statusParam));
        String resolutionParam = getStringParam("resolution", map);
        setResolution(resolutionParam == null ? null : TaskResolution.valueOf(resolutionParam));

        setSortkey(getIntegerParam("sortkey", map));

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
                description == null ? "" : description,
                status == null ? "" : status.name(),
                resolution == null ? "" : resolution.name(),
                sortkey == null ? "" : sortkey.toString(),
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();
            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("description"));
            SCHEMA.add(new EntityAttribute("status", Arrays.asList(TaskStatus.values()).stream().map(TaskStatus::name).collect(Collectors.toList())).withDefaultValue(TaskStatus.UNCONFIRMED.name()).withMandatory(true));
            SCHEMA.add(new EntityAttribute("resolution", Arrays.asList(TaskResolution.values()).stream().map(TaskResolution::name).collect(Collectors.toList())));
            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER));
        }
        return SCHEMA;
    }

    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{"getProductsForProductGroup"};
    }

    public String[] getRelatedActionsForEntity() {
        return new String[]{
                "Add product:create?className=Product&productGroupId=",
                "List products:list?className=Product&filter_productGroupId=",
        };
    }
}
