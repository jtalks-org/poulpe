package org.jtalks.poulpe.web.osod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

import java.util.List;

/**
 * As an opposite to OSIV filter that closes the Hibernate Session when request is finished to be processed, this one
 * (usually called Session per Conversation) allows the Session to live until ZK {@link Desktop} is cleaned and removed
 * which happens when user refreshes the page or timeout happens or the browser is closed. How it works: <ol><li>When a
 * thread starts to process the HTTP request, a new Hibernate Session is opened and associated with the Desktop. It also
 * binds the session with current thread so that Spring Transaction Manager finds it and uses this session to work with
 * Hibernate.</li><li>When thread stops processing the thread, the Session is unbound from the thread (because it might
 * be reused in future for other users and desktops). But the Session is not closed, it stays until desktop lives.</li>
 * <li>When new request reaches the server, the session is found by the same Desktop ID and we bind it to the new thread
 * again. After thread finishes, we do the same as in previous bullet.</li><li>When ZK Desktop dies, we close the
 * session.</li></ol><br/><b>Justification:</b><br/> In ZK applications it's not very convenient to use <a
 * href="https://community.jboss.org/wiki/OpenSessionInView">Open Session In View</a>, because this would effectively
 * mean we need to close the session after each request (AJAX as well). But <ul> <li>in ZK apps we don't reload the
 * whole page - we might open some dialogs which would require lazy fields to be loaded - but this will be a separate
 * request and the original Hibernate Session that loaded original objects is already closed, and this will cause {@link
 * org.hibernate.LazyInitializationException}. </li> <li>when session is closed and we need to open dialogs, this would
 * lead the same data being loaded twice or thrice or even more. The leaks of memory here is not a big deal usually, but
 * when we try to associate different objects loaded in different sessions, we risk to have exceptions while saving the
 * objects - Hibernate will recognize that the objects were loaded 2 times and won't allow to save them until only one
 * instance is associated with the session.</li><ul/><br/> <b>Possible Issues:</b><br/> The fact that we keep sessions
 * opened means that at some point they might contain too many objects - this would both lead to Hibernate work slower
 * and increasing the risks of out-of-memory exception. Thus when using it, we need to figure out at what point we need
 * to clear the session. This question is to be solved in the future.
 *
 * @author stanislav bashkirtsev
 * @see OpenSessions
 */
public class OpenSessionOnDesktopZkListener implements ExecutionInit, DesktopCleanup, ExecutionCleanup {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OpenSessions openSessions;

    /**
     * Creates a ZK Listener that uses {@link OpenSessions} provided by {@link SingletonOpenSessionsHolder} which should
     * be initialized by Spring Context beforehand. If you find a solution that allows to inject beans from the Spring
     * Context directly to the ZK listeners, this constructor should be removed, but as for now this is the only way to
     * create {@link OpenSessions} in Spring Context and then the same instance to be used in ZK Listeners out of Spring
     * Contexts.
     */
    public OpenSessionOnDesktopZkListener() {
        this(SingletonOpenSessionsHolder.getOpenSessions());
    }

    /**
     * This constructor should be used in preference to the default one because it gives an ability to provide an
     * instance of {@link OpenSessions} instead of using {@link SingletonOpenSessionsHolder} which smells bad, but as
     * for now we didn't find a way to use Spring IoC in zk.xml so in production {@link #OpenSessionOnDesktopZkListener()}
     * is used.
     *
     * @param openSessions an instance of Hibernate Sessions container to be able to bind/unbind/close sessions
     *                     according to events in ZK App Lifecycle
     */
    public OpenSessionOnDesktopZkListener(OpenSessions openSessions) {
        this.openSessions = openSessions;
    }

    /**
     * Is called when current desktop 'dies' - this happens if user closes the browser, or refreshes the page or the
     * timeout happens. This event means that we need to close the session, but not unbind it - unbinding happens always
     * when thread stops processing the request. <br/> <b>Rest is copied from interface JavaDocs:</b><br/>
     */
    @Override
    public void cleanup(Desktop desktop) throws Exception {
        try {
            openSessions.closeSession(desktop.getId());
        } catch (Exception e) {
            logger.warn("Error closing hibernate session", e);
            throw e;
        }
    }

    /**
     * Is called when thread starts, which means we need to create a Hibernate session if it's not created for current
     * desktop yet and bind it to the current thread. <br/> <b>Rest is copied from interface JavaDocs:</b><br/>
     * {@inheritDoc}
     */
    @Override
    public void init(Execution exec, Execution parent) throws Exception {
        try {
            openSessions.openSession(exec.getDesktop().getId());
        } catch (Exception e) {
            logger.warn("Error opening hibernate session", e);
            throw e;
        }
    }

    /**
     * This method is invoked when thread finishes processing the request, which means that we need to unbind the
     * session from the thread (but not close).<br/> <b>Rest is copied from interface JavaDocs:</b><br/> {@inheritDoc}
     */
    @Override
    public void cleanup(Execution exec, Execution parent, List<Throwable> errs) throws Exception {
        try {
            openSessions.unbindSession();
        } catch (Exception e) {
            logger.warn("Error closing hibernate session", e);
            throw e;
        }
    }
}
