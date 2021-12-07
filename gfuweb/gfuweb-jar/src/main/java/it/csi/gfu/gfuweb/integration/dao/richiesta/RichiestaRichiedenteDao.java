/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.richiesta;


import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RichiestaRichiedenteDao {
	public Long countRichiedentiToRichiesta(Long idRichiesta, Boolean rinuncia) throws DaoException, SystemException;
	public long countUltimaRichiestaRichiedente(Long idRichiesta)throws DaoException, SystemException;
}