/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
/**
 * Database migrations stored in this package, they create database tables and fill them with initial content; you need
 * to write new migrations if you want to change something in database. Note, that if you wrote a DML script
 * (e.g. updates/inserts/deletes), it will be rolled back if migration fails, but RDBMSs don't support rolling back of
 * DSL (e.g. altering tables).
 * <p>
 * <i>How it works</i>: we use Flyway for migrations and each time application starts it checks in its own table what
 * migration was run last time and if there are additional SQL scripts, it will execute them. So each time we need to
 * change something in the database (table structure or data), we need to create a new migration script that will
 * include all the updates. <b>Note</b>, that JTalks has common modules (JTalks Common) which contain common migrations
 * which are executed before Poulpe scripts. You can find Flyway configuration in one of Spring App Contexts and there
 * you'll find that both scripts from JTalks Common and Poulpe are executed during application deployment. Note, that
 * if migration was executed, you can't remove it or change it since those changes won't apply again (Flyway keeps track
 * of last executed script and won't execute it again). If you really want to remove/replace your migration or you want
 * to change it, you can't simply do operations with file, thus you need to change flyway table - remove a row with
 * information about your migration and set a flag that marks the last successful migration to previous migration.
 * </p>
 * <p>
 * <i>Justification</i>: <ul><li>First of all when we work on the project and someone wants to change the database
 * structure, a problem appears because others may not know that and thus they will have wrong schema which will lead to
 * errors. Hibernate itself can update schema, but only in very primitive cases like adding a new column, but it won't
 * for instance remove one if you renamed some property in the entity.</li><li>Second, we might need to change the data
 * inside tables after changes in the schema (for instance some static data like class names should be changed), and
 * we don't want to remove database content (well, in production we actually can't) or make this manual step since a lot
 * of people might be affected.</li></ul>
 * </p>
 * <p>
 * <i>How to write new migration:</i> you need to add one more file in the format {@code V[N]__[Descriptsion].sql}
 * (note two underscores). While writing a script, you have to ensure that it will run both when data you want to change
 * exists in database and when it doesn't. For instance if you need to add a new property to the Component, you need to
 * consider both cases - when this component exists and when it doesn't.
 * </p>
 *
 */
package org.jtalks.poulpe.migrations;