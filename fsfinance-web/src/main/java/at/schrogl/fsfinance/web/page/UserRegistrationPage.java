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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import at.schrogl.fsfinance.business.UserManagement;
import at.schrogl.fsfinance.business.configuration.AppConfig;
import at.schrogl.fsfinance.business.configuration.ConfigOption;
import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException;
import at.schrogl.fsfinance.persistence.entity.User;
import at.schrogl.fsfinance.web.model.UserRegistrationInfo;
import at.schrogl.fsfinance.web.page.template.TemplatePage;
import at.schrogl.fsfinance.web.validator.StringInputValidator;

public class UserRegistrationPage extends TemplatePage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private AppConfig config;

	@SpringBean
	private UserManagement userMgmt;

	private Form<UserRegistrationInfo> form;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		setPageTitle(getString("registration.pageTitle"));
		add(new Label("pageHeader", getString("registration.heading")));

		createPageInfoLabel();
		createForm();
		createFormFields();
		createCancelButton();
	}

	private void createPageInfoLabel() {
		if (isRegistrationEnabled()) {
			add(new Label("pageInfo", getString("registration.text")));
		} else {
			Label pageInfoLabel = new Label("pageInfo", getString("registration.text.disabled"));
			pageInfoLabel.add(AttributeModifier.append("class", "text-danger"));
			add(pageInfoLabel);
		}
	}

	private void createForm() {
		form = new Form<UserRegistrationInfo>("form", new CompoundPropertyModel<UserRegistrationInfo>(new UserRegistrationInfo())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();

				// TODO validate

				User newUser = new User();
				newUser.setUsername(getModelObject().getUsername());
				newUser.setEmail(getModelObject().getEmail());

				try {
					newUser = userMgmt.createUserAccount(newUser, getModelObject().getPassword());
				} catch (UserAlreadyExistsException e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void onError() {
				super.onError();
			}
		};

		form.setVisible(isRegistrationEnabled());
		add(form);
	}

	private void createFormFields() {
		createUsername();
		createEmail();
		createPassword();
	}

	private void createUsername() {
		TextField<String> username = new TextField<>("username");
		username.setLabel(Model.of(getString("registration.username.label")));
		username.add(AttributeModifier.replace("placeholder", getString("registration.username.placeholder")));
		username.setRequired(true);
		username.add(StringValidator.lengthBetween(5, 20));
		username.add(new StringInputValidator("[a-zA-z]+[a-zA-z0-9]+", "registration.username.error.pattern"));
		form.add(username);
	}

	private void createEmail() {
		EmailTextField email = new EmailTextField("email");
		email.setLabel(Model.of(getString("registration.email.label")));
		email.setRequired(true);
		form.add(email);
	}

	private void createPassword() {
		PasswordTextField password = new PasswordTextField("password");
		password.setLabel(Model.of("registration.password.label"));
		password.add(AttributeModifier.replace("placeholder", getString("registration.password.placeholder")));
		password.add(StringValidator.minimumLength(8));
		form.add(password);

		PasswordTextField passwordRepeated = new PasswordTextField("passwordRepeated");
		passwordRepeated.add(AttributeModifier.replace("placeholder", getString("registration.passwordRepeat.placeholder")));
		form.add(passwordRepeated);

		EqualPasswordInputValidator equalPasswordValidator = new EqualPasswordInputValidator(password, passwordRepeated);
		equalPasswordValidator.error(password, "registration.password.error.notEqual");
		form.add(equalPasswordValidator);
	}

	private void createCancelButton() {
		form.add(new Link<Void>("cancel") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});
	}

	private boolean isRegistrationEnabled() {
		return config.getConfigValueAsBool(ConfigOption.REGISTRATION_ENABLED);
	}

}
