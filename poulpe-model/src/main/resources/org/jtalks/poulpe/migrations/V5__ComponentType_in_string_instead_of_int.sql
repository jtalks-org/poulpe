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

UPDATE COMPONENTS c SET c.COMPONENT_TYPE = "FORUM" WHERE c.C_TYPE = 0; 
UPDATE COMPONENTS c SET c.COMPONENT_TYPE = "ARTICLE" WHERE c.C_TYPE = 1; 
UPDATE COMPONENTS c SET c.COMPONENT_TYPE = "ADMIN_PANEL" WHERE c.C_TYPE = 2; 

ALTER TABLE COMPONENTS DROP COLUMN C_TYPE, DROP INDEX `C_TYPE`;

