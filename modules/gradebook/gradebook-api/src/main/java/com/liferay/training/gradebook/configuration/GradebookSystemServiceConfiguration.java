package com.liferay.training.gradebook.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * Configuration interface for Gradebook service.
 * A user interface for this interface is automatically created
 * in Control Panel -> System settings.
 *
 * @author Monika Domiter
 */
@ExtendedObjectClassDefinition(
	category = "Gradebook", scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "com.liferay.training.gradebook.configuration.GradebookSystemServiceConfiguration",
	localization = "content/Language",
	name = "gradebook-service-configuration-name"
)
public interface GradebookSystemServiceConfiguration {

	@Meta.AD(
		deflt = "5", description = "title-min-length-description",
		name = "title-min-length-name", required = false
	)
	public int titleMinLength();

	@Meta.AD(
		deflt = "50", description = "title-max-length-description",
		name = "title-max-length-name", required = false
	)
	public int titleMaxLength();

}