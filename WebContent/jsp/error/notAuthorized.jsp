<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/struts-tags" prefix="s"%>

<div style="padding: 10px 15px;">
	<fieldset style="padding: 10px 10px;">
		<h4 style="color: #DD6903;">
			You are not authorized to view this page. If you have any questions
			regarding your access to this application, please send an email to
			<s:a href="mailto:%{contactEmail}">
				<s:property value="contactText" />
			</s:a>.
		</h4>
	</fieldset>
</div>

<br /><br /><br /><br />