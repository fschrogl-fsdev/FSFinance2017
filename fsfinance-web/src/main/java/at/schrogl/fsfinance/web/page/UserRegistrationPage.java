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
package at.schrogl.fsfinance.web.page;

import static org.wicketeer.modelfactory.ModelFactory.from;
import static org.wicketeer.modelfactory.ModelFactory.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
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

public class UserRegistrationPage extends TemplatePage<UserRegistrationInfo> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationPage.class);

	@SpringBean
	private AppConfig config;

	@SpringBean
	private UserManagement userMgmt;

	private Form<Void> submitForm;
	private TextField<String> usernameField;
	private EmailTextField emailField;
	private FeedbackPanel feedbackPanel;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setPageModelObject(new UserRegistrationInfo());

		createPageHeaders();
		createPageInfoLabel();

		createForm();
		createUsername();
		createEmailField();
		createPasswordFields();

		createFeedbackPanel();
	}

	private void createPageHeaders() {
		setPageTitle(getString("registration.pageTitle"));
		add(new Label("pageHeader", getString("registration.heading")));
	}

	private void createPageInfoLabel() {
		if (isRegistrationEnabled()) {
			add(new Label("pageInfo", getString("registration.text")));
		} else {
			add(new Label("pageInfo", getString("registration.text.disabled"))
					.add(AttributeModifier.append("class", "text-danger")));
		}
	}

	private void createForm() {
		Form<Void> form = new Form<Void>("form") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void callOnError(IFormSubmitter submitter) {
				super.callOnError(submitter);
				feedbackPanel.setVisible(true);
			}

			@Override
			protected void onSubmit() {
				super.onSubmit();

				try {
					userMgmt.createUserAccount(getPageModelObject().getUser(), getPageModelObject().getPassword());
					setResponsePage(HomePage.class);
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
		form.add(new StatelessLink<Void>("cancel") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
		});

		form.setVisible(isRegistrationEnabled());
		add(submitForm = form);
	}

	private void createUsername() {
		TextField<String> username = new TextField<>("username", model(from(getPageModel()).getUser().getUsername()));
		username.setLabel(Model.of(getString("registration.username.label")));
		username.add(new StringInputValidator("[a-zA-z]+[a-zA-z0-9]+", "registration.username.error.pattern"));
		username.add(new PropertyValidator<>());
		submitForm.add(username);
	}

	private void createEmailField() {
		EmailTextField email = new EmailTextField("email", model(from(getPageModel()).getUser().getEmail()));
		email.setLabel(Model.of(getString("registration.email.label")));
		email.setRequired(true);
		submitForm.add(email);
	}

	private void createPasswordFields() {
		PasswordTextField password = new PasswordTextField("password", model(from(getPageModel()).getPassword()));
		password.setLabel(Model.of(getString("registration.password.label")));
		password.add(new PropertyValidator<>());
		submitForm.add(password);

		PasswordTextField passwordRepeated = new PasswordTextField("passwordRepeated", model(from(getPageModel()).getPasswordRepeated()));
		passwordRepeated.add(AttributeModifier.replace("placeholder", getString("registration.passwordRepeat.placeholder")));
		passwordRepeated.setRequired(false);
		submitForm.add(passwordRepeated);

		EqualPasswordInputValidator equalPasswordValidator = new EqualPasswordInputValidator(password, passwordRepeated) {

			private static final long serialVersionUID = 1L;

			@Override
			protected String resourceKey() {
				return "registration.password.error.notEqual";
			}
		};
		submitForm.add(equalPasswordValidator);
	}

	private void createFeedbackPanel() {
		FeedbackPanel panel = new FeedbackPanel("feedbackPanel");
		panel.setVisible(false);
		add(feedbackPanel = panel);
	}

	private boolean isRegistrationEnabled() {
		return config.getConfigValueAsBool(ConfigOption.REGISTRATION_ENABLED);
	}

}
