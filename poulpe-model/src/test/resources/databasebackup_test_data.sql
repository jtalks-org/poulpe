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

INSERT INTO common_schema_version (current_version, description, installed_by, state, execution_time, checksum, script, type, installed_on, version) VALUES
(0, 'Moderators Group Column In Branches', 'root', 'SUCCESS', 229, 140599915, 'V12__Moderators_Group_Column_In_Branches.sql', 'SQL', '2012-10-10 17:04:01.0', '12'),
(0, 'Branch Table Column Type Change', 'root', 'SUCCESS', 140, 1899329008, 'V13__Branch_Table_Column_Type_Change.sql', 'SQL', '2012-10-10 17:04:01.0', '13'),
(0, 'Users Table'' "Unnecessary columns', 'root', 'SUCCESS', 232, NULL, 'V14__Users_Table_Unnecessary_columns.sql', 'SQL', '2012-10-10 17:04:02.0', '14');
