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
<!-- end IMPAC Wrapper -->

    <!-- header -->

    <div id="headerimg">
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
         
        <h1 alt="IMPACII And I2E Account Tracking System" id="EMheader">
            <div align = "left">
                <span id="tagline">
                    <span class="marker_inv">-</span>
                    IMPACII And I2E Account Tracking System
                </span>
            </div>
        </h1>
        <br>
<!-- end div tag moved to application_menu.jsp    </div> -->
        <!-- opening content div -->
         
        <!-- end header -->
            
            
            
            
            
            
            
            
            
            
            