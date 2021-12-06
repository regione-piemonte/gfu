/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;

public class AdeguamentoUrb   {

	private String descTipoPianoSovr = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoPianoSovr")
	public String getDescTipoPianoSovr() {
		return descTipoPianoSovr;
	}
	public void setDescTipoPianoSovr(String descTipoPianoSovr) {
		this.descTipoPianoSovr = descTipoPianoSovr;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AdeguamentoUrb adeguamentoUrb = (AdeguamentoUrb) o;
		return Objects.equals(descTipoPianoSovr, adeguamentoUrb.descTipoPianoSovr);
	}

	@Override
	public int hashCode() {
		return Objects.hash(descTipoPianoSovr);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AdeguamentoUrb {\n");

		sb.append("    descTipoPianoSovr: ").append(toIndentedString(descTipoPianoSovr)).append("\n");
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

