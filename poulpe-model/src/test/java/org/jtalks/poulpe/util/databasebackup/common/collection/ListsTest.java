package org.jtalks.poulpe.util.databasebackup.common.collection;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.Test;

public class ListsTest {

    @Test
    public void newArrayListTest() {
        List<String> sut = Lists.newArrayList();
        assertTrue(sut instanceof ArrayList);
        assertEquals(sut.size(), 0);
    }

    @Test
    public void newArrayListTTest() {
        List<String> expected = Arrays.asList("1", "2", "3");
        List<String> sut = Lists.newArrayList(expected.get(0), expected.get(1), expected.get(2));
        assertEquals(sut, expected);
    }

    @Test
    public void newLinkedListTest() {
        List<String> sut = Lists.newLinkedList();
        assertTrue(sut instanceof LinkedList);
        assertEquals(sut.size(), 0);
    }
}
