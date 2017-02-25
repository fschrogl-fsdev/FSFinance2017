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
