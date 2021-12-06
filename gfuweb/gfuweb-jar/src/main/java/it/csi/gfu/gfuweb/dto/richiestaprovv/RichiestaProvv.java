/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.richiestaprovv;

import java.math.BigDecimal;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class RichiestaProvv   {

	private Long idRichiesta = null;
	private Long idLeggeProvvDr = null;
	private String numProtocollo = null;
	private String dataProtocollo = null;
	private String descLeggeProvvDr = null;
	private BigDecimal idFinanziamento = null;

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
	@JsonProperty("idFinanziamento")
	public BigDecimal getIdFinanziamento() {
		return idFinanziamento;
	}
	public void setIdFinanziamento(BigDecimal idFinanziamento) {
		this.idFinanziamento = idFinanziamento;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RichiestaProvv richiestaProvv = (RichiestaProvv) o;
		return Objects.equals(idRichiesta, richiestaProvv.idRichiesta) &&
				Objects.equals(numProtocollo, richiestaProvv.numProtocollo) &&
				Objects.equals(dataProtocollo, richiestaProvv.dataProtocollo) &&
				Objects.equals(idLeggeProvvDr, richiestaProvv.idLeggeProvvDr) &&
				Objects.equals(descLeggeProvvDr, richiestaProvv.descLeggeProvvDr) &&
				Objects.equals(idFinanziamento, richiestaProvv.idFinanziamento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idRichiesta, numProtocollo, dataProtocollo, idLeggeProvvDr, descLeggeProvvDr, idFinanziamento);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RichiestaProvv {\n");

		sb.append("    idRichiesta: ").append(toIndentedString(idRichiesta)).append("\n");
		sb.append("    numProtocollo: ").append(toIndentedString(numProtocollo)).append("\n");
		sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
		sb.append("    idLeggeProvvDr: ").append(toIndentedString(idLeggeProvvDr)).append("\n");
		sb.append("    descLeggeProvvDr: ").append(toIndentedString(descLeggeProvvDr)).append("\n");
		sb.append("    idFinanziamento: ").append(toIndentedString(idFinanziamento)).append("\n");
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

