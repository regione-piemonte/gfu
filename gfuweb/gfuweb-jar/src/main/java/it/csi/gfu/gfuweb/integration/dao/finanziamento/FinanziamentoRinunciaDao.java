/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.finanziamento;


import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.rinuncia.Rinuncia;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface FinanziamentoRinunciaDao {
	
	public Rinuncia createFinanziamentoRinuncia(Rinuncia rinuncia)throws DaoException, SystemException;
	public void updateFinanziamentoRinuncia(Finanziamento finanziamento)throws DaoException, SystemException;
	public void deleteFinanziamentoRinuncia(Long idFinanziamentoRinuncia)throws DaoException, SystemException;
	
}