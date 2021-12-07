/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.percentuale;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;

public class Percentuale   {

	private Long idPercentuale = null;
	private Long valorePercentuale = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateNumber(getValorePercentuale(), Boolean.FALSE, null, null);		  
	}
	
	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdPercentuale(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idPercentuale")
	public Long getIdPercentuale() {
		return idPercentuale;
	}
	public void setIdPercentuale(Long idPercentuale) {
		this.idPercentuale = idPercentuale;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("valorePercentuale")
	public Long getValorePercentuale() {
		return valorePercentuale;
	}
	public void setValorePercentuale(Long valorePercentuale) {
		this.valorePercentuale = valorePercentuale;
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
		Percentuale percentuale = (Percentuale) o;
		return Objects.equals(idPercentuale, percentuale.idPercentuale) &&
				Objects.equals(valorePercentuale, percentuale.valorePercentuale) &&
				Objects.equals(isValid, percentuale.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idPercentuale, valorePercentuale, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Percentuale {\n");

		sb.append("    idPercentuale: ").append(toIndentedString(idPercentuale)).append("\n");
		sb.append("    valorePercentuale: ").append(toIndentedString(valorePercentuale)).append("\n");
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

