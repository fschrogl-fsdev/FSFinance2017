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

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException;
import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException.OffendingProperty;
import at.schrogl.fsfinance.business.support.BusinessLogger;
import at.schrogl.fsfinance.business.support.PasswordHasher;
import at.schrogl.fsfinance.persistence.dao.UserDAO;
import at.schrogl.fsfinance.persistence.entity.User;

@Component
public class UserManagement {

	private static final Logger TRACE_LOG = LoggerFactory.getLogger(UserManagement.class);

	@Autowired
	private BusinessLogger appLog;

	@Autowired
	private PasswordHasher pwdHasher;

	@Autowired
	private UserDAO userDao;

	@Transactional
	public User createUserAccount(User newUser, String password) throws UserAlreadyExistsException {
		Objects.requireNonNull(newUser, "New user must not be null!");
		Objects.requireNonNull(password, "User's password must not be null!");

		Optional<User> existingUser = userDao.findByUsername(newUser.getUsername());
		if (existingUser.isPresent()) {
			TRACE_LOG.debug("Unable to create new user {}. Username already exists in database!", newUser);
			throw new UserAlreadyExistsException(newUser, OffendingProperty.USERNAME_NOT_UNIQUE);
		}
		existingUser = userDao.findByEmail(newUser.getEmail());
		if (existingUser.isPresent()) {
			TRACE_LOG.debug("Unable to create new user {}. Email address already exists in database!", newUser);
			throw new UserAlreadyExistsException(newUser, OffendingProperty.EMAIL_NOT_UNIQUE);
		}

		pwdHasher.populateUserSecrets(newUser, password);
		newUser = userDao.save(newUser);
		appLog.info("SYSTEM", "New user successfully registered: {}", newUser);
		TRACE_LOG.info("New user registered/added: {}", newUser);

		return newUser;
	}

}
