/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/

package it.csi.gfu.gfuweb.business.dto;


import java.util.List;

public class Associazione {
	
    private String istatAssociazione;
    private String descrizioneAssociazione;
    private String codTipoFormaAssociativa;
    private List<Comune> comuni;
    
	public String getIstatAssociazione() {
		return istatAssociazione;
	}
	public void setIstatAssociazione(String istatAssociazione) {
		this.istatAssociazione = istatAssociazione;
	}
	public String getDescrizioneAssociazione() {
		return descrizioneAssociazione;
	}
	public void setDescrizioneAssociazione(String descrizioneAssociazione) {
		this.descrizioneAssociazione = descrizioneAssociazione;
	}
	public String getCodTipoFormaAssociativa() {
		return codTipoFormaAssociativa;
	}
	public void setCodTipoFormaAssociativa(String codTipoFormaAssociativa) {
		this.codTipoFormaAssociativa = codTipoFormaAssociativa;
	}
	public List<Comune> getComuni() {
		return comuni;
	}
	public void setComuni(List<Comune> comuni) {
		this.comuni = comuni;
	}  
	
	
}
