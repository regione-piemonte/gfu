/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.erogazione;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Erogazione   {

	public static int MAX_LENGTH_VALUTA = 10;
	public static int MAX_LENGTH_NUM_DETERMINA = 50;

	private Long idErogazione = null;
	private BigDecimal idTipoErogazione = null;
	private String descTipoErogazione = null;
	private BigDecimal idFinanziamento = null;
	private BigDecimal importoErogazione = null;
	private String valuta = null;
	private String numDetermina = null;
	private String dataDetermina = null;

	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateNumber(getIdTipoErogazione(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getIdFinanziamento(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getValuta(), Boolean.FALSE, MAX_LENGTH_VALUTA, null, null, null);
		if(getValuta() != null && getValuta().equalsIgnoreCase(Constants.DB.VALUTA_EURO)) {
			ValidatorDto.validateDecimalNumber(getImportoErogazione(), Boolean.FALSE, null, null);
		}else {
			ValidatorDto.validateNumber(getImportoErogazione(), Boolean.FALSE, null, null);
		}

		ValidatorDto.validateLength(getNumDetermina(), Boolean.FALSE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
		ValidatorDto.validateNumber(getNumDetermina(), Boolean.FALSE, null, null);
		ValidatorDto.validateDate(getDataDetermina(), Boolean.FALSE, null, null);
	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateLength(getNumDetermina(), Boolean.TRUE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
		ValidatorDto.validateNumber(getNumDetermina(), Boolean.TRUE, null, null);
		ValidatorDto.validateDate(getDataDetermina(), Boolean.TRUE, null, null);	
	}

	public Erogazione(Long idErogazione, BigDecimal idTipoErogazione, String descTipoErogazione, BigDecimal idFinanziamento,
			BigDecimal importoErogazione, String valuta, String numDetermina, String dataDetermina) {
		super();
		this.idErogazione = idErogazione;
		this.idTipoErogazione = idTipoErogazione;
		this.descTipoErogazione = descTipoErogazione;
		this.idFinanziamento = idFinanziamento;
		this.importoErogazione = importoErogazione;
		this.valuta = valuta;
		this.numDetermina = numDetermina;
		this.dataDetermina = dataDetermina;
	}


	public Erogazione() {
		super();
	}


	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idErogazione")
	public Long getIdErogazione() {
		return idErogazione;
	}
	public void setIdErogazione(Long idErogazione) {
		this.idErogazione = idErogazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idTipoErogazione")
	public BigDecimal getIdTipoErogazione() {
		return idTipoErogazione;
	}
	public void setIdTipoErogazione(BigDecimal idTipoErogazione) {
		this.idTipoErogazione = idTipoErogazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoErogazione")
	public String getDescTipoErogazione() {
		return descTipoErogazione;
	}
	public void setDescTipoErogazione(String descTipoErogazione) {
		this.descTipoErogazione = descTipoErogazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idFinanziamento")
	public BigDecimal getIdFinanziamento() {
		return idFinanziamento;
	}
	public void setIdFinanziamento(BigDecimal idFinanziamento) {
		this.idFinanziamento = idFinanziamento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("importoErogazione")
	public BigDecimal getImportoErogazione() {
		return importoErogazione;
	}
	public void setImportoErogazione(BigDecimal importoErogazione) {
		this.importoErogazione = importoErogazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("valuta")
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
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


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Erogazione erogazione = (Erogazione) o;
		return Objects.equals(idErogazione, erogazione.idErogazione) &&
				Objects.equals(idTipoErogazione, erogazione.idTipoErogazione) &&
				Objects.equals(descTipoErogazione, erogazione.descTipoErogazione) &&
				Objects.equals(idFinanziamento, erogazione.idFinanziamento) &&
				Objects.equals(importoErogazione, erogazione.importoErogazione) &&
				Objects.equals(valuta, erogazione.valuta) &&
				Objects.equals(numDetermina, erogazione.numDetermina) &&
				Objects.equals(dataDetermina, erogazione.dataDetermina);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idErogazione, idTipoErogazione, descTipoErogazione, idFinanziamento, importoErogazione, valuta, numDetermina, dataDetermina);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Erogazione {\n");

		sb.append("    idErogazione: ").append(toIndentedString(idErogazione)).append("\n");
		sb.append("    idTipoErogazione: ").append(toIndentedString(idTipoErogazione)).append("\n");
		sb.append("    descTipoErogazione: ").append(toIndentedString(descTipoErogazione)).append("\n");
		sb.append("    idFinanziamento: ").append(toIndentedString(idFinanziamento)).append("\n");
		sb.append("    importoErogazione: ").append(toIndentedString(importoErogazione)).append("\n");
		sb.append("    valuta: ").append(toIndentedString(valuta)).append("\n");
		sb.append("    numDetermina: ").append(toIndentedString(numDetermina)).append("\n");
		sb.append("    dataDetermina: ").append(toIndentedString(dataDetermina)).append("\n");
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

