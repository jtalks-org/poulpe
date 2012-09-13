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


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Is created in order to find out if we changed the classes or Spring contexts per se so that it won't start up. Usual
 * tests won't show this kind of problems because they don't instantiate app contexts, thus the only way to figure out
 * that the contexts are being created correctly is this test.
 *
 * @author Evgeny Surovtsev
 */
public class ApplicationContextTest {
    @Test
    public void applicationContextShouldConstructAllBeans() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "/org/jtalks/poulpe/model/entity/applicationContext-dao.xml",
                "/org/jtalks/poulpe/service/applicationContext-service.xml",
                "/org/jtalks/poulpe/service/applicationContext-service-security.xml",
                "/org/jtalks/poulpe/web/controller/applicationContext-controller.xml",
                "classpath:*/WEB-INF/applicationContext-web-view-security.xml"
        );
        assertNotNull(ctx);
    }
}


