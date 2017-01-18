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

import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import at.schrogl.fsfinance.persistence.model.User;

@Repository
public class UserDAO extends AbstractDAO<User> {

	public static final String NQ_findByUsername = "User_findByUsername";
	public static final String NQ_findByEmail = "User_findByEmail";

	@Override
	public User find(Long id) {
		return em.find(User.class, id);
	}

	@Transactional
	public Optional<User> findByUsername(String username) {
		TypedQuery<User> query = em.createNamedQuery(NQ_findByUsername, User.class);
		query.setParameter("username", username);

		try {
			User foundUser = query.getSingleResult();
			return Optional.of(foundUser);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Transactional
	public Optional<User> findByEmail(String email) {
		TypedQuery<User> query = em.createNamedQuery(NQ_findByEmail, User.class);
		query.setParameter("email", email);

		try {
			User foundUser = query.getSingleResult();
			return Optional.of(foundUser);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

}
