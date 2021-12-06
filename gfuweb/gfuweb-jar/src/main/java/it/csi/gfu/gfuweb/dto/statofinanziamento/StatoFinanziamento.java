/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.statofinanziamento;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class StatoFinanziamento   {

	private Long idStatoFinanziamento = null;
	private String descrizione = null;
	private String codStatoFinanziamento = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idStatoFinanziamento")
	public Long getIdStatoFinanziamento() {
		return idStatoFinanziamento;
	}
	public void setIdStatoFinanziamento(Long idStatoFinanziamento) {
		this.idStatoFinanziamento = idStatoFinanziamento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descrizione")
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	
	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("codStatoFinanziamento")
	public String getCodStatoFinanziamento() {
		return codStatoFinanziamento;
	}
	public void setCodStatoFinanziamento(String codStatoFinanziamento) {
		this.codStatoFinanziamento = codStatoFinanziamento;
	}
	


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		StatoFinanziamento statoFinanziamento = (StatoFinanziamento) o;
		return Objects.equals(idStatoFinanziamento, statoFinanziamento.idStatoFinanziamento) &&
				Objects.equals(descrizione, statoFinanziamento.descrizione)&&
				Objects.equals(codStatoFinanziamento, statoFinanziamento.codStatoFinanziamento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idStatoFinanziamento, descrizione,codStatoFinanziamento);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class StatoFinanziamento {\n");

		sb.append("    idStatoFinanziamento: ").append(toIndentedString(idStatoFinanziamento)).append("\n");
		sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
		sb.append("    codStatoFinanziamento: ").append(toIndentedString(codStatoFinanziamento)).append("\n");
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

