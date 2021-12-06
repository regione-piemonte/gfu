/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.richiedenti;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.servlet.http.HttpServletRequest;

@Path("/richiedenti")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the richiedenti API")
public interface RichiedentiApi  {

	@GET
	@Path("/{idRichiedente}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find Richiedente by Pk", response = Richiedente.class, tags={ "Richiedenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiedente.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiedente ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiedente with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readRichiedenteByPk( @PathParam("idRichiedente") Long idRichiedente,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

}
