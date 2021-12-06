/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;

public class ProvvedimentoUrb   {

	private String numProvvedimento = null;
	private String dataProvvedimento = null;
	private String descTipoProvvedimento = null;
	private String esitoProvvedimento = null;
	private String numBUR = null;
	private String dataBUR = null;

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
		ProvvedimentoUrb provvedimentoUrb = (ProvvedimentoUrb) o;
		return Objects.equals(numProvvedimento, provvedimentoUrb.numProvvedimento) &&
				Objects.equals(dataProvvedimento, provvedimentoUrb.dataProvvedimento) &&
				Objects.equals(descTipoProvvedimento, provvedimentoUrb.descTipoProvvedimento) &&
				Objects.equals(esitoProvvedimento, provvedimentoUrb.esitoProvvedimento) &&
				Objects.equals(numBUR, provvedimentoUrb.numBUR) &&
				Objects.equals(dataBUR, provvedimentoUrb.dataBUR);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numProvvedimento, dataProvvedimento, descTipoProvvedimento, esitoProvvedimento, numBUR, dataBUR);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ProvvedimentoUrb {\n");

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

