package com.liferay.training.gradebook.rest.application;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyService;
import com.liferay.portal.kernel.service.GroupService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.training.gradebook.model.Assignment;
import com.liferay.training.gradebook.service.AssignmentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * Simple REST application
 *
 * @author Monika Domiter
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/gradebook-rest",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Gradebook.Rest",
		"liferay.auth.verifier=true",	// default
		"liferay.oauth2=true"	// default
	},
	service = Application.class
)
public class AssignmentRestApplication extends Application {

	@GET
	@Path("/assignment/{assignmentid}")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAssignment(@PathParam("assignmentid") long assignmentId) {
		try {
			return JSONFactoryUtil.serialize(
				_assignmentService.getAssignment(assignmentId));
		}
		catch (Exception e) {
			e.printStackTrace();

			return "{}";
		}
	}

	@GET
	@Path("/assignments")
	@Produces({MediaType.APPLICATION_JSON})
	public String getAssignments() {
		try {
			List<Assignment> assignments = new ArrayList<>();
			Company company = _companyService.getCompanyById(
				_portal.getDefaultCompanyId());

			List<Group> groups = _groupService.getGroups(
				company.getCompanyId(), 0, true);

			for (Group group : groups) {
				assignments.addAll(
					_assignmentService.getAssignmentByGroupId(
						group.getGroupId()));
			}

			return JSONFactoryUtil.serialize(assignments);
		}
		catch (PortalException pe) {
			pe.printStackTrace();

			return "[{}]";
		}
	}

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	@Reference
	private AssignmentService _assignmentService;

	@Reference
	private CompanyService _companyService;

	@Reference
	private GroupService _groupService;

	@Reference
	private Portal _portal;

}