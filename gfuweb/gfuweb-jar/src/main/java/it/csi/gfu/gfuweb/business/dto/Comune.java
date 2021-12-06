/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.dto;

public class Comune {
	
    private String istatComune;
    private String descrizioneComune;
    private String descrizioneProvincia;
    private int numeroAbitanti;
   
	public String getIstatComune() {
		return istatComune;
	}
	public void setIstatComune(String istatComune) {
		this.istatComune = istatComune;
	}
	public String getDescrizioneComune() {
		return descrizioneComune;
	}
	public void setDescrizioneComune(String descrizioneComune) {
		this.descrizioneComune = descrizioneComune;
	}
	public int getNumeroAbitanti() {
		return numeroAbitanti;
	}
	public void setNumeroAbitanti(int numeroAbitanti) {
		this.numeroAbitanti = numeroAbitanti;
	}
	public String getDescrizioneProvincia() {
		return descrizioneProvincia;
	}
	public void setDescrizioneProvincia(String descrizioneProvincia) {
		this.descrizioneProvincia = descrizioneProvincia;
	}
}
