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
package at.schrogl.fsfinance.web.page.template;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class TemplatePage<T extends Serializable> extends WebPage {

	private static final long serialVersionUID = 1L;

	protected IModel<T> pageModel;
	protected HeaderPanel header;
	protected FooterPanel footer;

	public TemplatePage() {
		add(header = new HeaderPanel("header"));
		add(footer = new FooterPanel("footer"));
	}

	protected HeaderPanel getHeader() {
		return header;
	}

	protected FooterPanel getFooter() {
		return footer;
	}

	protected void setPageTitle(String title) {
		add(new Label("title", title));
	}

	protected IModel<T> getPageModel() {
		return pageModel;
	}

	protected void setPageModel(IModel<T> pageModel) {
		this.pageModel = pageModel;
	}

	protected T getPageModelObject() {
		return pageModel.getObject();
	}

	protected void setPageModelObject(T pageModelObject) {
		this.pageModel = Model.of(pageModelObject);
	}

}
