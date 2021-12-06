/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.parametriappl;

import it.csi.gfu.gfuweb.dto.parametriappl.ParametroAppl;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ParametriApplDao {
	public ParametroAppl findParametroApplByKey(String key)throws DaoException, SystemException;
}