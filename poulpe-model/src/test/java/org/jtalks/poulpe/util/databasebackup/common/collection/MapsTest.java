package org.jtalks.poulpe.util.databasebackup.common.collection;

import static org.testng.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class MapsTest {

    @Test
    public void newHashMapTest() {
        Map<String, String> sut = Maps.newHashMap();
        assertTrue(sut instanceof HashMap);
        assertEquals(sut.size(), 0);
    }
}
