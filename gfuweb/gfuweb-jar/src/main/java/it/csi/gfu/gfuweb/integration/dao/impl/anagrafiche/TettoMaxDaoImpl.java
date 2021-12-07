/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.anagrafiche;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.tetto.TettoMax;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.TettoMaxDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.TettoMaxRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.TettoMaxTotRichiedentiRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class TettoMaxDaoImpl extends JdbcDaoSupport implements TettoMaxDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public TettoMaxDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<TettoMax> findAllTettoMax(Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_TETTO_MAX, IMPORTO, VALUTA, TO_CHAR(DATA_INIZIO, 'DD/MM/YYYY') DATA_INIZIO, TO_CHAR(DATA_FINE, 'DD/MM/YYYY') DATA_FINE   " );
		sql.append("FROM GFU_D_TETTO_MAX " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" WHERE DATA_FINE IS NULL  ");
			}else {
				sql.append(" WHERE DATA_FINE IS NOT NULL  ");
			}
		}

		LOG.debug("[TettoMaxDaoImpl - findAllTettoMax] param  isValid = " + isValid);
		LOG.debug("[TettoMaxDaoImpl - findAllTettoMax] query =" + sql.toString());

		List<TettoMax> tettoMaxList = null;
		try {
			tettoMaxList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new TettoMaxRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[TettoMaxDaoImpl::findAllTettoMax]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[TettoMaxDaoImpl::findAllTettoMax] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[TettoMaxDaoImpl::findAllTettoMax] END");
		}
		return (List<TettoMax>) tettoMaxList;
	}

	@Override
	public TettoMax findTettoMaxByPk(Long idTettoMax) throws DaoException, SystemException {
		LOG.debug("[TettoMaxDaoImpl::findDrByPk] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_TETTO_MAX, IMPORTO, VALUTA, TO_CHAR(DATA_INIZIO, 'DD/MM/YYYY') DATA_INIZIO, TO_CHAR(DATA_FINE, 'DD/MM/YYYY') DATA_FINE   ");
		sql.append("FROM GFU_D_TETTO_MAX ");
		sql.append("WHERE  ");
		sql.append("ID_TETTO_MAX = :ID_TETTO_MAX ");

		TettoMax result = null;

		LOG.debug("[TettoMaxDaoImpl - findTettoMaxByPk] query =" + sql.toString());
		LOG.debug("[TettoMaxDaoImpl - findTettoMaxByPk] param  ID_TETTO_MAX = " + idTettoMax);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_TETTO_MAX", idTettoMax);

		try {
			result = (TettoMax) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new TettoMaxRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[TettoMaxDaoImpl - findTettoMaxByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[TettoMaxDaoImpl::findTettoMaxByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[TettoMaxDaoImpl::findTettoMaxByPk] END");
		}
		return result;
	}

	@Override
	public TettoMax createTettoMax(TettoMax tettoMax) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_D_TETTO_MAX (IMPORTO, VALUTA, DATA_INIZIO ) " + 
					" VALUES(:IMPORTO, '"+Constants.DB.VALUTA_EURO+"' , CURRENT_DATE ) RETURNING ID_TETTO_MAX ";		

			LOG.debug("[TettoMaxDaoImpl - createTettoMax] query =" + queryInsert.toString());
			LOG.debug("[TettoMaxDaoImpl - createTettoMax] param  tettoMax = " + tettoMax);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("IMPORTO", tettoMax.getImporto());

			LOG.debug("[TettoMaxDaoImpl::createTettoMax]  param [IMPORTO] = " + tettoMax.getImporto());

			Long idTettoMax = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			tettoMax.setIdTettoMax(idTettoMax);

			LOG.debug("[TettoMaxDaoImpl::createTettoMax]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[TettoMaxDaoImpl::createTettoMax]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[TettoMaxDaoImpl::createTettoMax] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[TettoMaxDaoImpl::createTettoMax] END ");
		}
		return tettoMax;
	}

	@Override
	public TettoMax updateTettoMax(TettoMax tettoMax) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_TETTO_MAX SET IMPORTO = :IMPORTO "+
					" WHERE ID_TETTO_MAX = :ID_TETTO_MAX ";		

			LOG.debug("[TettoMaxDaoImpl::updateTettoMax]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("IMPORTO", tettoMax.getImporto());
			paramMap.put("ID_TETTO_MAX", tettoMax.getIdTettoMax());

			LOG.debug("[TettoMaxDaoImpl::updateTettoMax]  param [IMPORTO] = " + tettoMax.getImporto());
			LOG.debug("[TettoMaxDaoImpl::updateTettoMax]  param [ID_TETTO_MAX] = " + tettoMax.getIdTettoMax());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[TettoMaxDaoImpl::updateTettoMax]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMax]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMax] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[TettoMaxDaoImpl::updateTettoMax] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMax] END ");
		}
		return tettoMax;
	}

	@Override
	public void deleteTettoMaxByPk(Long idTettoMax) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_TETTO_MAX SET DATA_FINE = CURRENT_DATE "+
					" WHERE ID_TETTO_MAX = :ID_TETTO_MAX ";		

			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("ID_TETTO_MAX", idTettoMax);

			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk]  param [ID_TETTO_MAX] = " + idTettoMax);

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[TettoMaxDaoImpl::deleteTettoMaxByPk] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[TettoMaxDaoImpl::deleteTettoMaxByPk] END ");
		}
	}

	@Override
	public TettoMaxTotRichiedenti findTettoMaxTotRichiedenti(Long idRichiesta, Long idLeggeProvDr) throws DaoException, SystemException {
		LOG.debug("[TettoMaxDaoImpl::findTettoMaxTotRichiedenti] BEGIN");

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT t.id_tetto_max,  T.IMPORTO IMPORTO_TETTO_MAX,T.IMPORTO*(SELECT COUNT(*)  "); 
		sql.append("FROM GFU_R_RICHIESTA_RICHIEDENTE RR ,  ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RP    ");
		sql.append("WHERE  ");
		sql.append("RR.FK_RICHIESTA = :FK_RICHIESTA ");  
		sql.append("AND RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE AND RP.FLG_RINUNCIA IS FALSE AND RP.FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR) IMPORTO_MAX_PER_RICHIEDENTI, T.VALUTA ");  
		sql.append("FROM GFU_D_TETTO_MAX T, ");  
		sql.append("GFU_T_RICHIESTA R  ");
		sql.append("WHERE R.ID_RICHIESTA = :FK_RICHIESTA ");  
		sql.append("AND ((R.DATA_PROTOCOLLO >= T.DATA_INIZIO   AND R.DATA_PROTOCOLLO < T.DATA_FINE  )  ");
		sql.append("OR (R.DATA_PROTOCOLLO >= T.DATA_INIZIO   AND T.DATA_FINE IS null))  ");
				
		TettoMaxTotRichiedenti result = null;

		LOG.debug("[TettoMaxDaoImpl - findTettoMaxTotRichiedenti] query =" + sql.toString());
		LOG.debug("[TettoMaxDaoImpl - findTettoMaxTotRichiedenti] param  idRichiesta = " + idRichiesta);
		LOG.debug("[TettoMaxDaoImpl - findTettoMaxTotRichiedenti] param  idLeggeProvDr = " + idLeggeProvDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_RICHIESTA", idRichiesta);
		paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvDr);

		try {
			result = (TettoMaxTotRichiedenti) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new TettoMaxTotRichiedentiRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[TettoMaxDaoImpl - findTettoMaxTotRichiedenti] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch(IncorrectResultSizeDataAccessException ex)
		{
			LOG.debug("[TettoMaxDaoImpl - findTettoMaxTotRichiedenti] RISULTATO ERRATO");
			throw new DaoException("Risultato errato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[TettoMaxDaoImpl::findTettoMaxTotRichiedenti] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[TettoMaxDaoImpl::findTettoMaxTotRichiedenti] END");
		}
		return result;
	}

	@Override
	public void updateTettoMaxAttivoToDisattivo() throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_TETTO_MAX SET DATA_FINE = CURRENT_DATE "+
					" WHERE DATA_FINE IS NULL ";		

			LOG.debug("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[TettoMaxDaoImpl::updateTettoMaxAttivoToDisattivo] END ");
		}
	}

}