<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ include file='include.jsp' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
	<title>Snowflake User Management</title>
	<script type="text/javascript" src="<c:out value="${basePath}"/>/media/js/jquery-1.11.1.js"></script>
	<link rel="stylesheet" type="text/css" href="<c:out value="${basePath}"/>/media/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="<c:out value="${basePath}"/>/media/css/stylesheet.css" />
	</head>
	<body>
		<div class="container-fluid">
			
			<%@ include file="menu.jsp" %>
	
			<div id = "content" class="row">
				<div class="col-xs-8 col-xs-offset-2">
					<h2 class="text-center"><c:out value='${datatype}'/> information</h2>
					<form id="form" method="post" enctype="application/json" class="form-horizontal" role="form">
						<input type="hidden" name="id" id="id" value="<c:out value='${it.id}'/>">
						
						<c:forEach items="${propertydeflist}" var="propertydef">
							<c:choose>
								<c:when test="${propertydef.nodataentry eq true}">
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${propertydef.propertytype eq 'Enumerated List'}">
											<div class="form-group">
												<label for="<c:out value='${it[propertydef.description]}'/>" class="col-sm-4 control-label"><c:out value="${propertydef.propertylabel}" /></label>
											    <div class="col-sm-6">
													<select name="<c:out value='${propertydef.description}'/>" id="<c:out value='${propertydef.description}'/>" class="form-control">
														<c:forEach items="${propertydef.enumeratedvalues}" var="optionval">
															<c:choose>
																<c:when test="${optionval eq it[propertydef.description]}">
																	<option value="<c:out value='${optionval}'/>" selected><c:out value='${optionval}'/></option>
																</c:when>
																<c:otherwise>
																	<option value="<c:out value='${optionval}'/>"><c:out value='${optionval}'/></option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
												  	</select>
											    </div>
											</div>
										</c:when>
										<c:when test="${propertydef.propertytype eq 'Boolean'}">
											<div class="form-group">
											    <label for="<c:out value='${propertydef.description}'/>" class="col-sm-4 control-label"><c:out value="${propertydef.propertylabel}" /></label>
											    <div class="col-sm-6">
											    	<c:choose>
														<c:when test="${true eq it[propertydef.description]}">
															<input type="checkbox" checked name="<c:out value='${propertydef.description}'/>" class="form-control" value="true">
														</c:when>
														<c:otherwise>
															<input type="checkbox" name="<c:out value='${propertydef.description}'/>" class="form-control" value="true">
														</c:otherwise>
													</c:choose>
											    </div>
											</div>
										</c:when>
										<c:when test="${propertydef.propertytype eq 'commalist' || propertydef.propertytype eq 'linelist'}">
											<c:choose>
												<c:when test="${propertydef.description eq 'groups'}">
													<c:set var="attrlist" value="${groups}"/>
												</c:when>
												<c:when test="${propertydef.description eq 'roles'}">
													<c:set var="attrlist" value="${roles}"/>
												</c:when>
												<c:when test="${propertydef.description eq 'permittedurls'}">
													<c:set var="attrlist" value="${permittedurls}"/>
												</c:when>
											</c:choose>
											<div class="form-group">
											   <label for="<c:out value='${propertydef.description}'/>" class="col-sm-4 control-label"><c:out value='${propertydef.propertylabel}'/></label>
											   <div class="col-sm-6">
													<select name="<c:out value='${propertydef.description}'/>" id="<c:out value='${propertydef.description}'/>" multiple style="width:200px;height:150px;" class="form-control">
													<c:forEach items="${attrlist}" var="allval">
															<c:forEach items="${it[propertydef.description]}" var="selval">
																<c:if test="${allval eq selval}">
																	<c:set var="selected" value="selected"/>
																</c:if>
															</c:forEach>
															<option value="<c:out value='${allval}'/>"  <c:out value='${selected}'/>> <c:out value="${allval}"/> </option>
															<c:set var="selected" value=""/>
														</c:forEach>
													</select>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<tr>
												<div class="form-group">
												    <label for="<c:out value='${propertydef.description}'/>" class="col-sm-4 control-label"><td><c:out value="${propertydef.propertylabel}" /></label>
												    <div class="col-sm-6">
												    	<c:choose>
												    		<c:when test="${propertydef.description eq 'password'}">
												    			<input type="password" name="<c:out value='${propertydef.description}'/>" class="form-control" id="<c:out value='${propertydef.description}'/>" placeholder="<c:out value='${propertydef.propertylabel}' />">
												    		</c:when>
												    		<c:otherwise>
												    			<input type="text" name="<c:out value='${propertydef.description}'/>" class="form-control" id="<c:out value='${propertydef.description}'/>" placeholder="<c:out value='${propertydef.propertylabel}' />" value="<c:out value='${it[propertydef.description]}'/>">
												    		</c:otherwise>
												    	</c:choose>
												    </div>
												</div>
											</tr>
										</c:otherwise>
									</c:choose>	
								</c:otherwise>
							</c:choose>	
						</c:forEach>
						<div class="form-group">
						    <div class="col-sm-6">
						    </div>
						    <div class="col-sm-6">
								<input id="submit" type="button" value="Save" class="btn btn-success">
						    </div>
						</div>
					</form>
				</div>
			</div>	
		    <div id = "footer" class="row"></div>    
		</div>
		
		<script>
			(function($) {
				$.fn.serializeFormJSON = function() {
				   var o = {};
				   $.each( this.find(":input"), function(i, el) { 
				   		var $el = $(el);
					    if(!$el.attr('name'))
					    	return;
					    if(el.type=="select-multiple"){
					    	var exp = {};
					    	o[$el.attr('name')] = $el.val() || [];
					    	$.each(o[$el.attr('name')], function(ind, val){
					    		exp[val] = $el.find('option[value='+val+']').text();
					    	});
					    	//o[$el.attr('name')] = exp;
					    	o[$el.attr('name')] = $el.val();
					    }else if(el.type=='checkbox'||el.type=='radio'){
					    	o[$el.attr('name')] = $el.prop('checked');
					    }else{
					    	o[$el.attr('name')] = $el.val();
					    }
					});
				   return o;
				};
			})(jQuery);
		
			$.ajaxSetup({
				contentType: "application/json; charset=utf-8",
				dataType: "json"
			});
	
			$(document).ready(function(){
				$('#submit').click(function() {
					var send = $("#form").serializeFormJSON();
					//alert(JSON.stringify(send));
					var task = "<c:out value='${task}'/>";
					var myurl;
					if(task == 'add') {
						myurl = "<c:out value='${path}'/>/sf/add/<c:out value='${datatype}'/>";
					} else {
						myurl = "<c:out value='${path}'/>/sf/update/<c:out value='${datatype}'/>";
					}
					//alert(myurl);
					$.ajax({
						url: myurl,
						type: "POST",
						data: JSON.stringify(send),
						error: function(xhr, error) {
							alert('Error!  Status = ' + xhr.status + ' Message = ' + error);
						},
						success: function(data) {
							window.location.href = "<c:out value="${path}"/>/sf/view/<c:out value='${datatype}'/>?ID="+data;
						}
					});
					return false; 
				});
			});
		</script>
	</body>
</html>