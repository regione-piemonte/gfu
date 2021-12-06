/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ProfiloDao {

	public Profilo findProfiloById(BigDecimal idProfilo) throws DaoException, SystemException;
	public List<Profilo> findProfilo() throws DaoException, SystemException;

}
