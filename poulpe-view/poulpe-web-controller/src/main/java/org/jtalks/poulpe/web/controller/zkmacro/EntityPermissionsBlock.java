/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.web.controller.zkmacro;

import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.jtalks.common.model.entity.Entity;

import com.google.common.collect.Lists;

/**
 * Group of blocks {@link PermissionManagementBlock} related to the same entity, contains list of blocks, caption and
 * entity for which set of permissions provided.
 * 
 * @see PermissionManagementBlock
 * @author Vyacheslav Zhivaev
 */
@Immutable
public class EntityPermissionsBlock {

    private final Entity entity;
    private final String caption;
    private final List<PermissionManagementBlock> blocks;

    /**
     * Constructs set of blocks with defined caption.
     * 
     * @param entity the entity for which permission block is
     * @param caption the caption for this block, can be used in view
     * @param blocks the list of {@link PermissionManagementBlock} blocks
     */
    public EntityPermissionsBlock(Entity entity, String caption, List<PermissionManagementBlock> blocks) {
        this.entity = entity;
        this.caption = caption;
        this.blocks = Lists.newArrayList(blocks);
    }

    /**
     * Gets entity for which set of permissions constructed.
     * 
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets group caption.
     * 
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets blocks of {@link PermissionManagementBlock}.
     * 
     * @return the blocks, list instance is UNMODIFIABLE
     */
    public List<PermissionManagementBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }
}
