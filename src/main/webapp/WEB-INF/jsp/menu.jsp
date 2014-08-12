<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ include file='include.jsp' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<script type="text/javascript" src="<c:out value="${basePath}"/>/media/js/jquery-1.11.1.js"></script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/media/js/bootstrap.min.js"></script>
<title>Snowflake User Management</title>
</head>
<body>
	<div id ="header" class="row">
		<div id= "header_logo" class="col-xs-12">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<!-- Brand and toggle get grouped for better mobile display -->
					<div class="navbar-header">
						<a class="navbar-brand" href="#">User Management Portal</a>
					</div>
				
					<!-- Collect the nav links, forms, and other content for toggling -->
					<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				      <ul class="nav navbar-nav">
				      	<li class="dropdown">
				          <a href="#" class="dropdown-toggle" data-toggle="dropdown">User<b class="caret"></b></a>
				          <ul class="dropdown-menu">
				            <li><a href="<c:out value="${path}"/>/sf/new/user">Add new</a></li>
				            <li><a href="<c:out value="${path}"/>/sf/view/all/user">View list</a></li>
				          </ul>
				        </li>
				        <li class="dropdown">
				          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Group<b class="caret"></b></a>
				          <ul class="dropdown-menu">
				            <li><a href="<c:out value="${path}"/>/sf/new/group">Add new</a></li>
				            <li><a href="<c:out value="${path}"/>/sf/view/all/group">View list</a></li>
				          </ul>
				        </li>
				        <li class="dropdown">
				          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Role<b class="caret"></b></a>
				          <ul class="dropdown-menu">
				            <li><a href="<c:out value="${path}"/>/sf/new/role">Add new</a></li>
				            <li><a href="<c:out value="${path}"/>/sf/view/all/role">View list</a></li>
				          </ul>
				        </li>
				        <li class="dropdown">
				          <a href="#" class="dropdown-toggle" data-toggle="dropdown">API Endpoints<b class="caret"></b></a>
				          <ul class="dropdown-menu">
				            <li><a href="<c:out value="${path}"/>/sf/new/allapplinks">Add new</a></li>
				            <li><a href="<c:out value="${path}"/>/sf/view/all/allapplinks">View list</a></li>
				          </ul>
				        </li>
				        <li>
				        	<a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
				        </li>
				      </ul>
				      Welcome <c:out value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}" />
				    </div>
				</div><!-- /.container-fluid -->
			</nav>
		</div>
	</div>
</body>
</html>