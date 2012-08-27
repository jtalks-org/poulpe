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

ALTER TABLE PROPERTIES
    CHANGE VALIDATION_RULE VALIDATION_RULE VARCHAR(128) NOT NULL;
ALTER TABLE DEFAULT_PROPERTIES
    CHANGE VALIDATION_RULE VALIDATION_RULE VARCHAR(128) NOT NULL;

UPDATE DEFAULT_PROPERTIES
    SET VALIDATION_RULE = '/([0-9]|1[0-9]|2[0-4])/'
    WHERE NAME = 'jcommune.session_timeout';

UPDATE DEFAULT_PROPERTIES
    SET VALIDATION_RULE = '/([0-9]{1,6}|10([0-3][0-9]{4}|4([0-7][0-9]{3}|8([0-4][0-9]{2}|5([0-6][0-9]|7[0-6])))))/',
    VALUE = '51200'
    WHERE NAME = 'jcommune.avatar_max_size';


UPDATE PROPERTIES
    SET VALIDATION_RULE = '/([0-9]|1[0-9]|2[0-4])/'
    WHERE NAME = 'jcommune.session_timeout';

UPDATE PROPERTIES
    SET VALIDATION_RULE = '/([0-9]{1,6}|10([0-3][0-9]{4}|4([0-7][0-9]{3}|8([0-4][0-9]{2}|5([0-6][0-9]|7[0-6])))))/',
    VALUE = '51200'
    WHERE NAME = 'jcommune.avatar_max_size';