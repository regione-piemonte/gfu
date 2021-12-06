/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.tetto;

import java.util.Objects;
import java.math.BigDecimal;
import org.codehaus.jackson.annotate.JsonProperty;

import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;

public class TettoMax   {

	public static int MAX_LENGTH_VALUTA = 50;

	private Long idTettoMax = null;
	private BigDecimal importo = null;
	private String valuta = null;
	private String dataInizio = null;
	private String dataFine = null;


	public void validate() throws DatiInputErratiException{
		ValidatorDto.validateLength(getValuta(), Boolean.FALSE, MAX_LENGTH_VALUTA, null, null, null);
		if(getValuta() != null && getValuta().equalsIgnoreCase(Constants.DB.VALUTA_EURO)) {
			ValidatorDto.validateDecimalNumber(getImporto(), Boolean.TRUE, null, null);
		}else {
			ValidatorDto.validateNumber(getImporto(), Boolean.TRUE, null, null);
		}

		ValidatorDto.validateDate(getDataInizio(), Boolean.FALSE, null, null);
		ValidatorDto.validateDate(getDataFine(), Boolean.FALSE, null, null);
	}

	public void validateUpdate() throws DatiInputErratiException
	{
		this.validate();
		ValidatorDto.validateNullValue(getIdTettoMax(), Boolean.TRUE, ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO, ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);  				
	}
	/**
	 **/


	@JsonProperty("idTettoMax") 

	public Long getIdTettoMax() {
		return idTettoMax;
	}
	public void setIdTettoMax(Long idTettoMax) {
		this.idTettoMax = idTettoMax;
	}

	/**
	 **/


	@JsonProperty("importo") 

	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 **/


	@JsonProperty("valuta") 

	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	/**
	 **/


	@JsonProperty("dataInizio") 

	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 **/


	@JsonProperty("dataFine") 

	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TettoMax tettoMax = (TettoMax) o;
		return Objects.equals(idTettoMax, tettoMax.idTettoMax) &&
				Objects.equals(importo, tettoMax.importo) &&
				Objects.equals(valuta, tettoMax.valuta) &&
				Objects.equals(dataInizio, tettoMax.dataInizio) &&
				Objects.equals(dataFine, tettoMax.dataFine);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTettoMax, importo, valuta, dataInizio, dataFine);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TettoMax {\n");

		sb.append("    idTettoMax: ").append(toIndentedString(idTettoMax)).append("\n");
		sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
		sb.append("    valuta: ").append(toIndentedString(valuta)).append("\n");
		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}

