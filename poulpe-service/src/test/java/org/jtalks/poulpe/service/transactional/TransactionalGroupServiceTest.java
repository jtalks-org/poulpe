///**
// * Copyright (C) 2011  JTalks.org Team
// * This library is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public
// * License as published by the Free Software Foundation; either
// * version 2.1 of the License, or (at your option) any later version.
// * This library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// * Lesser General Public License for more details.
// * You should have received a copy of the GNU Lesser General Public
// * License along with this library; if not, write to the Free Software
// * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
// */
//package org.jtalks.poulpe.service.transactional;
//
//import org.jtalks.common.model.entity.Group;
//import org.jtalks.poulpe.model.dao.GroupDao;
//import org.jtalks.poulpe.model.logic.UserBanner;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import static org.mockito.Mockito.*;
//
///**
// * Test for {@link TransactionalGroupService}
// */
//public class TransactionalGroupServiceTest {
//	private TransactionalGroupService service;
//
//	@Mock
//	GroupDao dao;
//
//	private Group group = new Group("new group");
//
//	@BeforeMethod
//	public void beforeMethod() {
//		MockitoAnnotations.initMocks(this);
//		service = new TransactionalGroupService(dao, mock(UserBanner.class));
//	}
//
//	@Test
//	public void deleteGroup() {
//		service.deleteGroup(group);
//		verify(dao).delete(group);
//	}
//
//	@Test
//	public void getAll() {
//		service.getAll();
//		verify(dao).getAll();
//	}
//
//	@Test
//	public void getByName() {
//		service.getByName("name");
//		verify(dao).getByName("name");
//	}
//
//}
