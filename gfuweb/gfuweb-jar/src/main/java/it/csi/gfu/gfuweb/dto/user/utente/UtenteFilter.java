/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.user.utente;

import java.util.Objects;
import java.math.BigDecimal;
import io.swagger.annotations.*;

public class UtenteFilter   {
  
  private String cognome = null;
  private String nome = null;
  private String codiceFiscale = null;
  private String eMail = null;
  private BigDecimal idProfilo = null;

  
  @ApiModelProperty(value = "UtenteFilter: cognome")

  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  @ApiModelProperty(value = "UtenteFilter: nome")

  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  @ApiModelProperty(value = "codice fiscale utente")

  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }
  
  @ApiModelProperty(value = "id profilo")

  // nome originario nello yaml: idProfilo 
  public BigDecimal getIdProfilo() {
    return idProfilo;
  }
  public void setIdProfilo(BigDecimal idProfilo) {
    this.idProfilo = idProfilo;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtenteFilter utenteFilter = (UtenteFilter) o;
    return Objects.equals(cognome, utenteFilter.cognome) &&
        Objects.equals(nome, utenteFilter.nome) &&
        Objects.equals(codiceFiscale, utenteFilter.codiceFiscale) &&
        Objects.equals(idProfilo, utenteFilter.idProfilo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cognome, nome, codiceFiscale, eMail, idProfilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtenteFilter {\n");
    
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    eMail: ").append(toIndentedString(eMail)).append("\n");
    sb.append("    idProfilo: ").append(toIndentedString(idProfilo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

