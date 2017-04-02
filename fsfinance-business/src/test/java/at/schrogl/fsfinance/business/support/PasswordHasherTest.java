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
package at.schrogl.fsfinance.business.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import at.schrogl.fsfinance.business.configuration.AppConfig;
import at.schrogl.fsfinance.business.configuration.ConfigOption;
import at.schrogl.fsfinance.persistence.entity.User;

public class PasswordHasherTest {

	@Mock
	private AppConfig configMock;

	@InjectMocks
	private PasswordHasher passwordHandler;

	@BeforeTest
	public void setup() throws NoSuchAlgorithmException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(configMock.getConfigValue(ConfigOption.PASSWORD_HASH_ALGO))
				.thenReturn(ConfigOption.PASSWORD_HASH_ALGO.getDefaultValue());

		passwordHandler.initialize();
	}

	@Test
	public void testGenerateRandomSalt() {
		String testSalt = passwordHandler.generateRandomSalt();

		Assert.assertFalse(testSalt == null || testSalt.isEmpty(), "Returned salt was null or empty!");
		try {
			Base64.getUrlDecoder().decode(testSalt);
		} catch (IllegalArgumentException e) {
			Assert.fail("Returned salt was not a valid URL-safe Base64 string. Salt: " + testSalt + " Error: " + e.getMessage());
		}
	}

	@Test(expectedExceptions = NullPointerException.class)
	public void testHashPasswordNull() {
		passwordHandler.hashPassword(null, null);
	}

	@Test
	public void testHashPassword() {
		String password = "123password";
		String salt = "34534ABGH=";
		String actualHashedPassword = passwordHandler.hashPassword(password, salt);

		Assert.assertFalse(actualHashedPassword == null || actualHashedPassword.isEmpty(), "Hashed password was null or empty!");
		try {
			Base64.getUrlDecoder().decode(actualHashedPassword);
		} catch (IllegalArgumentException e) {
			Assert.fail("Returned hashed password was not a valid URL-safe Base64 string. Hashed Password: " + actualHashedPassword + " Error: "
					+ e.getMessage());
		}

		try {
			MessageDigest hasher = MessageDigest.getInstance(configMock.getConfigValue(ConfigOption.PASSWORD_HASH_ALGO));
			byte[] digest = hasher.digest((password + salt).getBytes());
			String expectedHashedPassword = Base64.getUrlEncoder().encodeToString(digest);
			Assert.assertEquals(actualHashedPassword, expectedHashedPassword);
		} catch (NoSuchAlgorithmException e) {
			Assert.fail("Error initializing message digester. Unable to execute test. Error: " + e.getMessage());
		}
	}

	@Test
	public void testPopulateUserSecrets() {
		User newUser = new User("testUser", null, null, "user@mail.domain");
		passwordHandler.populateUserSecrets(newUser, "123password");

		Assert.assertFalse(newUser.getSalt() == null || newUser.getSalt().isEmpty(), "Salt must not be null or empty!");
		Assert.assertFalse(newUser.getPassword() == null || newUser.getPassword().isEmpty(), "Password must not be null or empty!");
	}

}
