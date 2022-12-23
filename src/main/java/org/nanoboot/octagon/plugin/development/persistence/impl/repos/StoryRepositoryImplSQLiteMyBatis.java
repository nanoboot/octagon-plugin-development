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

package org.nanoboot.octagon.plugin.development.persistence.impl.repos;

import org.nanoboot.octagon.plugin.development.classes.Story;
import org.nanoboot.octagon.plugin.development.persistence.api.StoryRepository;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.impl.repos.RepositoryImpl;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class StoryRepositoryImplSQLiteMyBatis extends RepositoryImpl<Story> implements StoryRepository {

    public StoryRepositoryImplSQLiteMyBatis(MapperApi<Story> mapper, Class type) {
        super(mapper, type);
    }
}
