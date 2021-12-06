/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.leggeprovvdr;

import java.util.List;

import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface LeggeProvvDrDao {
	public List<LeggeProvvDr> findAllProvvedimentiToRichiesta(Long idRichiesta)throws DaoException, SystemException;
	public long countLeggeProvvDrValid(Long idLeggeProvvDr)throws DaoException, SystemException;
	public long countUnivocitaLeggeProvvDr(Integer idLegge, Integer idProvvedimento, Integer idDr, Long idLeggeProvvDr)throws DaoException, SystemException;
	public void findAndUpdateLeggeProvvDr(Long idLegge, Long idProvvedimento, Long idDr) throws DaoException, SystemException;
}