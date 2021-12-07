/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.richiesta;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class RichiestaToRicercaAvanzata   {

	private Long idRichiesta = null;
	private String numProtocollo = null;
	private String dataProtocollo = null;
	private String descRichiedente = null;
	private String descTipoRichiedente = null;
	private String numPraticaPraurb = null;


	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idRichiesta")
	public Long getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(Long idRichiesta) {
		this.idRichiesta = idRichiesta;
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

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numPraticaPraurb")
	public String getNumPraticaPraurb() {
		return numPraticaPraurb;
	}
	public void setNumPraticaPraurb(String numPraticaPraurb) {
		this.numPraticaPraurb = numPraticaPraurb;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RichiestaToRicercaAvanzata richiestaToRicercaAvanzata = (RichiestaToRicercaAvanzata) o;
		return Objects.equals(idRichiesta, richiestaToRicercaAvanzata.idRichiesta) &&
				Objects.equals(numProtocollo, richiestaToRicercaAvanzata.numProtocollo) &&
				Objects.equals(dataProtocollo, richiestaToRicercaAvanzata.dataProtocollo) &&
				Objects.equals(descRichiedente, richiestaToRicercaAvanzata.descRichiedente) &&
				Objects.equals(descTipoRichiedente, richiestaToRicercaAvanzata.descTipoRichiedente)&&
				Objects.equals(numPraticaPraurb, richiestaToRicercaAvanzata.numPraticaPraurb);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRichiesta, numProtocollo, dataProtocollo, descRichiedente, descTipoRichiedente, numPraticaPraurb);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Richiesta {\n");

		sb.append("    idRichiesta: ").append(toIndentedString(idRichiesta)).append("\n");
		sb.append("    numProtocollo: ").append(toIndentedString(numProtocollo)).append("\n");
		sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
		sb.append("    descRichiedente: ").append(toIndentedString(descRichiedente)).append("\n");
		sb.append("    descTipoRichiedente: ").append(toIndentedString(descTipoRichiedente)).append("\n");
		sb.append("    numPraticaPraurb: ").append(toIndentedString(numPraticaPraurb)).append("\n");
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

