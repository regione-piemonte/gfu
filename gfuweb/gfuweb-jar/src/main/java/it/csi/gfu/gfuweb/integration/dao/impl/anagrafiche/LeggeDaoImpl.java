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

import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.LeggeDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.LeggeRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.leggeprovvdr.LeggeProvvDrRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class LeggeDaoImpl extends JdbcDaoSupport implements LeggeDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public LeggeDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Legge> findAllLeggi(Boolean isValid) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_LEGGE, DESCRIZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_LEGGE  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}

		LOG.debug("[LeggeDaoImpl - findAllLeggi] param  isValid = " + isValid);
		LOG.debug("[LeggeDaoImpl - findAllLeggi] query =" + sql.toString());

		List<Legge> leggeList = null;
		try {
			leggeList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new LeggeRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[LeggeDaoImpl::findAllLeggi]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::findAllLeggi] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::findAllLeggi] END");
		}
		return (List<Legge>) leggeList;
	}


	@Override
	public Legge findLeggeByPk(Long id) throws DaoException, SystemException {
		LOG.debug("[LeggeDaoImpl::findLeggeByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_LEGGE, DESCRIZIONE, IS_VALID ");
		sql.append("FROM GFU_D_LEGGE ");
		sql.append("WHERE  ");
		sql.append("ID_LEGGE = :ID_LEGGE ");

		Legge result = null;

		LOG.debug("[LeggeDaoImpl - findLeggeByPk] query =" + sql.toString());
		LOG.debug("[LeggeDaoImpl - findLeggeByPk] param  id = " + id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_LEGGE", id);

		try {
			result = (Legge) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new LeggeRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[LeggeDaoImpl - findLeggeByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::findLeggeByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::findLeggeByPk] END");
		}
		return result;
	}

	@Override
	public Legge createLegge(Legge legge)throws DaoException, SystemException {

		try {

			String queryInsert = " INSERT INTO GFU_D_LEGGE (DESCRIZIONE, IS_VALID) " + 
					" VALUES(:DESCRIZIONE, :IS_VALID) RETURNING ID_LEGGE ";		

			LOG.debug("[LeggeDaoImpl - createLegge] query =" + queryInsert.toString());
			LOG.debug("[LeggeDaoImpl - createLegge] param  legge = " + legge);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("DESCRIZIONE", legge.getDescrizione());
			paramMap.put("IS_VALID", legge.isIsValid());

			LOG.debug("[LeggeDaoImpl::createLegge]  param [DESCRIZIONE] = " + legge.getDescrizione());
			LOG.debug("[LeggeDaoImpl::createLegge]  param [IS_VALID] = " + legge.isIsValid());

			Long idLegge = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			legge.setIdLegge(idLegge);

			LOG.debug("[LeggeDaoImpl::createLegge]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[LeggeDaoImpl::createLegge]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::createLegge] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::createLegge] END ");
		}
		return legge;
	}

	@Override
	public Legge updateLegge(Legge legge) throws DaoException, SystemException
	{
		try {
			String queryUpdate = " UPDATE GFU_D_LEGGE SET DESCRIZIONE = :DESCRIZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_LEGGE = :ID_LEGGE ";		
			LOG.debug("[LeggeDaoImpl::updateLegge]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("DESCRIZIONE", legge.getDescrizione());
			paramMap.put("IS_VALID", legge.isIsValid());
			paramMap.put("ID_LEGGE", legge.getIdLegge());

			LOG.debug("[LeggeDaoImpl::updateLegge]  param [ID_LEGGE] = " + legge.getIdLegge());
			LOG.debug("[LeggeDaoImpl::updateLegge]  param [DESCRIZIONE] = " + legge.getDescrizione());
			LOG.debug("[LeggeDaoImpl::updateLegge]  param [IS_VALID] = " + legge.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
			LOG.debug("[LeggeDaoImpl::updateLegge]  Inserimento effettuato. Stato = SUCCESS ");
		} 
		catch (DataIntegrityViolationException ex) {
			LOG.debug("[LeggeDaoImpl::updateLegge]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[LeggeDaoImpl::updateLegge] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[LeggeDaoImpl::updateLegge] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[LeggeDaoImpl::updateLegge] END ");
		}

		return legge;
	}
	@Override
	public List<LeggeProvvDr> findAllLeggeProvvDr(String descLeggeProvvDr, Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ID_LEGGE_PROVV_DR, DESCRIZIONE, FK_LEGGE, (SELECT DESCRIZIONE FROM GFU_D_LEGGE WHERE FK_LEGGE = ID_LEGGE) DESC_LEGGE,  " );
		sql.append(" FK_PROVVEDIMENTO, (SELECT DESCRIZIONE FROM GFU_D_PROVVEDIMENTO WHERE FK_PROVVEDIMENTO = ID_PROVVEDIMENTO) DESC_PROVVEDIMENTO, ");
		sql.append(" FK_DR, (SELECT DESCRIZIONE FROM GFU_D_DR WHERE FK_DR = ID_DR) DESC_DR, DESCRIZIONE DESC_LEGGE_PROVV_DR, " );
		sql.append(" FK_VINCOLO_POPOLAZIONE, (select VP.SEGNO || ' ' || VP.POPOLAZIONE DESCRIZIONE FROM GFU_D_VINCOLO_POPOLAZIONE VP where FK_VINCOLO_POPOLAZIONE = VP.ID_VINCOLO_POPOLAZIONE) DESC_VINCOLO_POPOLAZIONE, IS_VALID ");
		sql.append(" FROM GFU_T_LEGGE_PROVV_DR   ");
		
		if(descLeggeProvvDr != null || isValid != null) {
			sql.append(" WHERE");
			if (descLeggeProvvDr != null) { 
				sql.append(" UPPER(DESCRIZIONE) LIKE UPPER('%"+ descLeggeProvvDr +"%') ");
			}
			if(descLeggeProvvDr != null && isValid != null) {
					sql.append(" AND IS_VALID IS  " + isValid);
			} else 
				if(isValid != null) {sql.append(" IS_VALID IS  " + isValid);}
		}

		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] param  descLeggiProvvDr = " + descLeggeProvvDr);
		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] param  isValid = " + isValid);
		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] query =" + sql.toString());

		List<LeggeProvvDr> leggeProvvDrList = null;
		try {
			leggeProvvDrList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new LeggeProvvDrRowMapper());
		} 
		catch(EmptyResultDataAccessException ex) {
			LOG.debug("\"[LeggeDaoImpl::findAllLeggeProvvDr]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::findAllLeggeProvvDr] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::findAllLeggeProvvDr] END");
		}
		return (List<LeggeProvvDr>) leggeProvvDrList;
	}

/*
	@Override
	public List<LeggeProvvDr> findAllLeggeProvvDr(String descLeggiProvvDr, Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT	LP.ID_LEGGE_PROVV_DR, LP.DESCRIZIONE, LP.FK_LEGGE, L.DESCRIZIONE DESC_LEGGE,  " );
		sql.append("LP.FK_PROVVEDIMENTO,  P.DESCRIZIONE DESC_PROVVEDIMENTO,	LP.FK_DR, DR.DESCRIZIONE DESC_DR, ");
		sql.append("LP.DESCRIZIONE DESC_LEGGE_PROVV_DR,	LP.FK_VINCOLO_POPOLAZIONE, " );
		sql.append("VP.SEGNO || ' ' || VP.POPOLAZIONE  DESC_VINCOLO_POPOLAZIONE, LP.IS_VALID ");		
		sql.append("FROM GFU_T_LEGGE_PROVV_DR  LP  ");
		sql.append("LEFT OUTER JOIN GFU_D_VINCOLO_POPOLAZIONE VP ON VP.ID_VINCOLO_POPOLAZIONE = LP.FK_VINCOLO_POPOLAZIONE ");
		sql.append("LEFT OUTER JOIN GFU_D_LEGGE L ON L.ID_LEGGE = LP.FK_LEGGE ");
		sql.append("LEFT OUTER JOIN GFU_D_PROVVEDIMENTO P ON P.ID_PROVVEDIMENTO = LP.FK_PROVVEDIMENTO ");
		sql.append("LEFT OUTER JOIN GFU_D_DR DR ON DR.ID_DR = LP.FK_DR ");

		if(descLeggiProvvDr != null || isValid != null) {
			sql.append(" WHERE");
			if (descLeggiProvvDr != null) { 
				sql.append(" (VP.SEGNO || ' '|| VP.POPOLAZIONE) LIKE ('%"+ descLeggiProvvDr +"%') ");
			}
			if(descLeggiProvvDr != null && isValid != null) {
				sql.append(" AND IS_VALID IS  " + isValid);
			} else 
				if(isValid != null) {sql.append(" IS_VALID IS  " + isValid);}
		}

		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] param  descLeggiProvvDr = " + descLeggiProvvDr);
		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] param  isValid = " + isValid);
		LOG.debug("[LeggeDaoImpl - findAllLeggeProvvDr] query =" + sql.toString());

		List<LeggeProvvDr> leggeProvvDrList = null;
		try {
			leggeProvvDrList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new LeggeProvvDrRowMapper());
		} 
		catch(EmptyResultDataAccessException ex) {
			LOG.debug("\"[LeggeDaoImpl::findAllLeggeProvvDr]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::findAllLeggeProvvDr] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::findAllLeggeProvvDr] END");
		}
		return (List<LeggeProvvDr>) leggeProvvDrList;
	}
*/
	@Override
	public LeggeProvvDr findLeggeProvvDrByPk(Long id) throws DaoException, SystemException {
		LOG.debug("[LeggeDaoImpl::findLeggeProvvDrByPk] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT	LP.ID_LEGGE_PROVV_DR, LP.DESCRIZIONE, LP.FK_LEGGE, L.DESCRIZIONE DESC_LEGGE,  " );
		sql.append("LP.FK_PROVVEDIMENTO,  P.DESCRIZIONE DESC_PROVVEDIMENTO,	LP.FK_DR, DR.DESCRIZIONE DESC_DR, ");
		sql.append("LP.DESCRIZIONE DESC_LEGGE_PROVV_DR,	LP.FK_VINCOLO_POPOLAZIONE, " );
		sql.append("VP.SEGNO || ' ' || VP.POPOLAZIONE  DESC_VINCOLO_POPOLAZIONE, LP.IS_VALID ");
		sql.append("FROM GFU_T_LEGGE_PROVV_DR  LP  ");
		sql.append("LEFT OUTER JOIN GFU_D_VINCOLO_POPOLAZIONE VP ON VP.ID_VINCOLO_POPOLAZIONE = LP.FK_VINCOLO_POPOLAZIONE ");
		sql.append("LEFT OUTER JOIN GFU_D_LEGGE L ON L.ID_LEGGE = LP.FK_LEGGE ");
		sql.append("LEFT OUTER JOIN GFU_D_PROVVEDIMENTO P ON P.ID_PROVVEDIMENTO = LP.FK_PROVVEDIMENTO ");
		sql.append("LEFT OUTER JOIN GFU_D_DR DR ON DR.ID_DR = LP.FK_DR ");
		sql.append("WHERE  ");
		sql.append("LP.ID_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR ");

		LeggeProvvDr result = null;

		LOG.debug("[LeggeDaoImpl - findLeggeProvvDrByPk] query =" + sql.toString());
		LOG.debug("[LeggeDaoImpl - findLeggeProvvDrByPk] param  id = " + id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_LEGGE_PROVV_DR", id);

		try {
			result = (LeggeProvvDr) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new LeggeProvvDrRowMapper());
		} 
		catch(EmptyResultDataAccessException ex) {
			LOG.debug("[LeggeDaoImpl - findLeggeProvvDrByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::findLeggeProvvDrByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::findLeggeProvvDrByPk] END");
		}
		return result;
	}

	@Override
	public LeggeProvvDr createLeggeProvvDr(LeggeProvvDr leggeProvvDr) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_T_LEGGE_PROVV_DR (FK_LEGGE, FK_PROVVEDIMENTO, FK_DR, DESCRIZIONE, FK_VINCOLO_POPOLAZIONE, IS_VALID) " + 
					" VALUES(:FK_LEGGE, :FK_PROVVEDIMENTO, :FK_DR, :DESCRIZIONE,:FK_VINCOLO_POPOLAZIONE, :IS_VALID) RETURNING ID_LEGGE_PROVV_DR ";		

			LOG.debug("[LeggeDaoImpl - createLeggeProvvDr] query =" + queryInsert.toString());
			LOG.debug("[LeggeDaoImpl - createLeggeProvvDr] param  LEGGE_PROVV_DR = " + leggeProvvDr);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("FK_LEGGE", leggeProvvDr.getIdLegge());
			paramMap.put("FK_PROVVEDIMENTO" , leggeProvvDr.getIdProvvedimento());
			paramMap.put("FK_DR", leggeProvvDr.getIdDr());
			paramMap.put("DESCRIZIONE", leggeProvvDr.getDescLeggeProvvDr());
			paramMap.put("FK_VINCOLO_POPOLAZIONE", leggeProvvDr.getIdVincoloPopolazione());
			paramMap.put("IS_VALID", leggeProvvDr.isIsValid());

			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [FK_LEGGE] = " + leggeProvvDr.getIdLegge());
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [FK_PROVVEDIMENTO] " + leggeProvvDr.getIdProvvedimento());
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [FK_DR] = " + leggeProvvDr.getIdDr());
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [DESCRIZIONE] = " + leggeProvvDr.getDescLeggeProvvDr());
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [FK_VINCOLO_POPOLAZIONE] = " + leggeProvvDr.getIdVincoloPopolazione());
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  param [IS_VALID] = " + leggeProvvDr.isIsValid());

			Long idLeggeProvvDr = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			leggeProvvDr.setIdLeggeProvvDr(idLeggeProvvDr);

			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeDaoImpl::createLeggeProvvDr] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeDaoImpl::createLeggeProvvDr] END ");
		}
		return leggeProvvDr;
	}

	@Override
	public LeggeProvvDr updateLeggeProvvDr(LeggeProvvDr leggeProvvDr) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_LEGGE_PROVV_DR SET FK_LEGGE = :FK_LEGGE, FK_PROVVEDIMENTO = :FK_PROVVEDIMENTO, "
					+ " FK_DR = :FK_DR, DESCRIZIONE = :DESCRIZIONE, FK_VINCOLO_POPOLAZIONE = :FK_VINCOLO_POPOLAZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR ";		

			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FK_LEGGE", leggeProvvDr.getIdLegge());
			paramMap.put("FK_PROVVEDIMENTO", leggeProvvDr.getIdProvvedimento());
			paramMap.put("FK_DR", leggeProvvDr.getIdDr());
			paramMap.put("DESCRIZIONE", leggeProvvDr.getDescLeggeProvvDr());
			paramMap.put("FK_VINCOLO_POPOLAZIONE", leggeProvvDr.getIdVincoloPopolazione());
			paramMap.put("IS_VALID", leggeProvvDr.isIsValid());
			paramMap.put("ID_LEGGE_PROVV_DR", leggeProvvDr.getIdLeggeProvvDr());

			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [ID_LEGGE_PROVV_DR] = " + leggeProvvDr.getIdLeggeProvvDr());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [FK_LEGGE] = " + leggeProvvDr.getIdLegge());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [FK_PROVVEDIMENTO] = " + leggeProvvDr.getIdProvvedimento());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [FK_DR] = " + leggeProvvDr.getIdDr());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [DESCRIZIONE] = " + leggeProvvDr.getDescLeggeProvvDr());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [FK_VINCOLO_POPOLAZIONE] = " + leggeProvvDr.getIdVincoloPopolazione());
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  param [IS_VALID] = " + leggeProvvDr.isIsValid());

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr]  Integrity Keys Violation");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[LeggeDaoImpl::updateLeggeProvvDr] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[LeggeDaoImpl::updateLeggeProvvDr] END ");
		}

		return leggeProvvDr;
	}
}
