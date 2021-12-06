/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;
import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface PercentualeDao {
	public List<Percentuale> findAllPercentuali(Boolean isValid) throws DaoException, SystemException ;
	public Percentuale findPercentualeByPk(Long idPercentuale) throws DaoException, SystemException ;
	public Percentuale createPercentuale(Percentuale percentuale)throws DaoException, SystemException;
	public Percentuale updatePercentuale(Percentuale percentuale) throws DaoException, SystemException;
	
}