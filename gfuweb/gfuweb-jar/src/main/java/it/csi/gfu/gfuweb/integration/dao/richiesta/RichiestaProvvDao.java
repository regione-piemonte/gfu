/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.richiesta;


import java.util.List;

import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RichiestaProvvDao {
	public Long countAssociazioneRichiestaProvv(Long idRichiesta, Long idLeggeProvvDr) throws DaoException, SystemException;
	public List<RichiestaProvv> findAllRichiestaProvvToRichiesta(Long idRichiesta) throws DaoException, SystemException;
	public void updateFkFinanziamento(Long fkFinanziamento,Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException;
	public List<Long> findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException;
	public void deleteRichiestaProvv(Long idRichiesta)throws DaoException, SystemException;
}