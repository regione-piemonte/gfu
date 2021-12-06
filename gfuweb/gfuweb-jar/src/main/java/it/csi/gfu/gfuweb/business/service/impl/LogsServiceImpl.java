/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.gfu.gfuweb.business.service.Logs;
import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.dto.Log;
import it.csi.gfu.gfuweb.dto.LogResult;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.HeaderUtil;
import it.csi.gfu.gfuweb.util.ResponseUtils;

@Service
public class LogsServiceImpl implements Logs{

	public Logger LOG = Logger.getLogger(Constants.LOGGER);
	@Autowired
	public BusinessLogic businessLogic;

	

	public Response createLogs(Log log, SecurityContext securityContext, HttpHeaders httpHeaders)
	{
		LOG.debug("[logsImpl: createLog] START "); 

		try
		{
			LogResult logResult = null; 

			ResponseBuilder rb = Response.ok("success!");
			LOG.debug("[Presentation] " + log.getMessage());	
			HeaderUtil.manageAllowedOrigins(httpHeaders, rb);

			return rb.build();
		}   
		catch (Exception e) 
		{
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
					entity(ResponseUtils.createJSONResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.name(), "Server indisponibile")).build();
		} 
		
	}

	
}