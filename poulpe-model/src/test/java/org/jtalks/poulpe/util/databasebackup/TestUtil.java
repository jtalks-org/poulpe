package org.jtalks.poulpe.util.databasebackup;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

public final class TestUtil {
    private TestUtil() {
    }

    public final static String makeLowerAndRemoveSpaces(final String str) {
        return str.toLowerCase().replaceAll("\\s", "");
    }

    public final static String removeEmptyStringsAndSqlComments(final String actualOutput) {
        Iterator<String> iterator = Iterables.filter(
                Splitter.on(LINEFEED).omitEmptyStrings().trimResults().split(actualOutput),
                new Predicate<String>() {
                    @Override
                    public boolean apply(@Nullable final String arg) {
                        return arg != null && !"--".equals(arg.substring(0, 2));
                    }
                }).iterator();

        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next());
        }
        return result.toString();
    }

    private static final String LINEFEED = "\n";
}
