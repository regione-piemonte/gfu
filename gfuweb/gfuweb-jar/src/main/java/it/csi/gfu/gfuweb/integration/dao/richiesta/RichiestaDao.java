/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.richiesta;

import java.util.List;

import it.csi.gfu.gfuweb.dto.excel.RichiesteForExcelRowDto;
import it.csi.gfu.gfuweb.dto.filter.RichiestaFilter;
import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.dto.richiesta.RichiestaToRicercaAvanzata;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RichiestaDao {

	public List<Richiesta> findAllRichieste(Boolean isValid)throws DaoException, SystemException ;
	public Richiesta findRichiestaByPk(Long idRichiesta)throws DaoException, SystemException ;
	public Richiesta createRichiesta(Richiesta richiesta)throws DaoException, SystemException ;
	public Richiesta updateRichiesta(Long idRichiesta, Richiesta richiesta)throws DaoException, SystemException ;
	public void updateRichiestaToFormaAssociativa(Long idRichiesta, Long idAssociazione)throws DaoException, SystemException ;
	public void deleteRichiesta(Long idRichiesta)throws DaoException, SystemException ;
	public List<RichiestaToRicercaAvanzata> findRichiestaByFilter(RichiestaFilter richiesta)throws DaoException, SystemException ;
	public long countUnivocitaNumProtDataProt(Richiesta richiesta)throws DaoException, SystemException ;
	public List<RichiesteForExcelRowDto> findElencoRichiesteExcelByFilter(RichiestaFilter filtro)throws DaoException, SystemException ;
	public void updateFormaAssociativaAtNullToRichiesta(Long idRichiesta)throws DaoException, SystemException ;
	}