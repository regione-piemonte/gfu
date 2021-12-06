/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.dto;

import java.util.Objects;
import org.codehaus.jackson.annotate.JsonProperty;
import io.swagger.annotations.*;


public class Error   {

	private Boolean success = null;
	private String code = null;
	private String message = null;

	@ApiModelProperty(value = "")
	@JsonProperty("success") 

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("code") 

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 **/

	@ApiModelProperty(value = "")
	@JsonProperty("message") 

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}




	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Error error = (Error) o;
		return Objects.equals(success, error.success) &&
				Objects.equals(code, error.code) &&
				Objects.equals(message, error.message) ;
	}

	@Override
	public int hashCode() {
		return Objects.hash(success, code, message);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Error {\n");

		sb.append("    success: ").append(toIndentedString(success)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

