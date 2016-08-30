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

-- 'FROM COMPONENTS' are not used, but query mast contain 'FROM dual' clause
--  @see <a href="http://dev.mysql.com">http://dev.mysql.com/doc/refman/5.0/en/select.html/a>.

-- SET NAMES used to avoid Illegal mix of collations error when '=' operator is called
SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';

SET @adminUserName := 'admin';
SET @passwordHash := '21232f297a57a5a743894a0e4a801fc3';
SET @adminGroupName := 'Administrators';
SET @poulpeComponentName := 'Admin panel';
SET @poulpeComponentType := 'ADMIN_PANEL';
SET @aclClass :='COMPONENT';
SET @adminPanelComponentId := 1;

INSERT IGNORE INTO COMPONENTS (CMP_ID, COMPONENT_TYPE, UUID, `NAME`, DESCRIPTION)
  SELECT @adminPanelComponentId, @poulpeComponentType, '7241a11-5620-87a0-a810-ed26496z92m7',@poulpeComponentName,'JTalks Admin panel' FROM dual
    WHERE NOT EXISTS (SELECT * FROM COMPONENTS components WHERE components.COMPONENT_TYPE=@poulpeComponentType);

-- 'FROM COMPONENTS' are not used, but query mast contain 'FROM dual' clause
--  @see <a href="http://dev.mysql.com">http://dev.mysql.com/doc/refman/5.0/en/select.html/a>.
INSERT INTO GROUPS (UUID, `NAME`, DESCRIPTION)
  SELECT '7141a12-5620-87h0-a210-ed26491k82m7',@adminGroupName, 'Administrators group.' FROM dual
    WHERE NOT EXISTS (SELECT gr.GROUP_ID FROM GROUPS gr WHERE gr.NAME=@adminGroupName);

-- IGNORE can be used here because USERNAME is unique column, so if table contain user with username='Admin', record
--  will not be added.
INSERT IGNORE INTO USERS (UUID, FIRST_NAME, LAST_NAME, USERNAME, ENCODED_USERNAME, EMAIL, PASSWORD, ROLE, SALT, ENABLED)
  VALUES('7241p12-2720-99h0-r210-ed26491k86j7', @adminUserName, @adminUserName, @adminUserName,
         @adminUserName, 'admin@jtalks.org', @passwordHash, 'ADMIN_ROLE', '', true);

-- Adding created Admin to Administrators group(created at this migration or common migration) ).
INSERT IGNORE INTO GROUP_USER_REF(USER_ID, GROUP_ID)
  SELECT u.ID, g.GROUP_ID FROM USERS u, GROUPS g
    WHERE u.USERNAME = @adminUserName AND g.NAME = @adminGroupName
          AND NOT EXISTS
            (SELECT gur.USER_ID, gur.GROUP_ID, u.ID, g.GROUP_ID FROM GROUP_USER_REF gur, USERS u, GROUPS g
               WHERE u.ID = gur.USER_ID
                 AND g.GROUP_ID = gur.GROUP_ID
                 AND u.USERNAME = @adminUserName
                 AND g.NAME = @adminGroupName);

-- Adding record with added component class.
INSERT IGNORE INTO acl_class (class) VALUE(@aclClass);

SET @acl_sid_group := (SELECT GROUP_CONCAT('usergroup:', CONVERT(GROUP_ID, char(19))) FROM GROUPS g WHERE g.NAME = @adminGroupName);
SET @acl_sid_user := (SELECT GROUP_CONCAT('user:', CONVERT(ID, char(19))) FROM USERS u WHERE u.USERNAME = @adminUserName);
SET @object_id_identity := (SELECT component.CMP_ID FROM COMPONENTS component WHERE component.COMPONENT_TYPE = @poulpeComponentType);


-- Adding record to acl_sid table, this record wires sid and user id.
INSERT INTO acl_sid (principal, sid)
  SELECT 1, @acl_sid_user
  FROM dual
  WHERE NOT exists (SELECT acl_sid.sid FROM acl_sid WHERE sid = @acl_sid_user);

SET @acl_sid_id_user := (SELECT sid.id FROM acl_sid sid WHERE sid.sid = @acl_sid_user);

-- Adding record to acl_sid table, this record wires sid and group id.
INSERT IGNORE INTO acl_sid (principal, sid)
  VALUES(0, @acl_sid_group);

SET @acl_sid_id_group := (SELECT sid.id FROM acl_sid sid WHERE sid.sid = @acl_sid_group);

SET @acl_class_id :=(SELECT class.id FROM acl_class class WHERE class.class = @aclClass);

INSERT IGNORE INTO acl_object_identity (object_id_class, object_id_identity, owner_sid, entries_inheriting)
  SELECT @acl_class_id, @object_id_identity, @acl_sid_id_user, 1 FROM dual
    WHERE NOT EXISTS
      (SELECT aoi.object_id_class, aoi.object_id_identity, aoi.owner_sid  FROM acl_object_identity aoi
         WHERE aoi.object_id_class = @acl_class_id
           AND aoi.object_id_identity = @object_id_identity
           AND aoi.owner_sid = @acl_sid_id_user);

SET @acl_object_identity_id := (SELECT aoi.id FROM acl_object_identity aoi
                                  WHERE aoi.object_id_class = @acl_class_id
                                    AND aoi.object_id_identity = @object_id_identity);

SET @ace_order_max := (SELECT MAX(ae.ace_order) FROM acl_entry ae);
SET @ace_order := (CASE WHEN  @ace_order_max is null THEN 0 ELSE @ace_order_max+1 END);

INSERT IGNORE INTO acl_entry (acl_object_identity, sid, ace_order, mask, granting, audit_success, audit_failure)
  SELECT  @acl_object_identity_id, @acl_sid_id_group, @ace_order, 16, 1, 0 , 0 FROM dual
    WHERE NOT EXISTS
       (SELECT ae.acl_object_identity, ae.sid FROM acl_entry ae
         WHERE ae.acl_object_identity = @acl_object_identity_id
           AND ae.sid = @acl_sid_id_group);

