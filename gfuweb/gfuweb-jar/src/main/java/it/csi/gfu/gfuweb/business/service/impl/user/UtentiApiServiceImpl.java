/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.user;

import javax.ws.rs.core.Response;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.gfu.gfuweb.business.service.*;
import it.csi.gfu.gfuweb.business.service.user.UtentiApi;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteResult;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.*;

@Service
public class UtentiApiServiceImpl implements UtentiApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
		
	@Autowired
	public BusinessLogic businessLogic;
	
	@Override  
	public Response getUtentiByFilter(UtenteFilter filtro,SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
		LOG.debug("[UtentiApiServiceImpl : getUtentiByFilter ] START "); 

		try {	        		        	 
			List<Utente> utenteResult =  (List<Utente>) businessLogic.getUtenteByFilter(filtro);

			ResponseBuilder rb = Response.ok(utenteResult);
			LOG.debug("[UtentiApiServiceImpl : getUtentiByFilter ] END ");
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NO_CONTENT).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NO_CONTENT.name(), ErrorMessages.NO_CONTENT)).build();
		} catch (SystemException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), ErrorMessages.INTERNAL_SERVER_ERROR)).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
	    } finally {		
	    	CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("getUtentiByFilter",filtro);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[UtentiApiServiceImpl::getUtentiByFilter] - Errore occorso durante il monitoraggio "+ e,e);									   
			}
		} 
	}

	@Override
	public Response getProfilo(SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		LOG.debug("UtentiApiService:getProfilo ] START "); 
		try {
			
            UtenteResult utenteResult =  new UtenteResult();
            Utente utente = new Utente();
            utente = (Utente) httpRequest.getSession().getAttribute("UTENTE");  
            utenteResult.setFullUtenteDto(utente);
			ResponseBuilder rb = Response.ok(utenteResult);		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			LOG.debug("[UtentiApiServiceImpl : getProfilo ] END ");	
			return rb.build();
		}  catch (NotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), ErrorMessages.NOT_FOUND)).build();
		} finally { 
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("getProfilo","");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[UtentiApiServiceImpl::getProfilo] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response logoutAppl(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return null; //TODO
	}	
}
