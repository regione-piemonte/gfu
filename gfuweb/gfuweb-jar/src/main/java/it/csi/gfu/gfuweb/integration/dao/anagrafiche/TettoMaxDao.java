/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;
import it.csi.gfu.gfuweb.dto.tetto.TettoMax;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface TettoMaxDao {

	public List<TettoMax> findAllTettoMax(Boolean isValid)throws DaoException, SystemException ;
	public TettoMax findTettoMaxByPk(Long idTettoMax)throws DaoException, SystemException ;
	public TettoMax createTettoMax(TettoMax tettoMax)throws DaoException, SystemException ;
	public TettoMax updateTettoMax(TettoMax tettoMax)throws DaoException, SystemException ;
	public void deleteTettoMaxByPk(Long idTettoMax)throws DaoException, SystemException ;
	public TettoMaxTotRichiedenti findTettoMaxTotRichiedenti(Long idRichiesta, Long idLeggeProvDr)throws DaoException, SystemException ;
	public void updateTettoMaxAttivoToDisattivo() throws DaoException, SystemException;

}