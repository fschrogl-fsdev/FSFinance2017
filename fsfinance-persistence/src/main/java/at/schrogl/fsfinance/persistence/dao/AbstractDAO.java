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

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractDAO<E> {

	protected EntityManager em;

	public abstract E find(Long id);

	public E save(E entity) {
		return em.merge(entity);
	}

	public E remove(E entity) {
		em.remove(entity);
		return entity;
	}
	
}
