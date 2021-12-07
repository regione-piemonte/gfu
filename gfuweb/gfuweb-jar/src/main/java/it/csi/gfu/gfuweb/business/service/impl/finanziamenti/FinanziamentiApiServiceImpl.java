/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.finanziamenti;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.gfu.gfuweb.business.service.*;
import it.csi.gfu.gfuweb.business.service.finanziamenti.FinanziamentiApi;
import it.csi.gfu.gfuweb.dto.erogazione.DeterminaToErogazioni;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbSintetica;
import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.RESTException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
import it.csi.gfu.gfuweb.util.ValidatorDto;
import it.csi.gfu.gfuweb.util.rest.RestResponseErrorHandler;

import org.springframework.http.HttpStatus;

@Service
public class FinanziamentiApiServiceImpl implements FinanziamentiApi{

	
	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	public static int MAX_LENGTH_ISTAT_COMUNE = 6;
	public static int MAX_LENGTH_NUM_PRATICA_URB = 10;

	@Override
	public Response readFinanziamentoByPk(Long idFinanziamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Finanziamento finanziamento = (Finanziamento) businessLogic.readFinanziamentoByPk(idFinanziamento);
			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : readFinanziamentoByPk ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readFinanziamentoByPk", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readFinanziamentoByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}


	@Override
	public Response readAllErogazioniToFinanziamento(Long idFinanziamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			List<Erogazione> listErogazione = (List<Erogazione>) businessLogic.readAllErogazioniToFinanziamento(idFinanziamento);
			ResponseBuilder rb = Response.ok(listErogazione);
			LOG.debug("[FinanziamentiApiServiceImpl : readAllErogazioniToFinanziamento ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllErogazioniToFinanziamento", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readAllErogazioniToFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response createErogazione(Long idFinanziamento, Erogazione erogazione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl : createErogazione ] START "); 
		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			erogazione.validate();

			Finanziamento finanziamento = businessLogic.createErogazione(idFinanziamento, erogazione);
			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : createErogazione ] END ");		      		  
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
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
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
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createErogazione", erogazione);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::createErogazione] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}
	@Override
	public Response updateAllErogazioniToDetermina(DeterminaToErogazioni determinaToErogazioni, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl: updateAllErogazioniToDetermina] START"); 

		try {
			determinaToErogazioni.validate();

			businessLogic.updateAllErogazioniToDetermina(determinaToErogazioni);

			ResponseBuilder rb = Response.ok();
			LOG.debug("[FinanziamentiApiServiceImpl : updateAllErogazioniToDetermina ] END");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateAllErogazioniToDetermina", determinaToErogazioni);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::updateAllErogazioniToDetermina] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response readStatoFinanziamento(Long idFinanziamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			StatoFinanziamento statoFinanziamento = (StatoFinanziamento) businessLogic.readStatoFinanziamento(idFinanziamento);
			ResponseBuilder rb = Response.ok(statoFinanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : readStatoFinanziamento ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readStatoFinanziamento", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readStatoFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}


	@Override
	public Response updateStatoFinToFinanziamento(Long idFinanziamento, Long idStatoFinanziamento,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl: updateStatoFinToFinanziamento] START"); 

		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idStatoFinanziamento, Boolean.TRUE, null, null);

			Finanziamento finanziamento = businessLogic.updateStatoFinToFinanziamento(idFinanziamento, idStatoFinanziamento);

			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : updateStatoFinToFinanziamento ] END");		      		  
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
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
			sb.append(" idStatoFinanziamento: ").append(idStatoFinanziamento==null?"":idStatoFinanziamento).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateStatoFinToFinanziamento", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::updateStatoFinToFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}


	@Override
	public Response updateErogazione(Long idFinanziamento, Long idErogazione, Erogazione erogazione,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl: updateErogazione] START"); 

		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			ValidatorDto.validateNumber(idErogazione, Boolean.TRUE, null, null);
			erogazione.validateUpdate();

			Finanziamento finanziamento = businessLogic.updateErogazione(idFinanziamento, idErogazione, erogazione);

			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : updateErogazione ] END");		      		  
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
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
			sb.append(" idErogazione: ").append(idErogazione==null?"":idErogazione).append("\n");
			sb.append(" Erogazione: ").append(erogazione).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateErogazione", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::updateErogazione] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}


	@Override
	public Response readPraticaUrbGfuToFinanziamento(Long idFinanziamento, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			Finanziamento finanziamento = (Finanziamento) businessLogic.readPraticaUrbGfuToFinanziamento(idFinanziamento);
			ResponseBuilder rb = Response.ok(finanziamento);
			LOG.debug("[FinanziamentiApiServiceImpl : readPraticaUrbGfuToFinanziamento ]");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
	
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readPraticaUrbGfuToFinanziamento", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readPraticaUrbGfuToFinanziamento] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}


	@Override
	public Response createPraticaUrbGfu(Long idFinanziamento, PraticaUrbGfu praticaUrbGfu, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl : createPraticaUrbGfu ] START "); 
		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);
			praticaUrbGfu.validate();

			Finanziamento finanziamentoResult = businessLogic.createPraticaUrbGfu(idFinanziamento, praticaUrbGfu);
			ResponseBuilder rb = Response.ok(finanziamentoResult);
			LOG.debug("[FinanziamentiApiServiceImpl : createPraticaUrbGfu ] END ");		      		  
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
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), "nessun dato contenuto]")).build();
		} 
		catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
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
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
			sb.append(" praticaUrbGfu: ").append(praticaUrbGfu).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createPraticaUrbGfu", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::createPraticaUrbGfu] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}


	@Override
	public Response deletePraticaUrbGfu(Long idFinanziamento, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[FinanziamentiApiServiceImpl: deletePraticaUrbGfu] START"); 

		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);

			Finanziamento finanziamentoResult = businessLogic.deletePraticaUrbGfu(idFinanziamento);

			ResponseBuilder rb = Response.ok(finanziamentoResult);
			LOG.debug("[FinanziamentiApiServiceImpl : deletePraticaUrbGfu ] END");		      		  
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
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
	
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("deletePraticaUrbGfu", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::deletePraticaUrbGfu] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}


	@Override
	public Response readPraticaUrbDetail(Long idFinanziamento, String numPratica, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateNumber(idFinanziamento, Boolean.TRUE, null, null);	
			ValidatorDto.validateLength(numPratica, Boolean.TRUE, MAX_LENGTH_NUM_PRATICA_URB, null, null, null);
			RestResponseErrorHandler handler = new RestResponseErrorHandler(HttpStatus.OK);
			
			PraticaUrb ret = businessLogic.readPraticaUrbDetail(numPratica, handler);
			
			LOG.debug("[FinanziamentiApiServiceImpl : readPraticaUrbDetail ] END");		
			return Response.ok(ret).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).build();		
		}  		
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}catch (RESTException e) {
			e.printStackTrace();
			return Response.status(e.getStatus()).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(), e.getError().getMessage())).build();
		} catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" idFinanziamento: ").append(idFinanziamento==null?"":idFinanziamento).append("\n");
			sb.append(" numPratica: ").append(numPratica==null?"":numPratica).append("\n");
	
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readPraticaUrbDetail", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readPraticaUrbDetail] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}


	@Override
	public Response readAllPraticheUrbByFilter(String istatComune, String dataProvvedimentoDa,
			String dataProvvedimentoA, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			ValidatorDto.validateLength(istatComune, Boolean.TRUE, MAX_LENGTH_ISTAT_COMUNE, null, null, null);
			ValidatorDto.validateDate(dataProvvedimentoDa, Boolean.TRUE, null, null);
			ValidatorDto.validateDate(dataProvvedimentoA, Boolean.TRUE, null, null);
			ValidatorDto.validateDateDaA(dataProvvedimentoDa, dataProvvedimentoA);
            RestResponseErrorHandler handler = new RestResponseErrorHandler(HttpStatus.OK);
			
            List<PraticaUrbSintetica> ret = businessLogic.readAllPraticheUrbByFilter(istatComune,dataProvvedimentoDa, dataProvvedimentoA, handler);
			
			LOG.debug("[FinanziamentiApiServiceImpl : readAllPraticheUrbByFilter ] END");		
			return Response.ok(ret).type(javax.ws.rs.core.MediaType.APPLICATION_JSON).build();					      		  		
		}  			
		catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato")).build();
		}
		catch (DatiInputErratiException e) {
			LOG.debug("BAD REQUEST: CODE:"+e.getError().getCode()+"MESSAGE:"+e.getError().getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(),e.getError().getMessage())).build();
		}
		catch (RESTException e) {
			e.printStackTrace();
			return Response.status(e.getStatus()).entity(ResponseUtils.createJSONResponseMessage(e.getError().getCode(), e.getError().getMessage())).build();
		} catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		}
		finally {
			StringBuilder sb = new StringBuilder();
			sb.append(" istatComune: ").append(istatComune==null?"":istatComune).append("\n");
			sb.append(" dataProvvedimentoDa: ").append(dataProvvedimentoDa==null?"":dataProvvedimentoDa).append("\n");
			sb.append(" dataProvvedimentoA: ").append(dataProvvedimentoA==null?"":dataProvvedimentoA).append("\n");
			
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllPraticheUrbByFilter", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[FinanziamentiApiServiceImpl::readAllPraticheUrbByFilter] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
}