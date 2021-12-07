/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.popolazione;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class VincoloPopolazione   {

	public static int MAX_LENGTH_DESCRIZIONE = 50;
	public static int MAX_LENGTH_SEGNO = 1;
	public static String VALORI_POSSIBILI_SEGNO = "(<|>)";

	private Long idVincoloPopolazione = null;
	private String descrizione = null;
	private String segno = null;
	private BigDecimal popolazione = null;
	private Boolean isValid = false;

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getDescrizione(), Boolean.FALSE, MAX_LENGTH_DESCRIZIONE, null, null, null);		
		ValidatorDto.validateLength(getSegno(), Boolean.FALSE, MAX_LENGTH_SEGNO, null, null, null);	
		ValidatorDto.validateNumber(getPopolazione(), Boolean.FALSE, null, null);		  	
		ValidatorDto.validateValoriPossibili(getSegno(), Boolean.TRUE, VALORI_POSSIBILI_SEGNO, null, null, null);
		ValidatorDto.validateNumber(getPopolazione(), Boolean.TRUE, null, null);		
		ValidatorDto.validateLength(getDescrizione(), Boolean.FALSE, MAX_LENGTH_DESCRIZIONE, null, null, null);		

	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();

		ValidatorDto.validateNullValue(getIdVincoloPopolazione(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				

	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idVincoloPopolazione")
	public Long getIdVincoloPopolazione() {
		return idVincoloPopolazione;
	}
	public void setIdVincoloPopolazione(Long idVincoloPopolazione) {
		this.idVincoloPopolazione = idVincoloPopolazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descrizione")
	public String getDescrizione() {

		String descrizione = getSegno()+" "+getPopolazione() ;

		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("segno")
	public String getSegno() {
		return segno;
	}
	public void setSegno(String segno) {
		this.segno = segno;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("popolazione")
	public BigDecimal getPopolazione() {
		return popolazione;
	}
	public void setPopolazione(BigDecimal popolazione) {
		this.popolazione = popolazione;
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
		VincoloPopolazione vincoloPopolazione = (VincoloPopolazione) o;
		return Objects.equals(idVincoloPopolazione, vincoloPopolazione.idVincoloPopolazione) &&
				Objects.equals(descrizione, vincoloPopolazione.descrizione) &&
				Objects.equals(segno, vincoloPopolazione.segno) &&
				Objects.equals(popolazione, vincoloPopolazione.popolazione) &&
				Objects.equals(isValid, vincoloPopolazione.isValid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idVincoloPopolazione, descrizione, segno, popolazione, isValid);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class VincoloPopolazione {\n");

		sb.append("    idVincoloPopolazione: ").append(toIndentedString(idVincoloPopolazione)).append("\n");
		sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
		sb.append("    segno: ").append(toIndentedString(segno)).append("\n");
		sb.append("    popolazione: ").append(toIndentedString(popolazione)).append("\n");
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

