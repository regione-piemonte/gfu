/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.praticaurb;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.csi.gfu.gfuweb.dto.praticaurb.AdeguamentoUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.ConferenzaUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.ProvvedimentoUrb;
import java.util.List;
import io.swagger.annotations.*;

public class PraticaUrb   {

	private String numPratica = null;
	private String numProtocollo = null;
	private String dataIngressoRegione = null;
	private String descTipoPratica = null;
	private String oggettoPratica = null;
	private String descStatoPratica = null;
	private String elencoComunePv = null;
	private List<ProvvedimentoUrb> provvedimentiUrb = new ArrayList<ProvvedimentoUrb>();
	private List<ConferenzaUrb> conferenzeUrb = new ArrayList<ConferenzaUrb>();
	private List<AdeguamentoUrb> adeguamentoUrb = new ArrayList<AdeguamentoUrb>();

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("numPratica")
	public String getNumPratica() {
		return numPratica;
	}
	public void setNumPratica(String numPratica) {
		this.numPratica = numPratica;
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
	@JsonProperty("dataIngressoRegione")
	public String getDataIngressoRegione() {
		return dataIngressoRegione;
	}
	public void setDataIngressoRegione(String dataIngressoRegione) {
		this.dataIngressoRegione = dataIngressoRegione;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descTipoPratica")
	public String getDescTipoPratica() {
		return descTipoPratica;
	}
	public void setDescTipoPratica(String descTipoPratica) {
		this.descTipoPratica = descTipoPratica;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("oggettoPratica")
	public String getOggettoPratica() {
		return oggettoPratica;
	}
	public void setOggettoPratica(String oggettoPratica) {
		this.oggettoPratica = oggettoPratica;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("descStatoPratica")
	public String getDescStatoPratica() {
		return descStatoPratica;
	}
	public void setDescStatoPratica(String descStatoPratica) {
		this.descStatoPratica = descStatoPratica;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("elencoComunePv")
	public String getElencoComunePv() {
		return elencoComunePv;
	}
	public void setElencoComunePv(String elencoComunePv) {
		this.elencoComunePv = elencoComunePv;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("provvedimentiUrb")
	public List<ProvvedimentoUrb> getProvvedimentiUrb() {
		return provvedimentiUrb;
	}
	public void setProvvedimentiUrb(List<ProvvedimentoUrb> provvedimentiUrb) {
		this.provvedimentiUrb = provvedimentiUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("conferenzeUrb")
	public List<ConferenzaUrb> getConferenzeUrb() {
		return conferenzeUrb;
	}
	public void setConferenzeUrb(List<ConferenzaUrb> conferenzeUrb) {
		this.conferenzeUrb = conferenzeUrb;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("adeguamentoUrb")
	public List<AdeguamentoUrb> getAdeguamentoUrb() {
		return adeguamentoUrb;
	}
	public void setAdeguamentoUrb(List<AdeguamentoUrb> adeguamentoUrb) {
		this.adeguamentoUrb = adeguamentoUrb;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PraticaUrb praticaUrb = (PraticaUrb) o;
		return Objects.equals(numPratica, praticaUrb.numPratica) &&
				Objects.equals(numProtocollo, praticaUrb.numProtocollo) &&
				Objects.equals(dataIngressoRegione, praticaUrb.dataIngressoRegione) &&
				Objects.equals(descTipoPratica, praticaUrb.descTipoPratica) &&
				Objects.equals(oggettoPratica, praticaUrb.oggettoPratica) &&
				Objects.equals(descStatoPratica, praticaUrb.descStatoPratica) &&
				Objects.equals(elencoComunePv, praticaUrb.elencoComunePv) &&
				Objects.equals(provvedimentiUrb, praticaUrb.provvedimentiUrb) &&
				Objects.equals(conferenzeUrb, praticaUrb.conferenzeUrb) &&
				Objects.equals(adeguamentoUrb, praticaUrb.adeguamentoUrb);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numPratica, numProtocollo, dataIngressoRegione, descTipoPratica, oggettoPratica, descStatoPratica, elencoComunePv, provvedimentiUrb, conferenzeUrb, adeguamentoUrb);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PraticaUrb {\n");

		sb.append("    numPratica: ").append(toIndentedString(numPratica)).append("\n");
		sb.append("    numProtocollo: ").append(toIndentedString(numProtocollo)).append("\n");
		sb.append("    dataIngressoRegione: ").append(toIndentedString(dataIngressoRegione)).append("\n");
		sb.append("    descTipoPratica: ").append(toIndentedString(descTipoPratica)).append("\n");
		sb.append("    oggettoPratica: ").append(toIndentedString(oggettoPratica)).append("\n");
		sb.append("    descStatoPratica: ").append(toIndentedString(descStatoPratica)).append("\n");
		sb.append("    elencoComunePv: ").append(toIndentedString(elencoComunePv)).append("\n");
		sb.append("    provvedimentiUrb: ").append(toIndentedString(provvedimentiUrb)).append("\n");
		sb.append("    conferenzeUrb: ").append(toIndentedString(conferenzeUrb)).append("\n");
		sb.append("    adeguamentoUrb: ").append(toIndentedString(adeguamentoUrb)).append("\n");
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

