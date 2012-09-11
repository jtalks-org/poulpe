/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jtalks.poulpe.web.controller.zkmacro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import org.jtalks.common.model.entity.Group;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author Куюнко
 */
public class DualListVmTest extends TestCase {
    
    private DualListVm dualListVm;
    
    public DualListVmTest(String testName) {
        super(testName);
    }
    
    /**
     * Test of initVm method, of class DualListVm.
     */
    @Test(dataProvider = "dataProviderLists")
    public void testInitVm(List<Group> fullList, List<Group> rightList) {
        dualListVm = new DualListVm();
        dualListVm.initVm(fullList, rightList);
    }

    /**
     * Test of add method, of class DualListVm.
     */
    @Test(dataProvider = "dataProviderLists")
    public void testAddAll(List<Group> fullList,List<Group> leftList) {
        initTest(fullList, leftList);
        dualListVm.addAll();
        assertEquals(dualListVm.getRight().getSize(), 3);
    }
    private void initTest(List<Group> fullList,List<Group> leftList){
        dualListVm = new DualListVm();
        dualListVm.initVm(fullList, leftList);
        assertEquals(dualListVm.getLeft().size(), 1);
    }
    
    @DataProvider
    public Object[][] dataProviderLists() {
        List<Group> fullList = new ArrayList<Group>();
        List<Group> leftList = new ArrayList<Group>();
        
        fullList.add(new Group("1"));
        fullList.add(new Group("2"));
        fullList.add(new Group("3"));
        
        leftList.add(new Group("1"));
        
        return new Object[][] {{fullList, leftList}};
    }    
}
