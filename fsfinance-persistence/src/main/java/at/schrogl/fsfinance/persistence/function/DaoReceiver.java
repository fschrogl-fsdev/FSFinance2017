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
package at.schrogl.fsfinance.persistence.function;

import javax.persistence.NoResultException;

import at.schrogl.fsfinance.persistence.dao.AbstractDAO;

/**
 * Functional interface for wrapping JPA single result queries.
 * <p>
 * The single function is allowed to throw {@link NoResultException}, hence a lambda function is also allowed to throw this exception. The
 * interface is used to execute JPA single result queries as lambda functions to reduce some boilderplate code in DAOs.
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 *
 * @param <E>
 *            The JPA entity type returned by this interface's method.
 * 
 * @see AbstractDAO
 */
@FunctionalInterface
public interface DaoReceiver<E> {

	E retrieve() throws NoResultException;

}
