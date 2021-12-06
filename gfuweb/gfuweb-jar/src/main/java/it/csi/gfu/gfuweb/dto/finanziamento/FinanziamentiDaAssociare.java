/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.finanziamento;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import io.swagger.annotations.*;


public class FinanziamentiDaAssociare   {
  
  private BigDecimal idFinanziamento = null;

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("idFinanziamento")
  public BigDecimal getIdFinanziamento() {
    return idFinanziamento;
  }
  public void setIdFinanziamento(BigDecimal idFinanziamento) {
    this.idFinanziamento = idFinanziamento;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FinanziamentiDaAssociare finanziamentiDaAssociare = (FinanziamentiDaAssociare) o;
    return Objects.equals(idFinanziamento, finanziamentiDaAssociare.idFinanziamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idFinanziamento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FinanziamentiDaAssociare {\n");
    
    sb.append("    idFinanziamento: ").append(toIndentedString(idFinanziamento)).append("\n");
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

