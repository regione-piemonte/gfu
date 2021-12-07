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

import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.ParereDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.ParereRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class ParereDaoImpl extends JdbcDaoSupport implements ParereDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public ParereDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Parere> findAllPareri(Boolean isValid) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PARERE, DESCRIZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_PARERE " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}
		sql.append(" ORDER BY ID_PARERE DESC ");

		LOG.debug("[ParereDaoImpl - findAllPareri] param  isValid = " + isValid);
		LOG.debug("[ParereDaoImpl - findAllPareri] query =" + sql.toString());

		List<Parere> parereList = null;
		try {
			parereList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new ParereRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[ParereDaoImpl::findAllPareri]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ParereDaoImpl::findAllPareri] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ParereDaoImpl::findAllPareri] END");
		}
		return (List<Parere>) parereList;
	}

	@Override
	public Parere findParereByPk(Long idParere) throws DaoException, SystemException {
		LOG.debug("[ParereDaoImpl::findParereByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PARERE, DESCRIZIONE, IS_VALID ");
		sql.append("FROM GFU_D_PARERE ");
		sql.append("WHERE  ");
		sql.append("ID_PARERE = :ID_PARERE ");

		Parere result = null;

		LOG.debug("[ParereDaoImpl - findParereByPk] query =" + sql.toString());
		LOG.debug("[ParereDaoImpl - findParereByPk] param  idParere = " + idParere);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_PARERE", idParere);

		try {
			result = (Parere) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new ParereRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[ParereDaoImpl - findParereByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ParereDaoImpl::findParereByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ParereDaoImpl::findParereByPk] END");
		}
		return result;
	}

	@Override
	public Parere createParere(Parere parere) throws DaoException, SystemException {

		try {

			String queryInsert = " INSERT INTO GFU_D_PARERE (DESCRIZIONE, IS_VALID) " + 
					" VALUES(:DESCRIZIONE, :IS_VALID) RETURNING ID_PARERE ";		

			LOG.debug("[ParereDaoImpl - createParere] query =" + queryInsert.toString());
			LOG.debug("[ParereDaoImpl - createParere] param  parere = " + parere);


			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("DESCRIZIONE", parere.getDescrizione());
			paramMap.put("IS_VALID", parere.isIsValid());

			LOG.debug("[ParereDaoImpl::createParere]  param [DESCRIZIONE] = " + parere.getDescrizione());
			LOG.debug("[ParereDaoImpl::createParere]  param [IS_VALID] = " + parere.isIsValid());

			Long idParere = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			parere.setIdParere(idParere);

			LOG.debug("[ParereDaoImpl::createParere]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ParereDaoImpl::createParere]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ParereDaoImpl::createParere] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ParereDaoImpl::createParere] END ");
		}
		return parere;
	}

	@Override
	public Parere updateParere(Parere parere) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_PARERE SET DESCRIZIONE = :DESCRIZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_PARERE = :ID_PARERE ";		

			LOG.debug("[ParereDaoImpl::updateParere]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("DESCRIZIONE", parere.getDescrizione());
			paramMap.put("IS_VALID", parere.isIsValid());
			paramMap.put("ID_PARERE", parere.getIdParere());

			LOG.debug("[ParereDaoImpl::updateParere]  param [ID_PARERE] = " + parere.getIdParere());
			LOG.debug("[ParereDaoImpl::updateParere]  param [DESCRIZIONE] = " + parere.getDescrizione());
			LOG.debug("[ParereDaoImpl::updateParere]  param [IS_VALID] = " + parere.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[ParereDaoImpl::updateParere]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex)
		{
			LOG.debug("[ParereDaoImpl::updateParere]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[ParereDaoImpl::updateParere] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[ParereDaoImpl::updateParere] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[ParereDaoImpl::updateParere] END ");
		}
		return parere;
	}

}