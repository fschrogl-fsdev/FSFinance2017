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
package at.schrogl.fsfinance.web.validator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class StringInputValidator implements IValidator<String> {

	private static final long serialVersionUID = 1L;

	private final Pattern pattern;
	private final String msgKey;

	public StringInputValidator(String regex) {
		this(regex, null);
	}

	public StringInputValidator(Pattern pattern) {
		this(pattern, null);
	}

	public StringInputValidator(String regex, String msgKey) {
		regex = Objects.requireNonNull(regex);
		this.pattern = Pattern.compile(regex);
		this.msgKey = msgKey;
	}

	public StringInputValidator(Pattern pattern, String msgKey) {
		this.pattern = Objects.requireNonNull(pattern);
		this.msgKey = msgKey;
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		Matcher patternMatcher = pattern.matcher(validatable.getValue());

		if (!patternMatcher.matches()) {
			ValidationError validationError = new ValidationError();
			
			if (msgKey != null)
				validationError.addKey(msgKey);
			else
				validationError.addKey(this);
			
			validationError.setVariable("pattern", pattern);
			validatable.error(validationError);
		}
	}

}
