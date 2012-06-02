package org.jtalks.poulpe.web.osop;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

import java.util.List;

/**
 * @author stanislav bashkirtsev
 */
public class OpenSessionOnPageZkListener implements ExecutionCleanup, ExecutionInit {
    private final OpenSessions openSessions = SingletonOpenSessionsHolder.getOpenSessions();

    @Override
    public void init(Execution exec, Execution parent) throws Exception {
        String desktopId = exec.getDesktop().getId();
        openSessions.openSession(desktopId);
    }

    @Override
    public void cleanup(Execution exec, Execution parent, List<Throwable> errs) throws Exception {
        String desktopId = exec.getDesktop().getId();
        openSessions.openSession(desktopId);
    }
}
