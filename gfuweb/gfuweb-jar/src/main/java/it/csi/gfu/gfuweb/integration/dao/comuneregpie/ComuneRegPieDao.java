/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.comuneregpie;

import java.util.List;

import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ComuneRegPieDao {
	public List<Comune> findAllComuni(String descComune, Boolean isValid) throws DaoException, SystemException ;
	public Comune findComuneByPk(String codIstat) throws DaoException, SystemException ;
}