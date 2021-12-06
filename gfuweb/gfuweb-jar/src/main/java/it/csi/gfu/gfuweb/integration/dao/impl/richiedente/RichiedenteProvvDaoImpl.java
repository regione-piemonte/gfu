/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.richiedente;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.richiedente.RichiedenteProvvDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class RichiedenteProvvDaoImpl extends JdbcDaoSupport implements RichiedenteProvvDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RichiedenteProvvDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public void createRichiedenteProvv(List<RichiestaProvv> richiestaProvvList, Long idRichiedente)
			throws DaoException, SystemException {
		try {
			LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv]  ");	
			Map<String, Object> paramMap = new HashMap<String, Object>();

			for (int i = 0; i < richiestaProvvList.size(); i++) {

				String queryInsert = " INSERT INTO GFU_R_RICHIEDENTE_PROVV (FK_RICHIEDENTE, FK_LEGGE_PROVV_DR)	VALUES(:FK_RICHIEDENTE, :FK_LEGGE_PROVV_DR) ";		

				paramMap.put("FK_RICHIEDENTE", idRichiedente);
				paramMap.put("FK_LEGGE_PROVV_DR", richiestaProvvList.get(i).getIdLeggeProvvDr());

				LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv]  param [idRichiedente] = " + idRichiedente);
				LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv]  param [idLeggeProvvDr] = " + richiestaProvvList.get(i).getIdLeggeProvvDr());

				namedJdbcTemplate.update(queryInsert, paramMap); 

			}

			LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv]  Inserimento effettuato. Stato = SUCCESS ");} 
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteProvvDaoImpl::createRichiedenteProvv] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteProvvDaoImpl::createRichiedenteProvv] END ");
		}
	}

	@Override
	public Long countRichiedenteProvvWithFlagDocumentazioneFalse(Long idFinanziamento, BigDecimal idLeggeProvvDr) throws DaoException, SystemException {
		LOG.debug("[RichiedenteProvvDaoImpl::countRichiedenteProvvWithFlagDocumentazioneFalse] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*)	FROM GFU_R_RICHIEDENTE_PROVV RP , GFU_T_FINANZIAMENTO F ,  ");
		sql.append("GFU_T_RICHIESTA R ,	GFU_R_RICHIESTA_RICHIEDENTE RR,	GFU_R_RICHIESTA_PROVV RIC_P ");
		sql.append("WHERE RP.FK_LEGGE_PROVV_DR = F.FK_LEGGE_PROVV_DR	");
		sql.append("AND R.ID_RICHIESTA = RR.FK_RICHIESTA ");
		sql.append("AND RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE ");
		sql.append("AND RIC_P.FK_RICHIESTA = R.ID_RICHIESTA ");
		sql.append("AND RIC_P.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("AND RIC_P.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append("AND F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO ");
		sql.append("AND F.FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR ");
		sql.append("AND FLG_DOCUMENTAZIONE = FALSE AND FLG_RINUNCIA = FALSE ");

		long conteggio = 0;

		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvvWithFlagDocumentazioneFalse] query = " + sql.toString());
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvvWithFlagDocumentazioneFalse] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvvWithFlagDocumentazioneFalse] param idLeggeProvvDr = " + idLeggeProvvDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_FINANZIAMENTO", idFinanziamento);
		paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvvWithFlagDocumentazioneFalse] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiedenteProvvDaoImpl::countRichiedenteProvvWithFlagDocumentazioneFalse] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiedenteProvvDaoImpl::countRichiedenteProvvWithFlagDocumentazioneFalse] END");
		}

		return conteggio;			
	}

	@Override
	public Long countRichiedentiCheHanRinunciatoAlProvvedimento(Long idRichiesta, Long idLeggeProvvDr,
			Long idRichiedente, Boolean rinuncia) throws DaoException, SystemException {
		LOG.debug("[RichiedenteProvvDaoImpl::countRichiedenteProvvWithFlagDocumentazioneFalse] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) FROM GFU_T_FINANZIAMENTO F , GFU_R_RICHIEDENTE_PROVV RP, GFU_R_RICHIESTA_PROVV RIC_P, GFU_R_RICHIESTA_RICHIEDENTE RIC_RIC ");
		sql.append("WHERE F.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append("AND RIC_P.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append(" and RIC_P.fk_finanziamento = f.id_finanziamento ");
		sql.append("AND F.FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR ");
		sql.append("AND RP.FK_RICHIEDENTE != :FK_RICHIEDENTE		");
		sql.append("AND RIC_P.FK_RICHIESTA = :FK_RICHIESTA ");		
		sql.append("AND RP.FLG_RINUNCIA =  "+ rinuncia +" ");
		sql.append("AND RIC_RIC.FK_RICHIESTA = RIC_P.FK_RICHIESTA ");
		sql.append("AND RIC_RIC.FK_RICHIEDENTE = RP.FK_RICHIEDENTE ");

		long conteggio = 0;

		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedentiCheNonHannoRinunciatoAlProvvedimento] query = " + sql.toString());
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedentiCheNonHannoRinunciatoAlProvvedimento] param FK_LEGGE_PROVV_DR = " + idLeggeProvvDr);
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedentiCheNonHannoRinunciatoAlProvvedimento] param FK_RICHIEDENTE = " + idRichiedente);
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedentiCheNonHannoRinunciatoAlProvvedimento] param FK_RICHIESTA = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
		paramMap.put("FK_RICHIEDENTE", idRichiedente);
		paramMap.put("FK_RICHIESTA", idRichiesta);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteProvvDaoImpl - countRichiedentiCheNonHannoRinunciatoAlProvvedimento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiedenteProvvDaoImpl::countRichiedentiCheNonHannoRinunciatoAlProvvedimento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiedenteProvvDaoImpl::countRichiedentiCheNonHannoRinunciatoAlProvvedimento] END");
		}

		return conteggio;	
	}

	@Override
	public Long countRichiedenteProvv(Long idRichiedente) throws DaoException, SystemException {
		LOG.debug("[RichiedenteProvvDaoImpl::countRichiedenteProvv] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) FROM GFU_R_RICHIEDENTE_PROVV WHERE FK_RICHIEDENTE = :FK_RICHIEDENTE ");

		long conteggio = 0;

		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvv] query = " + sql.toString());
		LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvv] param idRichiedente = " + idRichiedente);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_RICHIEDENTE", idRichiedente);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteProvvDaoImpl - countRichiedenteProvv] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiedenteProvvDaoImpl::countRichiedenteProvv] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiedenteProvvDaoImpl::countRichiedenteProvv] END");
		}

		return conteggio;			
	}

	@Override
	public void deleteRichiedenteProvv(Long idRichiedente) throws DaoException, SystemException {

		try {
			LOG.debug("[RichiedenteProvvDaoImpl::deleteRichiedenteProvv] idRichiedente = " + idRichiedente);

			String queryRelationDelete ="DELETE FROM GFU_R_RICHIEDENTE_PROVV RP WHERE  RP.FK_RICHIEDENTE = :FK_RICHIEDENTE ";

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("FK_RICHIEDENTE", idRichiedente);

			namedJdbcTemplate.update(queryRelationDelete, paramMap);  	

			LOG.debug("[RichiedenteProvvDaoImpl::deleteRichiedenteProvv] queryRelationDelete  = " + queryRelationDelete);
			LOG.debug("[RichiedenteProvvDaoImpl::deleteRichiedenteProvv]  eliminazione effettuata. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteProvvDaoImpl::deleteRichiedenteProvv] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteProvvDaoImpl::deleteRichiedenteProvv] END ");
		}
	}

	@Override
	public void updateFlagRinunciaToAllRichiedenti(Long idFinanziamento, Boolean flagRinuncia)
			throws DaoException, SystemException {
		try {

			String queryUpdate = " UPDATE GFU_R_RICHIEDENTE_PROVV SET FLG_RINUNCIA = :FLG_RINUNCIA "+
					"WHERE FK_RICHIEDENTE in "+
					"(SELECT RR.fk_richiedente "+
					"FROM GFU_T_FINANZIAMENTO F, "+
					"GFU_R_RICHIEDENTE_PROVV RP, "+
					"GFU_R_RICHIESTA_RICHIEDENTE RR, "+
					"GFU_R_RICHIESTA_PROVV RIC_P "+
					"WHERE F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO "+
					"AND RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE "+
					"AND RIC_P.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO "+
					"AND RIC_P.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR "+
					"AND RIC_P.FK_RICHIESTA = RR.FK_RICHIESTA) "+
					"AND FK_LEGGE_PROVV_DR IN ( "+
					"SELECT RIC_P.FK_LEGGE_PROVV_DR "+
					"FROM GFU_T_FINANZIAMENTO F, "+
					"GFU_R_RICHIESTA_PROVV RIC_P "+
					"WHERE "+
					"F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO 	"+			
					"AND RIC_P.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO "+
					"AND  RIC_P.FK_LEGGE_PROVV_DR IN (SELECT RIC_P2.FK_LEGGE_PROVV_DR FROM GFU_R_RICHIESTA_PROVV RIC_P2 WHERE RIC_P2.FK_FINANZIAMENTO = :ID_FINANZIAMENTO) "+
					") ";

			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti]  queryUpdate: = " + queryUpdate.toString());
			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FLG_RINUNCIA", flagRinuncia);
			paramMap.put("ID_FINANZIAMENTO", idFinanziamento);

			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti]  param [FLG_RINUNCIA] = " + flagRinuncia);
			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti]  param [ID_FINANZIAMENTO] = " + idFinanziamento);

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  

			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti]  Inserimento effettuato. Stato = SUCCESS ");
		} 
		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiedenteProvvDaoImpl::updateFlagRinunciaToAllRichiedenti] END ");
		}
		
	}
}