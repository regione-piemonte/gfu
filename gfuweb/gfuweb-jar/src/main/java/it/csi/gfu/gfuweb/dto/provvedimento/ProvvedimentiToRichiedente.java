/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.provvedimento;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class ProvvedimentiToRichiedente   {

	private Long idLeggeProvvDr = null;
	private String descLeggeProvvDr = null;
	private Boolean flagDocumentazione = null;
	private Boolean flagRinuncia = null;
	private String descVincoloPopolazione = null; 
	
	
	public ProvvedimentiToRichiedente(Long idLeggeProvvDr, Boolean flagDocumentazione, Boolean flagRinuncia,
			String descLeggeProvvDr, String descVincoloPopolazione) {
		super();
		this.idLeggeProvvDr = idLeggeProvvDr;
		this.flagDocumentazione = flagDocumentazione;
		this.flagRinuncia = flagRinuncia;
		this.descLeggeProvvDr = descLeggeProvvDr;
		this.descVincoloPopolazione = descVincoloPopolazione;
	}
	
	
	public ProvvedimentiToRichiedente() {
		super();
	}


	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idLeggeProvvDr")
	public Long getIdLeggeProvvDr() {
		return idLeggeProvvDr;
	}
	public void setIdLeggeProvvDr(Long idLeggeProvvDr) {
		this.idLeggeProvvDr = idLeggeProvvDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("flagDocumentazione")
	public Boolean isFlagDocumentazione() {
		return flagDocumentazione;
	}
	public void setFlagDocumentazione(Boolean flagDocumentazione) {
		this.flagDocumentazione = flagDocumentazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("flagRinuncia")
	public Boolean isFlagRinuncia() {
		return flagRinuncia;
	}
	public void setFlagRinuncia(Boolean flagRinuncia) {
		this.flagRinuncia = flagRinuncia;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("descLeggeProvvDr")
	public String getDescLeggeProvvDr() {
		return descLeggeProvvDr;
	}

	public void setDescLeggeProvvDr(String descLeggeProvvDr) {
		this.descLeggeProvvDr = descLeggeProvvDr;
	}
	
	@ApiModelProperty(value = "")
	@JsonProperty("descVincoloPopolazione")
	public String getDescVincoloPopolazione() {
		return descVincoloPopolazione;
	}

	public void setDescVincoloPopolazione(String descVincoloPopolazione) {
		this.descVincoloPopolazione = descVincoloPopolazione;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProvvedimentiToRichiedente provvedimentiToRichiedente = (ProvvedimentiToRichiedente) o;
		return Objects.equals(idLeggeProvvDr, provvedimentiToRichiedente.idLeggeProvvDr) &&
				Objects.equals(flagDocumentazione, provvedimentiToRichiedente.flagDocumentazione) &&
				Objects.equals(flagRinuncia, provvedimentiToRichiedente.flagRinuncia)&&
				Objects.equals(descLeggeProvvDr, provvedimentiToRichiedente.descLeggeProvvDr)&&
				Objects.equals(descVincoloPopolazione, provvedimentiToRichiedente.descVincoloPopolazione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idLeggeProvvDr, flagDocumentazione, flagRinuncia, descLeggeProvvDr,descVincoloPopolazione);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ProvvedimentiToRichiedente {\n");

		sb.append("    idLeggeProvvDr: ").append(toIndentedString(idLeggeProvvDr)).append("\n");
		sb.append("    flagDocumentazione: ").append(toIndentedString(flagDocumentazione)).append("\n");
		sb.append("    flagRinuncia: ").append(toIndentedString(flagRinuncia)).append("\n");
		sb.append("    descLeggeProvvDr: ").append(toIndentedString(descLeggeProvvDr)).append("\n");
		sb.append("    descVincoloPopolazione: ").append(toIndentedString(descVincoloPopolazione)).append("\n");
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

