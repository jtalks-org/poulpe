package org.jtalks.poulpe.web.controller.topictype;

import org.jtalks.common.validation.EntityValidator;
import org.jtalks.poulpe.model.entity.TopicType;
import org.jtalks.poulpe.service.TopicTypeService;
import org.jtalks.poulpe.web.controller.DialogManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
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
    @Mock
    private EntityValidator entityValidator;

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

        topicTypeVM = new TopicTypeVM(topicTypeService, dialogManager, entityValidator);
        TopicType selected = getTopicType();
        topicTypeVM.setSelected(selected);
        topicTypeVM.getTopicTypes().add(selected);
    }

    @Test
    public void testNewTopicType() {
        topicTypeVM.newTopicType();

        TopicType newTT = topicTypeVM.getSelected();
        assertTrue(newTT.getTitle().equals("New Title"));
        assertTrue(newTT.getDescription().equals("New Description"));
    }

    @Test
    public void testEditTopicType() {
        topicTypeVM.editTopicType();

        //due to static - message is null
        assertNull(topicTypeVM.getEditMessage());
    }

    @Test
    public void testSaveTopicType() {
        topicTypeVM.saveTopicType();

        verify(topicTypeVM.getTopicTypeService()).saveOrUpdate(any(TopicType.class));
    }

    @Test
    public void testDeleteTopicType() {
        topicTypeVM.deleteTopicType();

        verify(topicTypeService).deleteTopicType(any(TopicType.class));
    }

    @Test
    public void testCancelEditTopicType() {
        topicTypeVM.cancelEditTopicType();

        assertNull(topicTypeVM.getEditMessage());
        assertNull(topicTypeVM.getSelected());
    }

    @Test
    public void testDeleteFromList() {
        TopicType selected = topicTypeVM.getSelected();

        topicTypeVM.deleteFromList();

        assertFalse(topicTypeVM.getTopicTypes().contains(selected));
        assertNull(topicTypeVM.getSelected());
    }


}
