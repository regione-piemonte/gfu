/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ValidatorDto;

public class PraticaUrbGfu   {

	public static int MAX_LENGTH_NUM_ATTO_E_PRATICA_URB = 20;

	private Long idPraticaUrb = null;
	private String numPraticaUrb = null;
	private String numAttoApprovazioneUrb = null;
	private String dataAttoApprovazioneUrb = null;
	private BigDecimal fkFinanziamento = null;

	public void validate() throws DatiInputErratiException {

		ValidatorDto.validateLength(getNumPraticaUrb(), Boolean.TRUE, MAX_LENGTH_NUM_ATTO_E_PRATICA_URB, null, null, null);
		ValidatorDto.validateLength(getNumAttoApprovazioneUrb(), Boolean.TRUE, MAX_LENGTH_NUM_ATTO_E_PRATICA_URB, null, null, null);
		ValidatorDto.validateDate(getDataAttoApprovazioneUrb(), Boolean.TRUE, null, null);
		ValidatorDto.validateNumber(getFkFinanziamento(), Boolean.TRUE, null, null);

	}
	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idPraticaUrb")
	public Long getIdPraticaUrb() {
		return idPraticaUrb;
	}
	public void setIdPraticaUrb(Long idPraticaUrb) {
		this.idPraticaUrb = idPraticaUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numPraticaUrb")
	public String getNumPraticaUrb() {
		return numPraticaUrb;
	}
	public void setNumPraticaUrb(String numPraticaUrb) {
		this.numPraticaUrb = numPraticaUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numAttoApprovazioneUrb")
	public String getNumAttoApprovazioneUrb() {
		return numAttoApprovazioneUrb;
	}
	public void setNumAttoApprovazioneUrb(String numAttoApprovazioneUrb) {
		this.numAttoApprovazioneUrb = numAttoApprovazioneUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataAttoApprovazioneUrb")
	public String getDataAttoApprovazioneUrb() {
		return dataAttoApprovazioneUrb;
	}
	public void setDataAttoApprovazioneUrb(String dataAttoApprovazioneUrb) {
		this.dataAttoApprovazioneUrb = dataAttoApprovazioneUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("fkFinanziamento")
	public BigDecimal getFkFinanziamento() {
		return fkFinanziamento;
	}
	public void setFkFinanziamento(BigDecimal fkFinanziamento) {
		this.fkFinanziamento = fkFinanziamento;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PraticaUrbGfu praticaUrbGfu = (PraticaUrbGfu) o;
		return Objects.equals(idPraticaUrb, praticaUrbGfu.idPraticaUrb) &&
				Objects.equals(numPraticaUrb, praticaUrbGfu.numPraticaUrb) &&
				Objects.equals(numAttoApprovazioneUrb, praticaUrbGfu.numAttoApprovazioneUrb) &&
				Objects.equals(dataAttoApprovazioneUrb, praticaUrbGfu.dataAttoApprovazioneUrb) &&
				Objects.equals(fkFinanziamento, praticaUrbGfu.fkFinanziamento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idPraticaUrb, numPraticaUrb, numAttoApprovazioneUrb, dataAttoApprovazioneUrb, fkFinanziamento);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PraticaUrbGfu {\n");

		sb.append("    idPraticaUrb: ").append(toIndentedString(idPraticaUrb)).append("\n");
		sb.append("    numPraticaUrb: ").append(toIndentedString(numPraticaUrb)).append("\n");
		sb.append("    numAttoApprovazioneUrb: ").append(toIndentedString(numAttoApprovazioneUrb)).append("\n");
		sb.append("    dataAttoApprovazioneUrb: ").append(toIndentedString(dataAttoApprovazioneUrb)).append("\n");
		sb.append("    fkFinanziamento: ").append(toIndentedString(fkFinanziamento)).append("\n");
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

