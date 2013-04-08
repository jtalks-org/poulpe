package org.jtalks.poulpe.util.databasebackup.common.collection;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

public class SetsTest {

    @Test
    public void newHashSetTest() {
        Set<String> sut = Sets.newHashSet();
        assertTrue(sut instanceof HashSet);
        assertEquals(sut.size(), 0);
    }

    @Test
    public void newHashSetCollectionTTest() {
        List<String> expected = Arrays.asList("1", "2", "3");
        Set<String> sut = Sets.newHashSet(expected);
        assertTrue(sut.containsAll(expected));
        assertTrue(expected.containsAll(sut));
    }
}
