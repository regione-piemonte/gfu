/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.richiedente;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Richiedente   {
	
	public static int MAX_LENGTH_ISTAT_COMUNE = 50;

	private Long idRichiedente = null;
	private String istatComune = null;
	private String descComune = null;
	private Integer popolazione = null;
	private String descProvincia = null;
	private String siglaProvincia = null;
	
	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateLength(getIstatComune(), Boolean.FALSE, MAX_LENGTH_ISTAT_COMUNE, null, null, null);
		ValidatorDto.validateNumber(getPopolazione(), Boolean.FALSE, null, null);
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idRichiedente")
	public Long getIdRichiedente() {
		return idRichiedente;
	}
	public void setIdRichiedente(Long idRichiedente) {
		this.idRichiedente = idRichiedente;
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
	@JsonProperty("descComune")
	public String getDescComune() {
		return descComune;
	}
	public void setDescComune(String descComune) {
		this.descComune = descComune;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("popolazione")
	public Integer getPopolazione() {
		return popolazione;
	}
	public void setPopolazione(Integer popolazione) {
		this.popolazione = popolazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descProvincia")
	public String getDescProvincia() {
		return descProvincia;
	}
	public void setDescProvincia(String descProvincia) {
		this.descProvincia = descProvincia;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("siglaProvincia")
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Richiedente richiedente = (Richiedente) o;
		return Objects.equals(idRichiedente, richiedente.idRichiedente) &&
				Objects.equals(istatComune, richiedente.istatComune) &&
				Objects.equals(descComune, richiedente.descComune) &&
				Objects.equals(popolazione, richiedente.popolazione) &&
				Objects.equals(descProvincia, richiedente.descProvincia) &&
				Objects.equals(siglaProvincia, richiedente.siglaProvincia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRichiedente, istatComune, descComune, popolazione, descProvincia, siglaProvincia);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Richiedente {\n");

		sb.append("    idRichiedente: ").append(toIndentedString(idRichiedente)).append("\n");
		sb.append("    istatComune: ").append(toIndentedString(istatComune)).append("\n");
		sb.append("    descComune: ").append(toIndentedString(descComune)).append("\n");
		sb.append("    popolazione: ").append(toIndentedString(popolazione)).append("\n");
		sb.append("    descProvincia: ").append(toIndentedString(descProvincia)).append("\n");
		sb.append("    siglaProvincia: ").append(toIndentedString(siglaProvincia)).append("\n");
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

