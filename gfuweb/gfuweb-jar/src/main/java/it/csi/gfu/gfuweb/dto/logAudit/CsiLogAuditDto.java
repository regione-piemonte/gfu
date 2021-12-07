/*******************************************************************************
 * © Copyright Regione Piemonte – 2021
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 ******************************************************************************/
package it.csi.gfu.gfuweb.dto.logAudit;

import java.sql.Timestamp;

public class CsiLogAuditDto   {

	private Timestamp dataOra;

	private String idApp = null;

	private String ipAddress = null;

	private String utente = null;

	private String operazione = null;

	private String oggOper = null;

	private String keyOper = null;



	public String getIdApp() {
		return idApp;
	}
	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getOperazione() {
		return operazione;
	}
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}
	public String getOggOper() {
		return oggOper;
	}
	public void setOggOper(String oggOper) {
		this.oggOper = oggOper;
	}
	public String getKeyOper() {
		return keyOper;
	}
	public void setKeyOper(String keyOper) {
		this.keyOper = keyOper;
	}

	public Timestamp getDataOra() {
		return dataOra;
	}
	public void setDataOra(Timestamp dataOra) {
		this.dataOra = dataOra;
	}

}