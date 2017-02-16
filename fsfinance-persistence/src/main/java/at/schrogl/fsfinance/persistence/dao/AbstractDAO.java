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

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.schrogl.fsfinance.persistence.function.DaoReceiver;

@Repository
@Transactional(readOnly = true)
public abstract class AbstractDAO<E> {

	@PersistenceContext
	protected EntityManager em;

	protected void checkIllegalArgument(Object value) {
		if (value == null)
			throw new IllegalArgumentException("'null' is an illegal argument!");
	}

	protected void checkIllegalArgument(Object... value) {
		if (value == null || Arrays.asList(value).stream().anyMatch(v -> v == null))
			throw new IllegalArgumentException("'null' is an illegal argument!");
	}

	protected Optional<E> wrapAsOptional(DaoReceiver<E> daoFunction) {
		try {
			return Optional.ofNullable(daoFunction.retrieve());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public abstract Optional<E> find(Long id);

	@Transactional(readOnly = false)
	public E save(E entity) {
		return em.merge(entity);
	}

	@Transactional(readOnly = false)
	public E remove(E entity) {
		em.remove(entity);
		return entity;
	}

}
