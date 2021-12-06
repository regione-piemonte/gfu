/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.user;

import it.csi.gfu.gfuweb.dto.*;
import java.math.BigDecimal;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;

@Path("/profili")

@Produces({ "application/json" })
@io.swagger.annotations.Api("Profili API")

public interface ProfiliApi  {
   
    @GET    
    @Produces({"application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get Array[Profili]", notes = "Profili List", response = Profilo.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        	@io.swagger.annotations.ApiResponse(code = 200, message = "Success!", response = Profilo.class), 
        	@io.swagger.annotations.ApiResponse(code = 204, message = "No Content", response = Void.class),
        	@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = String.class),
        	@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = String.class),
        	@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = String.class),       
        	@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = String.class),       
        	@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})		
    public Response getProfili(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/{id}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Operation di accesso al Profilo by Id", notes = "Restituisce l'ogg Profilo", response = Profilo.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        	@io.swagger.annotations.ApiResponse(code = 200, message = "Success!", response = Profilo.class), 
        	@io.swagger.annotations.ApiResponse(code = 204, message = "No Content", response = Void.class),
        	@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = String.class),
        	@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = String.class),
        	@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = String.class),       
        	@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = String.class),       
        	@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    
    public Response getProfiloById( @PathParam("id") BigDecimal id,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
}
