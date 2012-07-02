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
-- Default username: Admin
-- Default password: Admin

-- 'FROM COMPONENTS' are not used, but query mast contain 'FROM dual' clause
--  @see <a href="http://dev.mysql.com">http://dev.mysql.com/doc/refman/5.0/en/select.html/a>.
INSERT IGNORE INTO COMPONENTS (COMPONENT_TYPE,UUID,NAME,DESCRIPTION)
  SELECT 'POULPE', '7241a11-5620-87a0-a810-ed26496z92m7','Admin panel','JTalks Admin panel' FROM dual
    WHERE NOT EXISTS (SELECT * FROM COMPONENTS WHERE COMPONENT_TYPE='POULPE');

-- 'FROM COMPONENTS' are not used, but query mast contain 'FROM dual' clause
--  @see <a href="http://dev.mysql.com">http://dev.mysql.com/doc/refman/5.0/en/select.html/a>.
INSERT INTO GROUPS (UUID,NAME,DESCRIPTION)
  SELECT '7141a12-5620-87h0-a210-ed26491k82m7','Administrators', 'Administrators group.' FROM dual
    WHERE NOT EXISTS (SELECT gr.GROUP_ID FROM GROUPS gr WHERE gr.NAME='Administrators');

-- IGNORE can be used here because USERNAME is unique column, so if table contain user with username='Admin', record
--  will not be added.
INSERT IGNORE INTO USERS (UUID,FIRST_NAME,LAST_NAME,USERNAME,ENCODED_USERNAME,EMAIL,PASSWORD,ROLE,SALT,REGISTRATION_DATE)
  VALUES('7241p12-2720-99h0-r210-ed26491k86j7','Admin','Admin','Admin','Admin','admin@jtalks.org','e3afed0047b08059d0fada10f400c1e5','ADMIN_ROLE','',NOW());

-- Adding created Admin to Administrators group(created at this migration or common migration) ).
INSERT IGNORE INTO GROUP_USER_REF(USER_ID,GROUP_ID)
  SELECT u.ID, g.GROUP_ID FROM USERS u, GROUPS g
    WHERE u.USERNAME = 'Admin' AND g.NAME = 'Administrators';

-- Adding record with added component class.
INSERT IGNORE INTO acl_class (CLASS) VALUE('COMPONENT');

-- Adding record to acl_sid table, this record wires sid and group id.
SET @acl_sid := (SELECT GROUP_CONCAT('usergroup:', CONVERT(GROUP_ID,char(19))) FROM GROUPS WHERE NAME= 'Administrators');

SET @object_id_identity := (SELECT component.CMP_ID FROM COMPONENTS component WHERE component.NAME = 'Admin panel');


INSERT IGNORE INTO acl_sid (PRINCIPAL,SID)
  VALUES(0,@acl_sid);

SET @acl_sid_id := (SELECT sid.ID FROM acl_sid sid WHERE sid.sid = @acl_sid);

SET @acl_class_id :=(SELECT class.ID FROM acl_class class WHERE class.class = 'COMPONENT');


INSERT IGNORE INTO acl_object_identity (object_id_class,object_id_identity,owner_sid,entries_inheriting)
  VALUES(@acl_class_id, @object_id_identity, @acl_sid_id, 1);

SET @acl_object_identity_id := (SELECT aoi.ID FROM acl_object_identity aoi WHERE object_id_class=@acl_class_id AND object_id_identity = @object_id_identity AND owner_sid = @acl_sid_id);

INSERT IGNORE INTO acl_entry (acl_object_identity,sid,mask,granting,audit_success,audit_failure, ace_order)
 VALUES(@acl_object_identity_id, @acl_sid_id, 16, 1, 0 , 0, 0);


