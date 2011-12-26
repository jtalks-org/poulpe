package org.jtalks.poulpe.web.controller.section;

import static org.testng.Assert.assertTrue;

import org.jtalks.poulpe.model.entity.Section;
import org.testng.annotations.Test;

public class ExtendedTreeNodeTest {

 
  @Test
  public void testNode() {
	  Section entity = new Section();
	  entity.setName("test");
	  entity.setDescription("test");
	  ExtendedTreeNode treeNode = new ExtendedTreeNode(entity);
	  assertTrue(treeNode.getData() instanceof Section);
	  assertTrue(treeNode.isExpanded());
	  treeNode.setExpanded(false);
	  assertTrue(!treeNode.isExpanded());
  }	

  
}
