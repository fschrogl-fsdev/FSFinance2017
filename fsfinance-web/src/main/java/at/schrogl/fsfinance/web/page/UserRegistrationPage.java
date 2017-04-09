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

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.schrogl.fsfinance.business.UserManagement;
import at.schrogl.fsfinance.business.configuration.AppConfig;
import at.schrogl.fsfinance.business.configuration.ConfigOption;
import at.schrogl.fsfinance.business.exception.UserAlreadyExistsException;
import at.schrogl.fsfinance.web.model.UserRegistrationInfo;
import at.schrogl.fsfinance.web.page.template.TemplatePage;
import at.schrogl.fsfinance.web.validator.StringInputValidator;

public class UserRegistrationPage extends TemplatePage {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationPage.class);

	@SpringBean
	private AppConfig config;

	@SpringBean
	private UserManagement userMgmt;

	private Form<UserRegistrationInfo> submitForm;
	private TextField<String> usernameField;
	private EmailTextField emailField;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(createPageHeaders());
		add(createPageInfoLabel());

		submitForm = createForm();
		usernameField = createUsername(submitForm);
		emailField = createEmailField(submitForm);
		createPasswordFields(submitForm);
		add(submitForm);
	}

	private Component createPageHeaders() {
		setPageTitle(getString("registration.pageTitle"));
		return new Label("pageHeader", getString("registration.heading"));
	}

	private Component createPageInfoLabel() {
		if (isRegistrationEnabled()) {
			return new Label("pageInfo", getString("registration.text"));
		} else {
			return new Label("pageInfo", getString("registration.text.disabled"))
					.add(AttributeModifier.append("class", "text-danger"));
		}
	}

	private Form<UserRegistrationInfo> createForm() {
		Form<UserRegistrationInfo> form = new Form<UserRegistrationInfo>("form",
				new CompoundPropertyModel<UserRegistrationInfo>(new UserRegistrationInfo())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				super.onSubmit();

				try {
					userMgmt.createUserAccount(getModelObject().getUser(), getModelObject().getPassword());
					setResponsePage(WebApplication.get().getHomePage());
				} catch (UserAlreadyExistsException uae) {
					Map<String, String> variables = new HashMap<>();

					switch (uae.getReason()) {
					case USERNAME_NOT_UNIQUE:
						variables.put("username", uae.getNewUser().getUsername());
						usernameField.error(new ValidationError(getString("registration.username.error.notUnique", Model.ofMap(variables))));
						break;

					case EMAIL_NOT_UNIQUE:
						variables.put("email", uae.getNewUser().getEmail());
						emailField.error(new ValidationError(getString("registration.email.error.notUnique", Model.ofMap(variables))));
						break;

					default:
						// TODO Generic error handling
						LOG.error("Error handling for offending property not defined!", uae);
						submitForm.error(getString("registration.error"));
					}
				}
			}
		};
		form.setVisible(isRegistrationEnabled());

		form.add(new StatelessLink<Void>("cancel") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});

		return form;
	}

	private TextField<String> createUsername(Form<UserRegistrationInfo> form) {
		TextField<String> username = new TextField<>("username", PropertyModel.of(form.getModelObject(), "user.username"));
		username.setLabel(Model.of(getString("registration.username.label")));
		username.add(new StringInputValidator("[a-zA-z]+[a-zA-z0-9]+", "registration.username.error.pattern"));
		username.add(new PropertyValidator<>());
		form.add(username);
		return username;
	}

	private EmailTextField createEmailField(Form<UserRegistrationInfo> form) {
		EmailTextField email = new EmailTextField("email", PropertyModel.of(form.getModelObject(), "user.email"));
		email.setLabel(Model.of(getString("registration.email.label")));
		email.setRequired(true);
		form.add(email);
		return email;
	}

	private void createPasswordFields(Form<UserRegistrationInfo> form) {
		PasswordTextField password = new PasswordTextField("password", PropertyModel.of(form.getDefaultModelObject(), "password"));
		password.setLabel(Model.of(getString("registration.password.label")));
		password.add(new PropertyValidator<>());
		form.add(password);

		PasswordTextField passwordRepeated = new PasswordTextField("passwordRepeated");
		passwordRepeated.add(AttributeModifier.replace("placeholder", getString("registration.passwordRepeat.placeholder")));
		passwordRepeated.setRequired(false);
		form.add(passwordRepeated);

		EqualPasswordInputValidator equalPasswordValidator = new EqualPasswordInputValidator(password, passwordRepeated) {

			private static final long serialVersionUID = 1L;

			@Override
			protected String resourceKey() {
				return "registration.password.error.notEqual";
			}
		};
		form.add(equalPasswordValidator);
	}

	private boolean isRegistrationEnabled() {
		return config.getConfigValueAsBool(ConfigOption.REGISTRATION_ENABLED);
	}

}
