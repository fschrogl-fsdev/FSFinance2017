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
package at.schrogl.fsfinance.web;

import org.apache.wicket.Page;
import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.devutils.stateless.StatelessChecker;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import at.schrogl.fsfinance.web.page.HomePage;

public class FSFinance extends WebApplication {

	@Override
	protected void init() {
		super.init();

		switch (getConfigurationType()) {
		case DEVELOPMENT:
			getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
			break;

		case DEPLOYMENT:
			break;
		}

		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		new BeanValidationConfiguration().configure(this);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
