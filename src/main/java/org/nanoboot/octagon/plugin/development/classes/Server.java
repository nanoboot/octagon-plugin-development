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
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Server implements Entity {
    private static List<EntityAttribute> SCHEMA;

    private UUID id;
    private String name;
    private String ports;
    private String category;
    private String note;

    public Integer[] getPortsAsArray() {
        if (ports == null) {
            return null;
        }
        String[] stringArray = ports.split(",");
        Integer[] portsAsArray = new Integer[stringArray.length];
        int i = 0;
        for (String s : stringArray) {
            portsAsArray[i] = Integer.valueOf(s.trim());
            i++;
        }
        return portsAsArray;
    }

    public void validate() {
        getPortsAsArray();
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setName(getStringParam("name", map));
        setPorts(getStringParam("ports", map));
        setCategory(getStringParam("category", map));
        setNote(getStringParam("note", map));
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
                ports == null ? "" : ports,
                category == null ? "" : category,
                note == null ? "" : note,
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if(SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("ports").withMandatory(true));
            SCHEMA.add(new EntityAttribute("category"));
            SCHEMA.add(new EntityAttribute("note"));
        }
        return SCHEMA;
    }

}
