/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;
import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface DrDao {
	public List<Dr> findAllDr(Boolean isValid) throws DaoException, SystemException ;
	public Dr findDrByPk(Long id) throws DaoException, SystemException ;
	public Dr createDr(Dr dr)throws DaoException, SystemException;
	public Dr updateDr(Dr dr) throws DaoException, SystemException;
	public List<TipoDr> findAllTipiDr() throws DaoException, SystemException ;
}
