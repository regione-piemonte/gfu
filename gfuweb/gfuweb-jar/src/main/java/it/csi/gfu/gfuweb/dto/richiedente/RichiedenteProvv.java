/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.richiedente;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.provvedimento.ProvvedimentiToRichiedente;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class RichiedenteProvv   {

	private Long idRichiedente = null;
	private String istatComune = null;
	private Integer popolazione = null;
	private String descComune = null;
	private String siglaProvincia = null;

	private List<ProvvedimentiToRichiedente> provvedimentiToRichiedente = new ArrayList<ProvvedimentiToRichiedente>();

	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateNullValue(getProvvedimentiToRichiedente(), Boolean.TRUE, null, null);	

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
	@JsonProperty("provvedimentiToRichiedente")
	public List<ProvvedimentiToRichiedente> getProvvedimentiToRichiedente() {
		return provvedimentiToRichiedente;
	}
	public void setProvvedimentiToRichiedente(List<ProvvedimentiToRichiedente> provvedimentiToRichiedente) {
		this.provvedimentiToRichiedente = provvedimentiToRichiedente;
	}



	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RichiedenteProvv richiedenteProvv = (RichiedenteProvv) o;
		return Objects.equals(idRichiedente, richiedenteProvv.idRichiedente) &&
				Objects.equals(istatComune, richiedenteProvv.istatComune) &&
				Objects.equals(popolazione, richiedenteProvv.popolazione) &&
				Objects.equals(descComune, richiedenteProvv.descComune) &&
				Objects.equals(siglaProvincia, richiedenteProvv.siglaProvincia) &&
				Objects.equals(provvedimentiToRichiedente, richiedenteProvv.provvedimentiToRichiedente) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRichiedente, istatComune, popolazione, descComune, siglaProvincia,provvedimentiToRichiedente);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RichiedenteProvv {\n");

		sb.append("    idRichiedente: ").append(toIndentedString(idRichiedente)).append("\n");
		sb.append("    istatComune: ").append(toIndentedString(istatComune)).append("\n");
		sb.append("    popolazione: ").append(toIndentedString(popolazione)).append("\n");
		sb.append("    descComune: ").append(toIndentedString(descComune)).append("\n");
		sb.append("    siglaProvincia: ").append(toIndentedString(siglaProvincia)).append("\n");
		sb.append("    provvedimentiToRichiedente: ").append(toIndentedString(provvedimentiToRichiedente)).append("\n");
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

