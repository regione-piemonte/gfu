/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.determine;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.determina.Determina;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;


@Path("/determine")

@Produces({ "application/json" })
@io.swagger.annotations.Api("Determine API")

public interface DetermineApi  {

	@GET
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads determina to numDetermina and dataDetermina", notes = "", response = Determina.class, tags={ "determina", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Determina.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request.comune ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A comune with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readDeterminaByNumAndData(@QueryParam("numDetermina") String numDetermina, @QueryParam("dataDetermina") String dataDetermina,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}

