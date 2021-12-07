/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.associazione;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.ValidatorDto;


public class Associazione   {

	public static int MAX_LENGTH_ISTAT_ASSOCIAZIONE_COD_FORMA_ASS = 25;
	public static int MAX_LENGTH_DESC_ASSOCIAZIONE = 200;
	public static int MAX_LENGTH_COD_TIPO_FORMA_ASSOCIATIVA = 8;


	private Long idAssociazione = null;
	private String istatAssociazione = null;
	private String descAssociazione = null;
	private String codTipoFormaAssociativa = null;
	private String descTipoFormaAssociativa = null;
	private List<Comune> comuni = new ArrayList<Comune>();

	public void validate() throws DatiInputErratiException
	{
		ValidatorDto.validateLength(getIstatAssociazione(), Boolean.FALSE, MAX_LENGTH_ISTAT_ASSOCIAZIONE_COD_FORMA_ASS, null, null, null);
		ValidatorDto.validateLength(getDescAssociazione(), Boolean.TRUE, MAX_LENGTH_DESC_ASSOCIAZIONE, null, null, null);
		ValidatorDto.validateLength(getCodTipoFormaAssociativa(), Boolean.TRUE, MAX_LENGTH_COD_TIPO_FORMA_ASSOCIATIVA, null, null, null);
		ValidatorDto.validateCodTipoFormaAssociativa(getCodTipoFormaAssociativa(), Boolean.TRUE);	 
		ValidatorDto.validateComuniAssociati(getComuni(), Boolean.TRUE, null, null);

	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idAssociazione")
	public Long getIdAssociazione() {
		return idAssociazione;
	}
	public void setIdAssociazione(Long idAssociazione) {
		this.idAssociazione = idAssociazione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("istatAssociazione")
	public String getIstatAssociazione() {
		return istatAssociazione;
	}
	public void setIstatAssociazione(String istatAssociazione) {
		this.istatAssociazione = istatAssociazione;
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
	@JsonProperty("comuni")
	public List<Comune> getComuni() {
		return comuni;
	}
	public void setComuni(List<Comune> comuni) {
		this.comuni = comuni;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Associazione associazione = (Associazione) o;
		return Objects.equals(idAssociazione, associazione.idAssociazione) &&
				Objects.equals(istatAssociazione, associazione.istatAssociazione) &&
				Objects.equals(descAssociazione, associazione.descAssociazione) &&
				Objects.equals(codTipoFormaAssociativa, associazione.codTipoFormaAssociativa) &&
				Objects.equals(descTipoFormaAssociativa, associazione.descTipoFormaAssociativa) &&
				Objects.equals(comuni, associazione.comuni);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAssociazione, istatAssociazione, descAssociazione, codTipoFormaAssociativa, descTipoFormaAssociativa, comuni);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Associazione {\n");

		sb.append("    idAssociazione: ").append(toIndentedString(idAssociazione)).append("\n");
		sb.append("    istatAssociazione: ").append(toIndentedString(istatAssociazione)).append("\n");
		sb.append("    descAssociazione: ").append(toIndentedString(descAssociazione)).append("\n");
		sb.append("    codTipoFormaAssociativa: ").append(toIndentedString(codTipoFormaAssociativa)).append("\n");
		sb.append("    descTipoFormaAssociativa: ").append(toIndentedString(descTipoFormaAssociativa)).append("\n");
		sb.append("    comuni: ").append(toIndentedString(comuni)).append("\n");
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

