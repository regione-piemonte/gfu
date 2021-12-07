/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.richiedente;

import java.math.BigDecimal;
import java.util.List;

import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface RichiedenteProvvDao {

	public void createRichiedenteProvv(List<RichiestaProvv> richiestaProvvList, Long idRichiedente)throws DaoException, SystemException ;
	public Long countRichiedenteProvvWithFlagDocumentazioneFalse(Long idFinanziamento, BigDecimal bigDecimal) throws DaoException, SystemException;
	public Long countRichiedentiCheHanRinunciatoAlProvvedimento(Long idRichiesta, Long idLeggeProvvDr,
			Long idRichiedente, Boolean rinuncia) throws DaoException, SystemException;
	public Long countRichiedenteProvv(Long idRichiedente) throws DaoException, SystemException ;
	public void deleteRichiedenteProvv(Long idRichiedente) throws DaoException, SystemException ;
	public void updateFlagRinunciaToAllRichiedenti(Long idFinanziamento, Boolean flagRinuncia) throws DaoException, SystemException;

}