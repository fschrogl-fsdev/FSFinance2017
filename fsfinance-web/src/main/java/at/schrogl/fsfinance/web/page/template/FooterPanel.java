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

import static org.apache.wicket.RuntimeConfigurationType.DEPLOYMENT;

import org.apache.wicket.Application;
import org.apache.wicket.devutils.stateless.StatelessComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.schrogl.fsfinance.business.configuration.BuildProperties;

@StatelessComponent
public class FooterPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private BuildProperties buildProperties;

	public FooterPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		boolean isDeploymentMode = (Application.get().getConfigurationType() == DEPLOYMENT);
		Fragment content = (isDeploymentMode) ? createDeploymentPanel() : createDevelopmentPanel();
		content.setRenderBodyOnly(true);
		add(content);
	}

	private Fragment createDeploymentPanel() {
		Fragment fragment = new Fragment("content", "fragment.footerDeployment", this);
		fragment.add(createVersion());
		return fragment;
	}

	private Fragment createDevelopmentPanel() {
		Fragment fragment = new Fragment("content", "fragment.footerDevelopment", this);
		fragment.add(createBuildTime());
		fragment.add(createVersion());
		fragment.add(createFeedbackPanel());
		return fragment;
	}

	private Label createVersion() {
		return new Label("version", "version: " + buildProperties.getVersion());
	}

	private Label createBuildTime() {
		return new Label("build", buildProperties.getTimestamp());
	}
	
	private FeedbackPanel createFeedbackPanel() {
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackPanel");
		feedbackPanel.setRenderBodyOnly(true);
		return feedbackPanel;
	}

}
