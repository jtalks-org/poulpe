package org.jtalks.poulpe.web.controller.section.mvvm;

import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.model.entity.PoulpeSection;
import org.jtalks.poulpe.service.ComponentService;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModelList;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Is used in order to work with page that allows admin to manage sections and branches (moving them, reordering,
 * removing, editing, etc.).
 *
 * @author stanislav bashkirtsev
 */
public class SectionsAndBranchesVm {
    private final ComponentService componentService;
    private final ListModelList<PoulpeSection> sections =
            new BindingListModelList<PoulpeSection>(new ArrayList<PoulpeSection>(), true);

    public SectionsAndBranchesVm(@NotNull ComponentService componentService) {
        this.componentService = componentService;
    }

    /**
     * Returns all the sections in our database in order they are actually sorted. Each invocation hits database for the
     * renewed list.
     *
     * @return all the sections in our database in order they are actually sorted or empty list if there are no
     *         sections. Can't return {@code null}.
     */
    public ListModelList<PoulpeSection> getSections() {
        sections.clear();
        sections.addAll(getJcommune().getSections());
        return sections;
    }

    private Jcommune getJcommune() {
        return (Jcommune) componentService.getByType(ComponentType.FORUM);
    }
}
