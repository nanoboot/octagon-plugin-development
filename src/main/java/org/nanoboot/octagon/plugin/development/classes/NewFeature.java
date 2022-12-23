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

import java.util.List;
//wanting a new capability or software feature.

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public final class NewFeature extends AbstractDevelopmentTask {
   private static List<EntityAttribute> SCHEMA;

   @Override
   public Class getEntityClass() {
      return getClass();
   }

   @Override
   public List<EntityAttribute> getSchema() {
      if (SCHEMA == null) {
         SCHEMA = EntityAttribute.copy(super.createAbstractSchema());
         for (EntityAttribute e : SCHEMA) {
            if (e.getName().equals("parentId")) {
               String fKName= getForeignKeyNameForParent();
               e.setCustomHumanName(String.valueOf(fKName.charAt(0)).toUpperCase() + fKName.substring(1));
               e.setForeignKey(fKName);
               e.setNamedList("getNewFeaturesForProduct");
               e.setNamedListArgPropertyName("productId");
               break;
            }
         }
      }
      return SCHEMA;
   }
   public void validateDelete() {
      super.validateDelete();
      throw new OctagonException(NewFeature.class.getSimpleName() + " cannot be deleted, because once it is created, it must stay there.");
   }
   @Override
   String getForeignKeyNameForParent() {
      return "newFeature";
   }
   }


