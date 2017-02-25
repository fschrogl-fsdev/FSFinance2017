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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Provides access to the values stored in file {@code build.properties}
 * <p>
 * Die File {@code build.properties} contains information about the artifacts version, its GIT revision and its build timestamp. The file is
 * automatically generated during builds by Maven's buildnumber plugin. The {@link BuildProperties} bean provides access to this information.
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 */
@Component
@PropertySource("classpath:build.properties")
public class BuildProperties {

	@Value("${version}")
	private String version;

	@Value("${revision}")
	private String revision;

	@Value("${timestamp}")
	private String timestamp;

	public String getVersion() {
		return version;
	}

	public String getRevision() {
		return revision;
	}

	public String getTimestamp() {
		return timestamp;
	}

}
