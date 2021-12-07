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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.RendicontoDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.RendicontoRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class RendicontoDaoImpl extends JdbcDaoSupport implements RendicontoDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RendicontoDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Rendiconto> findAllRendiconti(Boolean isValid) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_RENDICONTO, DESCRIZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_RENDICONTO  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}

		LOG.debug("[RendicontoDaoImpl - findAllRendiconti] param  isValid = " + isValid);
		LOG.debug("[RendicontoDaoImpl - findAllRendiconti] query =" + sql.toString());

		List<Rendiconto> rendicontoList = null;
		try {
			rendicontoList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new RendicontoRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RendicontoDaoImpl::findAllRendiconti]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RendicontoDaoImpl::findAllRendiconti] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RendicontoDaoImpl::findAllRendiconti] END");
		}
		return (List<Rendiconto>) rendicontoList;
	}

	@Override
	public Rendiconto findRendicontoByPk(Long idRendiconto) throws DaoException, SystemException {
		LOG.debug("[RendicontoDaoImpl::findRendicontoByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_RENDICONTO, DESCRIZIONE, IS_VALID ");
		sql.append("FROM GFU_D_RENDICONTO ");
		sql.append("WHERE  ");
		sql.append("ID_RENDICONTO = :ID_RENDICONTO ");

		Rendiconto result = null;

		LOG.debug("[RendicontoDaoImpl - findRendicontoByPk] query =" + sql.toString());
		LOG.debug("[RendicontoDaoImpl - findRendicontoByPk] param  idRendiconto = " + idRendiconto);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RENDICONTO", idRendiconto);

		try {
			result = (Rendiconto) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new RendicontoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[RendicontoDaoImpl - findRendicontoByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RendicontoDaoImpl::findRendicontoByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RendicontoDaoImpl::findRendicontoByPk] END");
		}
		return result;
	}

	@Override
	public Rendiconto createRendiconto(Rendiconto rendiconto) throws DaoException, SystemException {

		try {

			String queryInsert = " INSERT INTO GFU_D_RENDICONTO (DESCRIZIONE, IS_VALID) " + 
					" VALUES(:DESCRIZIONE, :IS_VALID) RETURNING ID_RENDICONTO ";		

			LOG.debug("[RendicontoDaoImpl - createRendiconto] query =" + queryInsert.toString());
			LOG.debug("[RendicontoDaoImpl - createRendiconto] param  rendiconto = " + rendiconto);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("DESCRIZIONE", rendiconto.getDescrizione());
			paramMap.put("IS_VALID", rendiconto.isIsValid());

			LOG.debug("[RendicontoDaoImpl::createRendiconto]  param [DESCRIZIONE] = " + rendiconto.getDescrizione());
			LOG.debug("[RendicontoDaoImpl::createRendiconto]  param [IS_VALID] = " + rendiconto.isIsValid());

			Long idRendiconto = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			rendiconto.setIdRendiconto(idRendiconto);

			LOG.debug("[RendicontoDaoImpl::createRendiconto]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[RendicontoDaoImpl::createRendiconto]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RendicontoDaoImpl::createRendiconto] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RendicontoDaoImpl::createRendiconto] END ");
		}
		return rendiconto;
	}

	@Override
	public Rendiconto updateRendiconto(Rendiconto rendiconto) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_RENDICONTO SET DESCRIZIONE = :DESCRIZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_RENDICONTO = :ID_RENDICONTO ";		

			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("DESCRIZIONE", rendiconto.getDescrizione());
			paramMap.put("IS_VALID", rendiconto.isIsValid());
			paramMap.put("ID_RENDICONTO", rendiconto.getIdRendiconto());

			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  param [ID_RENDICONTO] = " + rendiconto.getIdRendiconto());
			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  param [DESCRIZIONE] = " + rendiconto.getDescrizione());
			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  param [IS_VALID] = " + rendiconto.isIsValid());
			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex)
		{
			LOG.debug("[RendicontoDaoImpl::updateRendiconto]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RendicontoDaoImpl::updateRendiconto] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RendicontoDaoImpl::updateRendiconto] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RendicontoDaoImpl::updateRendiconto] END ");
		}
		return rendiconto;
	}
}