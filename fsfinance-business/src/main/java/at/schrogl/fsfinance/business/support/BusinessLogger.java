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
package at.schrogl.fsfinance.business.support;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import at.schrogl.fsfinance.business.configuration.Constants;

@Component
public class BusinessLogger {

	private static final Logger LOG = LoggerFactory.getLogger(Constants.BUSINESS_LOGGER_NAME);

	@PostConstruct
	public void initialize() {
		LOG.info("Business logger initialized! Business logger's name: {}", Constants.BUSINESS_LOGGER_NAME);
	}

	public void info(Object subject, String msg, Object... args) {
		LOG.info("{} | " + msg, subject, args);
	}

	public void warn(Object subject, String msg, Object... args) {
		LOG.warn("{} | " + msg, subject, args);
	}

	public void error(Object subject, String msg, Object... args) {
		LOG.error("{} | " + msg, subject, args);
	}

}
