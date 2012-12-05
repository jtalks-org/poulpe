package org.jtalks.poulpe.service.jmx;

import java.util.List;

/** Take from <a href="http://www.sureshpw.com/2012/04/dynamic-logging-with-log4j.html">here</a>. */
public interface Log4jConfiguratorMXBean {
    /** list of all the logger names and their levels */
    List<String> getLoggers();

    /** Get the log level for a given logger */
    String getLogLevel(String logger);

    /** Set the log level for a given logger */
    void setLogLevel(String logger, String level);
}

