/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.dr;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class TipoDr   {

	private Long idTipoDr = null;
	private String codTipoDr = null;
	private String descTipoDr = null;

	@ApiModelProperty(value = "")
	@JsonProperty("idTipoDr")
	public Long getIdTipoDr() {
		return idTipoDr;
	}
	public void setIdTipoDr(Long idTipoDr) {
		this.idTipoDr = idTipoDr;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("codTipoDr")
	public String getCodTipoDr() {
		return codTipoDr;
	}
	public void setCodTipoDr(String codTipoDr) {
		this.codTipoDr = codTipoDr;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoDr")
	public String getDescTipoDr() {
		return descTipoDr;
	}
	public void setDescTipoDr(String descTipoDr) {
		this.descTipoDr = descTipoDr;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TipoDr tipoDr = (TipoDr) o;
		return Objects.equals(idTipoDr, tipoDr.idTipoDr) &&
				Objects.equals(codTipoDr, tipoDr.codTipoDr) &&
				Objects.equals(descTipoDr, tipoDr.descTipoDr) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTipoDr, codTipoDr, descTipoDr);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TipoDr {\n");
		sb.append("    idTipoDr: ").append(toIndentedString(idTipoDr)).append("\n");
		sb.append("    codTipoDr: ").append(toIndentedString(codTipoDr)).append("\n");
		sb.append("    descTipoDr: ").append(toIndentedString(descTipoDr)).append("\n");
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
