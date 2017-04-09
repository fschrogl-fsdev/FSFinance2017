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
package at.schrogl.fsfinance.business.support;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.schrogl.fsfinance.business.configuration.AppConfig;
import at.schrogl.fsfinance.business.configuration.ConfigOption;
import at.schrogl.fsfinance.persistence.entity.User;

@Component
public class PasswordHasher {

	private static final Logger TRACE_LOG = LoggerFactory.getLogger(PasswordHasher.class);

	@Autowired
	private AppConfig config;

	private SecureRandom random;;
	private MessageDigest hasher;

	@PostConstruct
	public void initialize() throws NoSuchAlgorithmException {
		TRACE_LOG.info("Initializing RNG and password digester with algorithm {}", config.getConfigValue(ConfigOption.PASSWORD_HASH_ALGO));
		this.random = new SecureRandom();
		this.hasher = MessageDigest.getInstance(config.getConfigValue(ConfigOption.PASSWORD_HASH_ALGO));
	}

	public void populateUserSecrets(User newUser, String password) {
		TRACE_LOG.debug("Generating salt and hashed password for {}", newUser);
		newUser.setSalt(generateRandomSalt());
		newUser.setPasswordHash(hashPassword(password, newUser.getSalt()));
	}

	public String generateRandomSalt() {
		byte[] randomBytes = new byte[16];
		random.nextBytes(randomBytes);
		String salt = Base64.getUrlEncoder().encodeToString(randomBytes);
		TRACE_LOG.debug("Generated salt {} using SecureRandom with algorithm {}", salt, random.getAlgorithm());
		return salt;
	}

	public String hashPassword(String password, String salt) {
		Objects.requireNonNull(password);
		Objects.requireNonNull(salt);

		hasher.reset();
		hasher.update(password.getBytes(StandardCharsets.UTF_8));
		hasher.update(salt.getBytes(StandardCharsets.UTF_8));
		String hashedPassword = Base64.getUrlEncoder().encodeToString(hasher.digest());

		TRACE_LOG.debug("Hashed password and salt using {}: {}{} => {}", hasher.getAlgorithm(), password, salt, hashedPassword);
		return hashedPassword;
	}
}
