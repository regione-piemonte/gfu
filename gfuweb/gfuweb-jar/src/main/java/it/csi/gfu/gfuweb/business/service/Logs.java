/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.ApiParam;
import it.csi.gfu.gfuweb.dto.Log;
import it.csi.gfu.gfuweb.dto.LogResult;

@Path("/logs")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the logs to gfuweb")

public interface Logs
{
   

    @POST
    @Path("/log/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "servizio Log", notes = "inserisce entry di logs nel file deputato", response = LogResult.class, tags={ "createLogs",})
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "SUCCESS stringa di conferma funzionamento", response = LogResult.class),
        
        @io.swagger.annotations.ApiResponse(code = 302, message = "Found: Success -> redirect on came_from.", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request: When something wrong.", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = String.class) })
    public Response createLogs(@ApiParam(value = "logs da inserire", required=true) Log log, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders);


}