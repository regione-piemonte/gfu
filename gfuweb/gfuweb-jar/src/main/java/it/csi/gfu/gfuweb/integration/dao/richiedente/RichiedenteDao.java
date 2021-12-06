/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.richiedente;

import java.util.List;

import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.dto.richiedente.RichiedenteProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RichiedenteDao {

	public List<Richiedente> findAllRichiedenti(Long idRichiesta)throws DaoException, SystemException ;
	public Long countRichiedentiToRichiesta(Long idRichiesta)throws DaoException, SystemException ;
	public List<Richiedente> createRichiedente(Long idRichiesta, List<Richiedente> listRichiedente)throws DaoException, SystemException ;
	public Richiedente findRichiedenteByPk(Long idRichiedente)throws DaoException, SystemException ;
	public void deleteRichiedente(Long idRichiesta, Long idRichiedente)throws DaoException, SystemException ;
	public List<RichiedenteProvv> findAllRichiedenteProvv(Long idRichiesta)throws DaoException, SystemException ;
	public void updateRichiedenteProvv(Long idRichiesta, RichiedenteProvv richiedenteProvv)throws DaoException, SystemException ;
	}