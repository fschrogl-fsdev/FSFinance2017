/**
 * This file is part of FSFinance.
 *
 * FSFinance is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FSFinance is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FSFinance. If not, see <http://www.gnu.org/licenses/>.
 */
package at.schrogl.fsfinance.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException;
import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException.OffendingProperty;
import at.schrogl.fsfinance.persistence.entity.User;

/**
 * Integration test for {@link UserManagement}
 * <p>
 * This class integration tests {@link UserManagement}. For this a full Spring context with entity and transaction managers and an in-memory
 * database are started. Nothin is mocked and also transactions are executed.
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 */
@ContextConfiguration("classpath:applicationContext-test.xml")
public class UserManagementIT extends AbstractTransactionalTestNGSpringContextTests {

	public static final String TABLENAME_USER = "users";

	private User exisitingUser;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserManagement userManagement;

	@BeforeMethod
	public void setup() {
		exisitingUser = em.merge(new User("testUser0", "1234567890", "abcd=", "testUser0@mail.domain"));
		em.flush();

		Assert.assertEquals(exisitingUser, em.find(User.class, exisitingUser.getId()));
		Assert.assertEquals(countRowsInTable(TABLENAME_USER), 1);
	}

	@Test(expectedExceptions = UserAlreadyExistsException.class)
	public void testCreateUserAccountDuplicateUsername() throws Exception {
		User expectedUser = null;
		try {
			expectedUser = new User(exisitingUser.getUsername(), null, null, "another@mail.domain");
			userManagement.createUserAccount(expectedUser, "password");
		} catch (UserAlreadyExistsException actualException) {
			Assert.assertEquals(actualException.getNewUser(), expectedUser);
			Assert.assertEquals(actualException.getReason(), OffendingProperty.USERNAME_NOT_UNIQUE);
			Assert.assertEquals(countRowsInTable(TABLENAME_USER), 1);
			throw actualException;
		}
	}

	@Test(expectedExceptions = UserAlreadyExistsException.class)
	public void testCreateUserAccountDuplicateEmail() throws Exception {
		User expectedUser = null;
		try {
			expectedUser = new User("anotherUsername0", null, null, exisitingUser.getEmail());
			userManagement.createUserAccount(expectedUser, "password");
		} catch (UserAlreadyExistsException actualException) {
			Assert.assertEquals(actualException.getNewUser(), expectedUser);
			Assert.assertEquals(actualException.getReason(), OffendingProperty.EMAIL_NOT_UNIQUE);
			Assert.assertEquals(countRowsInTable(TABLENAME_USER), 1);
			throw actualException;
		}
	}

	@Test
	public void testCreateUserAccount() throws Exception {
		User newUser = new User("testUser1", null, null, "testUser1@mail.domain");
		User actualUser = userManagement.createUserAccount(newUser, "password");
		em.flush();

		Assert.assertNotNull(actualUser.getId());
		Assert.assertNotNull(actualUser.getPasswordHash());
		Assert.assertNotNull(actualUser.getSalt());
		Assert.assertEquals(countRowsInTable(TABLENAME_USER), 2);
	}

}
