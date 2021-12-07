/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.percentuali;

import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.business.service.percentuali.PercentualiApi;
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
public class PercentualiApiServiceImpl implements PercentualiApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response readAllPercentuali(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[PercentualiApiServiceImpl : readAllPercentuali ] START");
		try {
			List<Percentuale>percentualeList = businessLogic.readAllPercentuali(isValid);
			ResponseBuilder rb = Response.ok(percentualeList);
			LOG.debug("[PercentualiApiServiceImpl : readAllPercentuali ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllPercentuali", "");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[PercentualiApiServiceImpl::readAllPercentuali] - Errore occorso durante il monitoraggio "+ e,e);									   
			}			
		} 
	}
	@Override
	public Response readPercentualeByPk(Long id, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			Percentuale percentuale = (Percentuale) businessLogic.readPercentualeByPk(id);
			ResponseBuilder rb = Response.ok(percentuale);
			LOG.debug("[PercentualiApiServiceImpl : readPercentualeByPk ]");
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
			sb.append(" idPercentuale: ").append(id==null?"":id).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readPercentualeByPk", id);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[PercentualiApiServiceImpl::readPercentualeByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}

	@Override
	public Response createPercentuale(Percentuale percentuale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[PercentualiApiServiceImpl : createPercentuale ] START "); 
		try
		{
			percentuale.validate();

			Percentuale percentualeResult = businessLogic.createPercentuale(percentuale);
			ResponseBuilder rb = Response.ok(percentualeResult);
			LOG.debug("[PercentualiApiServiceImpl : createPercentuale ] END ");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createPercentuale", percentuale);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[PercentualiApiServiceImpl::createPercentuale] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}

	@Override
	public Response updatePercentuale(Percentuale percentuale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[PercentualiApiServiceImpl: updatePercentuale] START"); 

		try
		{
			percentuale.validateUpdate();		

			Percentuale percentualeResult = businessLogic.updatePercentuale(percentuale);

			ResponseBuilder rb = Response.ok(percentualeResult);
			LOG.debug("[PercentualiApiServiceImpl : updatePercentuale ] END");		      		  
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);
			return rb.build();
		}  
		catch (DaoException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(ResponseUtils.createJSONResponseMessage(Response.Status.NOT_FOUND.name(), "Dato non trovato]")).build();
		} 
		catch (SystemException e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updatePercentuale", percentuale);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[PercentualiApiServiceImpl::updatePercentuale] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
}
