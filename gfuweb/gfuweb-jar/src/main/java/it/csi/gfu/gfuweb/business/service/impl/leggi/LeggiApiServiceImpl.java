/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.leggi;

import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.business.service.leggi.LeggiApi;
import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeggiApiServiceImpl implements LeggiApi {

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response readAllLeggi(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl : readAllLeggi ] START");
		try {

			List<Legge>leggeList = businessLogic.readAllLeggi(isValid);
			ResponseBuilder rb = Response.ok(leggeList);
			LOG.debug("[LeggiApiServiceImpl : readAllLeggi ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllLeggi", "");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::readAllLeggi] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response readLeggeByPk(Long idLegge, SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws NotFoundException {
		try {
			Legge legge = (Legge) businessLogic.readLeggeByPk(idLegge);
			ResponseBuilder rb = Response.ok(legge);
			LOG.debug("[LeggiApiServiceImpl : readLeggeByPk ]");
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
			sb.append(" idLegge: ").append(idLegge==null?"":idLegge).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readLeggeByPk", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::readLeggeByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}

	@Override
	public Response createLegge(Legge legge, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl : createLegge ] START "); 
		try
		{
			legge.validate();

			Legge leggeResult = businessLogic.createLegge(legge);
			ResponseBuilder rb = Response.ok(leggeResult);
			LOG.debug("[LeggiApiServiceImpl : createLegge ] END ");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createLegge", legge);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::createLegge] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}

	@Override
	public Response updateLegge(Legge legge, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl: updateLegge] START"); 

		try
		{
			legge.validateUpdate();		

			Legge leggeResult = businessLogic.updateLegge(legge);

			ResponseBuilder rb = Response.ok(leggeResult);
			LOG.debug("[LeggiApiServiceImpl : updateLegge ] END");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateLegge", legge);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::updateLegge] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}

	@Override
	public Response readAllLeggeProvvDr(@QueryParam("descLeggiProvvDr") String descLeggiProvvDr, @QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,			
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl : readAllLeggeProvvDr ] START");
		try {

			List<LeggeProvvDr>leggeProvvDrList = businessLogic.readAllLeggeProvvDr(descLeggiProvvDr, isValid);
			ResponseBuilder rb = Response.ok(leggeProvvDrList);
			LOG.debug("[LeggiApiServiceImpl : readAllLeggeProvvDr ] END");
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
			sb.append(" descLeggiProvvDr: ").append(descLeggiProvvDr==null?"":descLeggiProvvDr).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllLeggeProvvDr", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::readAllLeggeProvvDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}
	@Override
	public Response readLeggeProvvDrByPk(Long idLeggeProvvDr, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LeggeProvvDr leggeProvvDr = (LeggeProvvDr) businessLogic.readLeggeProvvDrByPk(idLeggeProvvDr);
			ResponseBuilder rb = Response.ok(leggeProvvDr);
			LOG.debug("[LeggiApiServiceImpl : readLeggeProvvDrByPk ]");
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
			sb.append(" idLeggeProvvDr: ").append(idLeggeProvvDr==null?"":idLeggeProvvDr).append("\n");

			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readLeggeProvvDrByPk", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::readLeggeProvvDrByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
	@Override
	public Response createLeggeProvvDr(LeggeProvvDr leggeProvvDr, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl : createLeggeProvvDr ] START "); 
		try
		{
			leggeProvvDr.validate();

			LeggeProvvDr leggeProvvDrResult = businessLogic.createLeggeProvvDr(leggeProvvDr);
			ResponseBuilder rb = Response.ok(leggeProvvDrResult);
			LOG.debug("[LeggiApiServiceImpl : createLeggeProvvDr ] END ");		      		  
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
		}
		finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createLeggeProvvDr", leggeProvvDr);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::createLeggeProvvDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}
	@Override
	public Response updateLeggeProvvDr(LeggeProvvDr leggeProvvDr, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[LeggiApiServiceImpl: updateLeggeProvvDr] START"); 

		try
		{
			leggeProvvDr.validateUpdate();		

			LeggeProvvDr leggeProvvDrResult = businessLogic.updateLeggeProvvDr(leggeProvvDr);

			ResponseBuilder rb = Response.ok(leggeProvvDrResult);
			LOG.debug("[LeggiApiServiceImpl : updateLeggeProvvDr ] END");		      		  
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
		}
		finally {
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateLeggeProvvDr", leggeProvvDr);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[LeggiApiServiceImpl::updateLeggeProvvDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}
	}
}
