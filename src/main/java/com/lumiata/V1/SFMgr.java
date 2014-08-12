/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.V1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.lumiata.exception.NotFoundException;
import com.lumiata.handler.SFHandler;
import com.lumiata.util.SFConstants;
import com.lumiata.util.SFUtil;
import com.sun.jersey.api.view.Viewable;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author rakesh
 *
 */
@Path("/V1/sf")
@Api(value = "/V1/sf", description = "Snowflake Operations")
public class SFMgr {

	private static final Logger logger = Logger.getLogger(SFMgr.class);
	
	@GET
	@Path("/home")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Home page", notes = "Displays Home page", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying home") })
	public Response viewHome(@Context HttpServletRequest request,
			@ApiParam(value = "accessdenied", required = false) @QueryParam("accessdenied") boolean accessdenied) 
					throws NotFoundException {
		if(accessdenied) {
			request.setAttribute("msg", "You do not have permission to access this page!");
		}
		return Response.ok(new Viewable("/home.jsp", null)).build();
	}
	
	@GET
	@Path("/view/all/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Get Entity List", notes = "Displays List of All Entities", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying entity") })
	public Response viewUserList(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = false) @PathParam("datatypetype") String datatypetype) 
					throws NotFoundException {
		
		SFHandler sfHandler = SFHandler.instance();
		List<Map<String, Object>> result = sfHandler.getDataList(datatypetype);
		
		if (result != null) {
			setDefaultRequestAttributes(request, datatypetype, false);
			return Response.ok(new Viewable("/list.jsp", result)).build();
		} else {
			logger.debug("No objects found");
			return Response.ok(new Viewable("/list.jsp", new HashMap<String, Object>().values())).build();
		}
	}
	
	@GET
	@Path("/view/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Get Entity", notes = "Displays Entity in View Mode", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying entity") })
	public Response viewDetail(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype,
			@ApiParam(value = "ID", required = true) @QueryParam("ID") String ID) throws NotFoundException {
		Map<String, Object> objMap = null;
		if(ID != null && !ID.trim().isEmpty()) {
			objMap = SFHandler.instance().getDataById(ID, datatypetype);
		}
		
		if (objMap != null) {
			setDefaultRequestAttributes(request, datatypetype, false);
			return Response.ok(new Viewable("/view.jsp", objMap)).build();
		} else {
			logger.error(new NotFoundException(404, "Unable to load user"));
			throw new NotFoundException(404, "Unable to load user");
		}
	}
	
	@GET
	@Path("/new/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Add New Entity", notes = "Display Blank Entity in Add Mode", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying object") })
	public Response add(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = false) @PathParam("datatypetype") String datatypetype) 
					throws NotFoundException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		
		request.setAttribute("task", "add");
		setDefaultRequestAttributes(request, datatypetype, true);
		return Response.ok(new Viewable("/edit.jsp", mapObj)).build();
	}
	
	@GET
	@Path("/edit/{datatypetype}/")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Edit Entity", notes = "Displays Entity in Edit Mode", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying entity") })
	public Response edit(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype,
			@ApiParam(value = "ID", required = true) @QueryParam("ID") String ID) throws NotFoundException {
		Map<String, Object> objMap = null;
		SFHandler sfHandler = SFHandler.instance();
		if(ID != null && !ID.trim().isEmpty()) {
			objMap = sfHandler.getDataById(ID, datatypetype);
			if(objMap.containsKey("password")) {
				objMap.put("password", "");
			}
		}
		
		if (objMap != null) {
			request.setAttribute("task", "update");
			setDefaultRequestAttributes(request, datatypetype, true);
			return Response.ok(new Viewable("/edit.jsp", objMap)).build();
		} else {
			logger.error(new NotFoundException(404, "Unable to load entity"));
			throw new NotFoundException(404, "Unable to load entity");
		}
	}
	
	@POST
	@Path("/delete/{datatypetype}/")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Delete Entity", notes = "Deletes an Entity", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying entity") })
	public Response delete(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype,
			@ApiParam(value = "ID", required = true) @QueryParam("ID") String ID) throws NotFoundException {
		Map<String, Object> objMap = null;
		SFHandler sfHandler = SFHandler.instance();
		if(ID != null && !ID.trim().isEmpty()) {
			objMap = new HashMap<String, Object>();
			sfHandler.deleteData(ID, datatypetype);
		}
		
		if (objMap != null) {
			setDefaultRequestAttributes(request, datatypetype, false);
			return Response.ok(new Viewable("/edit.jsp", objMap)).build();
		} else {
			logger.error(new NotFoundException(404, "Unable to delete entity"));
			throw new NotFoundException(404, "Unable to delete entity");
		}
	}
	
	@POST
	@Path("/add/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Add Entity", notes = "Adds New Entity", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error updating user") })
	public Response addUserDetail(@Context HttpServletRequest request,
			JsonNode jsonNode,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype) 
					throws NotFoundException {
		
		String _ID = null;
		SFHandler sfHandler = SFHandler.instance();
		_ID = sfHandler.addData(jsonNode, datatypetype);
		
		if (_ID != null) {
			return Response.ok(_ID).build();
		} else {
			logger.error(new NotFoundException(404, "Unable to update user"));
			throw new NotFoundException(404, "Unable to update user");
		}
	}
	
	@POST
	@Path("/update/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Update Entity", notes = "Update Existing Entity", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error updating user") })
	public Response updateUserDetail(@Context HttpServletRequest request,
			JsonNode jsonNode,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype) throws NotFoundException {
		
		String _ID = null;
		SFHandler sfHandler = SFHandler.instance();
		_ID = sfHandler.updateData(jsonNode, datatypetype);
		
		if (_ID != null) {
			return Response.ok(_ID).build();
		} else {
			logger.error(new NotFoundException(404, "Unable to update user"));
			throw new NotFoundException(404, "Unable to update user");
		}
	}
	
	@GET
	@Path("/search/{datatypetype}")
	@Produces({ MediaType.TEXT_HTML })
	@ApiOperation(value = "Search User", notes = "Search User", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid input parameters"),
			@ApiResponse(code = 404, message = "Error displaying user") })
	public Response searchUser(@Context HttpServletRequest request,
			@ApiParam(value = "datatypetype", required = true) @PathParam("datatypetype") String datatypetype,
			@ApiParam(value = "keyword", required = true) @QueryParam("keyword") String keyword) 
					throws NotFoundException {
		List<Map<String, Object>> result = SFHandler.instance().searchData(keyword, datatypetype);
		if (result != null) {
			setDefaultRequestAttributes(request, datatypetype, false);
			return Response.ok(new Viewable("/search.jsp", result)).build();
		} else {
			return Response.ok(new Viewable("/search.jsp", new ArrayList<Map<String, Object>>())).build();
		}
	}
	
	private void setDefaultRequestAttributes(HttpServletRequest request, String datatypetype, boolean loadCollection) {
		SFHandler sfHandler = SFHandler.instance();
		Map<String, Object> colldef = sfHandler.getCollectionDef(datatypetype);
		List<Map<String, Object>> propertydefinitions = SFUtil.getSortedPropertyDef(colldef);
		if(loadCollection) {
			for(Map<String, Object> propertyDef : propertydefinitions) {
				String parentColl = (String)propertyDef.get("parentcollection");
				String parentProperty = (String)propertyDef.get("parentproperty");
				String propertyName = (String)propertyDef.get("**NESTED**");
				if(parentColl != null && parentProperty != null) {
					List<String> list = new ArrayList<String>();
					List<Map<String, Object>> parentList = SFHandler.instance().getDataList(parentColl);
					for(Map<String, Object> parentMap : parentList) {
						list.add((String)parentMap.get(parentProperty));
					}
					request.setAttribute(propertyName, list);
				}
			}
		}
		request.setAttribute(SFConstants.datatype, datatypetype);
		request.setAttribute(SFConstants.PROPERTYDEFLIST, propertydefinitions);
		
		//ObjectNode returnJson = JsonNodeFactory.instance.objectNode();
		//returnJson.put(SFConstants.datatype, datatypetype);
		//returnJson.put(SFConstants.PROPERTYDEFLIST, propertydefinitions);
	}
}
