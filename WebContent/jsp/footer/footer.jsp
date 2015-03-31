<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/ncilink.tld" prefix="ncilink"%>
 
 <!-- end nav and div tag from header.jsp    </div> -->
</nav>


<div class="panel-footer">
	<ol class="breadcrumb">
		<li><ncilink:WorkbenchApplicationLink application="WorkBench"
				applicationKey="common" popScript="callALink">Workbench</ncilink:WorkbenchApplicationLink>
		</li>
		<li><ncilink:WorkbenchApplicationLink application="IMPAC II"
				applicationKey="common" popScript="callALink"> IMPAC II  Web Applications</ncilink:WorkbenchApplicationLink>
		</li>
		<li><a href="http://www.nih.gov/about/privacy.htm"
			target="blank"> NIH Privacy Notice</a></li>
		<li style="float: right;"> <a href="http://www.cancer.gov" target="_blank"><img src="<s:url value="/images/nci_logo_bw.gif" />" alt="National Cancer Institute" width="65" height="43" align="absmiddle" ></a></li>
	</ol>
</div>

</div>