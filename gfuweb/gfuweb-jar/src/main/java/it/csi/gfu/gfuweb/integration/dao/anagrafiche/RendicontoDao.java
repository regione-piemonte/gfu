/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;
import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RendicontoDao {
	public List<Rendiconto> findAllRendiconti(Boolean isValid) throws DaoException, SystemException ;
	public Rendiconto findRendicontoByPk(Long idRendiconto) throws DaoException, SystemException ;
	public Rendiconto createRendiconto(Rendiconto rendiconto)throws DaoException, SystemException;
	public Rendiconto updateRendiconto(Rendiconto rendiconto) throws DaoException, SystemException;

}