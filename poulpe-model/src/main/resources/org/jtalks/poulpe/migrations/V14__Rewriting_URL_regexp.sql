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

ALTER TABLE DEFAULT_PROPERTIES MODIFY COLUMN `VALIDATION_RULE` VARCHAR(255) NOT NULL;

ALTER TABLE PROPERTIES MODIFY COLUMN `VALIDATION_RULE` VARCHAR(255) NOT NULL;


UPDATE DEFAULT_PROPERTIES
    SET VALIDATION_RULE = '/((https?:\\/\\/)?(([\\w][-\\w_\\.]*\\.[a-z]{1,5})|(localhost)|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?(\\/[-\\w_\\s()?=&#\\.]+)*\\/?)/'
    WHERE NAME = 'jcommune.url_address';

UPDATE PROPERTIES
    SET VALIDATION_RULE = '/((https?:\\/\\/)?(([\\w][-\\w_\\.]*\\.[a-z]{1,5})|(localhost)|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?(\\/[-\\w_\\s()?=&#\\.]+)*\\/?)/'
    WHERE NAME = 'jcommune.url_address';