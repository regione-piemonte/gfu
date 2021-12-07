/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.dr;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.gfu.gfuweb.business.service.*;
import it.csi.gfu.gfuweb.business.service.dr.DrApi;
import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;

@Service
public class DrApiServiceImpl implements DrApi{

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	@Override
	public Response readAllDr(@QueryParam("isValid") Boolean isValid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[DrApiServiceImpl : readAllDr ] START");
		try {

			List<Dr>drList = businessLogic.readAllDr(isValid);
			ResponseBuilder rb = Response.ok(drList);
			LOG.debug("[DrApiServiceImpl : readAllDr ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllDr", "");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DrApiServiceImpl::readAllDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 

	}
	@Override
	public Response readDrByPk(Long id, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			Dr dr = (Dr) businessLogic.readDrByPk(id);
			ResponseBuilder rb = Response.ok(dr);
			LOG.debug("[DrApiServiceImpl : readDrByPk ]");
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
			sb.append(" idDr: ").append(id==null?"":id).append("\n");
	
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readDrByPk", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DrApiServiceImpl::readDrByPk] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		} 
	}

	@Override
	public Response createDr(Dr dr, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[DrApiServiceImpl : createDr ] START "); 
		try {
			dr.validate();

			Dr drResult = businessLogic.createDr(dr);
			ResponseBuilder rb = Response.ok(drResult);
			LOG.debug("[DrApiServiceImpl : createDr ] END ");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("createDr", dr);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DrApiServiceImpl::createDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}	
		}

	}
	@Override
	public Response updateDr(Dr dr, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[DrApiServiceImpl: updateDr] START"); 

		try {
			dr.validateUpdate();		
			Dr drResult = businessLogic.updateDr(dr);

			ResponseBuilder rb = Response.ok(drResult);
			LOG.debug("[DrApiServiceImpl : updateDr ] END");		      		  
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("updateDr", dr);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DrApiServiceImpl::updateDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		}
	}
	@Override
	public Response readAllTipiDr(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOG.debug("[DrApiServiceImpl : readAllTipiDr ] START");
		try {

			List<TipoDr>parereList = businessLogic.readAllTipiDr();
			ResponseBuilder rb = Response.ok(parereList);
			LOG.debug("[DrApiServiceImpl : readAllTipiDr ] END");
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
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readAllTipiDr", "");

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DrApiServiceImpl::readAllTipiDr] - Errore occorso durante il monitoraggio "+ e,e);									   
			}				
		} 
	}
}
