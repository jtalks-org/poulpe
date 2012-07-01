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
-- While Poulpe first starts, it starts with empty database and there is no possibility to log into it.
-- This migration creates default user for this. In order to have a user, we need a migration that inserts
-- it and permissions (because user without permissions can't be logged into admin panel).

INSERT IGNORE INTO COMPONENTS (COMPONENT_TYPE,UUID,NAME,DESCRIPTION)
  select 'POULPE', '7241a11-5620-87a0-a810-ed26496z92m7','Admin panel','JTalks Admin panel' where not exists (select * from CCMP_TYPE;

INSERT INTO GROUPS (UUID,NAME,DESCRIPTION)
  SELECT '7141a12-5620-87h0-a210-ed26491k82m7','Administrators', 'Administrators group.' FROM GROUPS
    WHERE NOT EXISTS (SELECT * FROM GROUPS WHERE NAME='Administrators');

INSERT IGNORE INTO USERS (UUID,FIRST_NAME,LAST_NAME,USERNAME,ENCODED_USERNAME,EMAIL,PASSWORD,ROLE,SALT,REGISTRATION_DATE)
  VALUES('7241p12-2720-99h0-r210-ed26491k86j7','Admin','Admin','Admin','Admin','admin@jtalks.org','e3afed0047b08059d0fada10f400c1e5','ADMIN_ROLE','',NOW());

-- Adding created Admin to Administrators group(created at this migration or common migration) ).
INSERT IGNORE INTO GROUP_USER_REF(USER_ID,GROUP_ID)
  SELECT u.ID, g.GROUP_ID FROM USERS u, GROUPS g
    WHERE u.USERNAME = 'Admin' AND g.NAME = 'Administrators';


-- Adding record with added component class.
INSERT IGNORE INTO acl_class (CLASS) VALUE('COMPONENT');

-- Adding record to acl_sid table, this record wires sid and group id.
SET @acl_sid_id := 0;
INSERT IGNORE INTO acl_sid (PRINCIPAL,SID)
  SELECT 0, GROUP_CONCAT('usergroup:', CONVERT(GROUP_ID,char(19)))
    FROM GROUPS WHERE NAME= 'Administrators';

update acl_sid set name='Administrators', id = (SELECT @acl_sid_id := id) where name='Administrators';


INSERT IGNORE INTO acl_object_identity (object_id_class,object_id_identity,owner_sid,entries_inheriting)
  SELECT class.id, component.CMP_ID, @acl_sid_id, 1
    FROM acl_class class, GROUPS gr, COMPONENTS component
      WHERE component.NAME = 'Admin panel'
            AND class.class='COMPONENT'
            AND gr.NAME='Administrators';

INSERT IGNORE INTO acl_entry (acl_object_identity,sid,mask,granting,audit_success,audit_failure, ace_order)
  SELECT oid.id, @acl_sid_id, 16, 1, 0 , 0, 0
    FROM acl_class class, GROUPS gr, COMPONENTS component, acl_object_identity oid
      WHERE gr.name = 'Administrators'
            AND class.class='COMPONENT'
            AND oid.object_id_identity = component.CMP_ID
            AND component.NAME = 'Admin panel';

