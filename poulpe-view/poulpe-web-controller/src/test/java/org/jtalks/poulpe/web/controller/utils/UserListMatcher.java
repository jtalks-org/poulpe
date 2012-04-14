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
package org.jtalks.poulpe.web.controller.utils;

import java.util.List;

import org.jtalks.poulpe.model.entity.User;
import org.mockito.ArgumentMatcher;

@Deprecated
public class UserListMatcher extends ArgumentMatcher<List<User>> {

    List<User> users;

    public UserListMatcher(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean matches(Object argument) {
        if (!(argument instanceof List))
            return false;
        
        List list =(List)argument;
        for(Object ob : list){
            if(!users.contains(ob)){
                return false;
            }
        }    
        for(User u : users){
            if(!list.contains(u)){
                return false;
            }
        }        
        return true;
    }
}
