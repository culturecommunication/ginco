package fr.mcc.ginco.tests.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import fr.mcc.ginco.security.LockoutService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml"
})
public class LockoutServiceTest {
	
	@Inject
	@Named("lockoutService")
	private LockoutService lockOutService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public final void TestAccoutLockout()
	{
		for (int i=0;i<lockOutService.getMaxAttemps();i++) {
			lockOutService.notifyLoginFailure("test", System.currentTimeMillis());
		}
		Assert.isTrue(lockOutService.isLockedOut("test"));
		lockOutService.notifyLoginSuccess("test");
		Assert.isTrue(!lockOutService.isLockedOut("test"));
	}

}
