/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;

import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface LeggeDao {
	public List<LeggeProvvDr> findAllLeggeProvvDr(String descLeggiProvvDr, Boolean isValid) throws DaoException, SystemException ;
	public LeggeProvvDr findLeggeProvvDrByPk(Long id) throws DaoException, SystemException ;
	public LeggeProvvDr createLeggeProvvDr(LeggeProvvDr leggeProvvDr)throws DaoException, SystemException;
	public LeggeProvvDr updateLeggeProvvDr(LeggeProvvDr leggeProvvDr) throws DaoException, SystemException;
	public List<Legge> findAllLeggi(Boolean isValid) throws DaoException, SystemException ;
	public Legge findLeggeByPk(Long id) throws DaoException, SystemException ;
	public Legge createLegge(Legge legge)throws DaoException, SystemException;
	public Legge updateLegge(Legge legge) throws DaoException, SystemException;
}
