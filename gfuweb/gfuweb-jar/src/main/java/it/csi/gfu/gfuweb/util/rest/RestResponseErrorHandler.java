/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.exception.RESTException;


public class RestResponseErrorHandler implements ResponseErrorHandler {
	
	private List<HttpStatus> httpStatus;
	
	public RestResponseErrorHandler(HttpStatus status){
		if(this.httpStatus == null) {
			this.httpStatus = new ArrayList<HttpStatus>();
		}
		this.httpStatus.add(status);
	}
	
	public RestResponseErrorHandler(List<HttpStatus> status){
		this.httpStatus= status;
	}
	
	@Override
	public void handleError(ClientHttpResponse resp) throws IOException {
		Error error = new Error();
		error.setCode(resp.getStatusCode().toString());
		error.setMessage("Exception praurbapi service");
		throw new RESTException(resp.getRawStatusCode(), MediaType.APPLICATION_JSON_VALUE, error);
	}

	@Override
	public boolean hasError(ClientHttpResponse resp) throws IOException {
		if(!httpStatus.contains(resp.getStatusCode())  ) {
			return true;
		}
		return false;
	}

}
