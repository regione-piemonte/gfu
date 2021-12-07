/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.comune;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class Comune   {

	private String istatComune = null;
	private String descComune = null;
	private String descProvincia = null;
	private String siglaProvincia = null;
	private Integer popolazione = null;


	public Comune(String istatComune, String descComune, String descProvincia,String siglaProvincia, Integer popolazione)
	{
		super();
		this.istatComune = istatComune;
		this.descComune = descComune;
		this.descProvincia = descProvincia;
		this.siglaProvincia = siglaProvincia;
		this.popolazione = popolazione;
	}



	public Comune() {
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


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Comune comune = (Comune) o;
		return Objects.equals(istatComune, comune.istatComune) &&
				Objects.equals(descComune, comune.descComune) &&
				Objects.equals(descProvincia, comune.descProvincia) &&
				Objects.equals(siglaProvincia, comune.siglaProvincia) &&
				Objects.equals(popolazione, comune.popolazione);
	}

	@Override
	public int hashCode() {
		return Objects.hash(istatComune, descComune, descProvincia, siglaProvincia, popolazione);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Comune {\n");

		sb.append("    istatComune: ").append(toIndentedString(istatComune)).append("\n");
		sb.append("    descComune: ").append(toIndentedString(descComune)).append("\n");
		sb.append("    descProvincia: ").append(toIndentedString(descProvincia)).append("\n");
		sb.append("    siglaProvincia: ").append(toIndentedString(siglaProvincia)).append("\n");
		sb.append("    popolazione: ").append(toIndentedString(popolazione)).append("\n");
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

