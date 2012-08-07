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
 * @author Mikhail Zaitsev.
 */
public class UiPagination {

    private int activePage = 0;
    private int itemsPerPage = 50;

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
        if(this.activePage<0)this.activePage=0;
        if(this.activePage>this.itemsPerPage)this.activePage=this.itemsPerPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        if(this.itemsPerPage<0)this.itemsPerPage = 0;
    }

    public int getTotalAmountOfPages(int nItems) {
        if(itemsPerPage<=0)return 0;
        if((itemsPerPage%2)>0){
            return (nItems/itemsPerPage)+1;
        }else{
            return (nItems/itemsPerPage);
        }
    }


}
