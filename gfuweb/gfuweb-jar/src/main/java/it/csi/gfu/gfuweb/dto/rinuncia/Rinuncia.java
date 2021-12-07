/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.rinuncia;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;


public class Rinuncia   {

	private Long idFinanziamentoRinuncia = null;
	private BigDecimal importo = null;
	private String valuta = null;
	private String attoRinuncia = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idFinanziamentoRinuncia")
	public Long getIdFinanziamentoRinuncia() {
		return idFinanziamentoRinuncia;
	}
	public void setIdFinanziamentoRinuncia(Long idFinanziamentoRinuncia) {
		this.idFinanziamentoRinuncia = idFinanziamentoRinuncia;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("importo")
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
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
	@JsonProperty("attoRinuncia")
	public String getAttoRinuncia() {
		return attoRinuncia;
	}
	public void setAttoRinuncia(String attoRinuncia) {
		this.attoRinuncia = attoRinuncia;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Rinuncia rinuncia = (Rinuncia) o;
		return Objects.equals(idFinanziamentoRinuncia, rinuncia.idFinanziamentoRinuncia) &&
				Objects.equals(importo, rinuncia.importo) &&
				Objects.equals(valuta, rinuncia.valuta) &&
				Objects.equals(attoRinuncia, rinuncia.attoRinuncia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idFinanziamentoRinuncia, importo, valuta, attoRinuncia);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Rinuncia {\n");

		sb.append("    idFinanziamentoRinuncia: ").append(toIndentedString(idFinanziamentoRinuncia)).append("\n");
		sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
		sb.append("    valuta: ").append(toIndentedString(valuta)).append("\n");
		sb.append("    attoRinuncia: ").append(toIndentedString(attoRinuncia)).append("\n");
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

