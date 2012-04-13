package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.common.model.entity.Section;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeBranch;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.jtalks.poulpe.web.controller.section.TreeNodeFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
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
    private Jcommune jcommune;
    private boolean showCreateSectionDialog;
    private PoulpeSection selectedSection;

    public ForumStructureVm(@NotNull ComponentService componentService) {
        this.componentService = componentService;
    }

    @Command
    @NotifyChange("showCreateSectionDialog")
    public void showNewSectionDialog(@BindingParam("show") boolean show){
        showCreateSectionDialog = show;
        if(selectedSection == null){
            selectedSection = new PoulpeSection();
        }
    }

    @Command
    @NotifyChange("sections")
    public void saveSection(){
        jcommune.addSection(selectedSection);
        componentService.saveComponent(jcommune);
        selectedSection = null;
    }

    @Init
    public void initForumStructure(){
        jcommune = getJcommune();
    }

    /**
     * Returns all the sections in our database in order they are actually sorted.
     *
     * @return all the sections in our database in order they are actually sorted or empty list if there are no
     *         sections. Can't return {@code null}.
     */
    @SuppressWarnings("unchecked")
    public TreeModel getSections() {
        return new DefaultTreeModel(TreeNodeFactory.buildForumStructure(jcommune));
    }

    public boolean isShowCreateSectionDialog() {
        return showCreateSectionDialog;
    }

    public PoulpeSection getSelectedSection() {
        return selectedSection;
    }

    public void setSelectedSection(PoulpeSection selectedSection) {
        this.selectedSection = selectedSection;
    }

    private Jcommune getJcommune() {
        return (Jcommune) componentService.getByType(ComponentType.FORUM);
    }
}
