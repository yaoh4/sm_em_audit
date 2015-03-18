<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!-- <%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %> -->
<tiles:importAttribute name="selectedTab" scope="request" />
<tiles:importAttribute name="selectedSubNav" scope="request" />
<tiles:importAttribute name="selectedSubTab" scope="request" />
<tiles:importAttribute name="tabtitle" scope="request" />

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
   	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <link rel="stylesheet" type="text/css" href="../stylesheets/styles.css" />
<!-- <link rel="stylesheet" type="text/css" href="../stylesheets/content.css" /> -->
    <link rel="stylesheet" type="text/css" href="../stylesheets/displaytag.css"/>
    <link rel="stylesheet" type="text/css" href="../stylesheets/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="../stylesheets/custom.css"/>
	<link rel="stylesheet" type="text/css" href="../stylesheets/entmaint.css"/>
	<script language="JavaScript" src="../scripts/jquery-2.1.3.min.js" type="text/javascript"></script>
	<script language="JavaScript" src="../scripts/jquery-ui-1.11.3.js" type="text/javascript"></script>
	<script language="JavaScript" src="../scripts/bootstrap.js" type="text/javascript"></script>
	<script language="JavaScript" src="../scripts/bootstrap.min.js" type="text/javascript"></script>
	<script language="JavaScript" src="../scripts/respond.min.js" type="text/javascript"></script>
	<script language="JavaScript" src="../scripts/entMaint.js" type="text/javascript"></script>
	
	<!-- Page Title begins -->
	<title><tiles:getAsString name="Title"/></title>
	<!-- Page Title ends -->
	<sj:head />
	
</head>
<body>

        <!-- NCI header begins -->
    	<tiles:insertAttribute name="header"/>
        <!-- NCI header ends -->
        
        <!-- NCI Application header begins -->
        <tiles:insertAttribute name="menu"/>
        <!-- NCI Application header ends -->

        <!-- main content begins -->       
        
       <tiles:insertAttribute name="content"/>

        <!-- main content ends -->

        <!--Start Footer -->
      <tiles:insertAttribute name="footer"/>
         <!--End Footer -->

</body>
</html>
