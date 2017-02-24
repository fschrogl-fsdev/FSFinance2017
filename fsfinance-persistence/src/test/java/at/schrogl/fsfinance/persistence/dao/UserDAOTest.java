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
package at.schrogl.fsfinance.persistence.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.schrogl.fsfinance.persistence.model.User;

@ContextConfiguration("classpath:springcontext-test.xml")
@Sql(scripts = "classpath:/testdata/UserDAOTest.sql")
public class UserDAOTest extends AbstractTransactionalTestNGSpringContextTests {

	public static final String TABLENAME_USER = "users";

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserDAO userDao;

	@BeforeMethod
	private void setup() {
		Assert.assertNotEquals(countRowsInTable(TABLENAME_USER), 0, "DB must be pre-filled with at least one user!");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFindNull() {
		userDao.find(null);
	}

	@Test(expectedExceptions = NoSuchElementException.class)
	public void testFindInvalid() {
		Optional<User> actual = userDao.find(-1L);
		Assert.assertFalse(actual.isPresent(), "Optional is not empty!");
		actual.get();
	}

	@Test
	public void testFindUser() {
		Map<Long, User> usersById = new HashMap<>();
		em.createQuery("from User", User.class).getResultList().forEach(u -> usersById.put(u.getId(), u));
		Assert.assertEquals(usersById.size(), countRowsInTable(TABLENAME_USER));

		for (Entry<Long, User> userEntry : usersById.entrySet()) {
			Optional<User> actualUser = userDao.find(userEntry.getKey());
			Assert.assertTrue(actualUser.isPresent(), "Optional must not be empty!");
			Assert.assertEquals(actualUser.get(), userEntry.getValue());
		}
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFindByUsernameNull() {
		userDao.findByUsername(null);
	}

	@Test(expectedExceptions = NoSuchElementException.class)
	public void testFindByUsernameInvalid() {
		Optional<User> actual = userDao.findByUsername("");
		actual.get();
	}

	@Test
	public void testFindByUsername() {
		Map<String, User> usersById = new HashMap<>();
		em.createQuery("from User", User.class).getResultList().forEach(u -> usersById.put(u.getUsername(), u));
		Assert.assertEquals(usersById.size(), countRowsInTable(TABLENAME_USER));

		for (Entry<String, User> userEntry : usersById.entrySet()) {
			Optional<User> actualUser = userDao.findByUsername(userEntry.getKey());
			Assert.assertTrue(actualUser.isPresent(), "Optional must not be empty!");
			Assert.assertEquals(actualUser.get(), userEntry.getValue());
		}
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testFindByEmailNull() {
		userDao.findByEmail(null);
	}

	@Test(expectedExceptions = NoSuchElementException.class)
	public void testFindByEmailInvalid() {
		Optional<User> actual = userDao.findByEmail("");
		actual.get();
	}

	@Test
	public void testFindByEmail() {
		Map<String, User> usersById = new HashMap<>();
		em.createQuery("from User", User.class).getResultList().forEach(u -> usersById.put(u.getEmail(), u));
		Assert.assertEquals(usersById.size(), countRowsInTable(TABLENAME_USER));

		for (Entry<String, User> userEntry : usersById.entrySet()) {
			Optional<User> actualUser = userDao.findByEmail(userEntry.getKey());
			Assert.assertTrue(actualUser.isPresent(), "Optional must not be empty!");
			Assert.assertEquals(actualUser.get(), userEntry.getValue());
		}
	}

}
