/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.exception;

import it.csi.gfu.gfuweb.dto.Error;

public class DatiInputErratiException extends Exception {
	
	private static final long serialVersionUID = -4406252823837767071L;
	
	private Error error;
	protected Throwable throwable;

	public DatiInputErratiException(Error error) {
		super();
		this.setError(error);		
	}

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 */
	public DatiInputErratiException(String message) {
		super(message);
	}

	/**
	 * Method 'SystemException'
	 * 
	 * @param message
	 * @param throwable
	 */
	public DatiInputErratiException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

	/**
	 * Method 'getCause'
	 * 
	 * @return Throwable
	 */
	public Throwable getCause() {
		return throwable;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
