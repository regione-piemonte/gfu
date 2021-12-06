/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.rendiconto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Rendiconto   {

	public static int MAX_LENGTH_DESCRIZIONE = 50;

	private Long idRendiconto = null;
	private String descrizione = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getDescrizione(), Boolean.FALSE, MAX_LENGTH_DESCRIZIONE, null, null, null);
	}
	
	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdRendiconto(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idRendiconto")
	public Long getIdRendiconto() {
		return idRendiconto;
	}
	public void setIdRendiconto(Long idRendiconto) {
		this.idRendiconto = idRendiconto;
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
		Rendiconto rendiconto = (Rendiconto) o;
		return Objects.equals(idRendiconto, rendiconto.idRendiconto) &&
				Objects.equals(descrizione, rendiconto.descrizione) &&
				Objects.equals(isValid, rendiconto.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRendiconto, descrizione, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Rendiconto {\n");

		sb.append("    idRendiconto: ").append(toIndentedString(idRendiconto)).append("\n");
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
