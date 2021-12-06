/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.richiesta;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Richiesta   {

	public static int MAX_LENGTH_NUMERO_PROTOCOLLO = 15;
	public static int MAX_LENGTH_NOTE = 50;

	private Long idRichiesta = null;
	private String numProtocollo = null;
	private String dataProtocollo = null;
	private String note = null;
	private Integer idAssociazione = null;
	private String descAssociazione = null;
	private String codTipoFormaAssociativa = null;
	private String descTipoFormaAssociativa = null;
	private Boolean isValid = null;

	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateDate(getDataProtocollo(), Boolean.TRUE, null, null);
		ValidatorDto.validateDateBeforeEuroAndAfterToday(getDataProtocollo());
		ValidatorDto.validateLength(getNumProtocollo(), Boolean.TRUE, MAX_LENGTH_NUMERO_PROTOCOLLO, null, null, null);
		ValidatorDto.validateNumber(getIdAssociazione(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getNote(), Boolean.FALSE, MAX_LENGTH_NOTE, null, null, null);
	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdRichiesta(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	}

	/**
	 **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("idRichiesta")
	 public Long getIdRichiesta() {
		 return idRichiesta;
	 }
	 public void setIdRichiesta(Long idRichiesta) {
		 this.idRichiesta = idRichiesta;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("numProtocollo")
	 public String getNumProtocollo() {
		 return numProtocollo;
	 }
	 public void setNumProtocollo(String numProtocollo) {
		 this.numProtocollo = numProtocollo;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("dataProtocollo")
	 public String getDataProtocollo() {
		 return dataProtocollo;
	 }
	 public void setDataProtocollo(String dataProtocollo) {
		 this.dataProtocollo = dataProtocollo;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("note")
	 public String getNote() {
		 return note;
	 }
	 public void setNote(String note) {
		 this.note = note;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("idAssociazione")
	 public Integer getIdAssociazione() {
		 return idAssociazione;
	 }
	 public void setIdAssociazione(Integer idAssociazione) {
		 this.idAssociazione = idAssociazione;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("descAssociazione")
	 public String getDescAssociazione() {
		 return descAssociazione;
	 }
	 public void setDescAssociazione(String descAssociazione) {
		 this.descAssociazione = descAssociazione;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("codTipoFormaAssociativa")
	 public String getCodTipoFormaAssociativa() {
		 return codTipoFormaAssociativa;
	 }
	 public void setCodTipoFormaAssociativa(String codTipoFormaAssociativa) {
		 this.codTipoFormaAssociativa = codTipoFormaAssociativa;
	 }

	 /**
	  **/

	 @ApiModelProperty(value = "")
	 @JsonProperty("descTipoFormaAssociativa")
	 public String getDescTipoFormaAssociativa() {
		 return descTipoFormaAssociativa;
	 }
	 public void setDescTipoFormaAssociativa(String descTipoFormaAssociativa) {
		 this.descTipoFormaAssociativa = descTipoFormaAssociativa;
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
		 Richiesta richiesta = (Richiesta) o;
		 return Objects.equals(idRichiesta, richiesta.idRichiesta) &&
				 Objects.equals(numProtocollo, richiesta.numProtocollo) &&
				 Objects.equals(dataProtocollo, richiesta.dataProtocollo) &&
				 Objects.equals(note, richiesta.note) &&
				 Objects.equals(idAssociazione, richiesta.idAssociazione) &&
				 Objects.equals(descAssociazione, richiesta.descAssociazione) &&
				 Objects.equals(codTipoFormaAssociativa, richiesta.codTipoFormaAssociativa) &&
				 Objects.equals(descTipoFormaAssociativa, richiesta.descTipoFormaAssociativa) &&
				 Objects.equals(isValid, richiesta.isValid);
	 }

	 @Override
	 public int hashCode() {
		 return Objects.hash(idRichiesta, numProtocollo, dataProtocollo, note, idAssociazione, descAssociazione, codTipoFormaAssociativa, descTipoFormaAssociativa, isValid);
	 }

	 @Override
	 public String toString() {
		 StringBuilder sb = new StringBuilder();
		 sb.append("class Richiesta {\n");

		 sb.append("    idRichiesta: ").append(toIndentedString(idRichiesta)).append("\n");
		 sb.append("    numProtocollo: ").append(toIndentedString(numProtocollo)).append("\n");
		 sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
		 sb.append("    note: ").append(toIndentedString(note)).append("\n");
		 sb.append("    idAssociazione: ").append(toIndentedString(idAssociazione)).append("\n");
		 sb.append("    descAssociazione: ").append(toIndentedString(descAssociazione)).append("\n");
		 sb.append("    codTipoFormaAssociativa: ").append(toIndentedString(codTipoFormaAssociativa)).append("\n");
		 sb.append("    descTipoFormaAssociativa: ").append(toIndentedString(descTipoFormaAssociativa)).append("\n");
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

