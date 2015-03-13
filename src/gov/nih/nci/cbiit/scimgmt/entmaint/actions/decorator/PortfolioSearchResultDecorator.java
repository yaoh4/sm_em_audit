package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.AppLookupT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;

public class PortfolioSearchResultDecorator extends TableDecorator{
	
	public String getAction(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String name = portfolioVO.getNedFirstName() + " " + portfolioVO.getNedLastName();
		String id = ""+portfolioVO.getImpaciiUserId();
		String actionStr = "<div>" + "<input class=\"btn btn-primary btn-xs\" type=\"button\" onclick=\"submitNotes('" + name +"','" + id + "')\" value=\"Add Notes\"/>" + "</div>";
		return actionStr;
	}
	
	public String getAccountCreatedDate(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
	public String getApplicationRole(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String role = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			role = role + "<tr><td>" + roleVw.getRoleName()+"</td></tr>";
		}
		role = role + "</table>";
		return role;
	}
	
	public String getOrgId(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String orgId = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			orgId = orgId + "<tr><td>" + roleVw.getOrgId()+"</td></tr>";
		}
		orgId = orgId + "</table>";
		return orgId;
	}
	
	public String getRoleCreateOn(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<EmPortfolioRolesVw> roles = portfolioVO.getAccountRoles();
		if(roles == null || roles.size() == 0){
			return "";
		}
		String createDate = "<table width='100%' border='0'>";
		for(EmPortfolioRolesVw roleVw : roles){
			createDate = createDate + "<tr><td>" + new SimpleDateFormat("MM/dd/yyyy").format(roleVw.getCreatedDate()) + "</td></tr>";
		}
		createDate = createDate + "</table>";
		return createDate;
	}
	
	public String getFullName() {
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String fullName = "";
		if(!StringUtils.isBlank(portfolioVO.getNedLastName()) && !StringUtils.isBlank(portfolioVO.getNedFirstName())){
			if(!StringUtils.isBlank(portfolioVO.getNedEmailAddress())){
				fullName =  "<a href=\"mailto:" + portfolioVO.getNedEmailAddress() +  "\">" + portfolioVO.getNedLastName() + ", " + portfolioVO.getNedFirstName() + "</a>";
			}
			else{
				fullName = portfolioVO.getNedLastName() + ", " + portfolioVO.getNedFirstName();
			}
		}
		return fullName;
	}
	
	public String getLastUpdated(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String lastUpdated = "";
		if(!StringUtils.isBlank(portfolioVO.getNotesSubmittedByFullName()) && portfolioVO.getNotesSubmittedDate() !=null){
			lastUpdated = "Submitted on " +portfolioVO.getNotesSubmittedDate() + " by "  +portfolioVO.getNotesSubmittedByFullName();
		}
		return lastUpdated;
	}
	
	public String getNotes(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		String notes = "";
		String id = portfolioVO.getImpaciiUserId();
		if(!StringUtils.isBlank(portfolioVO.getNotes())){
			notes = "<div id=\"notesDiv_"+id+ "\">" + portfolioVO.getNotes() + "</div>";
		}
		else{
			notes = "<div id=\"notesDiv_"+id+ "\"> </div>";
		}
		return notes;
	}
	
	public String getDiscrepancy(){
		PortfolioAccountVO portfolioVO = (PortfolioAccountVO)getCurrentRowObject();
		List<AppLookupT> discrepancies = portfolioVO.getAccountDiscrepancies();
		StringBuffer sbu = new StringBuffer();
		for(AppLookupT disVw : discrepancies){
			if(disVw.getDescription() != null){
				sbu.append(disVw.getDescription() + "\n");
			}
		}
		return sbu.toString();
	}

	
}
