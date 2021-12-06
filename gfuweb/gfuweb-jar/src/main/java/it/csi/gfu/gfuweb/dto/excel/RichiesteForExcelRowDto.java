/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.excel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;


public class RichiesteForExcelRowDto   {

	private String numProtocollo = null;
	private String dataProtocollo = null;
	private String descRichiedente = null;
	private String descTipoRichiedente = null;
	private String descComune = null;
	private String siglaProv = null;
	private BigDecimal popolazione = null;
	private String rinuncia = null;
	private String descLeggeProvvDr = null;
	private BigDecimal importoFinanziabile = null;
	private BigDecimal importoAmmesso = null;
	private BigDecimal importoMaxPerComune = null;
	private String descParere = null;
	private String descRendiconto = null;
	private String descStatoFin = null;
	private BigDecimal importoRinuncia = null;
	private String attoRinuncia = null;
	private String noteFin = null;
	private BigDecimal importoErogazioneAcc = null;
	private String numDeterminaAcc = null;
	private String dataDeterminaAcc = null;
	private BigDecimal importoErogazioneSaldo = null;
	private String numDeterminaSaldo = null;
	private String dataDeterminaSaldo = null;
	private String numPraticaPraurb = null;
	private String numAttoApprovazioneUrb = null;
	private String dataAttoApprovazioneUrb = null;


	@ApiModelProperty(value = "")
	@JsonProperty("numProtocollo")
	public String getNumProtocollo() {
		return numProtocollo;
	}
	public void setNumProtocollo(String numProtocollo) {
		this.numProtocollo = numProtocollo;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataProtocollo")
	public String getDataProtocollo() {
		return dataProtocollo;
	}
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descRichiedente")
	public String getDescRichiedente() {
		return descRichiedente;
	}
	public void setDescRichiedente(String descRichiedente) {
		this.descRichiedente = descRichiedente;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoRichiedente")
	public String getDescTipoRichiedente() {
		return descTipoRichiedente;
	}
	public void setDescTipoRichiedente(String descTipoRichiedente) {
		this.descTipoRichiedente = descTipoRichiedente;
	}
	public String getDescComune() {
		return descComune;
	}
	public void setDescComune(String descComune) {
		this.descComune = descComune;
	}
	public String getSiglaProv() {
		return siglaProv;
	}
	public void setSiglaProv(String siglaProv) {
		this.siglaProv = siglaProv;
	}
	public BigDecimal getPopolazione() {
		return popolazione;
	}
	public void setPopolazione(BigDecimal popolazione) {
		this.popolazione = popolazione;
	}
	public String getRinuncia() {
		return rinuncia;
	}
	public void setRinuncia(String rinuncia) {
		this.rinuncia = rinuncia;
	}
	public String getDescLeggeProvvDr() {
		return descLeggeProvvDr;
	}
	public void setDescLeggeProvvDr(String descLeggeProvvDr) {
		this.descLeggeProvvDr = descLeggeProvvDr;
	}
	public BigDecimal getImportoFinanziabile() {
		return importoFinanziabile;
	}
	public void setImportoFinanziabile(BigDecimal importoFinanziabile) {
		this.importoFinanziabile = importoFinanziabile;
	}
	public BigDecimal getImportoAmmesso() {
		return importoAmmesso;
	}
	public void setImportoAmmesso(BigDecimal importoAmmesso) {
		this.importoAmmesso = importoAmmesso;
	}
	public BigDecimal getImportoMaxPerComune() {
		return importoMaxPerComune;
	}
	public void setImportoMaxPerComune(BigDecimal importoMaxPerComune) {
		this.importoMaxPerComune = importoMaxPerComune;
	}
	public String getDescParere() {
		return descParere;
	}
	public void setDescParere(String descParere) {
		this.descParere = descParere;
	}
	public String getDescRendiconto() {
		return descRendiconto;
	}
	public void setDescRendiconto(String descRendiconto) {
		this.descRendiconto = descRendiconto;
	}
	public String getDescStatoFin() {
		return descStatoFin;
	}
	public void setDescStatoFin(String descStatoFin) {
		this.descStatoFin = descStatoFin;
	}
	public BigDecimal getImportoRinuncia() {
		return importoRinuncia;
	}
	public void setImportoRinuncia(BigDecimal importoRinuncia) {
		this.importoRinuncia = importoRinuncia;
	}
	public String getAttoRinuncia() {
		return attoRinuncia;
	}
	public void setAttoRinuncia(String attoRinuncia) {
		this.attoRinuncia = attoRinuncia;
	}
	public String getNoteFin() {
		return noteFin;
	}
	public void setNoteFin(String noteFin) {
		this.noteFin = noteFin;
	}
	public BigDecimal getImportoErogazioneAcc() {
		return importoErogazioneAcc;
	}
	public void setImportoErogazioneAcc(BigDecimal importoErogazioneAcc) {
		this.importoErogazioneAcc = importoErogazioneAcc;
	}
	public String getNumDeterminaAcc() {
		return numDeterminaAcc;
	}
	public void setNumDeterminaAcc(String numDeterminaAcc) {
		this.numDeterminaAcc = numDeterminaAcc;
	}
	public String getDataDeterminaAcc() {
		return dataDeterminaAcc;
	}
	public void setDataDeterminaAcc(String dataDeterminaAcc) {
		this.dataDeterminaAcc = dataDeterminaAcc;
	}
	public BigDecimal getImportoErogazioneSaldo() {
		return importoErogazioneSaldo;
	}
	public void setImportoErogazioneSaldo(BigDecimal importoErogazioneSaldo) {
		this.importoErogazioneSaldo = importoErogazioneSaldo;
	}
	public String getNumDeterminaSaldo() {
		return numDeterminaSaldo;
	}
	public void setNumDeterminaSaldo(String numDeterminaSaldo) {
		this.numDeterminaSaldo = numDeterminaSaldo;
	}
	public String getDataDeterminaSaldo() {
		return dataDeterminaSaldo;
	}
	public void setDataDeterminaSaldo(String dataDeterminaSaldo) {
		this.dataDeterminaSaldo = dataDeterminaSaldo;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("numPraticaPraurb")
	public String getNumPraticaPraurb() {
		return numPraticaPraurb;
	}
	public void setNumPraticaPraurb(String numPraticaPraurb) {
		this.numPraticaPraurb = numPraticaPraurb;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("numAttoApprovazioneUrb")
	public String getNumAttoApprovazioneUrb() {
		return numAttoApprovazioneUrb;
	}
	public void setNumAttoApprovazioneUrb(String numAttoApprovazioneUrb) {
		this.numAttoApprovazioneUrb = numAttoApprovazioneUrb;
	}
	@ApiModelProperty(value = "")
	@JsonProperty("dataAttoApprovazioneUrb")
	public String getDataAttoApprovazioneUrb() {
		return dataAttoApprovazioneUrb;
	}
	public void setDataAttoApprovazioneUrb(String dataAttoApprovazioneUrb) {
		this.dataAttoApprovazioneUrb = dataAttoApprovazioneUrb;
	}
}