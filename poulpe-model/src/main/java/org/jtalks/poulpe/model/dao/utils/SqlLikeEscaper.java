package org.jtalks.poulpe.model.dao.utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Escapes symbols like {@code %, _, !} which are not being escaped by Prepared Statements in <i>like</i> statements.
 * For instance if we have a query {@code from Entity where name like ?}, then if we pass % sign, it will return all the
 * records from table even though we were searching only for a name which contains % symbol. In such situations you can
 * use this utility to escape those symbols if you need this.
 *
 * @author Anton Kolyaev
 */
public final class SqlLikeEscaper {
    private static final List<String> CONTROL_SYMBOLS = new ArrayList<String>();

    static {
        CONTROL_SYMBOLS.add("%");//anything
        CONTROL_SYMBOLS.add("_");//anyone
        CONTROL_SYMBOLS.add("!");//not
        CONTROL_SYMBOLS.add("^");//not
        CONTROL_SYMBOLS.add("[");//array_start
        CONTROL_SYMBOLS.add("]");//array_finish
    }

    /**
     * Just a utility, should not been instantiated.
     */
    private SqlLikeEscaper() {
    }

    /**
     * Escapes control characters for SQL query. Will do nothing if null or empty string was passed.
     *
     * @param toEscape string to be escaped
     * @return the escaped version of the specified string
     */
    public static String escapeControlCharacters(@Nullable String toEscape) {
        if(toEscape == null || toEscape.isEmpty()){
            return toEscape;
        }
        for (String controlSymbol : CONTROL_SYMBOLS) {
            toEscape = toEscape.replace(controlSymbol, "\\" + controlSymbol);
        }
        return toEscape;
    }
}
