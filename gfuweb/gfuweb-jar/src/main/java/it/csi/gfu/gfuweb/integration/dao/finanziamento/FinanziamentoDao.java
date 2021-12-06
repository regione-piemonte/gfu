/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.finanziamento;

import java.util.List;

import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface FinanziamentoDao {
	
	public Long countAssociazioneFinanziamentoWithStatoNotInCorso(Long idRichiesta, Long idLeggeProvvDr) throws DaoException, SystemException;
	public Finanziamento findFinanziamentoToProvRich(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException ;
	public Finanziamento createFinanziamento(Long idRichiesta, Long idLeggeProvvDr, Finanziamento finanziamento)throws DaoException, SystemException ;
	public Finanziamento findFinanziamentoByPk(Long idFinanziamento)throws DaoException, SystemException ;
	public Finanziamento updateFinanziamento(Long idFinanziamento, Finanziamento finanziamento)throws DaoException, SystemException ;
	public List<RichiestaProvv> findAllProvvFinToRichiesta(Long idStatoFinanziamento, Long idLeggeProvvDr,
			String dataProtRichiestaDa, String dataProtRichiestaA)throws DaoException, SystemException ;
	public void deleteFinanziamento(Long idFinanziamento)throws DaoException, SystemException;
	public Long countFinanziamentoConFlagRinunciaTrue(Long idFinanziamento) throws DaoException, SystemException;
	public void updateFkFinanziamentoRinunciaToNull(Long idFinanziamento)throws DaoException, SystemException;
	
}