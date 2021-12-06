/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;

import it.csi.gfu.gfuweb.dto.utils.ResponseErrorCode;


public class ResponseUtils {

	public static String createJSONResponseMessage(String code, String message) {
		String jsonResult = "";
		ResponseErrorCode ex = new ResponseErrorCode();
		
		ex.setCode(code);
		ex.setMessage(message);

		ObjectMapper om = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try 
		{
			om.writeValue(sw, ex);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			jsonResult = "";
		}
		jsonResult=sw.toString();

		return jsonResult;
	}
}
