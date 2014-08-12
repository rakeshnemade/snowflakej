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
	     
			<div id = "content" class="row">
				<div class="col-xs-8 col-xs-offset-2">
					<h2>User Details</h2>
					<table class="table table-striped">
						<tbody>
							<c:forEach items="${propertydeflist}" var="propertydef">
								<c:choose>
									<c:when test="${propertydef.nodataentry eq true}">
										
									</c:when>
									<c:otherwise>
										<tr>
											<td><c:out value="${propertydef.propertylabel}" /> </td>
											<td>
												<c:choose>
													<c:when test="${propertydef.multiplevalued eq true}">
														<c:forEach items="${it[propertydef.description]}" var="objval">
															<c:out value="${objval}"/><br>
														</c:forEach>
													</c:when>
													<c:otherwise>
														<c:out value="${it[propertydef.description]}"/>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>	
							</c:forEach>
							<tr>
								<td COLSPAN="2" class="text-center">
									<a href="<c:out value='${path}'/>/sf/edit/<c:out value='${datatype}'/>?ID=<c:out value='${it.id}'/>">
					  					<button class="btn btn-info">Edit</button>
									</a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="<c:out value='${path}'/>/sf/delete/<c:out value='${datatype}'/>?ID=<c:out value='${it.id}'/>">
					  					<button class="btn btn-info">Delete</button>
									</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>