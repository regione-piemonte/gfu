/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.associazione;

import java.util.List;

import it.csi.gfu.gfuweb.dto.associazione.Associazione;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface AssociazioneRegPieDao {
	public List<Associazione> findAllAssociazioni(String descAssociazione, String tipoFormaAss, Boolean isValid)throws DaoException, SystemException ;
	public List<Associazione> findAssociazioneByPk(Long idAssociazione)throws DaoException, SystemException ;
	public Associazione createAssociazione(Associazione associazione)throws DaoException, SystemException ;
}