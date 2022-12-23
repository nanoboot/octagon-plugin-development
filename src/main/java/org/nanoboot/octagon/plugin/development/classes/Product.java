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

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Product implements Entity {
    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;
    private String productNumber;
    private String applicationSymbolicIdentifier;
    private String productCode;
    private String name;
    private UUID productGroupId;
    private TaskStatus status;
    private TaskResolution resolution;
    private Integer sortkey;
    private String description;
    private UUID defaultMilestoneId;
    private UUID defaultProjectId;

    public void validate() {
        if (productNumber == null || applicationSymbolicIdentifier == null || productCode == null || name == null) {
            String msg = "This product has missing at least one of the mandatory properties (number or ASI or code or name)" + this.toString();
            throw new OctagonException(msg);
        }
        boolean numberPartOfProductNumberIsOk = true;
        try {
            Integer.valueOf(productNumber.substring(3, 8));
        } catch (Exception e) {
            numberPartOfProductNumberIsOk = false;
        }
        if (productNumber.length() != 8 || !productNumber.startsWith("pn-") || !numberPartOfProductNumberIsOk) {
            String debug = "productNumber.length()=" + productNumber.length();
            debug = debug + " productNumber.startsWith(\"pn-\") =" + productNumber.startsWith("pn-");
            debug = debug + " !numberPartOfProductNumberIsOk= " + (!numberPartOfProductNumberIsOk ? "true" : "false");
            throw new OctagonException("This product has not valid product number: " + productNumber + debug);
        }
        if (
                applicationSymbolicIdentifier.length() != 3 ||
                        !Character.isUpperCase(applicationSymbolicIdentifier.charAt(0)) ||
                        !Character.isUpperCase(applicationSymbolicIdentifier.charAt(1)) ||
                        !Character.isUpperCase(applicationSymbolicIdentifier.charAt(2))
        ) {
            throw new OctagonException("This product has not valid application symbolic identifier (ASI) : " + applicationSymbolicIdentifier);
        }


        if (productCode.startsWith("-") || productCode.endsWith("-")) {
            throw new OctagonException("This product has not valid product code: " + productCode);
        }
        String[] productCodeParts = productCode.split("-");
        for (String part : productCodeParts) {
            if (part.isBlank()) {
                String msg = "This product has not valid product code: " + productCode + " The reason is, that one part: \"" + part + "\" is empty";
                throw new OctagonException(msg);
            }
            if (!Character.isLetter(part.charAt(0))) {
                String msg = "This product has not valid product code: " + productCode + " The reason is part: " + part + ", which has to have first character letter, but this rule is not followed";
                throw new OctagonException(msg);
            }
            for (char ch : part.toCharArray()) {
                if (Character.isDigit(ch)) {
                    continue;
                }
                if (Character.isLetter(ch) && !Character.isLowerCase(ch)) {
                    String msg = "This product has not valid product code: " + productCode + " The reason is part: " + part + " and character " + ch;
                    throw new OctagonException(msg);
                }
            }
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setProductNumber(getStringParam("productNumber", map));
        setApplicationSymbolicIdentifier(getStringParam("applicationSymbolicIdentifier", map));
        setProductCode(getStringParam("productCode", map));
        setName(getStringParam("name", map));
        setProductGroupId(getUuidParam("productGroupId", map));
        String statusParam = getStringParam("status", map);
        setStatus(statusParam == null ? null : TaskStatus.valueOf(statusParam));
        String resolutionParam = getStringParam("resolution", map);
        setResolution(resolutionParam == null ? null : TaskResolution.valueOf(resolutionParam));
        setSortkey(getIntegerParam("sortkey", map));
        setDescription(getStringParam("description", map));
        setDefaultMilestoneId(getUuidParam("defaultMilestoneId", map));
        setDefaultProjectId(getUuidParam("defaultProjectId", map));
    }

    @Override
    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),
                productNumber == null ? "" : productNumber,
                applicationSymbolicIdentifier == null ? "" : applicationSymbolicIdentifier,
                productCode == null ? "" : productCode,
                name == null ? "" : name,
                productGroupId == null ? "" : productGroupId.toString(),
                status == null ? "" : status.name(),
                resolution == null ? "" : resolution.name(),
                sortkey == null ? "" : sortkey.toString(),
                description == null ? "" : description,
                defaultMilestoneId == null ? "" : defaultMilestoneId.toString(),
                defaultProjectId == null ? "" : defaultProjectId.toString(),
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("productNumber").withDefaultValue("pn-00???").withMandatory(true));
            SCHEMA.add(new EntityAttribute("applicationSymbolicIdentifier").withMandatory(true));
            SCHEMA.add(new EntityAttribute("productCode").withMandatory(true));
            SCHEMA.add(new EntityAttribute("name").withMandatory(true));
            SCHEMA.add(new EntityAttribute("productGroupId", "productGroup", "getProductGroups"));
            SCHEMA.add(new EntityAttribute("status", Arrays.asList(TaskStatus.values()).stream().map(TaskStatus::name).collect(Collectors.toList())).withDefaultValue(TaskStatus.UNCONFIRMED.name()).withMandatory(true));
            SCHEMA.add(new EntityAttribute("resolution", Arrays.asList(TaskResolution.values()).stream().map(TaskResolution::name).collect(Collectors.toList())));
            SCHEMA.add(new EntityAttribute("sortkey", EntityAttributeType.INTEGER));
            SCHEMA.add(new EntityAttribute("description"));
            SCHEMA.add(new EntityAttribute("defaultMilestoneId", "productMilestone", "getProductMilestonesForProduct", "id"));
            SCHEMA.add(new EntityAttribute("defaultProjectId", "project", "getProjects"));
}
        return SCHEMA;
    }

    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{
                "getEpicsForProduct",
                "getProductModulesForProduct",
                "getProductComponentsForProduct",
                "getProductMilestonesForProduct",
                "getProductReleasesForProduct",
                "getProductVersionsForProduct",
                "getGitlabReposForProduct",
                "getProposalsForProduct",
                "getNewFeaturesForProduct",
                "getEnhancementsForProduct",
                "getBugsForProduct",
                "getIncidentsForProduct",
                "getProblemsForProduct",
        };
    }

    public String[] getRelatedActionsForEntity() {
        return new String[]{
                "Add module:create?className=ProductModule&productId=",
                "Add component:create?className=ProductComponent&productId=",
                "Add milestone:create?className=ProductMilestone&productId=",
                "Add version:create?className=ProductVersion&productId=",
                "Add release:create?className=ProductRelease&productId=",
                "Add Gitlab repo:create?className=GitlabRepo&productId=",
                "Add epic:create?className=Epic&productId=",
                "Add proposal:create?className=Proposal&productId=",
                "Add new feature:create?className=NewFeature&productId=",
                "Add enhancement:create?className=Enhancement&productId=",
                "Add bug:create?className=Bug&productId=",
                "Add incident:create?className=Incident&productId=",
                "Add problem:create?className=Problem&productId=",
                "List modules:list?className=ProductModule&filter_productId=",
                "List components:list?className=ProductComponent&filter_productId=",
                "List milestones:list?className=ProductMilestone&filter_productId=",
                "List versions:list?className=ProductVersion&filter_productId=",
                "List releases:list?className=ProductRelease&filter_productId=",
                "List Gitlab repos:list?className=GitlabRepo&filter_productId=",
                "List epics:list?className=Epic&filter_productId=",
                "List proposals:list?className=Proposal&filter_productId=",
                "List new features:list?className=NewFeature&filter_productId=",
                "List enhancements:list?className=Enhancement&filter_productId=",
                "List bugs:list?className=Bug&filter_productId=",
                "List incidents:list?className=Incident&filter_productId=",
                "List problems:list?className=Problem&filter_productId=",
        };
    }
}
