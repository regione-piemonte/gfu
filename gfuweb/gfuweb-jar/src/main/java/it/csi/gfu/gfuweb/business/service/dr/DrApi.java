/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.dr;

import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.dto.Error;
import io.swagger.annotations.ApiParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.servlet.http.HttpServletRequest;

@Path("/dr")
@Produces({ "application/json" })
@io.swagger.annotations.Api("the dr API")
public interface DrApi  {

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Dr", notes = "Create a new Dr", response = Dr.class, tags={ "dr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Dr.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createDr(@ApiParam(value = "Dr object that needs to be added to the db" ,required=true) Dr dr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all Dr records from database", notes = "", response = Dr.class, responseContainer = "List", tags={ "dr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Dr.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Dr ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Dr with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllDr(  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{id}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find dr by Pk", response = Object.class, tags={ "dr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Object.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Dr ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A dr with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readDrByPk( @PathParam("id") Long id,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing Dr", notes = "", response = Dr.class, tags={ "dr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Dr.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Dr not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateDr(@ApiParam(value = "Dr object that needs to be update" ,required=true) Dr dr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/tipi")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all TipoDr records from database", notes = "", response = TipoDr.class, responseContainer = "List", tags={ "TipoDr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TipoDr.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Dr with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllTipiDr(  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
