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
package org.jtalks.poulpe.web.controller.zkutils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;

import static org.testng.Assert.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Nickolay Polyarniy
 */
public class BooleanStringConverterTest {

    BooleanStringConverter boolStrConverter = new BooleanStringConverter();

    @Test
    public void testStrToBoolTrue() throws Exception {
        String sTrue = "TrUe";
        assertEquals(boolStrConverter.coerceToUi(sTrue, null, null), true);
    }

    @Test
    public void testStrToBoolFalse() throws Exception {
        String sFalse = "fALSe";
        assertEquals(boolStrConverter.coerceToUi(sFalse, null, null), false);
    }

    @Test
    public void testBoolToStrTrue() throws Exception {
        boolean bool = true;
        assertEquals(boolStrConverter.coerceToBean(bool, null, null), "true");
    }

    @Test
    public void testBoolToStrFalse() throws Exception {
        boolean bool = false;
        assertEquals(boolStrConverter.coerceToBean(bool, null, null), "false");
    }

    @Test
    public void testObjectBoolToStrTrue() throws Exception {
        Boolean bool = true;
        assertEquals(boolStrConverter.coerceToBean(bool, null, null), "true");
    }

    @Test
    public void testObjectBoolToStrFalse() throws Exception {
        Boolean bool = false;
        assertEquals(boolStrConverter.coerceToBean(bool, null, null), "false");
    }
}
