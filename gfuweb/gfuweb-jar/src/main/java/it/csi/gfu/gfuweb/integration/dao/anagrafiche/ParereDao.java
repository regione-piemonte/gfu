/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;
import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ParereDao {
	public List<Parere> findAllPareri(Boolean isValid) throws DaoException, SystemException ;
	public Parere findParereByPk(Long id) throws DaoException, SystemException ;
	public Parere createParere(Parere parere)throws DaoException, SystemException;
	public Parere updateParere(Parere parere) throws DaoException, SystemException;
	
}