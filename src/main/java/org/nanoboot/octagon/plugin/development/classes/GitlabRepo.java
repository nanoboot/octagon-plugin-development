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
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class GitlabRepo implements Entity {

    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;
    /**
     * Product id.
     */
    private UUID productId;
    /**
     * Name.
     */
    private String name;
    /**
     * Url.
     */
    private String url;
    /**
     * Type.
     */
    private GitlabRepoType type;
    /**
     * Status.
     */
    private GitlabRepoStatus status;
    /**
     * Git standardization.
     */
    private Boolean gitStandardization;
    /**
     * Does doc repo exist.
     */
    private Boolean doesDocRepoExist;

    @Override
    public void validate() {

    }

    @Override
    public final void loadFromMap(final Map<String, String> map) {
        setProductId(getUuidParam("productId", map));
        setName(getStringParam("name", map));
        setUrl(getStringParam("url", map));

        String typeParam = getStringParam("type", map);
        setType(typeParam == null ? null : GitlabRepoType.valueOf(typeParam));
        String statusParam = getStringParam("status", map);
        setStatus(statusParam == null ? null : GitlabRepoStatus.valueOf(statusParam));

        setGitStandardization(getBooleanParam("gitStandardization", map));
        setDoesDocRepoExist(getBooleanParam("doesDocRepoExist", map));
    }

    @Override
    public final Class getEntityClass() {
        return getClass();
    }

    @Override
    public final String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),
                productId == null ? "" : productId.toString(),
                name == null ? "" : name,
                url == null ? "" : url,

                type == null ? "" : type.name(),
                status == null ? "" : status.name(),

                gitStandardization == null ? "" : convertBooleanToString(gitStandardization),
                doesDocRepoExist == null ? "" : convertBooleanToString(doesDocRepoExist),
        };
    }

    @Override
    public final List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("productId", "product", "getProducts").withReadonly(true));
            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("url", EntityAttributeType.LINK).withMandatory(true));

            List<String> typeList = Arrays.stream(GitlabRepoType.values())
                    .map(GitlabRepoType::name).collect(Collectors.toList());
            SCHEMA.add(new EntityAttribute("type", typeList));
            List<String> statusList = Arrays.stream(GitlabRepoStatus.values())
                    .map(GitlabRepoStatus::name).collect(Collectors.toList());
            SCHEMA.add(new EntityAttribute("status", statusList));
            SCHEMA.add(new EntityAttribute("gitStandardization", EntityAttributeType.BOOLEAN));
            SCHEMA.add(new EntityAttribute("doesDocRepoExist", EntityAttributeType.BOOLEAN));
        }
        return SCHEMA;
    }
}
