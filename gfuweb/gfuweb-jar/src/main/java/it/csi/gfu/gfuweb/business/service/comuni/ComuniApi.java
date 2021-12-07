/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.comuni;


import it.csi.gfu.gfuweb.dto.comune.*;
import it.csi.gfu.gfuweb.dto.Error;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;


@Path("/comuni")

@Produces({ "application/json" })
@io.swagger.annotations.Api("Comuni API")

public interface ComuniApi  {
	
	@GET
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all comuni records from database", notes = "", response = Comune.class, responseContainer = "List", tags={ "comuni", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Comune.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request.comune ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A comune with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllComuni(  @QueryParam("descComune") String descComune,  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/{codIstat}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find Comune by Pk", response = Comune.class, tags={ "comuni", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Comune.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Comune ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Comune with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readComuneByPk( @PathParam("codIstat") String codIstat,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

}

