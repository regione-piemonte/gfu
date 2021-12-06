/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import it.csi.gfu.gfuweb.dto.Error;

public class RESTException extends RuntimeException {

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;
	private MediaType mediaType;
	private Object entity;
	private Error error;
	
	public RESTException(int status, MediaType mediaType, Object entity, String message) {
		super(message);
		this.status = status;
		this.mediaType = mediaType;
		this.entity = entity;
	}
	
	
	public RESTException(int status, String mediaType, String entity, String message) {
		super(message);
		this.status = status;
		this.mediaType = MediaType.valueOf(mediaType);
		this.entity = entity;
	}
	
	public RESTException(int status, String mediaType, Error error) {
		this.status = status;
		this.mediaType = MediaType.valueOf(mediaType);
		this.error = error;
	}


	public int getStatus() {
		return this.status;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Response getResponse() {
		return getResponseBuilder().build();
	}
	
	public ResponseBuilder getResponseBuilder() {
		return Response.status(this.getStatus()).type(this.getMediaType()).entity(this.getEntity());
	}


	public Error getError() {
		return error;
	}


	public void setError(Error error) {
		this.error = error;
	}
}
