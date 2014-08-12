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
				<div class="row">
					<div class="col-xs-6">
						<h2><c:out value="${datatype}"/> list</h2>
					</div>
					<div class="col-xs-6 pull-right">
						<div class="input-group" style="margin-top:22px">
					      <input type="text" class="form-control" id="s_query"></input>
					      <span class="input-group-btn">
					        <button id="search" class="btn btn-default" type="button">Go!</button>
					      </span>
					    </div>
					</div>
				</div>
			
				<div class="row">
					<div id="table_ph" class="col-xs-12">
						<%@ include file="search.jsp" %>
					</div> 
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#search').click(function() {
				var keyword = $("#s_query").val();
				var myurl = "<c:out value='${path}'/>/sf/search/<c:out value='${datatype}'/>?keyword="+keyword;
				$.ajax({
					url: myurl,
					type: "GET",
					error: function(xhr, error) {
						alert('Error!  Status = ' + xhr.status + ' Message = ' + error);
					},
					success: function(data) {
						$('#table_ph').html(data);
					}
				});
				return false; 
			});
		});
	</script>
</body>
</html>