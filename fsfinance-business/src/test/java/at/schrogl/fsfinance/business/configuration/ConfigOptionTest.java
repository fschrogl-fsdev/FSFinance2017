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

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigOptionTest {

	@Test
	public void testAsProperties() {
		Properties actualProps = ConfigOption.asProperties();

		Assert.assertEquals(actualProps.size(), ConfigOption.values().length);
		for (ConfigOption option : ConfigOption.values()) {
			Assert.assertTrue(actualProps.containsKey(option.getKey()), "Option key not found in properties: " + option.getKey());
			Assert.assertEquals(actualProps.getProperty(option.getKey()), option.getDefaultValue());
		}
	}

}
