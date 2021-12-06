/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.richieste;

import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.business.service.richieste.RichiesteApi;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.filter.RichiestaFilter;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.dto.richiedente.RichiedenteProvv;
import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.dto.richiesta.RichiestaToRicercaAvanzata;
import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
import it.csi.gfu.gfuweb.util.ValidatorDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RichiesteApiServiceImpl implements RichiesteApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;


	@Override
	public Response readAllRichieste(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : readAllRichieste ] START");
		try {

			List<Richiesta>richiestaList = businessLogic.readAllRichieste(isValid);
			ResponseBuilder rb = Response.ok(richiestaList);
			LOG.debug("[RichiesteApiServiceImpl : readAllRichieste ] END");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllRichieste","");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readAllRichieste] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
	@Override
	public Response readRichiestaByPk(Long idRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			Richiesta richiesta = (Richiesta) businessLogic.readRichiestaByPk(idRichiesta);
			ResponseBuilder rb = Response.ok(richiesta);
			LOG.debug("[RichiesteApiServiceImpl : readRendicontoByPk ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readRichiestaByPk",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readRichiestaByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
	@Override
	public Response createRichiesta(Richiesta richiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : createRichiesta ] START "); 
		try
		{
			richiesta.validate();

			Richiesta richiestaResult = businessLogic.createRichiesta(richiesta);
			ResponseBuilder rb = Response.ok(richiestaResult);
			LOG.debug("[RichiesteApiServiceImpl : createRichiesta ] END ");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) 
		{		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createRichiesta",richiesta);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::createRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response updateRichiesta(Long idRichiesta, Richiesta richiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: updateRichiesta] START"); 

		try {
			richiesta.validateUpdate();		
			Richiesta richiestaResult = businessLogic.updateRichiesta(idRichiesta, richiesta);

			ResponseBuilder rb = Response.ok(richiestaResult);
			LOG.debug("[RichiesteApiServiceImpl : updateRichiesta ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateRichiesta",richiesta);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::updateRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}

	@Override
	public Response updateRichiestaToFormaAssociativa(Long idRichiesta, Long idAssociazione,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: updateRichiesta] START"); 

		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idAssociazione, Boolean.TRUE, null, null);

			businessLogic.updateRichiestaToFormaAssociativa(idRichiesta, idAssociazione);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[RichiesteApiServiceImpl : updateRichiesta ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idAssociazione: ").append(idAssociazione==null?"":idAssociazione).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateRichiestaToFormaAssociativa",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::updateRichiestaToFormaAssociativa] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response readAllRichiedenti(Long idRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : readAllRichiedenti ] START");
		try {

			List<Richiedente>richiedenteList = businessLogic.readAllRichiedenti(idRichiesta);
			ResponseBuilder rb = Response.ok(richiedenteList);
			LOG.debug("[RichiesteApiServiceImpl : readAllRichiedenti ] END");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllRichiedenti",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readAllRichiedenti] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response createRichiedente(Long idRichiesta, List<Richiedente> listRichiedente, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : createRichiedente ] START "); 
		try
		{
			ValidatorDto.validateNullValue(listRichiedente, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			for (int i = 0; i < listRichiedente.size(); i++) {
				listRichiedente.get(i).validate();
			}

			List<Richiedente> listRichiedenteResult = businessLogic.createRichiedente(idRichiesta,listRichiedente);
			ResponseBuilder rb = Response.ok(listRichiedenteResult);
			LOG.debug("[RichiesteApiServiceImpl : createRichiedente ] END ");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) 
		{		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			if(listRichiedente!=null) {
				for (int i = 0; i < listRichiedente.size(); i++) {
					sb.append(" Richiedente: ").append(listRichiedente.get(i)).append("\n");
				}
			}
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createRichiedente",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::createRichiedente] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}

	@Override
	public Response deleteRichiedente(Long idRichiesta, Long idRichiedente, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: deleteRichiedente] START"); 

		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idRichiedente, Boolean.TRUE, null, null);

			businessLogic.deleteRichiedente(idRichiesta, idRichiedente);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[RichiesteApiServiceImpl : deleteRichiedente ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idRichiedente: ").append(idRichiedente==null?"":idRichiedente).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("deleteRichiedente",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::deleteRichiedente] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response readAllProvvedimentiToRichiesta(Long idRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : readAllProvvedimentiToRichiesta ] START");
		try {

			List<LeggeProvvDr>provvedimentiList = businessLogic.readAllProvvedimentiToRichiesta(idRichiesta);
			ResponseBuilder rb = Response.ok(provvedimentiList);
			LOG.debug("[RichiesteApiServiceImpl : readAllProvvedimentiToRichiesta ] END");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllProvvedimentiToRichiesta",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readAllProvvedimentiToRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}
	@Override
	public Response deleteProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: deleteProvvToRichiedentiToRichiesta] START"); 

		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvvDr, Boolean.TRUE, null, null);

			businessLogic.deleteProvvToRichiedentiToRichiesta(idRichiesta, idLeggeProvvDr);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[RichiesteApiServiceImpl : deleteProvvToRichiedentiToRichiesta ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idLeggeProvvDr: ").append(idLeggeProvvDr==null?"":idLeggeProvvDr).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("deleteProvvToRichiedentiToRichiesta",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::deleteProvvToRichiedentiToRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}

	@Override
	public Response createLeggeProvvDrToRichiesta(Long idRichiesta, Long idLeggeProvvDr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : createLeggeProvvDrToRichiesta ] START "); 
		try
		{
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvvDr, Boolean.TRUE, null, null);

			List<RichiedenteProvv> listRichiedenteProvv = businessLogic.createLeggeProvvDrToRichiesta(idRichiesta,idLeggeProvvDr);
			ResponseBuilder rb = Response.ok(listRichiedenteProvv);
			LOG.debug("[RichiesteApiServiceImpl : createLeggeProvvDrToRichiesta ] END ");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) 
		{		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO)) {
				LOG.debug("!!!CODE 16 COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO, ErrorMessages.MESSAGE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO)) {
				LOG.debug("!!!CODE 17 RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO, ErrorMessages.MESSAGE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idLeggeProvvDr: ").append(idLeggeProvvDr==null?"":idLeggeProvvDr).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createLeggeProvvDrToRichiesta",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::createLeggeProvvDrToRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response readAllRichiedenteProvv(Long idRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : readAllRichiedenteProvv ] START");
		try {
			List<RichiedenteProvv>richiedenteProvvList = businessLogic.readAllRichiedenteProvv(idRichiesta);
			ResponseBuilder rb = Response.ok(richiedenteProvvList);
			LOG.debug("[RichiesteApiServiceImpl : readAllRichiedenteProvv ] END");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllRichiedenteProvv",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readAllRichiedenteProvv] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
	@Override
	public Response updateRichiedenteProvv(Long idRichiesta, RichiedenteProvv richiedenteProvv, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: updateRichiedenteProvv] START"); 

		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			richiedenteProvv.validate();

			businessLogic.updateRichiedenteProvv(idRichiesta, richiedenteProvv);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[RichiesteApiServiceImpl : updateRichiedenteProvv ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" richiedenteProvv: ").append(richiedenteProvv).append("\n");
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateRichiedenteProvv",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::updateRichiedenteProvv] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}
	@Override
	public Response readFinanziamentoToProvRich(Long idRichiesta, Long idLeggeProvvDr, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvvDr, Boolean.TRUE, null, null);

			Finanziamento finanziamento = (Finanziamento) businessLogic.readFinanziamentoToProvRich(idRichiesta, idLeggeProvvDr);
			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[RichiesteApiServiceImpl : readFinanziamentoToProvRich ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();

		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idLeggeProvvDr: ").append(idLeggeProvvDr==null?"":idLeggeProvvDr).append("\n");
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readFinanziamentoToProvRich",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readFinanziamentoToProvRich] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
	@Override
	public Response createFinanziamento(Long idRichiesta, Long idLeggeProvvDr, Finanziamento finanziamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl : createFinanziamento ] START "); 
		try
		{
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvvDr, Boolean.TRUE, null, null);
			finanziamento.validate();

			Finanziamento finanziamentoResult = businessLogic.createFinanziamento(idRichiesta,idLeggeProvvDr, finanziamento);
			ResponseBuilder rb = Response.ok(finanziamentoResult);
			LOG.debug("[RichiesteApiServiceImpl : createFinanziamento ] END ");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) 
		{		
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createFinanziamento",finanziamento);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::createFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response deleteRichiesta(Long idRichiesta, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: deleteRichiesta] START"); 

		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);

			businessLogic.deleteRichiesta(idRichiesta);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[RichiesteApiServiceImpl : deleteRichiesta ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("deleteRichiesta",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::deleteRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response readRichiesteByFilter(RichiestaFilter richiestaFilter, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			richiestaFilter.validate();
			List<RichiestaToRicercaAvanzata>richiestaList = businessLogic.readRichiesteByFilter(richiestaFilter);
			ResponseBuilder rb = Response.ok(richiestaList);
			LOG.debug("[RichiesteApiServiceImpl : readRichiesteByFilter ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readRichiesteByFilter",richiestaFilter);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readRichiesteByFilter] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response readAllProvvFinToRichiesta(@QueryParam("idStatoFinanziamento") Long idStatoFinanziamento, @QueryParam("idLeggeProvvDr") Long idLeggeProvvDr,
			@QueryParam("dataProtRichiestaDa") String dataProtRichiestaDa, @QueryParam("dataProtRichiestaA") String dataProtRichiestaA, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateNumber(idStatoFinanziamento, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvvDr, Boolean.FALSE, null, null);
			ValidatorDto.validateDate(dataProtRichiestaDa, Boolean.FALSE, null, null);
			ValidatorDto.validateDate(dataProtRichiestaA, Boolean.FALSE, null, null);
			ValidatorDto.validateDateDaA(dataProtRichiestaDa, dataProtRichiestaA);

			List<RichiestaProvv>richiestaProvvList = businessLogic.readAllProvvFinToRichiesta(idStatoFinanziamento, idLeggeProvvDr, dataProtRichiestaDa, dataProtRichiestaA);
			ResponseBuilder rb = Response.ok(richiestaProvvList);
			LOG.debug("[RichiesteApiServiceImpl : readAllProvvFinToRichiesta ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();

		}
		catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e)
		{
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idStatoFinanziamento: ").append(idStatoFinanziamento==null?"":idStatoFinanziamento).append("\n");
			sb.append(" idLeggeProvvDr: ").append(idLeggeProvvDr==null?"":idLeggeProvvDr).append("\n");
			sb.append(" dataProtRichiestaDa: ").append(dataProtRichiestaDa==null?"":dataProtRichiestaDa).append("\n");
			sb.append(" dataProtRichiestaA: ").append(dataProtRichiestaA==null?"":dataProtRichiestaA).append("\n");
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllProvvFinToRichiesta",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readAllProvvFinToRichiesta] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response updateFinanziamento(Long idRichiesta,Long idFinanziamento, Finanziamento finanziamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[RichiesteApiServiceImpl: updateFinanziamento] START"); 

		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			finanziamento.validate();

			Finanziamento finanziamentoResult = businessLogic.updateFinanziamento(idRichiesta,idFinanziamento, finanziamento);

			ResponseBuilder rb = Response.ok(finanziamentoResult);
			LOG.debug("[RichiesteApiServiceImpl : updateFinanziamento ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
			if(e.getMessage().equals(ErrorMessages.CODE_1_CHIAVE_DUPLICATA)) {
				LOG.debug("!!!CODE 1 chiave duplicata!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ErrorMessages.MESSAGE_1_CHIAVE_DUPLICATA)).build();				
			}
			if(e.getMessage().equals(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED)) {
				LOG.debug("!!!CODE 11 CONSTRAINT VIOLATED!!!!");
				e.printStackTrace();
				return Response.status(Response.Status.BAD_REQUEST).
						entity(ResponseUtils.createJSONResponseMessage(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ErrorMessages.MESSAGE_11_CONSTRAINT_VIOLATED)).build();				
			}
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		finally {

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateFinanziamento",finanziamento);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::updateFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response downloadExcelRichiesteByFilter(RichiestaFilter filtro, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		LOG.debug("[RichiesteApiServiceImpl: downloadExcelRichiesteByFilter] START"); 

		String fileName = "ExportRichieste";
		String mimeType = "xls";
		String contentDisposition = "attachment;filename=\"" + fileName + "." + mimeType;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			filtro.validate();
			businessLogic.createExcel(filtro, baos);

		} catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} 
		catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.BAD_REQUEST.name(), ErrorMessages.BAD_REQUEST)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("downloadExcelRichiesteByFilter",filtro);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::downloadExcelRichiesteByFilter] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
		return Response.ok(baos.toByteArray()).header("content-disposition", contentDisposition).build();
	}

	@Override
	public Response readTettoMaxTotRichiedenti(Long idRichiesta, Long idLeggeProvDr, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateNumber(idRichiesta, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idLeggeProvDr, Boolean.TRUE, null, null);

			TettoMaxTotRichiedenti tettoMaxTotRichiedenti = (TettoMaxTotRichiedenti) businessLogic.readTettoMaxToRichiedenti(idRichiesta, idLeggeProvDr);
			ResponseBuilder rb = Response.ok(tettoMaxTotRichiedenti);
			LOG.debug("[RichiesteApiServiceImpl : readTettoMaxTotRichiedenti ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();

		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idRichiesta: ").append(idRichiesta==null?"":idRichiesta).append("\n");
			sb.append(" idLeggeProvDr: ").append(idLeggeProvDr==null?"":idLeggeProvDr).append("\n");
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readTettoMaxTotRichiedenti",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RichiesteApiServiceImpl::readTettoMaxTotRichiedenti] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}
}
