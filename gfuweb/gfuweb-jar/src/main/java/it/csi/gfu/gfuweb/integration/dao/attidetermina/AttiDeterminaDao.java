/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.attidetermina;

import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface AttiDeterminaDao {
	public Long countDeterminaByNumAndDataDetermina(String numDetermina, String dataDetermina)throws DaoException, SystemException ;
	public Determina findDeterminaByNumAndData(String numDetermina, String dataDetermina)throws DaoException, SystemException ;
}