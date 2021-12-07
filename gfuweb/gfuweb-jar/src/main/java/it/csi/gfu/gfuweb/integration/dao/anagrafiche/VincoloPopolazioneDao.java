/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;

import it.csi.gfu.gfuweb.dto.popolazione.VincoloPopolazione;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface VincoloPopolazioneDao {
	public List<VincoloPopolazione> findAllVincoloPopolazione(Boolean isValid) throws DaoException, SystemException ;
	public VincoloPopolazione findVincoloPopolazioneByPk(Long idVincoloPopolazione)throws DaoException, SystemException ;
	public VincoloPopolazione createVincoloPopolazione(VincoloPopolazione vincoloPopolazione)throws DaoException, SystemException ;
	public VincoloPopolazione updateVincoloPopolazione(VincoloPopolazione vincoloPopolazione)throws DaoException, SystemException ;

}