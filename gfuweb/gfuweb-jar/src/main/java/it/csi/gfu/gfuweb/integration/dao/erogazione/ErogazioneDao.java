/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.erogazione;

import java.util.List;

import it.csi.gfu.gfuweb.dto.erogazione.DeterminaToErogazioni;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ErogazioneDao {

	public List<Erogazione> findAllErogazioniToFinanziamento(Long idFinanziamento)throws DaoException, SystemException;
	public void createErogazione(Long idFinanziamento, Erogazione erogazione)throws DaoException, SystemException;
	public void updateAllErogazioniToDetermina(DeterminaToErogazioni determinaToErogazioni)throws DaoException, SystemException ;
	public void updateErogazione(Long idFinanziamento,Long idErogazione, Erogazione erogazione)throws DaoException, SystemException ;
}