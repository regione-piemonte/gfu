/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;


public class DbContextHolder {

	   private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<DbType>();

	   public static void setDbType(DbType dbType) {
	       if(dbType == null){
	           throw new NullPointerException();
	       }
	      contextHolder.set(dbType);
	   }

	   public static DbType getDbType() {
	      return (DbType) contextHolder.get();
	   }

	   public static void clearDbType() {
	      contextHolder.remove();
	   }
	   
	   public enum DbType {  
		   DEV,
		   TST,
		   DEMO,
		   COLL,
		   PROD
		}

	}