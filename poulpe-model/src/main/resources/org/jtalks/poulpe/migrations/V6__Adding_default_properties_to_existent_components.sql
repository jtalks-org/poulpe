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

-- After adding a default property to DEFAULT_PROPERTIES, existent components won't have it 
-- unless it is manually added to the database
-- Query below adds properties to components if only they don't yet have them,
-- existent values will remain untouched

INSERT INTO `PROPERTIES`
    (`UUID`, `NAME`, `VALUE`, `CMP_ID`, `VALIDATION_RULE`) 
SELECT 
    UUID(), dp.`NAME`, dp.`VALUE`, c.`CMP_ID`, dp.`VALIDATION_RULE`
FROM 
    `DEFAULT_PROPERTIES` dp left outer join `PROPERTIES` p on dp.`NAME` = p.`NAME`
        join `COMPONENTS` c on c.`COMPONENT_TYPE` = dp.`BASE_COMPONENT_TYPE`
WHERE 
    p.`NAME` is NULL;



