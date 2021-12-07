/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.statofinanziamento;

import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface StatoFinanziamentoDao {

	public StatoFinanziamento findStatoFinanziamento(Long idFinanziamento)throws DaoException, SystemException;
	public void updateStatoFinToFinanziamento(Long idFinanziamento, Long idStatoFinanziamento, Long idStatoFinanziamentoPrec)throws DaoException, SystemException;
	
}