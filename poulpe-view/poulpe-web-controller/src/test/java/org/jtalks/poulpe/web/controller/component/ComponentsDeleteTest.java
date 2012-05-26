//package org.jtalks.poulpe.web.controller.component;
//
//import org.jtalks.common.model.entity.Component;
//import org.jtalks.common.model.entity.ComponentType;
//import org.jtalks.poulpe.model.entity.Jcommune;
//import org.jtalks.poulpe.service.ComponentService;
//import org.testng.annotations.Test;
//
//import static org.mockito.Matchers.any;
//import static org.testng.Assert.assertTrue;
//
///**
//* @author Leonid Kazancev
//*/
//public class ComponentsDeleteTest {
//    public void setComponentService(ComponentService componentService) {
//        this.componentService = componentService;
//    }
//
//    private ComponentService componentService;
//
//
//    @Test
//    public void testDeleteJcommune() {
//        Component jcommune = null;
//
//        if (!componentService.getAvailableTypes().contains(any(Jcommune.class))) {
//            jcommune = new Jcommune();
//        }
//        jcommune = componentService.getByType(ComponentType.FORUM);
//        componentService.deleteComponent(jcommune);
//
//        assertTrue(componentService.getByType(ComponentType.FORUM).equals(null));
//    }
//
//}
