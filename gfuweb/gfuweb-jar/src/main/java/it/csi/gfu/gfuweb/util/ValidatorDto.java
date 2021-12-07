/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;

public class ValidatorDto
{

	private static String VALORI_POSSIBILI_COD_TIPO_FORMA_ASSOCIATIVA = "("+
			Constants.DB.TIPO_FORMA_ASSOCIATIVA.COD_TIPO.CONVENZIONE+"|"+
			Constants.DB.TIPO_FORMA_ASSOCIATIVA.COD_TIPO.UNIONE_COMUNI_DATI_PIEMONTE+"|"+
			Constants.DB.TIPO_FORMA_ASSOCIATIVA.COD_TIPO.UNIONE_COMUNI_GFU+")";


	/**
	 * Verifica se un campo è vuoto
	 * @param field
	 * @return boolean
	 */
	public static boolean isEmpty(Object field)
	{
		if (field == null || field.toString().length() == 0)
		{
			return true;
		}

		return false;
	}



	public static boolean isDateAfter(String firstDate, String secondDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
		try
		{
			Date dFirstDate = sdf.parse(firstDate);
			Date dSecondDate = sdf.parse(secondDate);

			Boolean isAfterDate = false;
			 if (dFirstDate.compareTo(dSecondDate) > 0) {
	                isAfterDate = true;
	            } else if (dFirstDate.compareTo(dSecondDate) < 0) {
	                isAfterDate = false;
	            } else if (dFirstDate.compareTo(dSecondDate) == 0) {
	                isAfterDate = true;
	            } else {
	            	isAfterDate = false;
	            }
			
			return isAfterDate;
		}
		catch (ParseException e)
		{
			return false;
		}
	}


	public static String addToString(String source, char separator, String toBeInserted) throws DatiInputErratiException {
		int index = source.lastIndexOf(separator); 
		Error error = new Error();
		if(index >= 0 && index<source.length())
			return source.substring(0, index) + toBeInserted + source.substring(index);
		else{throw new DatiInputErratiException(error);}
	}


	public static boolean isDataCompresa(String dataDa, String dataA, String dataDaConfrontare) {
		try
		{
			if(dataDa != null && dataA != null && dataDaConfrontare != null) {
				Date dateDataDa = new SimpleDateFormat("dd/MM/yyyy").parse(dataDa);
				Date dateDataA = new SimpleDateFormat("dd/MM/yyyy").parse(dataA);
				Date dateDataDaConfrontare = new SimpleDateFormat("dd/MM/yyyy").parse(dataDaConfrontare);

				if (dateDataDaConfrontare.compareTo(dateDataDa) >= 0 && dateDataDaConfrontare.compareTo(dateDataA) <= 0) 
					return true;
				else 
					return false;
			}else return false;

		}catch (ParseException e)
		{
			return false;
		}
	}	

	public static String getCurrentDate() {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return sdf.format(date);
	}

	public static void validateCodiceFiscale(String codiceFiscale,Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(codiceFiscale))
		{
			error.setCode(ErrorMessages.CODE_29_PARAMETRO_CODICE_FISCALE_DA_IMPOSTARE);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_29_PARAMETRO_CODICE_FISCALE_DA_IMPOSTARE);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pMail =  Pattern.compile("[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]");
		if (! isEmpty(codiceFiscale))
		{
			if (! pMail.matcher(codiceFiscale).matches())
			{			
				error.setCode(ErrorMessages.CODE_30_PARAMETRO_CODICE_FISCALE_ERRATO);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_30_PARAMETRO_CODICE_FISCALE_ERRATO);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}
	}


	public static void validateLength(String field, Boolean requested, int maxLength, String errorMessageRequested, String errorMessageLength, String errorMessageCode) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && (field == null || field.equals("")))
		{
			error.setCode(! isEmpty(errorMessageCode) ? errorMessageCode : ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		if (! isEmpty(field))
		{		
			if (field.length() > maxLength)
			{
				error.setCode(ErrorMessages.CODE_3_TROPPI_CARATTERI);
				error.setMessage(! isEmpty(errorMessageLength) ? errorMessageLength : ErrorMessages.MESSAGE_3_TROPPI_CARATTERI);
				error.setSuccess(false);

				throw new DatiInputErratiException(error);
			}
		}
	}

	public static void validateNumber(Object number, Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(number))
		{
			error.setCode(ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pNumber =  Pattern.compile("[+-]?[0-9]+");
		if (! isEmpty(number))
		{
			if (! pNumber.matcher(number.toString()).matches())
			{			
				error.setCode(ErrorMessages.CODE_4_PARAMETRO_NUMERO_INTERO_ERRATO);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_4_PARAMETRO_NUMERO_INTERO_ERRATO);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}
	}


	public static void validateNullValue(Object value, Boolean requested,  String errorMessageRequested, String errorMessageCode) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && (value == null || value.equals("")))
		{
			error.setCode(! isEmpty(errorMessageCode) ? errorMessageCode : ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}
	}

	public static void validateDate(String date, Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(date))
		{
			error.setCode(ErrorMessages.CODE_6_PARAMETRO_DATA_DA_IMPOSTARE);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_6_PARAMETRO_DATA_DA_IMPOSTARE);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pDate =  Pattern.compile("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)");
		if (! isEmpty(date))
		{
			if (! pDate.matcher(date).matches())
			{			
				error.setCode(ErrorMessages.CODE_5_PARAMETRO_DATA_ERRATA);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_5_PARAMETRO_DATA_ERRATA);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}
	}

	public static void validateComuniAssociati(List<Comune> comuni, Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException{

		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(comuni))
		{
			error.setCode(ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		if (! isEmpty(comuni))
		{
			if (comuni.size()<2)
			{			
				error.setCode(ErrorMessages.CODE_8_PARAMETRO_COMUNI_MINORE_2);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_8_PARAMETRO_COMUNI_MINORE_2);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}


	}

	public static void validateCodTipoFormaAssociativa(String operation, Boolean requested) throws DatiInputErratiException
	{
		ValidatorDto.validateValoriPossibili(operation, requested, VALORI_POSSIBILI_COD_TIPO_FORMA_ASSOCIATIVA, null, ErrorMessages.CODE_9_PARAMETRO_COD_TIPO_FORMA_ASSOCIATIVA, ErrorMessages.MESSAGE_9_PARAMETRO_COD_TIPO_FORMA_ASSOCIATIVA);
	}


	public static void validateValoriPossibili(String field, Boolean requested, String valoriPossibili, String errorMessageRequested, String errorMessageCode, String errorMessageDescription) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && (field == null || field.equals("")))
		{
			error.setCode(ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pValoriPossibili =  Pattern.compile(valoriPossibili);
		if (! isEmpty(field))
		{
			if (! pValoriPossibili.matcher(field).matches())
			{		
				error.setCode(! isEmpty(errorMessageCode) ? errorMessageCode : ErrorMessages.CODE_10_PARAMETRO_ERRATO);	
				error.setMessage(! isEmpty(errorMessageDescription) ? errorMessageDescription : ErrorMessages.MESSAGE_10_PARAMETRO_ERRATO);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);
			}
		}
	}



	public static void validateDateDaA(String dataDa, String dataA) throws DatiInputErratiException {
		Error error = new Error();
		if(dataA !=null && !dataA.equals("")){
			if(dataDa == null || dataDa.equals("")) {
				error.setCode(ErrorMessages.CODE_6_PARAMETRO_DATA_DA_IMPOSTARE);
				error.setMessage(ErrorMessages.MESSAGE_6_PARAMETRO_DATA_DA_IMPOSTARE);									
				throw new DatiInputErratiException(error);	
			}
		}

	}

	public static void validateEmail(String mailAddress, Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(mailAddress))
		{
			error.setCode(ErrorMessages.CODE_31_PARAMETRO_EMAIL_DA_IMPOSTARE);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_31_PARAMETRO_EMAIL_DA_IMPOSTARE);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pMail =  Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
		if (! isEmpty(mailAddress))
		{
			if (! pMail.matcher(mailAddress).matches())
			{			
				error.setCode(ErrorMessages.CODE_32_PARAMETRO_EMAIL_ERRATA);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_32_PARAMETRO_EMAIL_ERRATA);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}
	}

	public static void validateDecimalNumber(Object number, Boolean requested, String errorMessageRequested, String errorMessageFormat) throws DatiInputErratiException
	{
		Error error = new Error();

		if ((requested != null && requested.booleanValue()) && isEmpty(number))
		{
			error.setCode(ErrorMessages.CODE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setMessage(! isEmpty(errorMessageRequested) ? errorMessageRequested : ErrorMessages.MESSAGE_2_PARAMETRO_CAMPO_OBBLIGATORIO);
			error.setSuccess(false);

			throw new DatiInputErratiException(error);	
		}

		Pattern pNumber =  Pattern.compile("^[0-9]\\d{0,9}(.\\d{1,2})?%?$");
		if (! isEmpty(number))
		{
			if (! pNumber.matcher(number.toString()).matches())
			{			
				error.setCode(ErrorMessages.CODE_34_PARAMETRO_IMPORTO_DECIMALE_ERRATO);
				error.setMessage(! isEmpty(errorMessageFormat) ? errorMessageFormat : ErrorMessages.MESSAGE_34_PARAMETRO_IMPORTO_DECIMALE_ERRATO);	
				error.setSuccess(false);

				throw new DatiInputErratiException(error);				
			}
		}
	}


	public static void validateDateBeforeEuroAndAfterToday(String inDate) throws DatiInputErratiException
	{
		Error error = new Error();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date inputDate = null;
		Date dateEuro;		
		try {
			inputDate = format.parse(inDate);
			dateEuro = format.parse("01/01/2001");
		} catch (ParseException e) {
			error.setCode(ErrorMessages.CODE_5_PARAMETRO_DATA_ERRATA);
			error.setMessage(ErrorMessages.MESSAGE_5_PARAMETRO_DATA_ERRATA);	
			throw new DatiInputErratiException(error);	

		}
		Date today = new Date();
		if(today.before(inputDate))
		{
			error.setCode(ErrorMessages.CODE_42_DATA_NON_VALIDA);
			error.setMessage(ErrorMessages.MESSAGE_42_DATA_NON_VALIDA);									
			throw new DatiInputErratiException(error);	
		}
		if(inputDate.before(dateEuro))
		{
			error.setCode(ErrorMessages.CODE_43_DATA_PASSATA_NON_VALIDA);
			error.setMessage(ErrorMessages.MESSAGE_43_DATA_PASSATA_NON_VALIDA);									
			throw new DatiInputErratiException(error);	
		}

	}

}
