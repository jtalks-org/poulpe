--
-- Copyright (C) 2011  JTalks.org Team
-- This library is free software; you can redistribute it and/or
-- modify it under the terms of the GNU Lesser General Public
-- License as published by the Free Software Foundation; either
-- version 2.1 of the License, or (at your option) any later version.
-- This library is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
-- Lesser General Public License for more details.
-- You should have received a copy of the GNU Lesser General Public
-- License along with this library; if not, write to the Free Software
-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
--

-- Before this we had such values in the acl_class table: org.jtalks.poulpe.model.entity.Component. But since
-- JCommune doesn't know anything about this class, we decided to rename those values to those like COMPONENT.
insert into acl_class values (1000, 'COMPONENT');

update acl_object_identity set object_id_class=1000
  where object_id_class in
        (select id from acl_class where class in('org.jtalks.poulpe.model.entity.Component',
                                                  'org.jtalks.poulpe.model.entity.Poulpe',
                                                  'org.jtalks.poulpe.model.entity.Jcommune'));
delete from acl_class where class in('org.jtalks.poulpe.model.entity.Component',
                                      'org.jtalks.poulpe.model.entity.Poulpe',
                                      'org.jtalks.poulpe.model.entity.Jcommune');
update acl_class set class='BRANCH' where class='org.jtalks.poulpe.model.entity.PoulpeBranch';
