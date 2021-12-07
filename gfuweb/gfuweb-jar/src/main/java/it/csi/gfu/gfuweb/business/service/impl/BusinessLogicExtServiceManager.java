/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import org.apache.log4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.parametriappl.ParametroAppl;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbSintetica;
import it.csi.gfu.gfuweb.business.service.BusinessLogicExtService;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.RESTException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.attidetermina.AttiDeterminaDao;
import it.csi.gfu.gfuweb.integration.dao.logAudit.CsiLogAuditDao;
import it.csi.gfu.gfuweb.integration.dao.parametriappl.ParametriApplDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;
import it.csi.gfu.gfuweb.util.rest.RestCaller;
import it.csi.gfu.gfuweb.util.rest.RestResponseErrorHandler;

@Component("businessLogicExtService")
public class BusinessLogicExtServiceManager implements BusinessLogicExtService
{
	protected transient Logger LOG = Logger.getLogger(Constants.LOGGER);

	@Context 
	private ServletContext context;

	@Autowired
	private AttiDeterminaDao attiDeterminaDao;
	@Autowired
	private CsiLogAuditDao csiLogAuditDao;
	@Autowired
	private ParametriApplDao parametroApplDao;

	private ResourceBundle resourceB;

	@Override
	public Determina readDeterminaByNumAndData(String numDetermina, String dataDetermina)
			throws DaoException, SystemException, DatiInputErratiException {
		Determina determina = new Determina();
		LOG.debug("[BusinessLogicExtServiceManager : readDeterminaByNumAndData ] ");

		if(dataDetermina!=null && !dataDetermina.equals("")) {
			ParametroAppl parametroAppl = parametroApplDao.findParametroApplByKey(Constants.DB.PARAMETRI_APPL.KEY.DATA_ULT_DETERMINA);
			
			if(ValidatorDto.isDateAfter(parametroAppl.getValore(),dataDetermina)) {
				determina = attiDeterminaDao.findDeterminaByNumAndData(numDetermina, dataDetermina);
			}else {
				Error error = new Error();
				error.setCode(ErrorMessages.CODE_49_DETERMINA_DATA_ULTIMA_DETERMINA_POSTERIORE);
				error.setMessage(ErrorMessages.MESSAGE_49_DETERMINA_DATA_ULTIMA_DETERMINA_POSTERIORE.replace("$DATA", parametroAppl.getValore()));
				throw new DatiInputErratiException(error) ;
				//TODO 
				/**
				 * CONTATTARE IL SERVIZIO STILO 
				 */
			}
		}
		return determina;
	}

	@Override
	public PraticaUrb readPraticaUrbDetail(String numPratica, RestResponseErrorHandler handler)
			throws SystemException, DatiInputErratiException, RESTException {
		LOG.debug("[BusinessLogicExtServiceManager : readPraticaUrbDetail ] ");
		resourceB= ResourceBundle.getBundle(Constants.CONFIG_API);
		String urlRequestPraurbapi = resourceB.getString(Constants.URL_REQUEST_PRAURBAPI);
		RestCaller rest = new RestCaller(HttpMethod.GET, urlRequestPraurbapi ,  "pratiche/" + numPratica)
				.contentType(MediaType.APPLICATION_JSON)
				.mapObject(false)
				.encodingUTF8(true)
				.responseErrorHandler(handler);				
		PraticaUrb ret = rest.callGeneric(new TypeReference<PraticaUrb>(){}, PraticaUrb.class);
		return ret;
	}


	@Override
	public List<PraticaUrbSintetica> readAllPraticheUrbByFilter(String istatComune, String dataProvvedimentoDa, String dataProvvedimentoA,
			RestResponseErrorHandler handler) throws SystemException, DatiInputErratiException, RESTException {
		LOG.debug("[BusinessLogicExtServiceManager : readAllPraticheUrbByFilter ] ");
		resourceB= ResourceBundle.getBundle(Constants.CONFIG_API);
		String urlRequestPraurbapi = resourceB.getString(Constants.URL_REQUEST_PRAURBAPI);
		RestCaller rest = new RestCaller(HttpMethod.GET, urlRequestPraurbapi ,  "pratiche")
				.contentType(MediaType.APPLICATION_JSON)
				.mapObject(false)
				.responseErrorHandler(handler)
				.queryParam("istatComune", istatComune)
				.queryParam("dataProvvedimentoDa", dataProvvedimentoDa)
				.queryParam("dataProvvedimentoA", dataProvvedimentoA);	
		PraticaUrbSintetica[] ret =	rest.callGeneric(new TypeReference<PraticaUrbSintetica[]>(){}, PraticaUrbSintetica[].class  );
		return Arrays.asList(ret);
	}

	@Override
	public CsiLogAuditDto setParameterCsiLogAudit(String operazione, Object oggOper) {
		LOG.debug("[BusinessLogicExtServiceManager: setParameterCsiLogAudit] " );
		CsiLogAuditDto csiLogAuditDto = new CsiLogAuditDto();
		csiLogAuditDto.setOperazione(operazione);
		csiLogAuditDto.setOggOper(oggOper.toString());
		return csiLogAuditDto;
	}

	@Override
	public void createCsiLogAudit(CsiLogAuditDto csiLogAudit, String shibIdentitaCodiceFiscale) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicExtServiceManager: createCsiLogAudit] " );

		CsiLogAuditDto csiLogAuditDto = new CsiLogAuditDto();

		LocalDateTime now = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(now);
		csiLogAuditDto.setDataOra(timestamp);
		csiLogAuditDto.setIdApp(Constants.ID_APP); 
		csiLogAuditDto.setIpAddress("");
		csiLogAuditDto.setUtente(shibIdentitaCodiceFiscale); 
		csiLogAuditDto.setOperazione(csiLogAudit.getOperazione());
		csiLogAuditDto.setOggOper(csiLogAudit.getOggOper());
		csiLogAuditDto.setKeyOper("");

		csiLogAuditDao.insertCsiLogAudit(csiLogAuditDto);
	}
}