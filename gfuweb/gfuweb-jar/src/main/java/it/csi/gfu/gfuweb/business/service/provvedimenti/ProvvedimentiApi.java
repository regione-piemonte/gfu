/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.provvedimenti;

import io.swagger.annotations.ApiParam;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.provvedimento.Provvedimento;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.servlet.http.HttpServletRequest;

@Path("/provvedimenti")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the provvedimenti API")
public interface ProvvedimentiApi  {


	@POST

	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Provvedimento", notes = "Create a new Provvedimento", response = Provvedimento.class, tags={ "provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Provvedimento.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createProvvedimento(@ApiParam(value = "Provvedimento object that needs to be added to the db" ,required=true) Provvedimento provvedimento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET


	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all Provvedimenti records from database", notes = "", response = Provvedimento.class, responseContainer = "List", tags={ "provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Provvedimento.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Provvedimento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Provvedimento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllProvvedimenti(  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{id}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find Provvedimento by Pk", response = Object.class, tags={ "provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Object.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Provvedimento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Provvedimento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readProvvedimentoByPk( @PathParam("id") Long id,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT

	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing Provvedimento", notes = "", response = Provvedimento.class, tags={ "provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Provvedimento.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "Legge not found", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateProvvedimento(@ApiParam(value = "Provvedimento object that needs to be update" ,required=true) Provvedimento provvedimento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
