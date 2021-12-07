/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;

import java.util.HashMap;

public class ServerHolder {

	   private static final ThreadLocal<HashMap> contextHolder = new ThreadLocal<HashMap>()
	   {
	        @Override public HashMap initialValue() {
	            return new HashMap();
	        }
	    };
	   

	   public static void setServerMap(HashMap serverMap) {
	       if(serverMap == null){
	           throw new NullPointerException();
	       }
	      contextHolder.set(serverMap);
	   }

	   public static HashMap getServerMap() {
	      return (HashMap) contextHolder.get();
	   }

	   public static void clearServerMap() {
	      contextHolder.remove();
	   }
	   

	}