<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="env" value="%{@gov.nih.nci.cbiit.scimgmt.entmaint.utils.CommonProperties@getInstance().getProperty(\'ENVIRONMENT\')}"/>


<s:url id="goHome" action="Home"/><s:a href="%{goHome}">Click here to go back to Home Page</s:a>
<div id="box1">
<br>
  <fieldset style="width: 925px">
    <legend>Application Error Notification</legend>
    <s:form action="sendErrorMessage">
      <s:hidden name="errorDetails" value="%{exceptionStack}"/>
      <div>
        <br></br>
         An error occurred in the Enterprise Maintenance system.
        <br></br>

        <br></br>

        <strong> Please report unusual error with  details of the action you took when the error occured </strong>

        <br></br><br></br>
         Notify Application Support of the error by clicking on the &quot;Send
        Email&quot; button below
        <br></br>

        <br></br>
      </div>
      <br></br>
      <br></br>
      <table width="701" border="0" cellspacing="0">
        <tr valign="middle">
          <td>&nbsp;</td>
        </tr>
        <tr valign="middle">
          <td>
            <s:textarea cols="60" rows="5" name="message" cssStyle="width: 365px;"/>
          </td>
        </tr>
        <tr valign="middle">
          <td>&nbsp;</td>
        </tr>
        <tr valign="middle">
          <td>
            <s:submit value="Send Email"/>
          </td>
        </tr>
        <tr valign="middle">
          <td>&nbsp;</td>
        </tr>

        <tr>
          <td align="center">
            <font size="2">
              <strong>Description</strong>
            </font>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td align="center">
            <pre>
              <s:actionerror/>
            </pre>
            <pre>
             <font size="2" color="blue"><strong><s:property value="%{exception.message}"/></strong> </font>
            </pre>
            <s:if test="%{ \'Production\' neq #env}">
              <font size="2">
                <strong>Details</strong>
              </font>
              <pre>
                <s:property value="exceptionStack"/>
              </pre>
            </s:if>
          </td>
        </tr>
        <tr valign="middle">
          <td>&nbsp;</td>
        </tr>
      </table>
    </s:form>
  </fieldset>
</div>