/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;

import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

public class  HeaderUtil {

	public static Logger LOG = Logger.getLogger(Constants.LOGGER);
	
	
	public static void manageAllowedOrigins(HttpHeaders httpHeaders, ResponseBuilder rb) {
		String origin = "http://localhost:4200";
		  try {
			@SuppressWarnings("rawtypes")
			ArrayList originList = (ArrayList)httpHeaders.getRequestHeaders().get("origin");
			if(originList!=null && !originList.isEmpty())
			{
				origin = (String)originList.get(0);
				LOG.debug(" FOUND: "+origin);
			}
		} catch (Exception e) {
			LOG.error("impossibile identificare l'origine");
		}
		  
		  String allowOrigin  = origin;
		  rb.header("Access-Control-Allow-Credentials", "true").
		     header("Access-Control-Allow-Methods", "HEAD, GET, POST, PUT, DELETE").
			 header("Access-Control-Allow-Origin", allowOrigin);
		  
	}
}