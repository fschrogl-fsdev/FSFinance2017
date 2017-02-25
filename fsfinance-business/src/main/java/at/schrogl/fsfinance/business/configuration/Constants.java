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

/**
 * Holds application-wide constants/parameters.
 * <p>
 * The values saved are read-only and needed for application bootstrapping.
 * 
 * @author Fritz Schrogl
 * @since 0.1.0
 * 
 * @see AppConfig
 */
public interface Constants {

	/** The application's brand name */
	static final String APP_NAME = "FSFinance";

	/** (Optional) Environment property to look up path to custom configuration file */
	static final String CONFIG_LOOKUP = "fsfinance.config";

	/** The application's business logger name (can be used to configure log filtering) */
	static final String BUSINESS_LOGGER_NAME = "business-logger";
	
}
