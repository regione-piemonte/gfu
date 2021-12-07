/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

public class LogResult 
{
  
  private Boolean success = null;
  private Log log = null;
  
  @ApiModelProperty(value = "")

  public Boolean isSuccess()
  {
    return success;
  }
  public void setSuccess(Boolean success)
  {
    this.success = success;
  }

	public Log getLog()
	{
		return log;
	}
	
	public void setLog(Log log)
	{
		this.log = log;
	}

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    LogResult logResult = (LogResult) o;
    return Objects.equals(success, logResult.success);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(success);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogResult {\n");
    
    sb.append("success: ").append(toIndentedString(success)).append("\n");
    sb.append("log: ").append(toIndentedString(log)).append("\n");
    sb.append("}");
  
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o)
  {
    if (o == null)
    {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}