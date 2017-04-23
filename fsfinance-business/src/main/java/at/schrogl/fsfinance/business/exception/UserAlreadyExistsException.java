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
package at.schrogl.fsfinance.business.exception;

import java.util.Objects;

import at.schrogl.fsfinance.persistence.entity.User;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public enum OffendingProperty {
		USERNAME_NOT_UNIQUE,
		EMAIL_NOT_UNIQUE;
	}

	private final OffendingProperty reason;
	private final User newUser;

	public UserAlreadyExistsException(User newUser, OffendingProperty reason) {
		this.newUser = Objects.requireNonNull(newUser);
		this.reason = Objects.requireNonNull(reason);
	}

	public User getNewUser() {
		return newUser;
	}

	public OffendingProperty getReason() {
		return reason;
	}

	@Override
	public String getMessage() {
		return String.format("Unable to create new user %s database constraint violated! Reason: %s", newUser, reason);
	}

}
