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
package org.jtalks.poulpe.pages;

/**
 * Provides transition between pages numbered.
 * @author Mikhail Zaitsev.
 */
public class UiPagination {

    private int activePage = 0;
    private int itemsPerPage = 50;

    /**
     * @return number of active pages
     */
    public int getActivePage() {
        return activePage;
    }

    /**
     * @param activePage number of active pages
     */
    public void setActivePage(int activePage) {
        this.activePage = activePage;
        if(this.activePage<0)this.activePage=0;
    }

    /**
     * @return the number of items per page
     */
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * @param itemsPerPage the number of items per page
     */
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        if(this.itemsPerPage<0)this.itemsPerPage = 0;
    }

    /**
     * Returns the total amount of pages
     * @param nItems number of total users
     * @return the total amount of pages
     */
    public int getTotalAmountOfPages(int nItems) {
        if(itemsPerPage<=0)return 0;
        if((nItems%2)>0){
            return (nItems/itemsPerPage)+1;
        }else{
            return (nItems/itemsPerPage);
        }
    }


}
