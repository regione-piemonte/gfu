/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.anagrafiche;

import java.util.List;

import it.csi.gfu.gfuweb.dto.provvedimento.Provvedimento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface ProvvedimentoDao {
	public List<Provvedimento> findAllProvvedimenti(Boolean isValid) throws DaoException, SystemException ;
	public Provvedimento findProvvedimentoByPk(Long id) throws DaoException, SystemException ;
	public Provvedimento createProvvedimento(Provvedimento provvedimento)throws DaoException, SystemException;
	public Provvedimento updateProvvedimento(Provvedimento provvedimento) throws DaoException, SystemException;
	public void deleteProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException;
	public void createRelRichiestaProvv(Long idRichiesta, Long idLeggeProvvDr)	throws DaoException, SystemException ;
	public void createRelRichiedenteProvv(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException;
	public long countUnivocitaRichiedenteProvvedimento(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException;
	public long countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento(Long idFinanziamento)	throws DaoException, SystemException ;
}