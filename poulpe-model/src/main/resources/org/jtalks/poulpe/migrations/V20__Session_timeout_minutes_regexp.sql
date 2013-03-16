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

UPDATE DEFAULT_PROPERTIES
    SET VALIDATION_RULE = '/([1-9]\\d{0,2}|1[0-3]\\d{2}|14[0-3]\\d|1440|0)/'
    WHERE NAME = 'jcommune.session_timeout';

-- 1 part: [1-9]\d{0,2} means that value must be a number from 1 to 999.
-- 2 part  [1]{1}[0123]{1}\d{2} means that value must be a number from 1000 to 1399.
-- 3 part  14[0123]{1}\d{1} means that value must be a number from 1401 to 1439.


UPDATE PROPERTIES
    SET VALIDATION_RULE = '/([1-9]\\d{0,2}|1[0-3]\\d{2}|14[0-3]\\d|1440|0)/'
    WHERE NAME = 'jcommune.session_timeout';

