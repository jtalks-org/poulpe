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

package org.jtalks.poulpe.model.pages;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test for {@link UiPagination} class.
 * @author Mikhail Zaitsev
 */
public class UiPaginationTest {

    private UiPagination uiPagination;

    @BeforeMethod
    public void beforeMethod() {
        uiPagination  = new UiPagination();
    }

    @Test
    public void getTotalAmountOfPagesTest(){
        assertTrue(uiPagination.getTotalAmountOfPages(100)==2);
        assertTrue(uiPagination.getTotalAmountOfPages(121)==3);

    }

    @Test
    public void setItemsPerPageTest(){
        uiPagination.setItemsPerPage(-1);
        assertTrue(uiPagination.getItemsPerPage()==0);
        uiPagination.setItemsPerPage(10);
        assertTrue(uiPagination.getItemsPerPage()==10);
    }

    @Test
    public void setActivePageTest(){
        uiPagination.setActivePage(-1);
        assertTrue(uiPagination.getActivePage()==0);
        uiPagination.setActivePage(10);
        assertTrue(uiPagination.getActivePage()==10);
    }


}
