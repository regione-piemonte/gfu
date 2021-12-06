/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.user;

import io.swagger.annotations.ApiParam;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteResult;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;

@Path("/utenti")

@Produces({ "application/json" })
@io.swagger.annotations.Api("Utenti API")

public interface UtentiApi  {
     
    @POST   
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Operation to get list Utente ", notes = "Response: Array[Utente]", response = Utente.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Success!", response = Utente.class), 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content", response = Void.class),
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = String.class),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = String.class),
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = String.class),       
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = String.class),       
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    
    public Response getUtentiByFilter(@ApiParam(value = "UtenteFilter" ,required=true) UtenteFilter filtro,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/utente/profilo")    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Operation di ", notes = "Response: Profilo", response = UtenteResult.class, tags={ "getProfilo", })
    @io.swagger.annotations.ApiResponses(value = { 
    	@io.swagger.annotations.ApiResponse(code = 200, message = "Success!", response = Utente.class), 
    	@io.swagger.annotations.ApiResponse(code = 204, message = "No Content", response = Void.class),
    	@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = String.class),
    	@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = String.class),
    	@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = String.class),       
    	@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = String.class),       
    	@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})
    public Response getProfilo( @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    
    @GET
    @Path("/utente/sessione")    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Operation verify user ", notes = "Restituisce l'oggetto profilo ", response = UtenteResult.class, tags={ "sessione", })
    @io.swagger.annotations.ApiResponses(value = { 
    	@io.swagger.annotations.ApiResponse(code = 200, message = "Success!", response = Utente.class), 
    	@io.swagger.annotations.ApiResponse(code = 204, message = "No Content", response = Void.class),
    	@io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request", response = String.class),
    	@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = String.class),
    	@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = String.class),       
    	@io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = String.class),       
    	@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class)})		
    public Response logoutAppl( @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    

}
