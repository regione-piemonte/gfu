/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.tetto;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;

public class TettoMaxTotRichiedenti   {

	private Integer idTettoMax = null;
	private Integer importoTettoMax = null;
	private Integer importoTettoMaxTotRichiedenti = null;
	private String valuta = null;


	/**
	 **/


	@JsonProperty("importoTettoMax") 

	public Integer getImportoTettoMax() {
		return importoTettoMax;
	}
	public void setImportoTettoMax(Integer importoTettoMax) {
		this.importoTettoMax = importoTettoMax;
	}

	/**
	 **/


	@JsonProperty("importoTettoMaxTotRichiedenti") 

	public Integer getImportoTettoMaxTotRichiedenti() {
		return importoTettoMaxTotRichiedenti;
	}
	public void setImportoTettoMaxTotRichiedenti(Integer importoTettoMaxTotRichiedenti) {
		this.importoTettoMaxTotRichiedenti = importoTettoMaxTotRichiedenti;
	}

	/**
	 **/


	@JsonProperty("idTettoMax")
	public Integer getIdTettoMax() {
		return idTettoMax;
	}
	public void setIdTettoMax(Integer idTettoMax) {
		this.idTettoMax = idTettoMax;
	}
	/**
	 **/


	@JsonProperty("valuta")
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TettoMaxTotRichiedenti tettoMaxTotRichiedenti = (TettoMaxTotRichiedenti) o;
		return Objects.equals(importoTettoMax, tettoMaxTotRichiedenti.importoTettoMax) &&
				Objects.equals(importoTettoMaxTotRichiedenti, tettoMaxTotRichiedenti.importoTettoMaxTotRichiedenti)&&
				Objects.equals(idTettoMax, tettoMaxTotRichiedenti.idTettoMax)&&
				Objects.equals(valuta, tettoMaxTotRichiedenti.valuta);
	}

	@Override
	public int hashCode() {
		return Objects.hash(importoTettoMax, importoTettoMaxTotRichiedenti, idTettoMax, valuta);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TettoMaxTotRichiedenti {\n");

		sb.append("    importoTettoMax: ").append(toIndentedString(importoTettoMax)).append("\n");
		sb.append("    importoTettoMaxTotRichiedenti: ").append(toIndentedString(importoTettoMaxTotRichiedenti)).append("\n");
		sb.append("    idTettoMax: ").append(toIndentedString(idTettoMax)).append("\n");
		sb.append("    valuta: ").append(toIndentedString(valuta)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}


}

