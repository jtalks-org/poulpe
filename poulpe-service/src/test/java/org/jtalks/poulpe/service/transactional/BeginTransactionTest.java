package org.jtalks.poulpe.service.transactional;

import org.jtalks.common.security.acl.AclManager;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.UserDao;
import org.jtalks.poulpe.model.entity.PoulpeUser;
import org.jtalks.poulpe.model.logic.UserBanner;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = {"classpath:/org/jtalks/poulpe/model/entity/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BeginTransactionTest extends AbstractTransactionalTestNGSpringContextTests {

    // sut
    private TransactionalUserService userService;
    // dependencies
    private UserDao userDao;
    private ComponentDao componentDaoMock;
    private AclManager aclManagerMock;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDao = mock(UserDao.class);
        componentDaoMock = mock(ComponentDao.class);
        aclManagerMock = mock(AclManager.class);
        userService = new TransactionalUserService(userDao, mock(UserBanner.class), aclManagerMock, componentDaoMock);
    }

    @Test(enabled = false)
    public void testDryRunRegistration() throws Exception {
        when(userDao.getByUsername(any(String.class))).thenReturn(null);
        when(userDao.getByEmail(any(String.class))).thenReturn(null);
        doNothing().when(userDao).save(any(PoulpeUser.class));
        userService.dryRunRegistration(TransactionalUserServiceTest.user());
    }
}