/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto.user.utente;

import java.util.Objects;
import io.swagger.annotations.*;

public class UtenteResult   {
   
  private Boolean success = null;
  private Utente fullUtenteDto = null;
  
  @ApiModelProperty(value = "")

  public Boolean isSuccess() {
    return success;
  }
  public void setSuccess(Boolean success) {
    this.success = success;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtenteResult utenteResult = (UtenteResult) o;
    return Objects.equals(success, utenteResult.success)&&
    		Objects.equals(fullUtenteDto, utenteResult.fullUtenteDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, fullUtenteDto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UtenteResult {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    fullUtenteDto: ").append(toIndentedString(fullUtenteDto)).append("\n");
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
  public Utente getFullUtenteDto() {
	return fullUtenteDto;
  }
  public void setFullUtenteDto(Utente fullUtenteDto) {
	this.fullUtenteDto = fullUtenteDto;
  }
}

