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

import it.csi.gfu.gfuweb.dto.popolazione.VincoloPopolazione;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.VincoloPopolazioneRowMapper;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.VincoloPopolazioneDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class VincoloPopolazioneDaoImpl extends JdbcDaoSupport implements VincoloPopolazioneDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public VincoloPopolazioneDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<VincoloPopolazione> findAllVincoloPopolazione(Boolean isValid) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_VINCOLO_POPOLAZIONE, SEGNO, POPOLAZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_VINCOLO_POPOLAZIONE  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}
		sql.append(" ORDER BY ID_VINCOLO_POPOLAZIONE,  IS_VALID " );

		LOG.debug("[VincoloPopolazioneDaoImpl - findAllVincoloPopolazione] param  isValid = " + isValid);
		LOG.debug("[VincoloPopolazioneDaoImpl - findAllVincoloPopolazione] query =" + sql.toString());

		List<VincoloPopolazione> popolazioneList = null;
		try {
			popolazioneList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new VincoloPopolazioneRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[VincoloPopolazioneDaoImpl::findAllVincoloPopolazione]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[VincoloPopolazioneDaoImpl::findAllVincoloPopolazione] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[VincoloPopolazioneDaoImpl::findAllVincoloPopolazione] END");
		}
		return (List<VincoloPopolazione>) popolazioneList;
	}

	@Override
	public VincoloPopolazione findVincoloPopolazioneByPk(Long idVincoloPopolazione) throws DaoException, SystemException {
		LOG.debug("[VincoloPopolazioneDaoImpl::findVincoloPopolazioneByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_VINCOLO_POPOLAZIONE, SEGNO, POPOLAZIONE, IS_VALID ");
		sql.append("FROM GFU_D_VINCOLO_POPOLAZIONE ");
		sql.append("WHERE  ");
		sql.append("ID_VINCOLO_POPOLAZIONE = :ID_VINCOLO_POPOLAZIONE ");

		VincoloPopolazione result = null;

		LOG.debug("[VincoloPopolazioneDaoImpl - findVincoloPopolazioneByPk] query =" + sql.toString());
		LOG.debug("[VincoloPopolazioneDaoImpl - findVincoloPopolazioneByPk] param  idVincoloPopolazione = " + idVincoloPopolazione);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_VINCOLO_POPOLAZIONE", idVincoloPopolazione);

		try {
			result = (VincoloPopolazione) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new VincoloPopolazioneRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[VincoloPopolazioneDaoImpl - findVincoloPopolazioneByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[VincoloPopolazioneDaoImpl::findVincoloPopolazioneByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[VincoloPopolazioneDaoImpl::findVincoloPopolazioneByPk] END");
		}
		return result;
	}

	@Override
	public VincoloPopolazione createVincoloPopolazione(VincoloPopolazione vincoloPopolazione) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_D_VINCOLO_POPOLAZIONE ( SEGNO, POPOLAZIONE, IS_VALID) " + 
					" VALUES(:SEGNO, :POPOLAZIONE, :IS_VALID) RETURNING ID_VINCOLO_POPOLAZIONE ";		

			LOG.debug("[VincoloPopolazioneDaoImpl - createVincoloPopolazione] query =" + queryInsert.toString());
			LOG.debug("[VincoloPopolazioneDaoImpl - createVincoloPopolazione] param  vincoloPopolazione = " + vincoloPopolazione);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("SEGNO", vincoloPopolazione.getSegno());
			paramMap.put("POPOLAZIONE", vincoloPopolazione.getPopolazione());
			paramMap.put("IS_VALID", vincoloPopolazione.isIsValid());

			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione]  param [SEGNO] = " + vincoloPopolazione.getSegno());
			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione]  param [POPOLAZIONE] = " + vincoloPopolazione.getPopolazione());
			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione]  param [IS_VALID] = " + vincoloPopolazione.isIsValid());

			Long idVincoloPopolazione = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			vincoloPopolazione.setIdVincoloPopolazione(idVincoloPopolazione);

			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[VincoloPopolazioneDaoImpl::createVincoloPopolazione] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[VincoloPopolazioneDaoImpl::createVincoloPopolazione] END ");
		}
		return vincoloPopolazione;
	}

	@Override
	public VincoloPopolazione updateVincoloPopolazione(VincoloPopolazione vincoloPopolazione) throws DaoException, SystemException {
		try
		{

			String queryUpdate = " UPDATE GFU_D_VINCOLO_POPOLAZIONE SET SEGNO = :SEGNO, POPOLAZIONE = :POPOLAZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_VINCOLO_POPOLAZIONE = :ID_VINCOLO_POPOLAZIONE ";		

			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("SEGNO", vincoloPopolazione.getSegno());
			paramMap.put("POPOLAZIONE", vincoloPopolazione.getPopolazione());
			paramMap.put("IS_VALID", vincoloPopolazione.isIsValid());
			paramMap.put("ID_VINCOLO_POPOLAZIONE", vincoloPopolazione.getIdVincoloPopolazione());

			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  param [ID_VINCOLO_POPOLAZIONE] = " + vincoloPopolazione.getIdVincoloPopolazione());
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  param [SEGNO] = " + vincoloPopolazione.getSegno());
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  param [POPOLAZIONE] = " + vincoloPopolazione.getPopolazione());
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  param [IS_VALID] = " + vincoloPopolazione.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex)
		{
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[VincoloPopolazioneDaoImpl::updateVincoloPopolazione] END ");
		}
		return vincoloPopolazione;	
	}

}