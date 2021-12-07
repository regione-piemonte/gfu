/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.finanziamenti;

import io.swagger.annotations.ApiParam;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.erogazione.DeterminaToErogazioni;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbSintetica;
import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.servlet.http.HttpServletRequest;

import javax.validation.constraints.*;

@Path("/finanziamenti")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the finanziamenti API")
public interface FinanziamentiApi  {

	@POST

	@Path("/{idFinanziamento}/erogazioni")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Erogazione", notes = "", response = Finanziamento.class, tags={ "Finanziamento | Erogazioni", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createErogazione( @PathParam("idFinanziamento") Long idFinanziamento,@ApiParam(value = "Erogazione object that needs to be added to the db" ,required=true) Erogazione erogazione,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idFinanziamento}/erogazioni")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find All Erogazioni to Finanziamento", notes = "Find All Erogazioni to Finanziamento", response = Erogazione.class, responseContainer = "List", tags={ "Finanziamento | Erogazioni", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Erogazione.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllErogazioniToFinanziamento( @PathParam("idFinanziamento") Long idFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idFinanziamento}")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find Finanziamento by Pk", notes = "Find Finanziamento by Pk", response = Finanziamento.class, tags={ "Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readFinanziamentoByPk( @PathParam("idFinanziamento") Long idFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idFinanziamento}/stato")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find Stato Finanziamento to Finanziamento", notes = "Find stato finanziamento", response = StatoFinanziamento.class, tags={ "Finanziamento | Stato Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = StatoFinanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readStatoFinanziamento( @PathParam("idFinanziamento") Long idFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/erogazioni/determine")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update all Erogazioni to link Determine", notes = "", response = Void.class, tags={ "Finanziamento | Erogazioni", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "Finanziamento not found", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateAllErogazioniToDetermina(@ApiParam(value = "" ,required=true) DeterminaToErogazioni determinaToErogazioni,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idFinanziamento}/stato/{idStatoFinanziamento}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update Stato Finanziamento to Finanziamento", notes = "", response = Finanziamento.class, tags={ "Finanziamento | Stato Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateStatoFinToFinanziamento( @PathParam("idFinanziamento") Long idFinanziamento, @PathParam("idStatoFinanziamento") Long idStatoFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idFinanziamento}/erogazioni/{idErogazione}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update Erogazione", notes = "", response = Finanziamento.class, tags={ "Finanziamento | Erogazioni update", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateErogazione(  @PathParam("idFinanziamento") Long idFinanziamento, @PathParam("idErogazione") Long idErogazione,@ApiParam(value = "Erogazione object that needs to be added to the db" ,required=true) Erogazione erogazione,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	 @GET
	    @Path("/{idFinanziamento}/pratiche")
	    
	    @Produces({ "application/json" })
	    @io.swagger.annotations.ApiOperation(value = "Reads Pratica Urbanistica Finanziamento", notes = "", response = Finanziamento.class, tags={ "Richiesta | Finanziamento | Pratica UrbGFU", })
	    @io.swagger.annotations.ApiResponses(value = { 
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	    public Response readPraticaUrbGfuToFinanziamento( @PathParam("idFinanziamento") Long idFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	 @POST
	    @Path("/{idFinanziamento}/pratiche")
	    @Consumes({ "application/json" })
	    @Produces({ "application/json" })
	    @io.swagger.annotations.ApiOperation(value = "Create a new Pratica Urb Gfu - Associa Pratica Urb a Finanziamento Gfu", notes = "", response = Finanziamento.class, tags={ "Richiesta | Finanziamento | Pratica UrbGFU", })
	    @io.swagger.annotations.ApiResponses(value = { 
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	    public Response createPraticaUrbGfu( @PathParam("idFinanziamento") Long idFinanziamento,@ApiParam(value = "Finanziamento object that needs to be added to the db" ,required=true) PraticaUrbGfu praticaUrbGfu,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	 @DELETE
	    @Path("/{idFinanziamento}/pratiche")
	    @Consumes({ "application/json" })
	    @Produces({ "application/json" })
	    @io.swagger.annotations.ApiOperation(value = "Delete associazione Pratica Urb to Finanziamento Gfu", notes = "", response = Finanziamento.class, tags={ "Richiesta | Finanziamento | Pratica UrbGFU", })
	    @io.swagger.annotations.ApiResponses(value = { 
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	    public Response deletePraticaUrbGfu( @PathParam("idFinanziamento") Long idFinanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	   @GET
	    @Path("/{idFinanziamento}/pratiche/{numPratica}")
	    
	    @Produces({ "application/json" })
	    @io.swagger.annotations.ApiOperation(value = "Reads Pratica Urbanistica Detail in PRAURB", notes = "", response = PraticaUrb.class, tags={ "Richiesta | Finanziamento | Pratica UrbGFU", })
	    @io.swagger.annotations.ApiResponses(value = { 
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = PraticaUrb.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. .", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	    public Response readPraticaUrbDetail( @PathParam("idFinanziamento") Long idFinanziamento, @PathParam("numPratica") String numPratica,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	   @GET
	    @Path("/pratiche/")
	    
	    @Produces({ "application/json" })
	    @io.swagger.annotations.ApiOperation(value = "Reads Pratiche in PRAURB by Filter (Istat Comune | dataProvDa | dataProvvA) ", notes = "", response = PraticaUrbSintetica.class, tags={ "Richiesta | Finanziamento | Pratica UrbGFU", })
	    @io.swagger.annotations.ApiResponses(value = { 
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = PraticaUrbSintetica.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. .", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
	        
	        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	    public Response readAllPraticheUrbByFilter( @NotNull  @QueryParam("istatComune") String istatComune,  @QueryParam("dataProvvedimentoDa") String dataProvvedimentoDa,  @QueryParam("dataProvvedimentoA") String dataProvvedimentoA,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
