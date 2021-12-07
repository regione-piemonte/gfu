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

import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.DrDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.DrRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.TipoDrRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class DrDaoImpl extends JdbcDaoSupport implements DrDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public DrDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Dr> findAllDr(Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_DR, FK_TIPO_DR, (select DESCRIZIONE from GFU_D_TIPO_DR where ID_TIPO_DR = FK_TIPO_DR) DESC_TIPO_DR, NUMERO, TO_CHAR(DATA, 'DD/MM/YYYY') DATA_DR, DESCRIZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_DR  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}

		LOG.debug("[DrDaoImpl - findAllDr] param  isValid = " + isValid);
		LOG.debug("[DrDaoImpl - findAllDr] query =" + sql.toString());

		List<Dr> drList = null;
		try {
			drList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new DrRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[DrDaoImpl::findAllDr]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[DrDaoImpl::findAllDr] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[DrDaoImpl::findAllDr] END");
		}
		return (List<Dr>) drList;

	}

	@Override
	public Dr findDrByPk(Long id) throws DaoException, SystemException {
		LOG.debug("[DrDaoImpl::findDrByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_DR, FK_TIPO_DR, (select DESCRIZIONE from GFU_D_TIPO_DR where ID_TIPO_DR = FK_TIPO_DR) DESC_TIPO_DR, NUMERO,TO_CHAR(DATA, 'DD/MM/YYYY') DATA_DR, DESCRIZIONE,  IS_VALID ");
		sql.append("FROM GFU_D_DR ");
		sql.append("WHERE  ");
		sql.append("ID_DR = :ID_DR ");

		Dr result = null;

		LOG.debug("[DrDaoImpl - findDrByPk] query =" + sql.toString());
		LOG.debug("[DrDaoImpl - findDrByPk] param  id = " + id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_DR", id);

		try {
			result = (Dr) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new DrRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[DrDaoImpl - findDrByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[DrDaoImpl::findDrByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[DrDaoImpl::findDrByPk] END");
		}
		return result;

	}

	@Override
	public Dr createDr(Dr dr) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_D_DR (FK_TIPO_DR, NUMERO, DATA, DESCRIZIONE, IS_VALID) " + 
					" VALUES(:FK_TIPO_DR, :NUMERO, TO_DATE(:DATA, 'DD/MM/YYYY'), :DESCRIZIONE, :IS_VALID) RETURNING ID_DR ";		

			LOG.debug("[DrDaoImpl - createDr] query =" + queryInsert.toString());
			LOG.debug("[DrDaoImpl - createDr] param  DR = " + dr);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("FK_TIPO_DR", dr.getIdTipoDr());
			paramMap.put("NUMERO" , dr.getNumeroDr());
			paramMap.put("DATA", dr.getDataDr());
			paramMap.put("DESCRIZIONE", dr.getDescDr());
			paramMap.put("IS_VALID", dr.isIsValid());

			LOG.debug("[DrDaoImpl::createDr]  param [FK_TIPO_DR] = " + dr.getIdTipoDr());
			LOG.debug("[DrDaoImpl::createDr]  param [NUMERO] = " + dr.getNumeroDr());
			LOG.debug("[DrDaoImpl::createDr]  param [DATA] = " + dr.getDataDr());
			LOG.debug("[DrDaoImpl::createDr]  param [DESCRIZIONE] = " + dr.getDescDr());
			LOG.debug("[DrDaoImpl::createDr]  param [IS_VALID] = " + dr.isIsValid());


			Long idDr = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			dr.setIdDr(idDr);

			LOG.debug("[DrDaoImpl::createDr]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[DrDaoImpl::createDr]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[DrDaoImpl::createDr] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[DrDaoImpl::createDr] END ");
		}
		return dr;

	}

	@Override
	public Dr updateDr(Dr dr) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_DR SET FK_TIPO_DR = :FK_TIPO_DR, NUMERO = :NUMERO, "
					+ " DATA =  TO_DATE(:DATA, 'DD/MM/YYYY'), DESCRIZIONE = :DESCRIZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_DR = :ID_DR ";		

			LOG.debug("[DrDaoImpl::updateDr]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FK_TIPO_DR", dr.getIdTipoDr());
			paramMap.put("NUMERO", dr.getNumeroDr());
			paramMap.put("DATA", dr.getDataDr());
			paramMap.put("DESCRIZIONE", dr.getDescDr());
			paramMap.put("IS_VALID", dr.isIsValid());
			paramMap.put("ID_DR", dr.getIdDr());

			LOG.debug("[DrDaoImpl::updateDr]  param [ID_DR] = " + dr.getIdDr());
			LOG.debug("[DrDaoImpl::updateDr]  param [FK_TIPO_DR] = " + dr.getIdTipoDr());
			LOG.debug("[DrDaoImpl::updateDr]  param [NUMERO] = " + dr.getNumeroDr());
			LOG.debug("[DrDaoImpl::updateDr]  param [DATA] = " + dr.getDataDr());
			LOG.debug("[DrDaoImpl::updateDr]  param [DESCRIZIONE] = " + dr.getDescDr());
			LOG.debug("[DrDaoImpl::updateDr]  param [IS_VALID] = " + dr.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[DrDaoImpl::updateDr]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[DrDaoImpl::updateDr]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[DrDaoImpl::updateDr] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[DrDaoImpl::updateDr] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[DrDaoImpl::updateDr] END ");
		}
		return dr;
	}

	@Override
	public List<TipoDr> findAllTipiDr() throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_TIPO_DR, COD_TIPO_DR, DESCRIZIONE" );
		sql.append("  FROM GFU_D_TIPO_DR  " );

		LOG.debug("[DrDaoImpl - findAllDr] query =" + sql.toString());

		List<TipoDr> tipoDrList = null;
		try {
			tipoDrList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new TipoDrRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[DrDaoImpl::findAllDr]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[DrDaoImpl::findAllDr] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[DrDaoImpl::findAllDr] END");
		}
		return (List<TipoDr>) tipoDrList;

	}
}
