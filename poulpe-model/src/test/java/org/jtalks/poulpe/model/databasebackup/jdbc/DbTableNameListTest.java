package org.jtalks.poulpe.model.databasebackup.jdbc;

import static org.testng.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * The class tests {@link DbTableNameList} by performing tests: (1) if a list returned from
 * DatabaseTableList.getPlainList contains all entries; (2) if method DatabaseTableList.getIndependentList resolves
 * table dependencies.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableNameListTest {
    private EmbeddedDatabase dataSource;
    private List<String> expectedIndependentList;

    /**
     * The method performs next actions:
     * 
     * (1) Setups an in-memory database and initialize db schema by applying poulpe-model/src/test/resources/schema.sql.
     * (2) Defines an expectedIndependentList. ExpectedIndependentList contains list of table names in independent
     * order, so tables at the top of the list don't depend on table on the bottom (this order is defined in the
     * schema.sql).
     */
    @BeforeClass
    private void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addScript("schema.sql").build();
        expectedIndependentList = Arrays.asList("acl_class", "acl_sid", "base_components", "common_schema_version",
                "components", "groups", "persistent_logins", "poulpe_schema_version", "ranks", "sections",
                "topic_types", "users", "acl_object_identity", "acl_entry", "branches", "default_properties",
                "group_user_ref", "properties");
    }

    /**
     * The method tests if {@link DatabaseTableList.getList()} enumerates and returns the whole list of tables.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void tableListReturnsAllTableNames() throws SQLException {
        List<String> expectedList = Lists.newArrayList(
                Lists.transform(expectedIndependentList, new UpperCaseFunction()));
        Collections.sort(expectedList);

        List<String> actualList = Lists.newArrayList(
                Lists.transform(DbTableNameList.getPlainList(dataSource), new UpperCaseFunction()));
        Collections.sort(actualList);

        assertEquals(actualList, expectedList);
    }

    /**
     * The method tests if {@link DatabaseTableList.getList()} enumerates and returns tables in the order so tables on
     * the top of the list are not dependent on the tables from the bottom.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void tableListResolvesDependencies() throws SQLException {
        List<String> expectedList = Lists.transform(expectedIndependentList, new UpperCaseFunction());
        List<String> actualList = Lists.transform(DbTableNameList.getIndependentList(dataSource),
                new UpperCaseFunction());

        assertEquals(actualList, expectedList);
    }

    /**
     * Closes previously opened resources such as database connection.
     */
    @AfterClass
    public void tearDown() {
        dataSource.shutdown();
    }

    /**
     * A simple function for convert list of Strings to list of upper-cased strings. Having this transformation we can
     * compare 2 lists in case-independent way.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    private static final class UpperCaseFunction implements Function<String, String> {
        @Override
        public String apply(final String arg0) {
            return arg0.toUpperCase();
        }

    }
}
