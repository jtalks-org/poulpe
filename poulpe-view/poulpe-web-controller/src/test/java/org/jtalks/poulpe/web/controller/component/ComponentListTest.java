package org.jtalks.poulpe.web.controller.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.fixtures.TestFixtures;
import org.jtalks.poulpe.web.controller.zkutils.BindUtilsWrapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author stanislav bashkirtsev
 */
@SuppressWarnings("unchecked")
public class ComponentListTest {

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new ComponentList();
        bindUtils = mock(BindUtilsWrapper.class);
        //set bind utils
        Field bindUtilsField = ComponentList.class.getDeclaredField("bindUtils");
        bindUtilsField.setAccessible(true);
        bindUtilsField.set(sut, bindUtils);
    }

    @Test
    public void renewShouldClearPreviousItems() {
        sut.add(TestFixtures.component(ComponentType.FORUM));
        sut.renew(new ArrayList<Component>());
        assertTrue(sut.getList().isEmpty());
    }

    @Test
    public void renewShouldFillWithNewValues() {
        sut.renew(Lists.newArrayList(TestFixtures.component(ComponentType.FORUM)));
        assertFalse(sut.getList().isEmpty());
    }

    @Test
    public void addShouldNotifyListeners() throws Exception {
        sut.add(createForum());
        verify(bindUtils).notifyAllPropsChanged(any(List.class));
    }

    @Test
    public void testRemove() throws Exception {
        sut.remove(createForum());
        verify(bindUtils).notifyAllPropsChanged(any(List.class));
    }

    @Test
    public void testComponentsUpdated() throws Exception {
        sut.componentsUpdated();
        verify(bindUtils).notifyAllPropsChanged(any(List.class));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIfNullWasRegisteredForNotification() {
        sut.registerListener(null);
    }

    @Test
    public void shouldRegisterVm() {
        Object vmToListen = new Object();
        sut.registerListener(vmToListen);
        sut.componentsUpdated();
        verify(bindUtils).notifyAllPropsChanged(Sets.newHashSet(vmToListen));
    }

    private Component createForum() {
        return TestFixtures.component(ComponentType.FORUM);
    }

    private ComponentList sut;
    private BindUtilsWrapper bindUtils;
}
