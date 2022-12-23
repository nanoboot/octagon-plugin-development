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

import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class ProductMilestone extends AbstractProductComponentOrModule {
    private static List<EntityAttribute> SCHEMA;
    private Boolean retired;
    @Override
    public void loadFromMap(Map<String, String> map) {
        super.loadFromMap(map);
        setRetired(getBooleanParam("retired", map));

    }
    @Override
    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),
                productId == null ? "" : productId.toString(),
                name == null ? "" : name,
                description == null ? "" : description,
                retired == null ? "" : convertBooleanToString(retired),
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = EntityAttribute.copy(super.createAbstractSchema());
            SCHEMA.add(new EntityAttribute("retired", EntityAttributeType.BOOLEAN));
        }
        return SCHEMA;
    }
}
