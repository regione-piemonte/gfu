/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.dr;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Dr   {

	public static int MAX_LENGTH_DESC_DR = 200;
	public static int MAX_LENGTH_NUMERO = 25;

	private Long idDr = null;
	private String descDr = null;
	private Integer idTipoDr = null;
	private String descTipoDr = null;
	private String numeroDr = null;
	private String dataDr = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getDescDr(), Boolean.FALSE, MAX_LENGTH_DESC_DR, null, null, null);
		ValidatorDto.validateLength(getNumeroDr(), Boolean.FALSE, MAX_LENGTH_NUMERO, null, null, null);
		ValidatorDto.validateDate(getDataDr(), Boolean.FALSE, null, null);

	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdDr(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	}


	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idDr")
	public Long getIdDr() {
		return idDr;
	}
	public void setIdDr(Long idDr) {
		this.idDr = idDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descDr")
	public String getDescDr() {
		return descDr;
	}
	public void setDescDr(String descDr) {
		this.descDr = descDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idTipoDr")
	public Integer getIdTipoDr() {
		return idTipoDr;
	}
	public void setIdTipoDr(Integer idTipoDr) {
		this.idTipoDr = idTipoDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoDr")
	public String getDescTipoDr() {
		return descTipoDr;
	}
	public void setDescTipoDr(String descTipoDr) {
		this.descTipoDr = descTipoDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numeroDr")
	public String getNumeroDr() {
		return numeroDr;
	}
	public void setNumeroDr(String numeroDr) {
		this.numeroDr = numeroDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataDr")
	public String getDataDr() {
		return dataDr;
	}
	public void setDataDr(String dataDr) {
		this.dataDr = dataDr;
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
		Dr dr = (Dr) o;
		return Objects.equals(idDr, dr.idDr) &&
				Objects.equals(descDr, dr.descDr) &&
				Objects.equals(idTipoDr, dr.idTipoDr) &&
				Objects.equals(descTipoDr, dr.descTipoDr) &&
				Objects.equals(numeroDr, dr.numeroDr) &&
				Objects.equals(dataDr, dr.dataDr) &&
				Objects.equals(isValid, dr.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDr, descDr, idTipoDr, descTipoDr, numeroDr, dataDr, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Dr {\n");

		sb.append("    idDr: ").append(toIndentedString(idDr)).append("\n");
		sb.append("    descDr: ").append(toIndentedString(descDr)).append("\n");
		sb.append("    idTipoDr: ").append(toIndentedString(idTipoDr)).append("\n");
		sb.append("    descTipoDr: ").append(toIndentedString(descTipoDr)).append("\n");
		sb.append("    numeroDr: ").append(toIndentedString(numeroDr)).append("\n");
		sb.append("    dataDr: ").append(toIndentedString(dataDr)).append("\n");
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

