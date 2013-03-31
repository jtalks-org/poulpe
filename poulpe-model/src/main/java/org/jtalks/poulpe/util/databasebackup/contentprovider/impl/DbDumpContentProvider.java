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
package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.dbdump.DatabaseDumpFactory;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * The class is responsible for provide database dump in a form of SQL commands which can be executed in a SQL console
 * for restore database.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbDumpContentProvider implements ContentProvider {

    /**
     * A constructor injects a data source variable which later will be used to generate database dump.
     * 
     * @param dbDumpFactory
     *            A DatabaseDumpFactory for creating "generate database dump command" object.
     */
    public DbDumpContentProvider(DatabaseDumpFactory dbDumpFactory) {
        Validate.notNull(dbDumpFactory, "dbDumpFactory must not be null");
        this.dbDumpFactory = dbDumpFactory;
    }

    @Override
    public void writeContent(OutputStream output) throws FileDownloadException {
        try {
            dbDumpFactory.newDbDumpCommand().execute(output);

        } catch (SQLException e) {
            throw new DatabaseExportingException(e);
        } catch (IOException e) {
            throw new DatabaseExportingException(e);
        }
    }

    @Override
    public String getMimeContentType() {
        return MIME_CONTENT_TYPE;
    }

    @Override
    public String getContentFileNameExt() {
        return FILE_NAME_EXT;
    }

    private final DatabaseDumpFactory dbDumpFactory;

    private static final String MIME_CONTENT_TYPE = "text/plain";
    private static final String FILE_NAME_EXT = ".sql";
}
