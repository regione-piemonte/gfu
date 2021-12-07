/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.tettomax;

import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.business.service.tettomax.TettoMaxApi;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.tetto.TettoMax;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
import it.csi.gfu.gfuweb.util.ValidatorDto;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TettoMaxApiServiceImpl implements TettoMaxApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;


	@Override
	public Response readAllTettoMax(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[TettoMaxApiServiceImpl : readAllTettoMax ] START");
		try {

			List<TettoMax>tettoMaxList = businessLogic.readAllTettoMax(isValid);
			ResponseBuilder rb = Response.ok(tettoMaxList);
			LOG.debug("[TettoMaxApiServiceImpl : readAllTettoMax ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllTettoMax","");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[TettoMaxApiServiceImpl::readAllTettoMax] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response readTettoMaxByPk(Long idTettoMax, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			TettoMax tettoMax = (TettoMax) businessLogic.readTettoMaxByPk(idTettoMax);
			ResponseBuilder rb = Response.ok(tettoMax);
			LOG.debug("[TettoMaxApiServiceImpl : readTettoMaxByPk ]");
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
			sb.append(" idTettoMax: ").append(idTettoMax==null?"":idTettoMax).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readTettoMaxByPk",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[TettoMaxApiServiceImpl::readTettoMaxByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response createTettoMax(TettoMax tettoMax, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[TettoMaxApiServiceImpl : createTettoMax ] START "); 
		try {
			tettoMax.validate();

			TettoMax tettoMaxResult = businessLogic.createTettoMax(tettoMax);
			ResponseBuilder rb = Response.ok(tettoMaxResult);
			LOG.debug("[TettoMaxApiServiceImpl : createTettoMax ] END ");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createTettoMax",tettoMax);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[TettoMaxApiServiceImpl::createTettoMax] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response updateTettoMax(TettoMax tettoMax, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[TettoMaxApiServiceImpl: updateTettoMax] START"); 

		try {
			tettoMax.validateUpdate();		
			TettoMax tettoMaxResult = businessLogic.updateTettoMax(tettoMax);

			ResponseBuilder rb = Response.ok(tettoMaxResult);
			LOG.debug("[TettoMaxApiServiceImpl : updateTettoMax ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateTettoMax",tettoMax);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[TettoMaxApiServiceImpl::updateTettoMax] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
	@Override
	public Response deleteTettoMaxByPk(Long idTettoMax, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[TettoMaxApiServiceImpl: deleteTettoMaxByPk] START"); 

		try {
			ValidatorDto.validateNumber(idTettoMax, Boolean.TRUE, null, null);	
			TettoMax tettoMax = businessLogic.deleteTettoMaxByPk(idTettoMax);

			ResponseBuilder rb = Response.ok(tettoMax);
			LOG.debug("[TettoMaxApiServiceImpl : deleteTettoMaxByPk ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) {
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
			sb.append(" idTettoMax: ").append(idTettoMax==null?"":idTettoMax).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("deleteTettoMaxByPk",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[TettoMaxApiServiceImpl::deleteTettoMaxByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}


}