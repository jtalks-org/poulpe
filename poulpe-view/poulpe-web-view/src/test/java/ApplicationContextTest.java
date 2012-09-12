/**
 * Copyright (C) 2012  JTalks.org Team
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


import static org.testng.Assert.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * @author Evgeny Surovtsev
 */
public class ApplicationContextTest {
    @Test
    public void testApplicationContextConfigurations() {
        ApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] {
        		"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml",
        		"classpath:/org/jtalks/poulpe/service/applicationContext-service.xml",
        		"classpath:/org/jtalks/poulpe/service/applicationContext-service-security.xml",
        		"classpath:/org/jtalks/poulpe/web/controller/applicationContext-controller.xml",
        		"/src/main/webapp/WEB-INF/applicationContext-web-view-security.xml"
        });
        assertNotNull(ctx);
    }
}


