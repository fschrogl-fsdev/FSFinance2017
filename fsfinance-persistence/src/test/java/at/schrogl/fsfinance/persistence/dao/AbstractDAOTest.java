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

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import at.schrogl.fsfinance.persistence.entity.User;

@ContextConfiguration("classpath:applicationContext-test.xml")
public class AbstractDAOTest extends AbstractTransactionalTestNGSpringContextTests {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private TestDao abstractDao;

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCheckIllegalArgumentSingle() {
		abstractDao.checkIllegalArgument((Object) null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCheckIllegalArgumentMultiple() {
		abstractDao.checkIllegalArgument(null, null);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testCheckIllegalArgumentMixed() {
		abstractDao.checkIllegalArgument("value", null);
	}

	@Test(expectedExceptions = NoSuchElementException.class)
	public void testWrapAsOptionalNull() {
		Optional<User> actual = abstractDao.wrapAsOptional(() -> {
			return null;
		});

		actual.get();
	}

	@Test
	public void testWrapAsOptional() {
		User expected = em.merge(new User("testUser", "123456789", "salt", "you@mail.domain"));
		Optional<User> actual = abstractDao.wrapAsOptional(() -> em.find(User.class, expected.getId()));

		Assert.assertEquals(actual.get(), expected);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testSaveNull() {
		abstractDao.save(null);
	}

	@Test
	public void testSave() {
		Assert.assertEquals(countRowsInTable("users"), 0, "Expected empty database!");

		User actual = abstractDao.save(new User("testUser", "123456789", "salt", "you@mail.domain"));
		em.flush();

		Assert.assertEquals(countRowsInTable("users"), 1, "Entity was not persisted!");
		Assert.assertNotNull(actual, "Persist method did not return entity!");
		Assert.assertNotNull(actual.getId(), "Entity has no ID set!");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testRemoveNull() {
		abstractDao.remove(null);
	}

	@Test
	public void testRemove() {
		User expected = em.merge(new User("testUser", "123456789", "salt", "you@mail.domain"));
		em.flush();

		Assert.assertEquals(countRowsInTable("users"), 1, "Entity for test was not persisted!");

		User actual = abstractDao.remove(expected);
		em.flush();

		Assert.assertEquals(countRowsInTable("users"), 0, "Entity for test was not deleted!");
		Assert.assertNotNull(actual, "Delete methode did not return entity!");
	}

	/* 
	 * ********** TestDao *********
	 */

	@Repository
	static class TestDao extends AbstractDAO<User> {

		@Override
		public Optional<User> find(Long id) {
			return Optional.ofNullable(em.find(User.class, id));
		}

	}
}
