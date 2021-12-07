/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto;

import java.util.Objects;
import java.util.Date;
import java.util.List;
import io.swagger.annotations.*;

public class Log
{

  private String level = null;
  private String message = null;
  private String app = null;
  private String fileName = null;
  private String lineNumber = null;
  private Date timestamp = null;
  private List additional = null;
  
	
  @ApiModelProperty(value = "additional")

  public List getAdditional() {
	return additional;
}
public void setAdditional(List additional) {
	this.additional = additional;
}
@ApiModelProperty(value = "fileName")
  public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
@ApiModelProperty(value = "lineNumber")
public String getLineNumber() {
	return lineNumber;
}
public void setLineNumber(String lineNumber) {
	this.lineNumber = lineNumber;
}
@ApiModelProperty(value = "timestamp")
public Date getTimestamp() {
	return timestamp;
}
public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
}
@ApiModelProperty(value = "level")
public String getLevel() {
	return level;
}
public void setLevel(String level) {
    this.level = level;
  }

  
  @ApiModelProperty(value = "message")

  
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  @ApiModelProperty(value = "app")
  public String getApp()
  {
  	return app;
  }
  public void setApp(String app)
  {
  	this.app = app;
  }


  @Override
  public int hashCode() {
    return Objects.hash(level, message, app);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Log {\n");
    
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    app: ").append(toIndentedString(app)).append("\n");
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