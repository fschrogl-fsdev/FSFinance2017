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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppConfigTest {

	private AnnotationConfigApplicationContext ctx;

	@BeforeMethod
	public void setup() {
		this.ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
	}

	@AfterMethod
	public void tearDown() {
		if (ctx != null)
			ctx.close();
	}

	@Test
	public void testInitializeDefault() {
		ctx.refresh();
		AppConfig actualConfig = ctx.getBean(AppConfig.class);
		for (ConfigOption expectedOption : ConfigOption.values()) {
			Assert.assertEquals(actualConfig.getConfigValue(expectedOption), expectedOption.getDefaultValue());
		}
	}

	@Test
	public void testInitializeCustom() throws IOException {
		Map<ConfigOption, Object> overriddenProperties = new HashMap<>();
		overriddenProperties.put(ConfigOption.BUSINESSLOGGER_NAME, "testValue");

		Map<ConfigOption, Object> expectedProperties = createExpectedProperties(overriddenProperties);
		Path customPropsTmpFile = createCustomConfigTmpFile(overriddenProperties);

		MutablePropertySources propertySources = ctx.getEnvironment().getPropertySources();
		MockPropertySource mockEnvVars = new MockPropertySource().withProperty(Constants.CONFIG_LOOKUP, customPropsTmpFile.toString());
		propertySources.replace(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, mockEnvVars);

		ctx.refresh();
		AppConfig actualConfig = ctx.getBean(AppConfig.class);

		for (Entry<ConfigOption, Object> expectedEntry : expectedProperties.entrySet()) {
			Assert.assertEquals(actualConfig.getConfigValue(expectedEntry.getKey()), expectedEntry.getValue());
		}
	}

	private Map<ConfigOption, Object> createExpectedProperties(Map<ConfigOption, Object> overriddenProperties) {
		Map<ConfigOption, Object> expectedProperties = new HashMap<>();
		for (ConfigOption configOption : ConfigOption.values()) {
			expectedProperties.put(configOption, configOption.getDefaultValue());
		}
		expectedProperties.putAll(overriddenProperties);
		return expectedProperties;
	}

	private Path createCustomConfigTmpFile(Map<ConfigOption, Object> properties) throws IOException {
		Path tempFile = Files.createTempFile("data-" + getClass().getSimpleName(), ".tmp");
		try (BufferedWriter bfw = Files.newBufferedWriter(tempFile, StandardOpenOption.WRITE)) {
			for (Entry<ConfigOption, Object> contentEntry : properties.entrySet()) {
				ConfigOption configOption = contentEntry.getKey();
				bfw.write(configOption.getKey() + "=" + contentEntry.getValue());
				bfw.newLine();
			}
		}
		tempFile.toFile().deleteOnExit();
		return tempFile;
	}

}
