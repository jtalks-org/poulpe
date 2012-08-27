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

-- At first: ([0-9]|1[0-9]|2[0-4]) means that value must be a number from 0 to 24.
-- This scope consist of three sub-scopes: 0-9, 10-19, 20-24.
--
-- 0-9:      So value can be from '0' to '9' (see [0-9])
-- or 10-19: So value starts by '1', and ens by character from '0' to '9'. So we have such regexp: 1[0-9]
-- or 20-24: First char is '2', and the second from '0' to '4'. So: 2[0-4]
--
-- So:
-- Value must be from 0 to 24. So it must be 0-9(regexp: "[0-9]") or 10-19(regexp: "1[0-9]") or 20-24(regexp: "2[0-4]").
-- So we get regexp: "[0-9] | 1[0-9] | 2[0-4]" (because '|' means logic "or")

UPDATE DEFAULT_PROPERTIES
    SET VALIDATION_RULE = '/([0-9]{1,6}|10([0-3][0-9]{4}|4([0-7][0-9]{3}|8([0-4][0-9]{2}|5([0-6][0-9]|7[0-6])))))/',
    VALUE = '51200'
    WHERE NAME = 'jcommune.avatar_max_size';

-- More readable version:
-- line 1: [0-9]{1,6}
-- line 2:           | 10([0-3][0-9]{4}
-- line 3:                              | 4([0-7][0-9]{3}
-- line 4:                                                | 8([0-4][0-9]{2}
-- line 5:                                                                  | 5([0-6][0-9]
-- line 6:                                                                                 |  7[0-6]))))
-- Scope, which regexp describes: 0 - 1048576
-- It is similar to previous regexp, but a little bit more complex.
--
-- "[0-9]{1,6}" (from line 1) - value may be from 0 to 999999 (because {1,6} means, that char may be in count from 1 to 6)
-- Another part describes scope from 1000000 to 1048576:
--  1. As we can see this values starts from "10"(and >10<00000, and >10<05342, and >10<42645, and >10<48576 - so every number
--    from 1000000 to 1048576 starts with "10"). So in start of line 2 we have: "10" - that mean, that number starts with "10"
--    and ends with smth, described in brackets
--  2. line 2: "[0-3][0-9]{4}" - means, that number, that already starts from "10", continued by such chars:
--      [0-3], [0-9], [0-9], [0-9], [0-9] (because "{4}" means repeating of "[0-9]" 4 times).
--      So we cover here such scope: from 10(from start of line 2) 0(from [0-3]) 0000(from [0-9]{4}) = 1000000
--                                   to   10(from start of line 2) 3(from [0-3]) 9999(from [0-9]{4}) = 1039999
--  3. If value is not in scope 1000000-1039999, then start of line 3 adds to "10" char '4'
--     and line 3 adds to "104" scope from 0(see [0-7])000(see [0-9]{3}) to 7(see [0-7])999(see [0-9]{3}).
--     So line 3 covers such scope: 1040000 - 1047999.
--  And so on:
--  line 4 covers: 1048000-1048499
--  line 5 covers:         1048500-1048569
--  line 6 covers:                 1048570-104876


UPDATE PROPERTIES
    SET VALIDATION_RULE = '/([0-9]|1[0-9]|2[0-4])/'
    WHERE NAME = 'jcommune.session_timeout';

UPDATE PROPERTIES
    SET VALIDATION_RULE = '/([0-9]{1,6}|10([0-3][0-9]{4}|4([0-7][0-9]{3}|8([0-4][0-9]{2}|5([0-6][0-9]|7[0-6])))))/',
    VALUE = '51200'
    WHERE NAME = 'jcommune.avatar_max_size';