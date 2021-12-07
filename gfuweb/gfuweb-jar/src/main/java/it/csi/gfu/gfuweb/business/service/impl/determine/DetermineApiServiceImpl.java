/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl.determine;

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

import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.business.service.*;
import it.csi.gfu.gfuweb.business.service.determine.DetermineApi;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;
import it.csi.gfu.gfuweb.util.ValidatorDto;

@Service
public class DetermineApiServiceImpl implements DetermineApi{

	public static int MAX_LENGTH_NUM_DETERMINA = 50;

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	@Autowired
	public BusinessLogicExtService businessLogicExtService;

	@Override
	public Response readDeterminaByNumAndData(@QueryParam("numDetermina") String numDetermina, @QueryParam("dataDetermina") String dataDetermina,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOG.debug("[DetermineApiServiceImpl : readDeterminaByNumAndData ] START");
		try {

			ValidatorDto.validateLength(numDetermina, Boolean.TRUE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
			ValidatorDto.validateNumber(numDetermina, Boolean.TRUE, null, null);
			ValidatorDto.validateDate(dataDetermina, Boolean.TRUE, null, null);

			Determina determina = businessLogicExtService.readDeterminaByNumAndData(numDetermina,dataDetermina);
			ResponseBuilder rb = Response.ok(determina);
			LOG.debug("[DetermineApiServiceImpl : readDeterminaByNumAndData ] END");
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
			sb.append(" numDetermina: ").append(numDetermina==null?"":numDetermina).append("\n");
			sb.append(" dataDetermina: ").append(dataDetermina==null?"":dataDetermina).append("\n");
			CsiLogAuditDto csiLogAuditDto =  businessLogic.setParameterCsiLogAudit("readDeterminaByNumAndData", sb);

			try {
				businessLogic.createCsiLogAudit(csiLogAuditDto , (String) httpRequest.getSession().getAttribute("CODICE_FISCALE")  );
			} catch (DaoException | SystemException e) {
				LOG.error("[DetermineApiServiceImpl::readDeterminaByNumAndData] - Errore occorso durante il monitoraggio "+ e,e);									   
			}		
		} 
	}
}