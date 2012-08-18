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

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dionis
 *         7/6/12 2:49 AM
 */
public class Launcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(8080);

        server.addConnector(connector);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
//        webAppContext.setWar("src/main/webapp");
        webAppContext.setWar("poulpe-view/poulpe-web-view/src/main/webapp");
        final HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(new Handler[]{webAppContext});
        server.setHandler(handlerList);

        server.start();
        long end = System.currentTimeMillis();
        LOGGER.info("Initialization took {} ms", end-start);
        server.join();
    }
}
