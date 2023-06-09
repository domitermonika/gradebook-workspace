<%@ include file="../init.jsp" %>

<liferay-ui:error key="serviceErrorDetails">
	<liferay-ui:message
		arguments='<%= SessionErrors.get(liferayPortletRequest, "serviceErrorDetails") %>'
		key="error.assignment-service-error"
	/>
</liferay-ui:error>

<liferay-ui:error key="assignmentTitleEmpty" message="error.assignment-title-empty" />
<liferay-ui:error key="assignmentDescriptionEmpty" message="error.assignment-description-empty" />

<%-- Generate add / edit action URL and set title. --%>

<c:choose>
	<c:when test="${not empty assignment}">
		<portlet:actionURL name="<%= MVCCommandNames.EDIT_ASSIGNMENT %>" var="assignmentActionURL">
			<portlet:param name="redirect" value="${param.redirect}" />
		</portlet:actionURL>

		<c:set value="edit-assignment" var="editTitle" />
	</c:when>
	<c:otherwise>
		<portlet:actionURL name="<%= MVCCommandNames.ADD_ASSIGNMENT %>" var="assignmentActionURL">
			<portlet:param name="redirect" value="${param.redirect}" />
		</portlet:actionURL>

		<c:set value="add-assignment" var="editTitle" />
	</c:otherwise>
</c:choose>

<div class="container-fluid-1280 edit-assignment">
	<h1><liferay-ui:message key="${editTitle}" /></h1>

	<aui:model-context bean="${assignment}" model="${assignmentClass}" />

	<aui:form action="${assignmentActionURL}" name="fm">
		<aui:input field="assignmentId" name="assignmentId" type="hidden" />

		<div class="sheet"><div class="panel-group panel-group-flush">
			<aui:fieldset>

				<%-- Title field. --%>

				<aui:input name="title">
					<aui:validator name="required" />

					<%-- Custom AUI validator. --%>
					<aui:validator errorMessage="error.assignment-title-format" name="custom">
								function(val, fieldNode, ruleValue) {
									var wordExpression =
											new RegExp("^[^\\[\\]\\^$<>]*$");
									return wordExpression.test(val);
								}
					</aui:validator>
				</aui:input>

				<%-- Description field. --%>

				<aui:input name="description">
					<aui:validator name="required" />
				</aui:input>

				<%-- Due date field. --%>

				<aui:input name="dueDate">
					<aui:validator name="required" />
				</aui:input>
			</aui:fieldset>
		</div></div>

		<%--Buttons. --%>

		<aui:button-row>
			<aui:button cssClass="btn btn-primary" type="submit" />
			<aui:button cssClass="btn btn-secondary" href="${param.redirect}" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>