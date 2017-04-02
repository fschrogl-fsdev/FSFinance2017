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
package at.schrogl.fsfinance.business.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * This class is a central access point to the application's configuration.
 * <p>
 * The application uses a built-in default configuration, which can be overriden (full or partially) by specifing a path to a custom
 * configuration file (in Java {@link Properties} file format). This property must use the key {@value Constants#CONFIG_LOOKUP} and can be
 * defined in multiple locations. Spring's {@link Environment#getProperty(String)} is used to retrieve this optionally specified property.
 * <p>
 * Spring is initialized in MVC/Web mode, hence Spring's {@link StandardServletEnvironment} is used as environment abstract. Therefore
 * the custom config property is search in the following locations and in the given order (from top to bottom):
 * <ul>
 * <li>Servlet Config Parameters</li>
 * <li>Servlet Context Parameters (web.xml's context-param)</li>
 * <li>JNDI (from java:comp/env/)</li>
 * <li>JVM System Properties (-D command-line arguments)</li>
 * <li>JVM System Environment (env variables)</li>
 * </ul>
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 * 
 * @see AbstractEnvironment
 * @see StandardServletEnvironment
 */
@Component
public class AppConfig {

	private static final Logger TRACE_LOG = LoggerFactory.getLogger(AppConfig.class);

	private Properties configuration;

	@Autowired
	private Environment environment;

	@PostConstruct
	public void initialize() throws NamingException, IOException {
		String customConfigLocation = environment.getProperty(Constants.CONFIG_LOOKUP);

		if (customConfigLocation == null) {
			TRACE_LOG.info("No custom configuration file specified. Using built-in default configuration!");
			this.configuration = ConfigOption.asProperties();
		} else {
			TRACE_LOG.info("Path to custom configuration file specified! Loading: {}", customConfigLocation);
			try (FileInputStream fis = new FileInputStream(new File(customConfigLocation))) {
				Properties configuration = new Properties();
				configuration.putAll(ConfigOption.asProperties());
				configuration.load(fis);
				this.configuration = configuration;
			}
		}

		if (TRACE_LOG.isDebugEnabled()) {
			String effectiveConfig = configuration.entrySet().stream()
					.map(p -> p.getKey() + "=" + p.getValue())
					.collect(Collectors.joining(", "));
			TRACE_LOG.debug("Effective configuration: {}", effectiveConfig);
		}
	}

	public String getConfigValue(ConfigOption option) {
		return configuration.getProperty(option.getKey());
	}

	public int getConfigValueAsInt(ConfigOption option) {
		return Integer.parseInt(getConfigValue(option));
	}

	public boolean getConfigValueAsBool(ConfigOption option) {
		return Boolean.parseBoolean(getConfigValue(option));
	}

}
