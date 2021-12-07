/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.exception;

/**
 * @generated
 */
public class ServizioException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	/**
	 */
	protected Throwable throwable;

	/**
	 * Method 'ServizioException'
	 * 
	 * @param message
	 */
	public ServizioException(String message) {
		super(message);
	}

	/**
	 * Method 'ServizioException'
	 * 
	 * @param message
	 * @param throwable
	 */
	public ServizioException(String message, Throwable throwable) {
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

}
