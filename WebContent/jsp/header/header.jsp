<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tld/ncilink.tld" prefix="ncilink"%>



	<div id="impac2">
		<img src="<s:url value="/images/nci_i2e_logo.png" />" alt="National Cancer Institute" width="421" height="44" border="0" usemap="#ncibannermap" />
		<map name="ncibannermap" id="ncibannermap">
		 	<area shape="rect" coords="13,1,293,35" href="http://www.cancer.gov/" alt="National Cancer Institute" />	
		</map>	
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
                <li>
                    User:
                    <strong><s:property value="%{#session.nciUser.fullName}"/></strong>
                </li>
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
                <li class="last">                                                    
                     <a href="mailto:<s:property value="%{getPropertyValue('COMMENTS_EMAIL')}"/>?subject=Enterprise Maintenance">Send Comments</a>
                </li>
            </ul>
        </div>
         
        
        <br>
        

	<header class="row">
		<h2>IMPAC II & I2E Accounts Tracking System</h2> 
	</header>
	<br>
	
	<nav class="row">
        
<!-- end div tag moved to application_menu.jsp    </div> -->
        <!-- opening content div -->
         
        <!-- end header -->
            
            
            
            
            
            
            
            
            
            
            