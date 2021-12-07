/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;


public class Constants
{
	public final static String COMPONENT_NAME = "gfuweb";
	public final static String  IRIDE_ID_SESSIONATTR = "IRIDE_ID_SESSIONATTR";
	public static final String ID_APP = "GFU_RP_01_GFUWEB";

	public static final String DEV = "dev";
	public static final String TST = "tst";
	public static final String COLL = "coll";
	public static final String PROD = "prod";
	public static final String DEMO = "demo";
	public static final String REGP = "regp";

	public static final String DEPLOY_ENV_PREPROD = "PREPROD";
	public static final String DEPLOY_ENV_PROD = "PROD";
	public static final String DEPLOY_ENV_LOCAL = "LOCAL";

	public static final String LOGGER = "gfuweb";
	public static final String LINEA_CLIENTE_RP = "rp-01";

	public static final String PROD_DS = "dataSourceProd";
	public static final String TEST_DS = "dataSourceTest";
	
	public static final String CONFIG_API = "configApi";
	public static final String URL_REQUEST_PRAURBAPI = "urlRequestPraurbapi";

	public static final String TIPO_FORMA_ASSOCIATIVA_UCDP = "UCDP";

	public static class PARAMETER
	{
		public static class REG_PIE
		{
			public static String ISTAT_REGIONE_01 = "01";
			public static int R_STATUS_1 = 1;
			public static String D_STOP_9999 = "31/12/9999";
		}
	}

	public static class DB
	{
		public static class TIPO_FORMA_ASSOCIATIVA
		{
			public static class COD_TIPO
			{
				public static final String UNIONE_COMUNI_DATI_PIEMONTE = "UCDP";
				public static final String CONVENZIONE = "CONV";
				public static final String UNIONE_COMUNI_GFU = "UCGFU";
			}
		}

		public static class STATO_FINANZIAMENTO
		{
			public static class COD_STATO_FINANZIAMENTO
			{
				public static final String IN_CORSO = "INCORSO";
				public static final String SOSPESO = "SOSP";
				public static final String PERFEZIONATO_PER_ACCONTO = "PERFACC";
				public static final String ACCONTO_EROGATO = "ACCEROG";
				public static final String PERFEZIONATO_PER_SALDO = "PERFSAL";
				public static final String CONCLUSO = "CONCL";
				public static final String RESPINTO = "RESP";
				public static final String REVOCATO = "REVOC";
				public static final String ANNULLATO_PER_RINUNCIA = "ANNXRIN";
				public static final String VALIDATO = "VALID";
			}

			public static class ID_STATO_FINANZIAMENTO
			{
				public static final Integer IN_CORSO = 1;
				public static final Integer SOSPESO = 2;
				public static final Integer PERFEZIONATO_PER_ACCONTO = 3;
				public static final Integer ACCONTO_EROGATO = 4;
				public static final Integer PERFEZIONATO_PER_SALDO = 5;
				public static final Integer CONCLUSO = 6;
				public static final Integer RESPINTO = 7;
				public static final Integer REVOCATO = 8;
				public static final Integer ANNULLATO_PER_RINUNCIA = 9;
				public static final Integer VALIDATO = 10;

				public static final Integer RIPRISTINA_FINANZIAMENTO = 0;
			}					
		}
		public static class TIPO_EROGAZIONE
		{
			public static class ID_TIPO_EROGAZIONE
			{
				public static final Integer ACCONTO = 1;
				public static final Integer SALDO = 2;
			}
		}
		
		public static class PARAMETRI_APPL
		{
			public static class KEY
			{
				public static final String DATA_ULT_DETERMINA = "DATA_ULT_DETERMINA";
			}
		}

		public static final String VALUTA_EURO = "Euro";
	}

	public static class EXCEL_RICERCA_AVANZATA
	{
		public static class INTESTAZIONE
		{		
			public static String[] PRIMA_RIGA = new String[] {"N. PROT.", "DATA PROT.", "RICHIEDENTE", "TIPO RICHIEDENTE", "COMUNE",
					"PROV.","POPOLAZ.","RINUNCIA","LEGGE - PROVVEDIMENTO - D.R","FINANZIABILE TOT.","AMMESSO TOT.","IMPORTO MAX TOT.",
					"PARERE","RENDICONTO","NUM. ATTO APPROVAZIONE URB.","DATA ATTO APPROVAZIONE URB.","STATO FINANZ.", "IMPORTO TOT. RINUNCIA","ATTO RINUNCIA","NOTE",
					"ACCONTO TOT.","NUM.DETERMINA ACC.","DATA DETERMINA ACC.","SALDO TOT.","NUM.DETERMINA SALDO","DATA DETERMINA SALDO", "NUM. PRATICA URB."};			
		}
	}
}