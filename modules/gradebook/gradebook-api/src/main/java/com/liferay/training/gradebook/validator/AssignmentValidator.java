package com.liferay.training.gradebook.validator;

import com.liferay.training.gradebook.exception.AssignmentValidationException;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Monika Domiter
 */
public interface AssignmentValidator {

	/**
	 * Validates an Assignment
	 *
	 * @param title
	 * @param description
	 * @param dueDate
	 * @throws AssignmentValidationException
	 */
	public void validate(
			Map<Locale, String> title, String description, Date dueDate)
		throws AssignmentValidationException;

}