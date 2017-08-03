<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/ncilink.tld" prefix="ncilink"%>


<!-- IMPAC Wrapper -->
<div id="impac" style="width: 100%;" >
  <div>
    <span>
      <a href="NCI I2E - IMPAC II Extensions" target="_blank">NCI I2E - IMPAC II Extensions</a>
    </span>
  </div>
</div>


<div class="container">


    <!-- header -->

    	<div id="admin_nav">
            <ul style="float: left; padding-top: 5px;">
                <li>
                    <ncilink:WorkbenchApplicationLink application="WorkBench"
                                                      applicationKey="common"
                                                      popScript="callALink">Workbench</ncilink:WorkbenchApplicationLink>
                </li>
                <li class="last">
                    <ncilink:WorkbenchApplicationLink application="IMPAC II"
                                                      applicationKey="common"
                                                      popScript="callALink">IMPAC
                                                                            II
                                                                            Web
                                                                            Applications</ncilink:WorkbenchApplicationLink>
                </li>
            </ul>
            
            <ul>
            	<s:if test="isI2eDeveloper()">
                	<li>
                		<s:a href="restoreUser.action">Restore User</s:a>
                	</li>
                	<li class="dropdown">
                    	<a id="menu-changeUser" class="dropdown-toggle" data-toggle="dropdown" href="#">Change User</a>
                        <ul class="dropdown-menu extended tasks-bar" id="changeUser">
                        	<li style="background-image: none;">
                            	<form action="#" class="form-vertical" method="get">
                                <div class="row-fluid">
                                	<div class="span4">
                                    	<div class="form-group ui-widget">
                                         	<input class="form-control autocomplete" id="input-changeUser" placeholder="Enter First or Last Name" type="text"/>
                                    	</div>
                                	</div>
                            	</form>
                        	</li>
                    	</ul>
                	</li>
                </s:if>
                
            	<s:if test="nciUser != null">
	                <li>
	                    User:
	                    <strong><s:property value="%{nciUser.fullName}"/></strong>
	                </li>
                </s:if>
                <li>
                    Env.:
                    <strong><s:property value="%{getPropertyValue('ENVIRONMENT')}"/></strong>
                </li>
                <li class="last">
                    Version:
                    <strong><s:property value="%{getPropertyValue('VERSION')}"/></strong>
                </li>				
            </ul>
             
            <ul>
            	<li>                                                    
                     <a href="#" onclick="window.open('<s:property value="%{getPropertyValue('HELP_DOCUMENT')}"/>')">Help</a>
                </li>
                <li class="last">                                                    
                     <a href="mailto:<s:property value="%{getPropertyValue('COMMENTS_EMAIL')}"/>?subject=Enterprise Maintenance">Send Comments</a>
                </li>
            </ul>
        </div>
         
        

	<header class="row">
		<h2>Enterprise Maintenance</h2>
		<h5><span style="font-weight:bold;">&nbsp;IMPAC II & I2E Accounts Tracking System</span></h5> 
	</header>
	<br>
	
	<nav class="row">
        
<!-- end div tag moved to application_menu.jsp    </div> -->
        <!-- opening content div -->
         
        <!-- end header -->
            
            
            
            
            
            
            
            
            
            
            