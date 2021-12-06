/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.business.service;

import java.util.List;

import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrb;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbSintetica;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.RESTException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.rest.RestResponseErrorHandler;

public interface BusinessLogicExtService
{
	public Determina readDeterminaByNumAndData(String numDetermina, String dataDetermina)throws DaoException, SystemException,DatiInputErratiException;
	public CsiLogAuditDto setParameterCsiLogAudit(String operazione, Object oggOper);
	public void createCsiLogAudit(CsiLogAuditDto csiLogAuditDto, String shibIdentitaCodiceFiscale)throws DaoException, SystemException;
	public PraticaUrb readPraticaUrbDetail(String numPratica, RestResponseErrorHandler handler)throws SystemException, DatiInputErratiException, RESTException;
	public List<PraticaUrbSintetica> readAllPraticheUrbByFilter(String istatComune, String dataProvvedimentoDa, String dataProvvedimentoA,	RestResponseErrorHandler handler)throws SystemException, DatiInputErratiException, RESTException;

}