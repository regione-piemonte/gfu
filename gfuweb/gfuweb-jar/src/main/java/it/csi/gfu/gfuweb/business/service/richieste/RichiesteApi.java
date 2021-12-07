/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/

package it.csi.gfu.gfuweb.business.service.richieste;

import io.swagger.annotations.ApiParam;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.filter.RichiestaFilter;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.dto.richiedente.RichiedenteProvv;
import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Path("/richieste")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the richieste API")
public interface RichiesteApi  {


	@POST
	@Path("/{idRichiesta}/richiedenti/provvedimenti/{idLeggeProvvDr}")	
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Creare rel provv richiedente to richiesta", notes = "Create RichiedenteProvv to richiesta", response = RichiedenteProvv.class, responseContainer = "List", tags={ "Richiesta  | Richiedenti | Provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RichiedenteProvv.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiedente ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiedente with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createLeggeProvvDrToRichiesta( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idLeggeProvvDr") Long idLeggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@POST
	@Path("/{idRichiesta}/richiedenti")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create Richiedenti To idRichiesta", notes = "", response = Richiedente.class, responseContainer = "List", tags={ "Richiesta | Richiedenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiedente.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createRichiedente( @PathParam("idRichiesta") Long idRichiesta,@ApiParam(value = "Richiedente object that needs to be added to the db" ,required=true) List<Richiedente> listRichiedente,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@POST
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Richiesta", notes = "", response = Void.class, tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createRichiesta(@ApiParam(value = "Richiesta object that needs to be added to the db" ,required=true) Richiesta richiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@DELETE
	@Path("/{idRichiesta}/richiedenti/provvedimenti/{idLeggeProvvDr}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Delete ProvvToRichiedenti to idRichiesta", notes = "", response = Void.class, tags={ "Richiesta  | Richiedenti | Provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Richiedente not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response deleteProvvToRichiedentiToRichiesta( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idLeggeProvvDr") Long idLeggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@DELETE
	@Path("/{idRichiesta}/richiedenti/{idRichiedente}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Delete an existing Richiedente", notes = "", response = Void.class, tags={ "Richiesta | Richiedenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Richiedente not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response deleteRichiedente( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idRichiedente") Long idRichiedente,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}/provvedimenti")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all Provvedimenti  To idRichiesta ", notes = "", response = LeggeProvvDr.class, responseContainer = "List", tags={ "Richiesta | Provvedimenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = LeggeProvvDr.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiedente ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiedente with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllProvvedimentiToRichiesta( @PathParam("idRichiesta") Long idRichiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}/richiedenti")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads all richiedenti records To idRichiesta ", notes = "", response = Richiedente.class, responseContainer = "List", tags={ "Richiesta | Richiedenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiedente.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiedente ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiedente with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllRichiedenti( @PathParam("idRichiesta") Long idRichiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads richiesta to PK from database", notes = "", response = Richiesta.class, responseContainer = "List", tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiesta.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiesta ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiesta with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllRichieste(  @QueryParam("isValid") Boolean isValid,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads richiesta to PK from database", notes = "", response = Richiesta.class, tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiesta.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiesta ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiesta with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readRichiestaByPk( @PathParam("idRichiesta") Long idRichiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idRichiesta}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing Richiesta", notes = "", response = Void.class, tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Richiesta not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateRichiesta( @PathParam("idRichiesta") Long idRichiesta,@ApiParam(value = "Richiesta object that needs to be update" ,required=true) Richiesta richiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idRichiesta}/{idAssociazione}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update Richiesta to Forma Associativa", notes = "", response = Void.class, tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateRichiestaToFormaAssociativa( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idAssociazione") Long idAssociazione,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}/richiedenti/provvedimenti")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find Richiedenti+Provv by idRichiesta", notes = "Find Richiedenti+Provv by idRichiesta", response = RichiedenteProvv.class, responseContainer = "List", tags={ "Richiesta | RichiedenteProvv", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RichiedenteProvv.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Richiedente ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Richiedente with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllRichiedenteProvv( @PathParam("idRichiesta") Long idRichiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idRichiesta}/richiedenti/provvedimenti")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update RichiedenteProvv", notes = "", response = RichiedenteProvv.class, tags={ "Richiesta | RichiedenteProvv", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RichiedenteProvv.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateRichiedenteProvv( @PathParam("idRichiesta") Long idRichiesta,@ApiParam(value = "Richiedente object that needs to be added to the db" ,required=true) RichiedenteProvv richiedenteProvv,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}/provvedimenti/{idLeggeProvvDr}/finanziamenti")
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads Finanziamento To Provv To Richiesta", notes = "", response = Finanziamento.class, tags={ "Richiesta | Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),        
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),        
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readFinanziamentoToProvRich( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idLeggeProvvDr") Long idLeggeProvvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@POST
	@Path("/{idRichiesta}/provvedimenti/{idLeggeProvvDr}/finanziamenti")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Create a new Finanziamento", notes = "", response = Finanziamento.class, tags={ "Richiesta | Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),	        
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response createFinanziamento( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idLeggeProvvDr") Long idLeggeProvvDr,@ApiParam(value = "Finanziamento object that needs to be added to the db" ,required=true) Finanziamento finanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);


	@DELETE
	@Path("/{idRichiesta}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Delete an existing Richiesta", notes = "", response = Void.class, tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),
			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "Richiesta not found", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response deleteRichiesta( @PathParam("idRichiesta") Long idRichiesta,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@POST
	@Path("/richiestefilter")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Search by Filter", notes = "", response = Richiesta.class, responseContainer = "List", tags={ "Richieste", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Richiesta.class, responseContainer = "List"),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),        
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 405, message = "Invalid input", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })    
	public Response readRichiesteByFilter(@ApiParam(value = "Richiesta object that needs to be added to the db" ,required=true) RichiestaFilter richiestaFilter,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);


	@GET
	@Path("/provvedimenti/finanziamenti")

	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Find All Provvedimenti and  Finanziamenti", notes = "Find All Provvedimenti and  Finanziamenti", response = RichiestaProvv.class, responseContainer = "List", tags={ "Richiesta | Provvedimenti&amp;Finanziamenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = RichiestaProvv.class, responseContainer = "List"),

			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. Finanziamento ID must be an integer and bigger than 0.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "A Finanziamento with the specified ID was not found.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readAllProvvFinToRichiesta( @NotNull  @QueryParam("idStatoFinanziamento") Long idStatoFinanziamento,  @QueryParam("idLeggeProvvDr") Long idLeggeProvvDr,  @QueryParam("dataProtRichiestaDa") String dataProtRichiestaDa,  @QueryParam("dataProtRichiestaA") String dataProtRichiestaA,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/{idRichiesta}/finanziamenti/{idFinanziamento}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Update an existing Finanziamento", notes = "", response = Finanziamento.class, tags={ "Finanziamento", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Finanziamento.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Invalid ID supplied", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "Finanziamento not found", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 405, message = "Validation exception", response = Error.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response updateFinanziamento(@PathParam("idRichiesta") Long idRichiesta, @PathParam("idFinanziamento") Long idFinanziamento,@ApiParam(value = "Finanziamento object that needs to be update" ,required=true) Finanziamento finanziamento,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/exp")
	@Produces({ "application/vnd.ms-excel" })
	@io.swagger.annotations.ApiOperation(value = "servizio di dowload excel ricerca avanzata richiesta", notes = "dowload excel ricerca avanzata richiesta", response = byte[].class, tags={"downloadExcelRicercaAvanzataRichiesta",})
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "SUCCESS stringa di conferma funzionamento", response = byte[].class),

			@io.swagger.annotations.ApiResponse(code = 302, message = "Found: Success -> redirect on came_from.", response = Void.class),

			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request: When something wrong.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "utente non autorizzato a compiere l'operazione", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 403, message = "richiesta rifiutata", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 404, message = "Dato non trovato", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error", response = String.class),

			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = String.class) })
	public Response downloadExcelRichiesteByFilter(@ApiParam(value = "Filtro di ricerca" ,required=true) RichiestaFilter filtro, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/{idRichiesta}/richiedenti/{idLeggeProvDr}/tettomax/")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@io.swagger.annotations.ApiOperation(value = "Reads Tetto max Tot Richiedenti", notes = "", response = TettoMaxTotRichiedenti.class, tags={ "tetto max | richiedenti", })
	@io.swagger.annotations.ApiResponses(value = { 
			@io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = TettoMaxTotRichiedenti.class),
			@io.swagger.annotations.ApiResponse(code = 204, message = "No resource content", response = Void.class),        
			@io.swagger.annotations.ApiResponse(code = 400, message = "Bad request. TettoMaxTotRichiedenti ID must be an integer and bigger than 0.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 401, message = "Authorization information is missing or invalid.", response = Error.class),        
			@io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 404, message = "A TettoMaxTotRichiedenti with the specified ID was not found.", response = Error.class),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.", response = String.class),
			@io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error.", response = Void.class) })
	public Response readTettoMaxTotRichiedenti( @PathParam("idRichiesta") Long idRichiesta, @PathParam("idLeggeProvDr") Long idLeggeProvDr,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

}


