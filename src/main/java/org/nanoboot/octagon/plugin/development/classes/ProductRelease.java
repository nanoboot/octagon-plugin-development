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
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.time.moment.LocalDate;
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
public class ProductRelease implements Entity {
    /**
     * Static schema.
     */
    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    protected UUID id;
    /**
     * Product id.
     */
    protected UUID productId;
    /**
     * Product version id.
     */
    protected UUID productVersionId;
    /**
     * Release date in format yyyy-mm-dd.
     */
    protected LocalDate releaseDate;
    /**
     * Git standardization rate.
     */
    protected Integer gitStandardization;
    /**
     * SonarQube rate.
     */
    protected Integer sonarQube;
    /**
     * Test coverate rate.
     */
    protected Integer testCoverage;
    /**
     * Checkstyle rate.
     */
    protected Integer checkStyle;
    @Override
    public final String getName() {
        return id == null ? null : id.toString();
    }

    @Override
    public void validate() {

    }

    @Override
    public final void loadFromMap(final Map<String, String> map) {
        setProductId(getUuidParam("productId", map));
        setProductVersionId(getUuidParam("productVersionId", map));
        setReleaseDate(getLocalDateParam("releaseDate", map));

        setGitStandardization(getIntegerParam("gitStandardization", map));
        setSonarQube(getIntegerParam("sonarQube", map));
        setTestCoverage(getIntegerParam("testCoverage", map));
        setCheckStyle(getIntegerParam("checkStyle", map));

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
                productVersionId == null ? "" : productVersionId.toString(),
                releaseDate == null ? "" : releaseDate.toString(),

                gitStandardization == null ? "" : gitStandardization.toString(),
                sonarQube == null ? "" : sonarQube.toString(),
                testCoverage == null ? "" : testCoverage.toString(),
                checkStyle == null ? "" : checkStyle.toString(),
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {

            SCHEMA = new ArrayList<>();
            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("productId", "product", "getProducts").withPreselectionProperty(true).withReadonly(true));
            SCHEMA.add(new EntityAttribute("productVersionId", "productVersion", "getVersionsForProduct", "productId").withMandatory(true));
            SCHEMA.add(new EntityAttribute("releaseDate", EntityAttributeType.LOCAL_DATE).withMandatory(true));

            SCHEMA.add(new EntityAttribute("gitStandardization", EntityAttributeType.INTEGER).withMandatory(true));
            SCHEMA.add(new EntityAttribute("sonarQube", EntityAttributeType.INTEGER).withCustomHumanName("Sonar Qube").withMandatory(true));
            SCHEMA.add(new EntityAttribute("testCoverage", EntityAttributeType.INTEGER).withMandatory(true));
            SCHEMA.add(new EntityAttribute("checkStyle", EntityAttributeType.INTEGER).withCustomHumanName("CheckStyle").withMandatory(true));
        }
        return SCHEMA;
    }
    @Override
    public final void setPreselectionProperty(final String value) {
        this.setProductId(value != null ? UUID.fromString(value.trim()) : null);
    }
}
