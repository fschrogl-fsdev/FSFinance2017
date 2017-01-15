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
package at.schrogl.fsfinance.web.page;

import org.apache.wicket.markup.html.basic.Label;

import at.schrogl.fsfinance.web.page.template.Template;

public class UserRegistration extends Template {

	private static final long serialVersionUID = 1L;

	public UserRegistration() {
		setPageTitle("User registration");

		add(new Label("pageHeader", "Register a new account"));
		add(new Label("pageInfo", "To register a new user you have to choose a unique username, provide a valid e-Mail address, for contact purposes, and use a strong password."));
	}

}
