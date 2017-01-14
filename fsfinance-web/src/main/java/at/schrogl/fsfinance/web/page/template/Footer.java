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
package at.schrogl.fsfinance.web.page.template;

import javax.inject.Inject;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import at.schrogl.fsfinance.business.BuildProperties;

public class Footer extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
	private BuildProperties buildProperties;

	public Footer(String id) {
		super(id);
		addVersion();
		addBuildInfo();
		addApplicationMode();
	}

	private void addVersion() {
		add(new Label("version", "version: " + buildProperties.getVersion()));
	}

	private void addBuildInfo() {
		Label buildInfo = new Label("build", buildProperties.getTimestamp());
		buildInfo.add(new AttributeModifier("title", buildProperties.getRevision()));
		add(buildInfo);
	}

	private void addApplicationMode() {
		add(new Label("appMode", "Mode: DEVELOPMENT"));
		RuntimeConfigurationType currentAppMode = Application.get().getConfigurationType();
		if (currentAppMode == RuntimeConfigurationType.DEPLOYMENT) {
			get("appMode").setVisible(false);
		}
	}

}
