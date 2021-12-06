/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.finanziamento;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import io.swagger.annotations.*;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.dto.rinuncia.Rinuncia;
import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;

public class Finanziamento   {

	public static int MAX_LENGTH_VALUTA = 10;
	public static int MAX_LENGTH_NOTE = 400;
	public static int MAX_LENGTH_ATTO_RINUNCIA = 250;
	public static int MAX_LENGTH_NUM_DETERMINA = 50;

	private Long idFinanziamento = null;
	private BigDecimal importoFinanziabile = null;
	private BigDecimal importoAmmesso = null;
	private String valuta = null;
	private BigDecimal fkImportoTettoMax = null;
	private PraticaUrbGfu praticaUrbGfu = null;
	private String note = null;
	private BigDecimal idLeggeProvvDr = null;
	private Percentuale percentuale = null;
	private Parere parere = null;
	private Rendiconto rendiconto = null;
	private StatoFinanziamento statoFinanziamento = null;
	private StatoFinanziamento statoFinanziamentoPrec = null;
	private Rinuncia rinuncia = null;
	private List<Erogazione> erogazioni = new ArrayList<Erogazione>();
	

	public void validate() throws DatiInputErratiException {

		ValidatorDto.validateLength(getValuta(), Boolean.FALSE, MAX_LENGTH_VALUTA, null, null, null);
		if(getValuta() != null && getValuta().equalsIgnoreCase(Constants.DB.VALUTA_EURO)) {
			ValidatorDto.validateDecimalNumber(getImportoFinanziabile(), Boolean.FALSE, null, null);
			ValidatorDto.validateDecimalNumber(getImportoAmmesso(), Boolean.FALSE, null, null);
		}else {
			ValidatorDto.validateNumber(getImportoFinanziabile(), Boolean.FALSE, null, null);
			ValidatorDto.validateNumber(getImportoAmmesso(), Boolean.FALSE, null, null);
		}

		ValidatorDto.validateNumber(getFkImportoTettoMax(), Boolean.FALSE, null, null);
		ValidatorDto.validateLength(getNote(), Boolean.FALSE, MAX_LENGTH_NOTE, null, null, null);
		ValidatorDto.validateNumber(getIdLeggeProvvDr(), Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getPercentuale() != null ? getPercentuale().getIdPercentuale():null, Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getParere() != null ? getParere().getIdParere():null, Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getRendiconto() != null ? getRendiconto().getIdRendiconto() :null, Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getStatoFinanziamento() != null ? getStatoFinanziamento().getIdStatoFinanziamento():null, Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getStatoFinanziamentoPrec() != null ? getStatoFinanziamentoPrec().getIdStatoFinanziamento() : null, Boolean.FALSE, null, null);
		ValidatorDto.validateNumber(getRinuncia() != null ? getRinuncia().getIdFinanziamentoRinuncia() : null, Boolean.FALSE, null, null);

		if(getRinuncia() != null) {
			/**
			 * se lo stato è perfezionato per saldo , l'importo rinuncia è obbligatorio
			 */
			if(getStatoFinanziamento()!=null && getStatoFinanziamento().getIdStatoFinanziamento().equals(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue())) {
				if(getRinuncia().getValuta()!=null && getRinuncia().getValuta().equalsIgnoreCase(Constants.DB.VALUTA_EURO)) {
					ValidatorDto.validateDecimalNumber(getRinuncia().getImporto(), Boolean.TRUE, ErrorMessages.MESSAGE_25_IMPORTO_RINUNCIA_OBBLIGATORIO, null);
				}else {
					ValidatorDto.validateNumber(getRinuncia().getImporto(), Boolean.TRUE, ErrorMessages.MESSAGE_25_IMPORTO_RINUNCIA_OBBLIGATORIO, null);
				}
			}
			ValidatorDto.validateLength(getRinuncia().getAttoRinuncia(), Boolean.FALSE, MAX_LENGTH_ATTO_RINUNCIA, null, null, null);
		}

		if(getErogazioni()!= null && !getErogazioni().isEmpty()) {
			for (int i = 0; i < getErogazioni().size(); i++) {
				if(getErogazioni().get(i).getValuta()!=null && getErogazioni().get(i).getValuta().equalsIgnoreCase(Constants.DB.VALUTA_EURO)) {
					ValidatorDto.validateDecimalNumber(getErogazioni().get(i).getImportoErogazione(), Boolean.TRUE, null, null);
				}else {
					ValidatorDto.validateNumber(getErogazioni().get(i).getImportoErogazione(), Boolean.TRUE, null, null);
				}
				ValidatorDto.validateDate(getErogazioni().get(i).getDataDetermina(), Boolean.FALSE, null, null);
				ValidatorDto.validateNullValue(getErogazioni().get(i).getIdTipoErogazione(), Boolean.TRUE, null, null);

				ValidatorDto.validateLength(getErogazioni().get(i).getNumDetermina(), Boolean.FALSE, MAX_LENGTH_NUM_DETERMINA, null, null, null);
			}
		}
				
	}
	
	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("idFinanziamento")
	public Long getIdFinanziamento() {
		return idFinanziamento;
	}
	public void setIdFinanziamento(Long idFinanziamento) {
		this.idFinanziamento = idFinanziamento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("importoFinanziabile")
	public BigDecimal getImportoFinanziabile() {
		return importoFinanziabile;
	}

	public void setImportoFinanziabile(BigDecimal importoFinanziabile) {
		this.importoFinanziabile = importoFinanziabile;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("importoAmmesso")
	public BigDecimal getImportoAmmesso() {
		return importoAmmesso;
	}
	public void setImportoAmmesso(BigDecimal importoAmmesso) {
		this.importoAmmesso = importoAmmesso;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("valuta")
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("fkImportoTettoMax")
	public BigDecimal getFkImportoTettoMax() {
		return fkImportoTettoMax;
	}
	public void setFkImportoTettoMax(BigDecimal fkImportoTettoMax) {
		this.fkImportoTettoMax = fkImportoTettoMax;
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
	@JsonProperty("idLeggeProvvDr")
	public BigDecimal getIdLeggeProvvDr() {
		return idLeggeProvvDr;
	}
	public void setIdLeggeProvvDr(BigDecimal idLeggeProvvDr) {
		this.idLeggeProvvDr = idLeggeProvvDr;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("Percentuale")
	public Percentuale getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(Percentuale percentuale) {
		this.percentuale = percentuale;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("Parere")
	public Parere getParere() {
		return parere;
	}
	public void setParere(Parere parere) {
		this.parere = parere;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("Rendiconto")
	public Rendiconto getRendiconto() {
		return rendiconto;
	}
	public void setRendiconto(Rendiconto rendiconto) {
		this.rendiconto = rendiconto;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("StatoFinanziamento")
	public StatoFinanziamento getStatoFinanziamento() {
		return statoFinanziamento;
	}
	public void setStatoFinanziamento(StatoFinanziamento statoFinanziamento) {
		this.statoFinanziamento = statoFinanziamento;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("StatoFinanziamentoPrec")
	public StatoFinanziamento getStatoFinanziamentoPrec() {
		return statoFinanziamentoPrec;
	}
	public void setStatoFinanziamentoPrec(StatoFinanziamento statoFinanziamentoPrec) {
		this.statoFinanziamentoPrec = statoFinanziamentoPrec;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("Rinuncia")
	public Rinuncia getRinuncia() {
		return rinuncia;
	}
	public void setRinuncia(Rinuncia rinuncia) {
		this.rinuncia = rinuncia;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("Erogazioni")
	public List<Erogazione> getErogazioni() {
		return erogazioni;
	}
	public void setErogazioni(List<Erogazione> erogazioni) {
		this.erogazioni = erogazioni;
	}
	
	  /**
	   **/
	  
	  @ApiModelProperty(value = "")
	  @JsonProperty("PraticaUrbGfu")
	  public PraticaUrbGfu getPraticaUrbGfu() {
	    return praticaUrbGfu;
	  }
	  public void setPraticaUrbGfu(PraticaUrbGfu praticaUrbGfu) {
	    this.praticaUrbGfu = praticaUrbGfu;
	  }


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Finanziamento finanziamento = (Finanziamento) o;
		return Objects.equals(idFinanziamento, finanziamento.idFinanziamento) &&
				Objects.equals(importoFinanziabile, finanziamento.importoFinanziabile) &&
				Objects.equals(importoAmmesso, finanziamento.importoAmmesso) &&
				Objects.equals(valuta, finanziamento.valuta) &&
				Objects.equals(fkImportoTettoMax, finanziamento.fkImportoTettoMax) &&
				Objects.equals(note, finanziamento.note) &&
				Objects.equals(idLeggeProvvDr, finanziamento.idLeggeProvvDr) &&
				Objects.equals(percentuale, finanziamento.percentuale) &&
				Objects.equals(parere, finanziamento.parere) &&
				Objects.equals(rendiconto, finanziamento.rendiconto) &&
				Objects.equals(statoFinanziamento, finanziamento.statoFinanziamento) &&
				Objects.equals(statoFinanziamentoPrec, finanziamento.statoFinanziamentoPrec) &&
				Objects.equals(rinuncia, finanziamento.rinuncia) &&
				Objects.equals(erogazioni, finanziamento.erogazioni) &&
				Objects.equals(praticaUrbGfu, finanziamento.praticaUrbGfu);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idFinanziamento, importoFinanziabile, importoAmmesso, valuta, fkImportoTettoMax, note, idLeggeProvvDr, percentuale, parere, rendiconto, statoFinanziamento, statoFinanziamentoPrec, rinuncia, erogazioni, praticaUrbGfu);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Finanziamento {\n");

		sb.append("    idFinanziamento: ").append(toIndentedString(idFinanziamento)).append("\n");
		sb.append("    importoFinanziabile: ").append(toIndentedString(importoFinanziabile)).append("\n");
		sb.append("    importoAmmesso: ").append(toIndentedString(importoAmmesso)).append("\n");
		sb.append("    valuta: ").append(toIndentedString(valuta)).append("\n");
		sb.append("    fkImportoTettoMax: ").append(toIndentedString(fkImportoTettoMax)).append("\n");
		sb.append("    note: ").append(toIndentedString(note)).append("\n");
		sb.append("    idLeggeProvvDr: ").append(toIndentedString(idLeggeProvvDr)).append("\n");
		sb.append("    percentuale: ").append(toIndentedString(percentuale)).append("\n");
		sb.append("    parere: ").append(toIndentedString(parere)).append("\n");
		sb.append("    rendiconto: ").append(toIndentedString(rendiconto)).append("\n");
		sb.append("    statoFinanziamento: ").append(toIndentedString(statoFinanziamento)).append("\n");
		sb.append("    statoFinanziamentoPrec: ").append(toIndentedString(statoFinanziamentoPrec)).append("\n");
		sb.append("    rinuncia: ").append(toIndentedString(rinuncia)).append("\n");
		sb.append("    erogazioni: ").append(toIndentedString(erogazioni)).append("\n");
		sb.append("    praticaUrbGfu: ").append(toIndentedString(praticaUrbGfu)).append("\n");
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

