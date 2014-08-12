<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ include file='include.jsp' %>

<table class="table table-striped">
	<thead>
		<c:forEach items="${propertydeflist}" var="propertydef">
			<c:if test="${propertydef.showintable eq true}">
				<th><c:out value="${propertydef.propertylabel}" /></th>
			</c:if>
		</c:forEach>
	</thead>
	<tbody>
		<c:forEach var="obj" items="${it}">
			<tr>
				<c:forEach items="${propertydeflist}" var="propertydef">
					<c:if test="${propertydef.showintable eq true}">
						<td><a href="<c:out value='${path}'/>/sf/view/<c:out value='${datatype}'/>?ID=<c:out value='${obj.id}'/>"> <c:out value="${obj[propertydef.description]}" /> </a></td>
					</c:if>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>