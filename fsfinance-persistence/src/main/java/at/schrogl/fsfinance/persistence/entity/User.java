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
package at.schrogl.fsfinance.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import at.schrogl.fsfinance.persistence.dao.UserDAO;

@Entity
@Table(name = "USERS")
@NamedQueries({
		@NamedQuery(name = UserDAO.NQ_findByUsername, query = "SELECT u FROM User u WHERE u.username = :username"),
		@NamedQuery(name = UserDAO.NQ_findByEmail, query = "SELECT u FROM User u WHERE u.email = :email") })
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String passwordHash;
	private String salt;
	private String email;

	public User() {
	}

	public User(String username, String password, String salt, String email) {
		this.username = username;
		this.passwordHash = password;
		this.salt = salt;
		this.email = email;
	}

	@Id
	@GeneratedValue
	@Column(name = "usr_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Size(min = 5, max = 25)
	@Column(name = "usr_username", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@NotNull
	@Column(name = "usr_passwordHash", nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@NotNull
	@Column(name = "usr_salt", nullable = false)
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@NotNull
	@Pattern(regexp = ".+@.+")
	@Column(name = "usr_email", unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return String.format("User[id=%d/username=%s]", id, username);
	}

}
