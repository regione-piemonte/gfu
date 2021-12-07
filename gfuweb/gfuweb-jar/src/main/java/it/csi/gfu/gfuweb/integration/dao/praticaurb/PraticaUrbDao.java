/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.praticaurb;

import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface PraticaUrbDao {

	public PraticaUrbGfu createPraticaUrbGfu(Long idFinanziamento, PraticaUrbGfu praticaUrbGfu)throws DaoException, SystemException ;
	public void deletePraticaUrbGfu(Long idFinanziamento)throws DaoException, SystemException ;
	public long countUnivocitaFinanziamento(Long idFinanziamento)throws DaoException, SystemException;

}