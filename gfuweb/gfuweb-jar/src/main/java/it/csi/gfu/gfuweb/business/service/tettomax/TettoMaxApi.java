/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.tettomax;

import java.math.BigDecimal;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.tetto.TettoMax;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/tettomax")

public interface TettoMaxApi  {

	@POST

	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Tetto max", notes = "Create a new Tetto max", response = TettoMax.class, tags={ "tetto-max", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TettoMax.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createTettoMax( TettoMax tettoMax,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

	@DELETE
	@Path("/{idTettoMax}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Delete an existing TettoMax", notes = "", response = Void.class, tags={ "TettoMax", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "TettoMax not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response deleteTettoMaxByPk( @PathParam("idTettoMax") Long idTettoMax,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

	@GET


	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all TettoMax records from database", notes = "", response = TettoMax.class, responseContainer = "List", tags={ "tetto-max", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TettoMax.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. TettoMax ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A TettoMax with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllTettoMax( @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

	@GET
	@Path("/{idTettoMax}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find tettoMax by Pk", response = Object.class, tags={ "TettoMax", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Object.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. TettoMax ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A TettoMax with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readTettoMaxByPk( @PathParam("idTettoMax") Long idTettoMax,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
	@PUT

	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing TettoMax", notes = "", response = TettoMax.class, tags={ "TettoMax", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TettoMax.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "TettoMax not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateTettoMax( TettoMax tettoMax,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
}
