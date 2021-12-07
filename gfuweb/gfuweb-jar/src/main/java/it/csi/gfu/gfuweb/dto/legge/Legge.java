/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.legge;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Legge   {

	public static int MAX_LENGTH_DESCRIZIONE = 100;

	private Long idLegge = null;
	private String descrizione = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getDescrizione(), Boolean.FALSE, MAX_LENGTH_DESCRIZIONE, null, null, null);
	}
	
	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdLegge(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idLegge")
	public Long getIdLegge() { 
		return idLegge;
	}
	public void setIdLegge(Long idLegge) {
		this.idLegge = idLegge;
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
	@JsonProperty("isValid")
	public Boolean isIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Legge legge = (Legge) o;
		return Objects.equals(idLegge, legge.idLegge) &&
				Objects.equals(descrizione, legge.descrizione) &&
				Objects.equals(isValid, legge.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idLegge, descrizione, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Legge {\n");

		sb.append("    idLegge: ").append(toIndentedString(idLegge)).append("\n");
		sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
		sb.append("    isValid: ").append(toIndentedString(isValid)).append("\n");
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

