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

import java.io.Serializable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;

import at.schrogl.fsfinance.web.page.template.TemplatePage;

public class HomePage extends TemplatePage<Serializable> {

	private static final long serialVersionUID = 1L;

	public HomePage() {
		setPageTitle("Home");

		add(new Label("pageHeader", "Home"));
		add(new Label("pageInfo", "This is a placeholder page for a logged-in user's home/dashboard."));

		add(new StatelessLink<Void>("linkRegistration") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(UserRegistrationPage.class);
			}
		});
	}

}
