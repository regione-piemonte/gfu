/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto;

import java.util.Objects;
import java.math.BigDecimal;
import io.swagger.annotations.*;



public class Profilo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private BigDecimal idProfilo = null;
  private String descrizioneProfilo = null;
  private String codProfilo = null;

  /**
   * Id univoco del profilo
   **/
  
  @ApiModelProperty(value = "Id univoco del profilo")

  // nome originario nello yaml: idProfilo 
  public BigDecimal getIdProfilo() {
    return idProfilo;
  }
  public void setIdProfilo(BigDecimal idProfilo) {
    this.idProfilo = idProfilo;
  }

  /**
   * descrizione del profilo
   **/
  
  @ApiModelProperty(value = "descrizione del profilo")

  // nome originario nello yaml: descrizioneProfilo 
  public String getDescrizioneProfilo() {
    return descrizioneProfilo;
  }
  public void setDescrizioneProfilo(String descrizioneProfilo) {
    this.descrizioneProfilo = descrizioneProfilo;
  }

  /**
   * codice esteso
   **/
  
  @ApiModelProperty(value = "codice profilo")

  // nome originario nello yaml: codProfilo 
  public String getCodProfilo() {
    return codProfilo;
  }
  public void setCodProfilo(String codProfilo) {
    this.codProfilo = codProfilo;
  }


  


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(idProfilo, profilo.idProfilo) &&
        Objects.equals(descrizioneProfilo, profilo.descrizioneProfilo) &&
        Objects.equals(codProfilo, profilo.codProfilo) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idProfilo, descrizioneProfilo, codProfilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");
    
    sb.append("    idProfilo: ").append(toIndentedString(idProfilo)).append("\n");
    sb.append("    descrizioneProfilo: ").append(toIndentedString(descrizioneProfilo)).append("\n");
    sb.append("    codProfilo: ").append(toIndentedString(codProfilo)).append("\n");

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

