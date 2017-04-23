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
package at.schrogl.fsfinance.persistence.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import at.schrogl.fsfinance.persistence.entity.User;

@Repository
public class UserDAO extends AbstractDAO<User> {

	public static final String NQ_findByUsername = "User_findByUsername";
	public static final String NQ_findByEmail = "User_findByEmail";

	@Override
	public Optional<User> find(Long id) {
		return Optional.ofNullable(em.find(User.class, id));
	}

	public Optional<User> findByUsername(String username) {
		checkIllegalArgument(username);
		return wrapAsOptional(() -> em.createNamedQuery(NQ_findByUsername, User.class)
				.setParameter("username", username)
				.getSingleResult());
	}

	public Optional<User> findByEmail(String email) {
		checkIllegalArgument(email);
		return wrapAsOptional(() -> em.createNamedQuery(NQ_findByEmail, User.class)
				.setParameter("email", email)
				.getSingleResult());
	}

}
