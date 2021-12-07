/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.leggi;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;

@Path("/leggi")

@Produces({ "application/json" })
@io.swagger.annotations.Api("Leggi API")

public interface LeggiApi  {

		@POST
		@Consumes({ "application/json" })
		@Produces({ "application/json" })
		@io.swagger.annotations.ApiOperation(value = "Create a new Legge", notes = "", response = Legge.class, tags={ "leggi", })
		@io.swagger.annotations.ApiResponses(value = { 
				@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Legge.class),

				@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

				@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
		public Response createLegge(@ApiParam(value = "Legge object that needs to be added to the db" ,required=true) Legge legge,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

		@GET 
		@Produces({ "application/json" })
		@io.swagger.annotations.ApiOperation(value = "Reads all leggi records from database", notes = "", response = Legge.class, responseContainer = "List", tags={ "leggi", })
		@io.swagger.annotations.ApiResponses(value = { 
				@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Legge.class, responseContainer = "List"),

				@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

				@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Legge ID must be an integer and bigger than 0.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 404, message = "A Legge with the specified ID was not found.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

				@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
		public Response readAllLeggi(  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

		@GET
		@Path("/{id}")
		@Produces({ "application/json" })
		@io.swagger.annotations.ApiOperation(value = "Find by Pk", notes = "Find Legge by Pk", response = Legge.class, tags={ "leggi", })
		@io.swagger.annotations.ApiResponses(value = { 
				@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Legge.class),

				@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

				@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Legge ID must be an integer and bigger than 0.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 404, message = "A Legge with the specified ID was not found.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

				@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
		public Response readLeggeByPk( @PathParam("id") Long id,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

		@PUT  
		@Consumes({ "application/json" })
		@Produces({ "application/json" })
		@io.swagger.annotations.ApiOperation(value = "Update an existing Legge", notes = "", response = Legge.class, tags={ "leggi", })
		@io.swagger.annotations.ApiResponses(value = { 
				@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Legge.class),

				@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 404, message = "Legge not found", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),

				@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

				@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
		public Response updateLegge(@ApiParam(value = "Legge object that needs to be update" ,required=true) Legge legge,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	
	@POST
	@Path("/provvedimenti/dr")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new LeggeProvvDr", notes = "Create a new LeggeProvvDr", response = LeggeProvvDr.class, tags={ "LeggeProvvDr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = LeggeProvvDr.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createLeggeProvvDr(@ApiParam(value = "LeggeProvvDr object that needs to be added to the db" ,required=true) LeggeProvvDr leggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/provvedimenti/dr")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all LeggiProvvDr records from database", notes = "", response = LeggeProvvDr.class, responseContainer = "List", tags={ "LeggeProvvDr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = LeggeProvvDr.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Dr with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllLeggeProvvDr(  @QueryParam("descLeggeProvvDr") String descLeggeProvvDr,  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest );

	@GET
	@Path("/provvedimenti/dr/{idLeggeProvvDr}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads a LeggiProvvDr record from database", notes = "", response = LeggeProvvDr.class, responseContainer = "List", tags={ "LeggeProvvDr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = LeggeProvvDr.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Dr with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readLeggeProvvDrByPk( @PathParam("idLeggeProvvDr") Long idLeggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/provvedimenti/dr")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing LeggeProvvDr", notes = "", response = LeggeProvvDr.class, tags={ "LeggeProvvDr", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = LeggeProvvDr.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "Dr not found", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateLeggeProvvDr(@ApiParam(value = "Dr object that needs to be update" ,required=true) LeggeProvvDr leggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	
}
