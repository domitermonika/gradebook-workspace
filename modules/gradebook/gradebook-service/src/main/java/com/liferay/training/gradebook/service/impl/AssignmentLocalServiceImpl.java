/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.gradebook.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.base.AssignmentLocalServiceBaseImpl;
import com.liferay.training.gradebook.validator.AssignmentValidator;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the assignment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * <code>com.liferay.training.gradebook.service.AssignmentLocalService</code>
 * interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Monika Domiter
 */
@Component(
	property = "model.class.name=com.liferay.training.gradebook.model.Assignment",
	service = AopService.class
)
public class AssignmentLocalServiceImpl extends AssignmentLocalServiceBaseImpl {

	@Override
	public Assignment addAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not supported.");
	}

	public Assignment addAssignment(
			long groupId, String title, String description, Date dueDate,
			ServiceContext serviceContext)
		throws PortalException {

		// Validate assignment parameters.

		_assignmentValidator.validate(title, description, dueDate);

		// Get group and user.

		Group group = groupLocalService.getGroup(groupId);
		long userId = serviceContext.getUserId();
		User user = userLocalService.getUser(userId);

		// Generate primary key for the assignment

		long assignmentId = counterLocalService.increment(
			Assignment.class.getName());

		// Create assignment. This doesn't yet persist the entity.

		Assignment assignment = createAssignment(assignmentId);

		// Populate fields.

		assignment.setCompanyId(group.getCompanyId());
		assignment.setCreateDate(serviceContext.getCreateDate(new Date()));
		assignment.setDueDate(dueDate);
		assignment.setDescription(description);
		assignment.setModifiedDate(serviceContext.getModifiedDate(new Date()));
		assignment.setTitle(title);
		assignment.setUserId(userId);
		assignment.setUserName(user.getScreenName());

		// Persist assignment to database

		return super.addAssignment(assignment);
	}

	public List<Assignment> getAssignmentByGroupId(long groupId) {
		return assignmentPersistence.findByGroupId(groupId);
	}

	public List<Assignment> getAssignmentByGroupId(
		long groupId, int start, int end) {

		return assignmentPersistence.findByGroupId(groupId, start, end);
	}

	public List<Assignment> getAssignmentByGroupId(
		long groupId, int start, int end,
		OrderByComparator<Assignment> orderByComparator) {

		return assignmentPersistence.findByGroupId(
			groupId, start, end, orderByComparator);
	}

	public List<Assignment> getAssignmentsByKeywords(
		long groupId, String keywords, int start, int end,
		OrderByComparator<Assignment> orderByComparator) {

		return assignmentLocalService.dynamicQuery(
			getKeywordSearchDynamicQuery(groupId, keywords), start, end,
			orderByComparator);
	}

	public long getAssignmentsCountByKeywords(long groupId, String keywords) {
		return assignmentLocalService.dynamicQueryCount(
			getKeywordSearchDynamicQuery(groupId, keywords));
	}

	@Override
	public Assignment updateAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not supported.");
	}

	public Assignment updateAssignment(
			long assignmentId, String title, String description, Date dueDate,
			ServiceContext serviceContext)
		throws PortalException {

		// Validate assignment parameters.

		_assignmentValidator.validate(title, description, dueDate);

		// Get the Assignment by id

		Assignment assignment = getAssignment(assignmentId);

		// Set updated fields and modification date

		assignment.setModifiedDate(new Date());
		assignment.setTitle(title);
		assignment.setDescription(description);
		assignment.setDueDate(dueDate);
		assignment = super.updateAssignment(assignment);

		return assignment;
	}

	private DynamicQuery getKeywordSearchDynamicQuery(
		long groupId, String keywords) {

		DynamicQuery dynamicQuery = dynamicQuery().add(
			RestrictionsFactoryUtil.eq("groupId", groupId));

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery =
				RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(
				RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(
				RestrictionsFactoryUtil.like(
					"description", "%" + keywords + "%"));
			dynamicQuery.add(disjunctionQuery);
		}

		return dynamicQuery;
	}

	@Reference
	AssignmentValidator _assignmentValidator;

}