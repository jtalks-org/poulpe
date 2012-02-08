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

import java.util.Collections;
import java.util.Set;

import org.jtalks.common.model.entity.Rank;
import org.jtalks.common.validation.EntityValidator;
import org.jtalks.common.validation.ValidationError;
import org.jtalks.common.validation.ValidationException;
import org.testng.annotations.Test;
import org.jtalks.poulpe.model.dao.RankDao;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 *
 * @author Pavel Vervenko
 */
public class TransactionalRankServiceTest {
    @Mock EntityValidator entityValidator;
    @Mock RankDao rankDao;
    private TransactionalRankService rankService;
    private Rank rank;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rankService = new TransactionalRankService(rankDao, entityValidator);
        rank = new Rank("");
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
    public void testSaveRank() {
        rankService.saveRank(rank);
        verify(entityValidator).throwOnValidationFailure(rank);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testSaveRankException() {
        String notUniqueName = "name";
        rank.setRankName(notUniqueName);
        givenConstraintsViolations();
        rankService.saveRank(rank);
    }
    
    private void givenConstraintsViolations() {
        Set<ValidationError> dontCare = Collections.<ValidationError> emptySet();
        doThrow(new ValidationException(dontCare)).when(entityValidator).throwOnValidationFailure(any(Rank.class));
    }
}
