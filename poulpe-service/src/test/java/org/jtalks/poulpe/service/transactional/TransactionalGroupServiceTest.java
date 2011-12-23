package org.jtalks.poulpe.service.transactional;

import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.dao.GroupDao;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.jtalks.poulpe.model.entity.Group;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.*;

public class TransactionalGroupServiceTest {
    private TransactionalGroupService service;
    @Mock
    private GroupDao dao;
    private List<Group> list, listByName;
    final private String name = "name";
    final Group group = new Group("new group");

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
        service = new TransactionalGroupService(dao);
        list = new ArrayList<Group>();
        listByName = new ArrayList<Group>();
        when(dao.getAll()).thenReturn(list);
        when(dao.getMatchedByName(name)).thenReturn(listByName);
    }

    @Test
    public void deleteGroup() {
        service.deleteGroup(group);
        verify(dao).delete(group.getId());
    }

    @Test
    public void getAll() {
        assertTrue(service.getAll().equals(list));
    }

    @Test
    public void getAllMatchedByName() {
        assertTrue(service.getAllMatchedByName(name).equals(listByName));
    }

    @Test
    public void saveGroup() throws NotUniqueException {
        service.saveGroup(group);
        verify(dao).saveOrUpdate(group);
    }

    @Test(expectedExceptions = NotUniqueException.class)
    public void saveGroupWithException() throws NotUniqueException {
        when(dao.isGroupDuplicated(group)).thenReturn(true);
        service.saveGroup(group);
    }
}
