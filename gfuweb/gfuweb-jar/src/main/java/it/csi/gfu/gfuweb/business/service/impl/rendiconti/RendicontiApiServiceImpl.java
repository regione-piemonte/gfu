/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.rendiconti;

import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.business.service.rendiconti.RendicontiApi;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
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
public class RendicontiApiServiceImpl implements RendicontiApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response readAllRendiconti(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RendicontiApiServiceImpl : readAllRendiconti ] START");
		try {

			List<Rendiconto>rendicontoList = businessLogic.readAllRendiconti(isValid);
			ResponseBuilder rb = Response.ok(rendicontoList);
			LOG.debug("[RendicontiApiServiceImpl : readAllRendiconti ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllRendiconti","");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RendicontiApiServiceImpl::readAllRendiconti] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}
	@Override
	public Response readRendicontoByPk(Long idRendiconto, SecurityContext securityContext, HttpHeaders httpHeaders,

			HttpServletRequest httpRequest) {
		try {
			Rendiconto rendiconto = (Rendiconto) businessLogic.readRendicontoByPk(idRendiconto);
			ResponseBuilder rb = Response.ok(rendiconto);
			LOG.debug("[RendicontiApiServiceImpl : readRendicontoByPk ]");
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
			sb.append(" idRendiconto: ").append(idRendiconto==null?"":idRendiconto).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readRendicontoByPk",sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RendicontiApiServiceImpl::readRendicontoByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}

	@Override
	public Response createRendiconto(Rendiconto rendiconto, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RendicontiApiServiceImpl : createRendiconto ] START "); 
		try
		{
			rendiconto.validate();

			Rendiconto rendicontoResult = businessLogic.createRendiconto(rendiconto);
			ResponseBuilder rb = Response.ok(rendicontoResult);
			LOG.debug("[RendicontiApiServiceImpl : createRendiconto ] END ");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createRendiconto",rendiconto);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RendicontiApiServiceImpl::createRendiconto] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response updateRendiconto(Rendiconto rendiconto, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[RendicontiApiServiceImpl: updateRendiconto] START"); 

		try
		{
			rendiconto.validateUpdate();		

			Rendiconto rendicontoResult = businessLogic.updateRendiconto(rendiconto);

			ResponseBuilder rb = Response.ok(rendicontoResult);
			LOG.debug("[RendicontiApiServiceImpl : updateRendiconto ] END");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateRendiconto",rendiconto);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[RendicontiApiServiceImpl::updateRendiconto] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
}
