/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;
public class ErrorMessages
{

	public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	public static final String NO_CONTENT = "No Content";
	public static final String NOT_FOUND = "Not Found";
	public static final String CODE_1_CHIAVE_DUPLICATA = "1";	
	public static final String MESSAGE_1_CHIAVE_DUPLICATA = "Duplicated Key";	
	public static final String BAD_REQUEST = "Bad Request";

	public static final String CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO = "2";
	public static final String MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO = "Campo obbligatorio";

	public static final String CODE_3_TROPPI_CARATTERI = "3";
	public static final String MESSAGE_3_TROPPI_CARATTERI = "Sono stati inseriti troppi caratteri";

	public static final String CODE_4_PARAMETRO_NUMERO_INTERO_ERRATO = "4";
	public static final String MESSAGE_4_PARAMETRO_NUMERO_INTERO_ERRATO = "Il campo deve essere un numero intero";

	public static final String CODE_5_PARAMETRO_DATA_ERRATA = "5";
	public static final String MESSAGE_5_PARAMETRO_DATA_ERRATA = "La data non e' nel formato corretto dd/mm/yyyy";

	public static final String CODE_6_PARAMETRO_DATA_DA_IMPOSTARE = "6";
	public static final String MESSAGE_6_PARAMETRO_DATA_DA_IMPOSTARE = "La data da non e' stata impostata";

	public static final String CODE_7_RICERCA_NON_HA_PRODOTTO_RISULTATI = "7";
	public static final String MESSAGE_7_RICERCA_NON_HA_PRODOTTO_RISULTATI = "La ricerca non ha prodotto risultati";

	public static final String CODE_8_PARAMETRO_COMUNI_MINORE_2 = "8";
	public static final String MESSAGE_8_PARAMETRO_COMUNI_MINORE_2 = "Devono essere presenti due o piu' comuni selezionati";

	public static final String CODE_9_PARAMETRO_COD_TIPO_FORMA_ASSOCIATIVA = "9";
	public static final String MESSAGE_9_PARAMETRO_COD_TIPO_FORMA_ASSOCIATIVA = "Parametro non valido. Valori possibili: CONV|UCGFU|UCDP";

	public static final String CODE_10_PARAMETRO_ERRATO = "10";
	public static final String MESSAGE_10_PARAMETRO_ERRATO = "Il parametro inserito non e' consentito";

	public static final String CODE_11_CONSTRAINT_VIOLATED = "11";
	public static final String MESSAGE_11_CONSTRAINT_VIOLATED = "Associazione non presente";

	public static final String CODE_12_RICHIEDENTI_ASSOCIATI_RICHIESTA = "12";
	public static final String MESSAGE_12_RICHIEDENTI_ASSOCIATI_RICHIESTA = "esistono dei richiedenti associati a questa richiesta. Non e' possibile modificare la Forma Associativa";

	public static final String CODE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA = "13";
	public static final String MESSAGE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA = "Non e' possibile inserire o eliminare il richiedente in quanto esiste almeno un finanziamento con stato successivo a in corso";

	public static final String CODE_14_PROVVEDIMENTO_CON_FINANZIAMENTO_PRESENTE_PER_COMUNE = "14";
	public static final String MESSAGE_14_PROVVEDIMENTO_CON_FINANZIAMENTO_PRESENTE_PER_COMUNE = "Il Comune ha già presentato una richiesta di finanziamento per il tipo di provvedimento scelto";

	public static final String CODE_15_PROVVEDIMENTO_CON_FINANZIAMENTO_ASSOCIATO = "15";
	public static final String MESSAGE_15_PROVVEDIMENTO_CON_FINANZIAMENTO_ASSOCIATO = "Non è possibile cancellare il provvedimento: lo stato del finanziamento associato non lo consente.";

	public static final String CODE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO = "16";
	public static final String MESSAGE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO = "Comune gia' presente per il provvedimento";

	public static final String CODE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO = "17";
	public static final String MESSAGE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO = "La richiesta risulta essere gia' presente per il provvedimento.";

	public static final String CODE_18_RICHIESTA_SENZA_RICHIEDENTI_ASSOCIATI = "18";
	public static final String MESSAGE_18_RICHIESTA_SENZA_RICHIEDENTI_ASSOCIATI = "Non e' possibile aggiungere provvedimenti in quanto non esistono richiedenti associati.";

	public static final String CODE_19_ASSOCIAZIONE_RICHIESTA_PROVV_INESISTENTE = "19";
	public static final String MESSAGE_19_ASSOCIAZIONE_RICHIESTA_PROVV_INESISTENTE = "Non esiste nessuna associazione tra richiesta e provvedimento.";

	public static final String CODE_20_FINANZIAMENTO_INESISTENTE = "20";
	public static final String MESSAGE_20_FINANZIAMENTO_INESISTENTE = "Finanziamento inesistente.";

	public static final String CODE_21_VALIDAZIONE_NON_EFFETTUATA_DOCUMENTAZIONE_NON_PRESENTE = "21";
	public static final String MESSAGE_21_VALIDAZIONE_NON_EFFETTUATA_DOCUMENTAZIONE_NON_PRESENTE = "Non e' possibile validare il finanziamento in quanto per il provvedimento selezionato non tutti i richiedenti hanno presentato la documentazione.";

	public static final String CODE_22_VALIDAZIONE_SENZA_IMPORTO_AMMESSO = "22";
	public static final String MESSAGE_22_VALIDAZIONE_SENZA_IMPORTO_AMMESSO = "Validazione non consentita in quanto non e' presente l'importo ammesso";

	public static final String CODE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO= "23";
	public static final String MESSAGE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO = "Lo stato del finanziamento non consente di effettuare l'operazione.";

	public static final String CODE_24_RINUNCIA_DOCUMENTAZIONE_FINANZIAMENTO_PROVV_NON_AMMESSO= "24";
	public static final String MESSAGE_24_RINUNCIA_DOCUMENTAZIONE_FINANZIAMENTO_PROVV_NON_AMMESSO = "Lo stato del finanziamento non consente di modificare la rinuncia e la documentazione.";

	public static final String CODE_25_IMPORTO_RINUNCIA_OBBLIGATORIO = "25";
	public static final String MESSAGE_25_IMPORTO_RINUNCIA_OBBLIGATORIO = "E' obbligatorio inserire l'importo rinuncia";

	public static final String CODE_27_NUM_E_DATA_PROT_GIA_PRESENTE= "27";
	public static final String MESSAGE_27_NUM_E_DATA_PROT_GIA_PRESENTE = "Risulta gia' presente una richiesta con questo numero e data protocollo.";

	public static final String CODE_28_DOCUMENTAZIONE_NON_MODIFICABILE = "28";
	public static final String MESSAGE_28_DOCUMENTAZIONE_NON_MODIFICABILE = "Non e' piu' possibile modificare la documentazione in quanto lo stato del finanziamento non lo consente.";

	public static final String CODE_29_PARAMETRO_CODICE_FISCALE_DA_IMPOSTARE = "29";
	public static final String MESSAGE_29_PARAMETRO_CODICE_FISCALE_DA_IMPOSTARE = "Il codice fiscale non e' stato impostato";

	public static final String CODE_30_PARAMETRO_CODICE_FISCALE_ERRATO = "30";
	public static final String MESSAGE_30_PARAMETRO_CODICE_FISCALE_ERRATO = "Il codice fiscale non risulta corretto.";

	public static final String CODE_31_PARAMETRO_EMAIL_DA_IMPOSTARE = "31";
	public static final String MESSAGE_31_PARAMETRO_EMAIL_DA_IMPOSTARE = "L'indirizzo email non e' stata impostato";

	public static final String CODE_32_PARAMETRO_EMAIL_ERRATA = "32";
	public static final String MESSAGE_32_PARAMETRO_EMAIL_ERRATA = "L'indirizzo email non e' nel formato corretto xxx@xxx.xxx";

	public static final String CODE_33_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO = "33";
	public static final String MESSAGE_33_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO = "Il Comune ha gia' presentato una richiesta di finanziamento per il tipo di provvedimento scelto";

	public static final String CODE_34_PARAMETRO_IMPORTO_DECIMALE_ERRATO = "34";
	public static final String MESSAGE_34_PARAMETRO_IMPORTO_DECIMALE_ERRATO = "Il campo deve essere un numero decimale ";

	public static final String CODE_35_DOCUMENTAZIONE_NON_MODIFICABILE_STATO_NOT_IN_CORSO_OR_SOSPESO = "35";
	public static final String MESSAGE_35_DOCUMENTAZIONE_NON_MODIFICABILE_STATO_NOT_IN_CORSO_OR_SOSPESO = "Non e' piu' possibile modificare la documentazione in quanto lo stato del finanziamento non lo permette.";

	public static final String CODE_36_RIPRISTINA_NON_POSSIBILE_DOCUMENTAZIONE_NON_PRESENTE = "36";
	public static final String MESSAGE_36_RIPRISTINA_NON_POSSIBILE_DOCUMENTAZIONE_NON_PRESENTE = "Non e' possibile ripristinare il finanziamento in VALIDATO in quanto per il provvedimento selezionato non tutti i richiedenti hanno presentato la documentazione.";

	public static final String CODE_37_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO_RIPRISTINA_FIN = "37";
	public static final String MESSAGE_37_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO_RIPRISTINA_FIN = "Non e' piu' possibile ripristinare il finanziamento in quanto il Comune ha gia' presentato una richiesta per il tipo di provvedimento scelto";

	public static final String CODE_39_IMPORTO_RINUNCIA_OBBLIGATORIO = "39";
	public static final String MESSAGE_39_IMPORTO_RINUNCIA_OBBLIGATORIO = "Il campo importo rinuncia deve essere valorizzato.";

	public static final String CODE_40_RINUNCIA_ULTIMO_COMUNE_ACCONTO_EROGATO = "40";
	public static final String MESSAGE_40_RINUNCIA_ULTIMO_COMUNE_ACCONTO_EROGATO = "Non possono rinunciare tutti i Comuni in quanto l'acconto risulta già erogato. Occorre revocare il provvedimento .";

	public static final String CODE_41_FINANZIAMENTO_SENZA_IMPORTO_RINUNCIA = "41";
	public static final String MESSAGE_41_FINANZIAMENTO_SENZA_IMPORTO_RINUNCIA = "Salvataggio non consentito in quanto esiste almeno un Comune che ha rinunciato al finanziamento e non risulta inserito nessun importo rinuncia.";

	public static final String CODE_42_DATA_NON_VALIDA = "42";
	public static final String MESSAGE_42_DATA_NON_VALIDA = "Non e' possibile impostare una data futura.";

	public static final String CODE_43_DATA_PASSATA_NON_VALIDA = "43";
	public static final String MESSAGE_43_DATA_PASSATA_NON_VALIDA = "Non e' possibile impostare una data precedente al 2001.";

	public static final String CODE_44_FINANZIAMENTO_PRATICA_URB_GIA_PRESENTE= "44";
	public static final String MESSAGE_44_FINANZIAMENTO_PRATICA_URB_GIA_PRESENTE = "Risulta gia' presente una pratica associata al finanziamento corrente.";

	public static final String CODE_45_LEGGE_PROVV_DR_GIA_PRESENTE= "45";
	public static final String MESSAGE_45_LEGGE_PROVV_DR_GIA_PRESENTE = "Risulta gia' presente una Legge con questo Provvedimento e D.R.";

	public static final String CODE_46_RICHIEDENTI_ASSOCIATI_RICHIESTA_MODIFICA_PROTOCOLLO_NON_AMMESSO = "46";
	public static final String MESSAGE_46_RICHIEDENTI_ASSOCIATI_RICHIESTA_MODIFICA_PROTOCOLLO_NON_AMMESSO = "Esistono dei richiedenti associati a questa richiesta. Non e' piu' possibile modificare la richiesta";

	public static final String CODE_48_LEGGE_PROVV_DR_NON_VALIDA = "48";
	public static final String MESSAGE_48_LEGGE_PROVV_DR_NON_VALIDA = "Non e' possibile legare la richiesta al provvedimento scelto in quanto non risulta attivo.";

	public static final String CODE_49_DETERMINA_DATA_ULTIMA_DETERMINA_POSTERIORE = "49";
	public static final String MESSAGE_49_DETERMINA_DATA_ULTIMA_DETERMINA_POSTERIORE = "Le determine successive al $DATA non sono presenti in Stilo. E' comunque possibile procedere con l'associazione.";


}
