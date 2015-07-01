<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<s:include value="/jsp/content/manageAccounts.jsp" />
<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js"	type="text/javascript"></script>
<script language="JavaScript" src="../scripts/entMaint_JQuery.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css"	href="../stylesheets/jquery-ui-1.11.3.css" />

<div class="tab-content">
	<div class="tab-pane fade active in" id="portfolioSearchCriteria">
		<s:form action="%{formAction}" cssClass="form-horizontal">
			<fieldset style="padding: 15px 0;">
			
				<div class="form-group">
					<label class="control-label col-sm-3" for="f-name">I2E
						User First Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userFirstname" cssClass="form-control" maxlength="192" style="width:590px;"
							placeholder="Enter First Name" value="%{#session.i2ePortfolioSearchVO.userFirstname}" id="f-name" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-sm-3" for="l-name">I2E
						User Last Name:</label>
					<div class="col-sm-9">
						<s:textfield name="searchVO.userLastname" cssClass="form-control" maxlength="192" style="width:590px;"
							placeholder="Enter Last Name" value="%{#session.i2ePortfolioSearchVO.userLastname}" id="l-name" />
					</div>
				</div>	
				<s:if test="isSuperUser()">				
					<div class="form-group">
						<label class="control-label col-sm-3" for="portfolioOrg">NCI Organization:</label>
						<div id="orgListId" class="col-sm-9">
							<s:select name="searchVO.organization" onchange="onOrgChange(this.value);" 
								id="portfolioOrg" cssClass="form-control"
								value="%{#session.i2ePortfolioSearchVO.organization}" list="session.orgList"
								listKey="optionKey" listValue="optionValue" headerKey="all"
								headerValue="All" style="width:590px;" />
						</div>
					</div>       
					
					<div class="form-group" style="margin-top: -10px;">
						<label class="control-label col-sm-3" for="excludeNciCheck"> </label>
						<div class="col-sm-9">
							<s:checkbox name="searchVO.excludeNCIOrgs" id="excludeNciCheck" />
							<label style="font-weight: normal; font-size: 0.9em;">Exclude
								NCI Orgs with IC Coordinators</label>
						</div>
					</div>
				</s:if>
				
				<s:else>
					<div class="form-group">
						<label class="control-label col-sm-3" for="portfolioOrg">NCI Organization:</label>
						<div id="orgListId" class="col-sm-9">
							<s:select name="searchVO.organization" id="portfolioOrg" cssClass="form-control"
								value="%{#session.i2ePortfolioSearchVO.organization}" list="session.orgList"
								listKey="optionKey" listValue="optionValue" headerKey="all"
								headerValue="All" style="width:590px;" />
						</div>
					</div> 
				</s:else>
				<div class="form-group">
					<label class="control-label col-sm-3" for="portfolioCategory">Category:</label>
					<div class="col-sm-9">
						<s:select name="searchVO.category" id="portfolioCategory"
							onchange="onCategoryChage(this.value);" cssClass="form-control"
							value="%{#session.portfolioSearchVO.category}" list="session.categoryList"
							listKey="optionKey" listValue="optionValue" style="width:590px;" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<s:submit value="Search" cssClass="btn btn-primary" onclick="#"  />
						<s:submit value="Clear"	action="i2e/clearSearchPortfolioAccounts" cssClass="btn btn-default" />
					</div>
				</div>
			</fieldset>
		</s:form>
	</div>
	<br />
	<!--  For tab-content -->
</div>