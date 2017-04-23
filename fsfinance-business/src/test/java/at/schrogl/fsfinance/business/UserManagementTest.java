/**
 * FSFinance - WebApp to track daily expenses
 * Copyright © 2017 Fritz Schrogl (fsdev@schrogl.at)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.schrogl.fsfinance.business;

import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException;
import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException.OffendingProperty;
import at.schrogl.fsfinance.business.support.BusinessLogger;
import at.schrogl.fsfinance.business.support.PasswordHasher;
import at.schrogl.fsfinance.persistence.dao.UserDAO;
import at.schrogl.fsfinance.persistence.entity.User;

/**
 * Unit test for {@link UserManagement}
 * <p>
 * This class unit tests {@link UserManagement}. All dependencies of {@link UserManagement} are replaced by mock objects and injected prior test
 * start. Hence no Spring context is created and declarative transaction configuration is ignored.
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 */
public class UserManagementTest {

	private static final String MOCKDATA_USERNAME_COLLISION = "duplicatedUsername";
	private static final String MOCKDATA_EMAIL_COLLISION = "duplicatedEmail";
	private static final User MOCKDATA_SAVED_USER = new User("saveMock", null, null, null);

	@Mock
	private BusinessLogger blSpy;

	@Mock
	private PasswordHasher pwhMock;

	@Mock
	private UserDAO userDaoMock;

	@InjectMocks
	private UserManagement userManagement;

	@BeforeTest
	public void setup() {
		MockitoAnnotations.initMocks(this);
		configurePasswordHandlerMock();
		configureUserDaoMock();
	}

	private void configurePasswordHandlerMock() {
		Mockito.doAnswer(invocationOnMock -> {
			User newUser = (User) invocationOnMock.getArgument(0);
			newUser.setPasswordHash("hashedPassword");
			newUser.setSalt("someSalt");
			return null;
		}).when(pwhMock).populateUserSecrets(Mockito.any(), Mockito.anyString());
	}

	private void configureUserDaoMock() {
		Mockito.when(userDaoMock.findByUsername(MOCKDATA_USERNAME_COLLISION))
				.thenReturn(Optional.of(new User(MOCKDATA_USERNAME_COLLISION, null, null, null)));
		Mockito.when(userDaoMock.findByEmail(MOCKDATA_EMAIL_COLLISION))
				.thenReturn(Optional.of(new User(null, null, null, MOCKDATA_EMAIL_COLLISION)));
		Mockito.when(userDaoMock.save(Mockito.any())).thenReturn(MOCKDATA_SAVED_USER);
	}

	@AfterMethod
	public void teardownMethod() {
		Mockito.validateMockitoUsage();
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testCreateUserAccountArg0Null() throws Exception {
		userManagement.createUserAccount(null, "123");
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testCreateUserAccountArg1Null() throws Exception {
		userManagement.createUserAccount(new User(), null);
	}

	@Test(expectedExceptions = UserAlreadyExistsException.class)
	public void testCreateUserAccountDuplicateUsername() throws Exception {
		User expectedUser = null;
		try {
			expectedUser = new User(MOCKDATA_USERNAME_COLLISION, null, null, null);
			userManagement.createUserAccount(expectedUser, "123");
		} catch (UserAlreadyExistsException actualException) {
			Assert.assertEquals(actualException.getNewUser(), expectedUser);
			Assert.assertEquals(actualException.getReason(), OffendingProperty.USERNAME_NOT_UNIQUE);
			throw actualException;
		}
	}

	@Test(expectedExceptions = UserAlreadyExistsException.class)
	public void testCreateUserAccountDuplicateEmail() throws Exception {
		User expectedUser = null;
		try {
			expectedUser = new User(null, null, null, MOCKDATA_EMAIL_COLLISION);
			userManagement.createUserAccount(expectedUser, "123");
		} catch (UserAlreadyExistsException actualException) {
			Assert.assertEquals(actualException.getNewUser(), expectedUser);
			Assert.assertEquals(actualException.getReason(), OffendingProperty.EMAIL_NOT_UNIQUE);
			throw actualException;
		}
	}

	@Test
	public void testCreateUserAccount() throws Exception {
		User newUser = new User();
		String password = "123";
		User actualUser = userManagement.createUserAccount(newUser, password);

		Assert.assertEquals(actualUser, MOCKDATA_SAVED_USER);
		Mockito.verify(pwhMock).populateUserSecrets(newUser, password);
	}

}
