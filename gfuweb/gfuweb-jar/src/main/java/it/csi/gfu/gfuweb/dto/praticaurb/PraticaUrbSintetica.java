/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;

public class PraticaUrbSintetica   {

	private String numPratica = null;
	private String numProvvedimento = null;
	private String dataProvvedimento = null;
	private String descTipoProvvedimento = null;
	private String esitoProvvedimento = null;
	private String numBUR = null;
	private String dataBUR = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numPratica")
	public String getNumPratica() {
		return numPratica;
	}
	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numProvvedimento")
	public String getNumProvvedimento() {
		return numProvvedimento;
	}
	public void setNumProvvedimento(String numProvvedimento) {
		this.numProvvedimento = numProvvedimento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataProvvedimento")
	public String getDataProvvedimento() {
		return dataProvvedimento;
	}
	public void setDataProvvedimento(String dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoProvvedimento")
	public String getDescTipoProvvedimento() {
		return descTipoProvvedimento;
	}
	public void setDescTipoProvvedimento(String descTipoProvvedimento) {
		this.descTipoProvvedimento = descTipoProvvedimento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("esitoProvvedimento")
	public String getEsitoProvvedimento() {
		return esitoProvvedimento;
	}
	public void setEsitoProvvedimento(String esitoProvvedimento) {
		this.esitoProvvedimento = esitoProvvedimento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numBUR")
	public String getNumBUR() {
		return numBUR;
	}
	public void setNumBUR(String numBUR) {
		this.numBUR = numBUR;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataBUR")
	public String getDataBUR() {
		return dataBUR;
	}
	public void setDataBUR(String dataBUR) {
		this.dataBUR = dataBUR;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PraticaUrbSintetica praticaUrbSintetica = (PraticaUrbSintetica) o;
		return Objects.equals(numPratica, praticaUrbSintetica.numPratica) &&
				Objects.equals(numProvvedimento, praticaUrbSintetica.numProvvedimento) &&
				Objects.equals(dataProvvedimento, praticaUrbSintetica.dataProvvedimento) &&
				Objects.equals(descTipoProvvedimento, praticaUrbSintetica.descTipoProvvedimento) &&
				Objects.equals(esitoProvvedimento, praticaUrbSintetica.esitoProvvedimento) &&
				Objects.equals(numBUR, praticaUrbSintetica.numBUR) &&
				Objects.equals(dataBUR, praticaUrbSintetica.dataBUR);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numPratica, numProvvedimento, dataProvvedimento, descTipoProvvedimento, esitoProvvedimento, numBUR, dataBUR);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PraticaUrbSintetica {\n");

		sb.append("    numPratica: ").append(toIndentedString(numPratica)).append("\n");
		sb.append("    numProvvedimento: ").append(toIndentedString(numProvvedimento)).append("\n");
		sb.append("    dataProvvedimento: ").append(toIndentedString(dataProvvedimento)).append("\n");
		sb.append("    descTipoProvvedimento: ").append(toIndentedString(descTipoProvvedimento)).append("\n");
		sb.append("    esitoProvvedimento: ").append(toIndentedString(esitoProvvedimento)).append("\n");
		sb.append("    numBUR: ").append(toIndentedString(numBUR)).append("\n");
		sb.append("    dataBUR: ").append(toIndentedString(dataBUR)).append("\n");
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

