/* Poulpe in previous migrations touches PROPERTIES table which lays in Common
 * scope. This script modify this table to reconcile it columns with 
 * definitions from common.  
 */
ALTER TABLE PROPERTIES MODIFY VALIDATION_RULE VARCHAR(64) NOT NULL DEFAULT '';