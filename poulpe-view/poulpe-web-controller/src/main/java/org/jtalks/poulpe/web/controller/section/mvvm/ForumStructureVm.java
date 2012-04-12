package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.section.TreeNodeFactory;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeModel;

import javax.validation.constraints.NotNull;

/**
 * Is used in order to work with page that allows admin to manage sections and branches (moving them, reordering,
 * removing, editing, etc.).
 *
 * @author stanislav bashkirtsev
 */
public class ForumStructureVm {
    private final ComponentService componentService;
    private TreeModel sections;

    public ForumStructureVm(@NotNull ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Returns all the sections in our database in order they are actually sorted. Each invocation hits database for the
     * renewed list.
     *
     * @return all the sections in our database in order they are actually sorted or empty list if there are no
     *         sections. Can't return {@code null}.
     */
    @SuppressWarnings("unchecked")
    public TreeModel getSections() {
        sections = new DefaultTreeModel(TreeNodeFactory.buildForumStructure(getJcommune()));
        return sections;
    }

    private Jcommune getJcommune() {
        return (Jcommune) componentService.getByType(ComponentType.FORUM);
    }
}
