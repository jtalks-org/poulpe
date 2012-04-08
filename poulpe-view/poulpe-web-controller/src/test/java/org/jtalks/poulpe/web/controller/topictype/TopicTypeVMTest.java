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
 * See {@see TopicTypeVm}
 *
 * @author Vahluev Vyacheslav
 */
public class TopicTypeVmTest {

    private TopicTypeVm topicTypeVm;
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

        topicTypeVm = new TopicTypeVm(topicTypeService, dialogManager, entityValidator);
        TopicType selected = getTopicType();
        topicTypeVm.setSelected(selected);
        topicTypeVm.getTopicTypes().add(selected);
    }

    @Test
    public void testNewTopicType() {
        topicTypeVm.newTopicType();

        TopicType newTT = topicTypeVm.getSelected();
        assertEquals(newTT.getTitle(), "New Title");
        assertEquals(newTT.getDescription(),"New Description");
    }

    @Test
    public void testEditTopicType() {
        topicTypeVm.editTopicType();

        //due to static - message is null
        assertNull(topicTypeVm.getEditMessage());
    }

    @Test
    public void testSaveTopicType() {
        topicTypeVm.saveTopicType();

        verify(topicTypeVm.getTopicTypeService()).saveOrUpdate(any(TopicType.class));
    }

    @Test
    public void testDeleteTopicType() {
        topicTypeVm.deleteTopicType();

        verify(topicTypeService).deleteTopicType(any(TopicType.class));
    }

    @Test
    public void testCancelEditTopicType() {
        topicTypeVm.cancelEditTopicType();

        assertNull(topicTypeVm.getEditMessage());
        assertNull(topicTypeVm.getSelected());
    }

    @Test
    public void testDeleteFromList() {
        TopicType selected = topicTypeVm.getSelected();

        topicTypeVm.deleteFromList(selected);

        assertFalse(topicTypeVm.getTopicTypes().contains(selected));
        assertNull(topicTypeVm.getSelected());
    }


}
