/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.filter;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class RichiestaFilter   {

	public static int MAX_LENGTH_NUMERO_PROTOCOLLO = 15;
	public static int MAX_LENGTH_DENOMINAZIONE_ASSOCIAZIONE = 50;
	public static int MAX_LENGTH_NUM_DETERMINA = 50;
	public static int MAX_LENGTH_ATTO_RINUNCIA = 250;
	public static int MAX_LENGTH_ISTAT_COMUNE = 6;
	public static int MAX_LENGTH_NUM_PRATICA_URB = 20;
	public static int MAX_LENGTH_NUM_APPROVAZIONE_URB = 20;

	private String numProtocollo = null;
	private String dataProtocolloDa = null;
	private String dataProtocolloA = null;
	private String siglaProv = null;
	private String istatComune = null;
	private BigDecimal idTipoFormaAssociativa = null;
	private String denominazioneAssociazione = null;
	private BigDecimal idLeggeProvvDr = null;
	private BigDecimal idVincoloPopolazione = null;
	private BigDecimal idStatoFinanziamento = null;
	private String numDetermina = null;
	private String dataDetermina = null;
	private BigDecimal idParere = null;
	private BigDecimal idRendiconto = null;
	private String attoRinuncia = null;
	private String dataAttoApprovazioneUrb = null;
	private String numPraticaUrb = null;
	private String numAttoApprovazioneUrb = null;
	private Boolean flagPraticaUrbanisticaAssociata = null;


	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateDate(getDataProtocolloDa(), Boolean.FALSE, null, null);		
		ValidatorDto.validateDate(getDataProtocolloA(), Boolean.FALSE, null, null);
		ValidatorDto.validateDateDaA(getDataProtocolloDa(), getDataProtocolloA());
		ValidatorDto.validateLength(getNumProtocollo(), Boolean.FALSE, MAX_LENGTH_NUMERO_PROTOCOLLO, null, null, null);
		ValidatorDto.validateNumber(getIdTipoFormaAssociativa(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getDenominazioneAssociazione(), Boolean.FALSE, MAX_LENGTH_DENOMINAZIONE_ASSOCIAZIONE, null, null, null);
		ValidatorDto.validateNumber(getIdLeggeProvvDr(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getIdVincoloPopolazione(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getIdStatoFinanziamento(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getNumDetermina(), Boolean.FALSE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
		ValidatorDto.validateDate(getDataDetermina(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getIdParere(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getIdRendiconto(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getAttoRinuncia(), Boolean.FALSE, MAX_LENGTH_ATTO_RINUNCIA, null, null, null);
		ValidatorDto.validateLength(getIstatComune(), Boolean.FALSE, MAX_LENGTH_ISTAT_COMUNE, null, null, null);
		ValidatorDto.validateDate(getDataAttoApprovazioneUrb(), Boolean.FALSE, null, null);	
		ValidatorDto.validateLength(getNumPraticaUrb(), Boolean.FALSE, MAX_LENGTH_NUM_PRATICA_URB, null, null, null);
		ValidatorDto.validateLength(getNumAttoApprovazioneUrb(), Boolean.FALSE, MAX_LENGTH_NUM_APPROVAZIONE_URB, null, null, null);	
	}


	/**
	 **/

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
	@JsonProperty("dataProtocolloDa")
	public String getDataProtocolloDa() {
		return dataProtocolloDa;
	}
	public void setDataProtocolloDa(String dataProtocolloDa) {
		this.dataProtocolloDa = dataProtocolloDa;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataProtocolloA")
	public String getDataProtocolloA() {
		return dataProtocolloA;
	}
	public void setDataProtocolloA(String dataProtocolloA) {
		this.dataProtocolloA = dataProtocolloA;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("istatComune")
	public String getIstatComune() {
		return istatComune;
	}
	public void setIstatComune(String istatComune) {
		this.istatComune = istatComune;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idTipoFormaAssociativa")
	public BigDecimal getIdTipoFormaAssociativa() {
		return idTipoFormaAssociativa;
	}
	public void setIdTipoFormaAssociativa(BigDecimal idTipoFormaAssociativa) {
		this.idTipoFormaAssociativa = idTipoFormaAssociativa;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("denominazioneAssociazione")
	public String getDenominazioneAssociazione() {
		return denominazioneAssociazione;
	}
	public void setDenominazioneAssociazione(String denominazioneAssociazione) {
		this.denominazioneAssociazione = denominazioneAssociazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idLeggeProvvDr")
	public BigDecimal getIdLeggeProvvDr() {
		return idLeggeProvvDr;
	}
	public void setIdLeggeProvvDr(BigDecimal idLeggeProvvDr) {
		this.idLeggeProvvDr = idLeggeProvvDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idVincoloPopolazione")
	public BigDecimal getIdVincoloPopolazione() {
		return idVincoloPopolazione;
	}
	public void setIdVincoloPopolazione(BigDecimal idVincoloPopolazione) {
		this.idVincoloPopolazione = idVincoloPopolazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idStatoFinanziamento")
	public BigDecimal getIdStatoFinanziamento() {
		return idStatoFinanziamento;
	}
	public void setIdStatoFinanziamento(BigDecimal idStatoFinanziamento) {
		this.idStatoFinanziamento = idStatoFinanziamento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numDetermina")
	public String getNumDetermina() {
		return numDetermina;
	}
	public void setNumDetermina(String numDetermina) {
		this.numDetermina = numDetermina;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataDetermina")
	public String getDataDetermina() {
		return dataDetermina;
	}
	public void setDataDetermina(String dataDetermina) {
		this.dataDetermina = dataDetermina;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idParere")
	public BigDecimal getIdParere() {
		return idParere;
	}
	public void setIdParere(BigDecimal idParere) {
		this.idParere = idParere;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idRendiconto")
	public BigDecimal getIdRendiconto() {
		return idRendiconto;
	}
	public void setIdRendiconto(BigDecimal idRendiconto) {
		this.idRendiconto = idRendiconto;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("attoRinuncia")
	public String getAttoRinuncia() {
		return attoRinuncia;
	}
	public void setAttoRinuncia(String attoRinuncia) {
		this.attoRinuncia = attoRinuncia;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("siglaProv")
	public String getSiglaProv() {
		return siglaProv;
	}

	public void setSiglaProv(String siglaProv) {
		this.siglaProv = siglaProv;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("dataAttoApprovazioneUrb")
	public String getDataAttoApprovazioneUrb() {
		return dataAttoApprovazioneUrb;
	}

	public void setDataAttoApprovazioneUrb(String dataAttoApprovazioneUrb) {
		this.dataAttoApprovazioneUrb = dataAttoApprovazioneUrb;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("numPraticaUrb")
	public String getNumPraticaUrb() {
		return numPraticaUrb;
	}

	public void setNumPraticaUrb(String numPraticaUrb) {
		this.numPraticaUrb = numPraticaUrb;
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
	@JsonProperty("flagPraticaUrbanisticaAssociata")
	public Boolean getFlagPraticaUrbanisticaAssociata() {
		return flagPraticaUrbanisticaAssociata;
	}

	public void setFlagPraticaUrbanisticaAssociata(Boolean flagPraticaUrbanisticaAssociata) {
		this.flagPraticaUrbanisticaAssociata = flagPraticaUrbanisticaAssociata;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RichiestaFilter richiestaFilter = (RichiestaFilter) o;
		return Objects.equals(numProtocollo, richiestaFilter.numProtocollo) &&
				Objects.equals(dataProtocolloDa, richiestaFilter.dataProtocolloDa) &&
				Objects.equals(dataProtocolloA, richiestaFilter.dataProtocolloA) &&
				Objects.equals(istatComune, richiestaFilter.istatComune) &&
				Objects.equals(idTipoFormaAssociativa, richiestaFilter.idTipoFormaAssociativa) &&
				Objects.equals(denominazioneAssociazione, richiestaFilter.denominazioneAssociazione) &&
				Objects.equals(idLeggeProvvDr, richiestaFilter.idLeggeProvvDr) &&
				Objects.equals(idVincoloPopolazione, richiestaFilter.idVincoloPopolazione) &&
				Objects.equals(idStatoFinanziamento, richiestaFilter.idStatoFinanziamento) &&
				Objects.equals(numDetermina, richiestaFilter.numDetermina) &&
				Objects.equals(dataDetermina, richiestaFilter.dataDetermina) &&
				Objects.equals(idParere, richiestaFilter.idParere) &&
				Objects.equals(idRendiconto, richiestaFilter.idRendiconto) &&
				Objects.equals(attoRinuncia, richiestaFilter.attoRinuncia)&&
				Objects.equals(siglaProv, richiestaFilter.siglaProv)&&
				Objects.equals(dataAttoApprovazioneUrb, richiestaFilter.dataAttoApprovazioneUrb)&&
				Objects.equals(numAttoApprovazioneUrb, richiestaFilter.numAttoApprovazioneUrb)&&
				Objects.equals(numPraticaUrb, richiestaFilter.numPraticaUrb)&&
				Objects.equals(flagPraticaUrbanisticaAssociata, richiestaFilter.flagPraticaUrbanisticaAssociata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numProtocollo, dataProtocolloDa, dataProtocolloA, istatComune, idTipoFormaAssociativa, denominazioneAssociazione, idLeggeProvvDr, idVincoloPopolazione, idStatoFinanziamento, numDetermina, dataDetermina, idParere, idRendiconto, attoRinuncia, siglaProv, dataAttoApprovazioneUrb, numAttoApprovazioneUrb, numPraticaUrb, flagPraticaUrbanisticaAssociata);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RichiestaFilter {\n");

		sb.append("    numProtocollo: ").append(toIndentedString(numProtocollo)).append("\n");
		sb.append("    dataProtocolloDa: ").append(toIndentedString(dataProtocolloDa)).append("\n");
		sb.append("    dataProtocolloA: ").append(toIndentedString(dataProtocolloA)).append("\n");
		sb.append("    istatComune: ").append(toIndentedString(istatComune)).append("\n");
		sb.append("    idTipoFormaAssociativa: ").append(toIndentedString(idTipoFormaAssociativa)).append("\n");
		sb.append("    denominazioneAssociazione: ").append(toIndentedString(denominazioneAssociazione)).append("\n");
		sb.append("    idLeggeProvvDr: ").append(toIndentedString(idLeggeProvvDr)).append("\n");
		sb.append("    idVincoloPopolazione: ").append(toIndentedString(idVincoloPopolazione)).append("\n");
		sb.append("    idStatoFinanziamento: ").append(toIndentedString(idStatoFinanziamento)).append("\n");
		sb.append("    numDetermina: ").append(toIndentedString(numDetermina)).append("\n");
		sb.append("    dataDetermina: ").append(toIndentedString(dataDetermina)).append("\n");
		sb.append("    idParere: ").append(toIndentedString(idParere)).append("\n");
		sb.append("    idRendiconto: ").append(toIndentedString(idRendiconto)).append("\n");
		sb.append("    attoRinuncia: ").append(toIndentedString(attoRinuncia)).append("\n");
		sb.append("    siglaProv: ").append(toIndentedString(siglaProv)).append("\n");
		sb.append("    dataAttoApprovazioneUrb: ").append(toIndentedString(dataAttoApprovazioneUrb)).append("\n");
		sb.append("    numAttoApprovazioneUrb: ").append(toIndentedString(numAttoApprovazioneUrb)).append("\n");
		sb.append("    numPraticaUrb: ").append(toIndentedString(numPraticaUrb)).append("\n");
		sb.append("    flagPraticaUrbanisticaAssociata: ").append(toIndentedString(flagPraticaUrbanisticaAssociata)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}

