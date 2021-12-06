/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao;

import java.util.List;

import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface UtenteDao {

	public List<Utente>  findUtenteByFilter(UtenteFilter utenteFilter) throws DaoException, SystemException;
	public List<Utente> findAllUtenti()throws DaoException, SystemException;
	public Utente findUtenteByPk(Long idUtente)throws DaoException, SystemException;
	public Utente createUtente(Utente utente)throws DaoException, SystemException;
	public Utente updateUtente(Utente utente)throws DaoException, SystemException;
	public void deleteUtenteByPk(Long idUtente)throws DaoException, SystemException;
	public Utente findUtenteAuth(UtenteFilter utenteFilter) throws DaoException, SystemException;
}
