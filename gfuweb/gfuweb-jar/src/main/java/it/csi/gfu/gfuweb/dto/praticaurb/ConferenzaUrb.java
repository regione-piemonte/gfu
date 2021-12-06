/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;

public class ConferenzaUrb   {

	private String descTipoConferenza = null;
	private String descTipoPassoIter = null;
	private String dataPassoIter = null;
	private String numProtocolloPassoIter = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoConferenza")
	public String getDescTipoConferenza() {
		return descTipoConferenza;
	}
	public void setDescTipoConferenza(String descTipoConferenza) {
		this.descTipoConferenza = descTipoConferenza;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoPassoIter")
	public String getDescTipoPassoIter() {
		return descTipoPassoIter;
	}
	public void setDescTipoPassoIter(String descTipoPassoIter) {
		this.descTipoPassoIter = descTipoPassoIter;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("dataPassoIter")
	public String getDataPassoIter() {
		return dataPassoIter;
	}
	public void setDataPassoIter(String dataPassoIter) {
		this.dataPassoIter = dataPassoIter;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numProtocolloPassoIter")
	public String getNumProtocolloPassoIter() {
		return numProtocolloPassoIter;
	}
	public void setNumProtocolloPassoIter(String numProtocolloPassoIter) {
		this.numProtocolloPassoIter = numProtocolloPassoIter;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ConferenzaUrb conferenzaUrb = (ConferenzaUrb) o;
		return Objects.equals(descTipoConferenza, conferenzaUrb.descTipoConferenza) &&
				Objects.equals(descTipoPassoIter, conferenzaUrb.descTipoPassoIter) &&
				Objects.equals(dataPassoIter, conferenzaUrb.dataPassoIter) &&
				Objects.equals(numProtocolloPassoIter, conferenzaUrb.numProtocolloPassoIter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(descTipoConferenza, descTipoPassoIter, dataPassoIter, numProtocolloPassoIter);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ConferenzaUrb {\n");

		sb.append("    descTipoConferenza: ").append(toIndentedString(descTipoConferenza)).append("\n");
		sb.append("    descTipoPassoIter: ").append(toIndentedString(descTipoPassoIter)).append("\n");
		sb.append("    dataPassoIter: ").append(toIndentedString(dataPassoIter)).append("\n");
		sb.append("    numProtocolloPassoIter: ").append(toIndentedString(numProtocolloPassoIter)).append("\n");
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

