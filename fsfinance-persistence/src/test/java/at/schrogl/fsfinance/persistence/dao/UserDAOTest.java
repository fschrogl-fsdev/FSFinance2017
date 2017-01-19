package at.schrogl.fsfinance.persistence.dao;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:springcontext-test.xml")
public class UserDAOTest extends AbstractTestNGSpringContextTests {

	@Inject
	private UserDAO userDao;

	@Test
	public void testFindLong0L() {
		Assert.assertNull(userDao.find(0L));
	}
	
}
