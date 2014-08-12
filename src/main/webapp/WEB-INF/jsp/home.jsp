<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ include file='include.jsp' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
	<link rel="stylesheet" type="text/css" href="<c:out value="${basePath}"/>/media/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="<c:out value="${basePath}"/>/media/css/stylesheet.css" />
	<title>Snowflake User Management</title>
	</head>
	<body>
		<div class="container-fluid">
		
			<%@ include file="menu.jsp" %>
			
			<h4 style="color:red"><c:out value="${msg}" /></h4>
			
		</div>
	</body>
</html>