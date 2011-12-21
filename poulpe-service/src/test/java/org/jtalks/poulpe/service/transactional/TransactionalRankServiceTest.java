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
package org.jtalks.poulpe.service.transactional;

import org.jtalks.poulpe.model.entity.Rank;
import org.jtalks.poulpe.service.exceptions.NotUniqueException;
import org.testng.annotations.Test;
import org.jtalks.poulpe.model.dao.RankDao;
import org.testng.annotations.BeforeMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Pavel Vervenko
 */
public class TransactionalRankServiceTest {

    private RankDao rankDao;
    private TransactionalRankService rankService;
    private Rank rank;

    @BeforeMethod
    public void setUp() throws Exception {
        rankDao = mock(RankDao.class);
        rankService = new TransactionalRankService(rankDao);
        rank = new Rank();
    }

    @Test
    public void testGetAll() {
        rankService.getAll();
        verify(rankDao).getAll();
    }

    @Test
    public void testDeleteRank() {
        rankService.deleteRank(rank);
        verify(rankDao).delete(rank);
    }

    @Test
    public void testSaveRank() throws NotUniqueException {
        rankService.saveRank(rank);
        verify(rankDao).saveOrUpdate(rank);
    }

    @Test(expectedExceptions = NotUniqueException.class)
    public void testSaveRankException() throws NotUniqueException {
        String notUniqueName = "name";
        rank.setRankName(notUniqueName);
        when(rankDao.isRankNameExists(notUniqueName)).thenReturn(Boolean.TRUE);
        rankService.saveRank(rank);
    }
}
