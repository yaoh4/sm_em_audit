<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ page buffer = "16kb" %>


 <display:table name="emAuditsVO.statusHistories" id="auditHistoryId"
     decorator="gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator.AuditHistoryTableDecorator">
	    <display:column title="Run Date" property="createDate"
	                                 		format="{0,date,MM/dd/yyyy h:mm a}" />
		<display:column title="Action" property="actionCode" />
		<display:column title="Comments" property="comments" />
 </display:table>
