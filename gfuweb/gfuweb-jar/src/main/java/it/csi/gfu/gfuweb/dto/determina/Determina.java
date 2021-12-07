/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.determina;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;


public class Determina   {

	private String url = null;
	private Integer numDetermina = null;
	private String dataDetermina = null;

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("numDetermina")
	public Integer getNumDetermina() {
		return numDetermina;
	}
	public void setNumDetermina(Integer numDetermina) {
		this.numDetermina = numDetermina;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("dataDetermina")
	public String getDataDetermina() {
		return dataDetermina;
	}
	public void setDataDetermina(String dataDetermina) {
		this.dataDetermina = dataDetermina;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Determina determina = (Determina) o;
		return Objects.equals(url, determina.url)&&
				Objects.equals(numDetermina, determina.numDetermina)&&
				Objects.equals(dataDetermina, determina.dataDetermina);
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, numDetermina, dataDetermina);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Determina {\n");

		sb.append("    url: ").append(toIndentedString(url)).append("\n");
		sb.append("    numDetermina: ").append(toIndentedString(numDetermina)).append("\n");
		sb.append("    dataDetermina: ").append(toIndentedString(dataDetermina)).append("\n");
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

