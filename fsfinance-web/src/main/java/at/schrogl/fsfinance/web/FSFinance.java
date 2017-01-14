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
package at.schrogl.fsfinance.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import at.schrogl.fsfinance.web.page.UserRegistration;

public class FSFinance extends WebApplication {

	@Override
	protected void init() {
		super.init();

		AnnotationConfigApplicationContext springCtx = new AnnotationConfigApplicationContext();
		springCtx.scan("at.schrogl.fsfinance");
		springCtx.refresh();

		getComponentInstantiationListeners().add(new SpringComponentInjector(this, springCtx));
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return UserRegistration.class;
	}

}
