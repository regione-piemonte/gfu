/*******************************************************************************
* Copyright CSI-Piemonte -2021
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.PercentualeDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.PercentualeRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class PercentualeDaoImpl extends JdbcDaoSupport implements PercentualeDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public PercentualeDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Percentuale> findAllPercentuali(Boolean isValid) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PERCENTUALE, VALORE_PERCENTUALE, IS_VALID " );
		sql.append("  FROM GFU_D_PERCENTUALE  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}

		LOG.debug("[PercentualeDaoImpl - findAllPercentuali] param  isValid = " + isValid);
		LOG.debug("[PercentualeDaoImpl - findAllPercentuali] query =" + sql.toString());

		List<Percentuale> percentualeList = null;
		try {
			percentualeList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new PercentualeRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[PercentualeDaoImpl::findAllPercentuali]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[PercentualeDaoImpl::findAllPercentuali] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[PercentualeDaoImpl::findAllPercentuali] END");
		}
		return (List<Percentuale>) percentualeList;
	}

	@Override
	public Percentuale findPercentualeByPk(Long id) throws DaoException, SystemException {
		LOG.debug("[PercentualeDaoImpl::findPercentualeByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PERCENTUALE, VALORE_PERCENTUALE, IS_VALID ");
		sql.append("FROM GFU_D_PERCENTUALE ");
		sql.append("WHERE  ");
		sql.append("ID_PERCENTUALE = :ID_PERCENTUALE ");

		Percentuale result = null;

		LOG.debug("[PercentualeDaoImpl - findPercentualeByPk] query =" + sql.toString());
		LOG.debug("[PercentualeDaoImpl - findPercentualeByPk] param  id = " + id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_PERCENTUALE", id);

		try {
			result = (Percentuale) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new PercentualeRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[PercentualeDaoImpl - findPercentualeByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[PercentualeDaoImpl::findPercentualeByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[PercentualeDaoImpl::findPercentualeByPk] END");
		}
		return result;
	}

	@Override
	public Percentuale createPercentuale(Percentuale percentuale) throws DaoException, SystemException {

		try {

			String queryInsert = " INSERT INTO GFU_D_PERCENTUALE (VALORE_PERCENTUALE, IS_VALID) " + 
					" VALUES(:VALORE_PERCENTUALE, :IS_VALID) RETURNING ID_PERCENTUALE ";		

			LOG.debug("[PercentualeDaoImpl - createPercentuale] query =" + queryInsert.toString());
			LOG.debug("[PercentualeDaoImpl - createPercentuale] param  percentuale = " + percentuale);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("VALORE_PERCENTUALE", percentuale.getValorePercentuale());
			paramMap.put("IS_VALID", percentuale.isIsValid());

			LOG.debug("[PercentualeDaoImpl::createPercentuale]  param [VALORE_PERCENTUALE] = " + percentuale.getValorePercentuale());
			LOG.debug("[PercentualeDaoImpl::createPercentuale]  param [IS_VALID] = " + percentuale.isIsValid());

			Long idPercentuale = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			percentuale.setIdPercentuale(idPercentuale);

			LOG.debug("[PercentualeDaoImpl::createPercentuale]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[PercentualeDaoImpl::createPercentuale]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[PercentualeDaoImpl::createPercentuale] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[PercentualeDaoImpl::createPercentuale] END ");
		}
		return percentuale;
	}

	@Override
	public Percentuale updatePercentuale(Percentuale percentuale) throws DaoException, SystemException {
		try
		{
			
			String queryUpdate = " UPDATE GFU_D_PERCENTUALE SET VALORE_PERCENTUALE = :VALORE_PERCENTUALE, IS_VALID = :IS_VALID "+
					" WHERE ID_PERCENTUALE = :ID_PERCENTUALE ";		

			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("VALORE_PERCENTUALE", percentuale.getValorePercentuale());
			paramMap.put("IS_VALID", percentuale.isIsValid());
			paramMap.put("ID_PERCENTUALE", percentuale.getIdPercentuale());

			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  param [ID_PERCENTUALE] = " + percentuale.getIdPercentuale());
			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  param [VALORE_PERCENTUALE] = " + percentuale.getValorePercentuale());
			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  param [IS_VALID] = " + percentuale.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				 throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex)
		{
			LOG.debug("[PercentualeDaoImpl::updatePercentuale]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[PercentualeDaoImpl::updatePercentuale] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[PercentualeDaoImpl::updatePercentuale] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[PercentualeDaoImpl::updatePercentuale] END ");
		}
		return percentuale;
	}
}