package org.jtalks.poulpe.web.controller.topictype;

import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Tests for TopicType ViewModel
 * See {@see TopicTypeVM}
 *
 * @author Vahluev Vyacheslav
 */
public class TopicTypeVMTest {

    private TopicTypeVM topicTypeVM;
    @Mock
    private TopicTypeService topicTypeService;
    @Mock
    private DialogManager dialogManager;

    public TopicType getTopicType() {
        TopicType topicType = new TopicType();
        topicType.setDescription("Desc");
        topicType.setTitle("Title");
        topicType.setId(12345);

        return topicType;
    }

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        topicTypeVM = new TopicTypeVM(topicTypeService, dialogManager);
        topicTypeVM.setSelected(getTopicType());
    }

    @Test
    public void testNewTopicType() {
        int oldSize = topicTypeVM.getTopicTypes().size();

        topicTypeVM.newTopicType();

        int newSize = topicTypeVM.getTopicTypes().size();
        TopicType newTT = topicTypeVM.getSelected();
        assertTrue(newSize - oldSize == 1);
        assertTrue(newTT.getTitle().equals("New Title"));
        assertTrue(newTT.getDescription().equals("New Description"));
    }

    @Test
    public void testSaveTopicType() {
        topicTypeVM.saveTopicType();

        verify(topicTypeService).saveOrUpdate(topicTypeVM.getSelected());
    }

    @Test
    public void testDeleteTopicType() {
        topicTypeVM.deleteTopicType();

        verify(dialogManager).confirmDeletion(eq(topicTypeVM.getSelected().getTitle()), any(DialogManager.Performable.class));
    }

    @Test
    public void testDeleteFromList() {
        TopicType selected = topicTypeVM.getSelected();

        topicTypeVM.deleteFromList();

        assertFalse(topicTypeVM.getTopicTypes().contains(selected));
        assertNull(topicTypeVM.getSelected());
        assertNull(topicTypeVM.getDeleteMessage());
    }
}
