/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.provvedimento;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Provvedimento   {

	public static int MAX_LENGTH_DESC_PROVVEDIMENTO = 150;

	private Long idProvvedimento = null;
	private String descProvvedimento = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getDescProvvedimento(), Boolean.FALSE, MAX_LENGTH_DESC_PROVVEDIMENTO, null, null, null);
	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdProvvedimento(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				

	}


	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idProvvedimento")
	public Long getIdProvvedimento() {
		return idProvvedimento;
	}
	public void setIdProvvedimento(Long idProvvedimento) {
		this.idProvvedimento = idProvvedimento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descProvvedimento")
	public String getDescProvvedimento() {
		return descProvvedimento;
	}
	public void setDescProvvedimento(String descProvvedimento) {
		this.descProvvedimento = descProvvedimento;
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
		Provvedimento provvedimento = (Provvedimento) o;
		return Objects.equals(idProvvedimento, provvedimento.idProvvedimento) &&
				Objects.equals(descProvvedimento, provvedimento.descProvvedimento) &&
				Objects.equals(isValid, provvedimento.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idProvvedimento, descProvvedimento, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Provvedimento {\n");

		sb.append("    idProvvedimento: ").append(toIndentedString(idProvvedimento)).append("\n");
		sb.append("    descProvvedimento: ").append(toIndentedString(descProvvedimento)).append("\n");
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

