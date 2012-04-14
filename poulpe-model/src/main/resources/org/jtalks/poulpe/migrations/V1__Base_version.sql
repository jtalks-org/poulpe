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

CREATE TABLE `POULPE_GROUP` (
  `GROUP_ID` BIGINT(20) NOT NULL,
  `BRANCH_ID` BIGINT(20) NULL DEFAULT NULL,
  `POSITION` INT(11) NULL DEFAULT NULL);

CREATE  TABLE `BRANCH_USER_REF` (
  `BRANCH_ID` BIGINT(20) NOT NULL,
  `USER_ID` BIGINT(20) NOT NULL,
  CONSTRAINT `FK_BRANCH_USER_REF_USERS_USER_ID`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `USERS` (`ID`),
  CONSTRAINT `FK_BRANCH`
    FOREIGN KEY (`BRANCH_ID`)
    REFERENCES `BRANCHES` (`BRANCH_ID`));

CREATE  TABLE `TOPIC_TYPES`(
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `UUID` VARCHAR(255) NOT NULL ,
  `TITLE` VARCHAR(255) NOT NULL ,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ID`) ,
  UNIQUE INDEX `UUID` (`UUID` ASC) ,
  UNIQUE INDEX `TITLE` (`TITLE` ASC));