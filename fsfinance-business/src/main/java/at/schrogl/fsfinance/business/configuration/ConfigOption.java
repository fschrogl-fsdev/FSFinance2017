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
package at.schrogl.fsfinance.business.configuration;

import java.util.Properties;

public enum ConfigOption {

	BUSINESSLOGGER_NAME("logger.business.name", "business-logger"),
	REGISTRATION_ENABLED("registration.enabled", Boolean.TRUE.toString()),
	PASSWORD_HASH_ALGO("security.password.hashalgo", "SHA-256");

	private final String key;
	private final String defaultValue;

	private ConfigOption(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getKey() {
		return key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public int getDefaultValueAsInt() {
		return Integer.parseInt(defaultValue);
	}

	public boolean getDefaultValueAsBool() {
		return Boolean.parseBoolean(defaultValue);
	}

	public static Properties asProperties() {
		Properties properties = new Properties();
		for (ConfigOption option : ConfigOption.values()) {
			properties.setProperty(option.getKey(), option.getDefaultValue());
		}
		return properties;
	}

}
