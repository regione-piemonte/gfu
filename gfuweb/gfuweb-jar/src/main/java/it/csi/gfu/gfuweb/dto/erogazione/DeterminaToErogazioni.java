/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.erogazione;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.finanziamento.FinanziamentiDaAssociare;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;
import it.csi.gfu.gfuweb.dto.Error;


public class DeterminaToErogazioni   {

	public static int MAX_LENGTH_NUM_DETERMINA = 50;

	private String numDetermina = null;
	private String dataDetermina = null;
	private List<FinanziamentiDaAssociare> finanziamentiDaAssociare = new ArrayList<FinanziamentiDaAssociare>();

	public void validate() throws DatiInputErratiException {
		ValidatorDto.validateLength(getNumDetermina(), Boolean.TRUE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
		ValidatorDto.validateNumber(getNumDetermina(), Boolean.TRUE, null, null);
		ValidatorDto.validateDate(getDataDetermina(), Boolean.TRUE, null, null);
		if(!getFinanziamentiDaAssociare().isEmpty()) {
			for (int i = 0; i < getFinanziamentiDaAssociare().size(); i++) {
				ValidatorDto.validateNumber(getFinanziamentiDaAssociare().get(i).getIdFinanziamento(), Boolean.TRUE, null, null);
			}
		}else {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_20_FINANZIAMENTO_INESISTENTE);
			error.setMessage(ErrorMessages.MESSAGE_20_FINANZIAMENTO_INESISTENTE);
			throw new DatiInputErratiException(error);
		}
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numDetermina")
	public String getNumDetermina() {
		return numDetermina;
	}
	public void setNumDetermina(String numDetermina) {
		this.numDetermina = numDetermina;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataDetermina")
	public String getDataDetermina() {
		return dataDetermina;
	}
	public void setDataDetermina(String dataDetermina) {
		this.dataDetermina = dataDetermina;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("FinanziamentiDaAssociare")
	public List<FinanziamentiDaAssociare> getFinanziamentiDaAssociare() {
		return finanziamentiDaAssociare;
	}
	public void setFinanziamentiDaAssociare(List<FinanziamentiDaAssociare> finanziamentiDaAssociare) {
		this.finanziamentiDaAssociare = finanziamentiDaAssociare;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeterminaToErogazioni determinaToErogazioni = (DeterminaToErogazioni) o;
		return Objects.equals(numDetermina, determinaToErogazioni.numDetermina) &&
				Objects.equals(dataDetermina, determinaToErogazioni.dataDetermina) &&
				Objects.equals(finanziamentiDaAssociare, determinaToErogazioni.finanziamentiDaAssociare);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numDetermina, dataDetermina, finanziamentiDaAssociare);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DeterminaToErogazioni {\n");

		sb.append("    numDetermina: ").append(toIndentedString(numDetermina)).append("\n");
		sb.append("    dataDetermina: ").append(toIndentedString(dataDetermina)).append("\n");
		sb.append("    finanziamentiDaAssociare: ").append(toIndentedString(finanziamentiDaAssociare)).append("\n");
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

