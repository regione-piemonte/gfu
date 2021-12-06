/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.legge;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class LeggeProvvDr   {

	public static int MAX_LENGTH_DESC_LEGGE_PROVV_DR = 450;
	public static int MAX_LENGTH_DESC_LEGGE = 100;
	public static int MAX_LENGTH_DESC_PROVVEDIMENTO = 150;
	public static int MAX_LENGTH_DESC_DR = 200;
	public static int MAX_LENGTH_DESC_VINCOLO_POPOLAZIONE = 50;


	private Long idLeggeProvvDr = null;
	private String descLeggeProvvDr = null;
	private Integer idLegge = null;
	private String descLegge = null;
	private Integer idProvvedimento = null;
	private String descProvvedimento = null;
	private Integer idDr = null;
	private String descDr = null;
	private Integer idVincoloPopolazione = null;
	private String descVincoloPopolazione = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateNullValue(getIdLegge(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
		ValidatorDto.validateLength(getDescLegge(), Boolean.TRUE, MAX_LENGTH_DESC_LEGGE, null, null, null);
		ValidatorDto.validateNumber(getIdProvvedimento(), Boolean.FALSE, null, null);  				
		ValidatorDto.validateLength(getDescProvvedimento(), Boolean.FALSE, MAX_LENGTH_DESC_PROVVEDIMENTO, null, null, null);
		ValidatorDto.validateNumber(getIdDr(), Boolean.FALSE, null, null);  				
		ValidatorDto.validateLength(getDescDr(), Boolean.FALSE, MAX_LENGTH_DESC_DR, null, null, null);
		ValidatorDto.validateNumber(getIdVincoloPopolazione(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getDescVincoloPopolazione(), Boolean.FALSE, MAX_LENGTH_DESC_VINCOLO_POPOLAZIONE, null, null, null);
		ValidatorDto.validateLength(getDescLeggeProvvDr(), Boolean.FALSE, MAX_LENGTH_DESC_LEGGE_PROVV_DR, null, null, null);

	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdLeggeProvvDr(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				

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
	@JsonProperty("descLeggeProvvDr")
	public String getDescLeggeProvvDr() {	
		return descLeggeProvvDr;
	}

	public void setDescLeggeProvvDr(String descLeggeProvvDr) {
		this.descLeggeProvvDr = descLeggeProvvDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idLegge")
	public Integer getIdLegge() {
		return idLegge;
	}
	public void setIdLegge(Integer idLegge) {
		this.idLegge = idLegge;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descLegge")
	public String getDescLegge() {
		return descLegge;
	}
	public void setDescLegge(String descLegge) {
		this.descLegge = descLegge;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idProvvedimento")
	public Integer getIdProvvedimento() {
		return idProvvedimento;
	}
	public void setIdProvvedimento(Integer idProvvedimento) {
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
	@JsonProperty("idDr")
	public Integer getIdDr() {
		return idDr;
	}
	public void setIdDr(Integer idDr) {
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
	@JsonProperty("idVincoloPopolazione")
	public Integer getIdVincoloPopolazione() {
		return idVincoloPopolazione;
	}
	public void setIdVincoloPopolazione(Integer idVincoloPopolazione) {
		this.idVincoloPopolazione = idVincoloPopolazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descVincoloPopolazione")
	public String getDescVincoloPopolazione() {
		return descVincoloPopolazione;
	}
	public void setDescVincoloPopolazione(String descVincoloPopolazione) {
		this.descVincoloPopolazione = descVincoloPopolazione;
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
		LeggeProvvDr leggeProvvDr = (LeggeProvvDr) o;
		return Objects.equals(idLeggeProvvDr, leggeProvvDr.idLeggeProvvDr) &&
				Objects.equals(descLeggeProvvDr, leggeProvvDr.descLeggeProvvDr) &&
				Objects.equals(idLegge, leggeProvvDr.idLegge) &&
				Objects.equals(descLegge, leggeProvvDr.descLegge) &&
				Objects.equals(idProvvedimento, leggeProvvDr.idProvvedimento) &&
				Objects.equals(descProvvedimento, leggeProvvDr.descProvvedimento) &&
				Objects.equals(idDr, leggeProvvDr.idDr) &&
				Objects.equals(descDr, leggeProvvDr.descDr) &&
				Objects.equals(idVincoloPopolazione, leggeProvvDr.idVincoloPopolazione) &&
				Objects.equals(descVincoloPopolazione, leggeProvvDr.descVincoloPopolazione) &&
				Objects.equals(isValid, leggeProvvDr.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idLeggeProvvDr, descLeggeProvvDr, idLegge, descLegge, idProvvedimento, descProvvedimento, idDr, descDr, idVincoloPopolazione, descVincoloPopolazione, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class LeggeProvvDr {\n");

		sb.append("    idLeggeProvvDr: ").append(toIndentedString(idLeggeProvvDr)).append("\n");
		sb.append("    descLeggeProvvDr: ").append(toIndentedString(descLeggeProvvDr)).append("\n");
		sb.append("    idLegge: ").append(toIndentedString(idLegge)).append("\n");
		sb.append("    descLegge: ").append(toIndentedString(descLegge)).append("\n");
		sb.append("    idProvvedimento: ").append(toIndentedString(idProvvedimento)).append("\n");
		sb.append("    descProvvedimento: ").append(toIndentedString(descProvvedimento)).append("\n");
		sb.append("    idDr: ").append(toIndentedString(idDr)).append("\n");
		sb.append("    descDr: ").append(toIndentedString(descDr)).append("\n");
		sb.append("    idVincoloPopolazione: ").append(toIndentedString(idVincoloPopolazione)).append("\n");
		sb.append("    descVincoloPopolazione: ").append(toIndentedString(descVincoloPopolazione)).append("\n");
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

