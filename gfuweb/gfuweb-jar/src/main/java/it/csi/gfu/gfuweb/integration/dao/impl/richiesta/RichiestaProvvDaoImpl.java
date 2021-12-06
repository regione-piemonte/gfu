/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.richiesta;

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
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta.RichiestaProvvRowMapper;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaProvvDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class RichiestaProvvDaoImpl extends JdbcDaoSupport implements RichiestaProvvDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RichiestaProvvDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public Long countAssociazioneRichiestaProvv(Long idRichiesta, Long idLeggeProvvDr) throws DaoException, SystemException {
		LOG.debug("[RichiestaProvvDaoImpl::countAssociazioneRichiestaProvv] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_R_RICHIESTA_PROVV ");
		sql.append(" WHERE ");
		sql.append(" FK_RICHIESTA = :ID_RICHIESTA ");
		sql.append(" AND FK_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR ");

		long conteggio = 0;

		LOG.debug("[RichiestaProvvDaoImpl - countAssociazioneRichiestaProvv] query = " + sql.toString());
		LOG.debug("[RichiestaProvvDaoImpl - countAssociazioneRichiestaProvv] param idRichiesta = " + idRichiesta);
		LOG.debug("[RichiestaProvvDaoImpl - countAssociazioneRichiestaProvv] param idLeggeProvvDr = " + idLeggeProvvDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);
		paramMap.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaProvvDaoImpl - countAssociazioneRichiestaProvv] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiestaProvvDaoImpl::countAssociazioneRichiestaProvv] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiestaProvvDaoImpl::countAssociazioneRichiestaProvv] END");
		}

		return conteggio;			
	}

	@Override
	public List<RichiestaProvv> findAllRichiestaProvvToRichiesta(Long idRichiesta)
			throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT FK_RICHIESTA, FK_LEGGE_PROVV_DR FROM GFU_R_RICHIESTA_PROVV WHERE FK_RICHIESTA = :FK_RICHIESTA ");

		LOG.debug("[RichiestaProvvDaoImpl - findAllRichiestaProvvToRichiesta] param  idRichiesta = " + idRichiesta);
		LOG.debug("[RichiestaProvvDaoImpl - findAllRichiestaProvvToRichiesta] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("FK_RICHIESTA", idRichiesta);

		List<RichiestaProvv> richiestaProvvList = null;
		try {		
			richiestaProvvList = namedJdbcTemplate.query(sql.toString(), paramMap, new RichiestaProvvRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RichiestaProvvDaoImpl::findAllRichiestaProvvToRichiesta]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaProvvDaoImpl::findAllRichiestaProvvToRichiesta] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaProvvDaoImpl::findAllRichiestaProvvToRichiesta] END");
		}
		return (List<RichiestaProvv>) richiestaProvvList;
	}

	@Override
	public void updateFkFinanziamento(Long fkFinanziamento, Long idRichiesta, Long idLeggeProvvDr) throws DaoException, SystemException {
		try
		{

			String queryUpdate = " UPDATE GFU_R_RICHIESTA_PROVV SET FK_FINANZIAMENTO = :FK_FINANZIAMENTO "
					+ "WHERE FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR AND FK_RICHIESTA = :FK_RICHIESTA ";

			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FK_FINANZIAMENTO", fkFinanziamento);
			paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
			paramMap.put("FK_RICHIESTA", idRichiesta);

			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  param [FK_FINANZIAMENTO] = " + fkFinanziamento);
			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  param [FK_LEGGE_PROVV_DR] = " + idLeggeProvvDr);
			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  param [FK_RICHIESTA] = " + idRichiesta);

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiestaProvvDaoImpl::updateFkFinanziamento] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiestaProvvDaoImpl::updateFkFinanziamento] END ");
		}

	}

	@Override
	public List<Long> findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr)throws DaoException, SystemException {
		LOG.debug("[RichiestaProvvDaoImpl::findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT FK_FINANZIAMENTO ");
		sql.append("FROM GFU_R_RICHIESTA_PROVV ");
		sql.append("WHERE  ");
		sql.append("FK_RICHIESTA = :FK_RICHIESTA ");
		if(idLeggeProvvDr != null) {
			sql.append(" AND FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR ");
		}

		List<Long> idFinanziamentoList = null;

		LOG.debug("[RichiestaProvvDaoImpl - findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] query =" + sql.toString());
		LOG.debug("[RichiestaProvvDaoImpl - findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] param  idRichiesta = " + idRichiesta);
		LOG.debug("[RichiestaProvvDaoImpl - findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] param  idLeggeProvvDr = " + idLeggeProvvDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_RICHIESTA", idRichiesta);
		if(idLeggeProvvDr != null) {
			paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
		}

		try {
			idFinanziamentoList = namedJdbcTemplate.queryForList(sql.toString(), paramMap, Long.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaProvvDaoImpl - findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaProvvDaoImpl::findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaProvvDaoImpl::findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta] END");
		}
		return idFinanziamentoList;
	}


	@Override
	public void deleteRichiestaProvv(Long idRichiesta) throws DaoException, SystemException {
		try {
			LOG.debug("[RichiestaProvvDaoImpl::deleteRichiestaProvv] idRichiesta = " + idRichiesta);

			String queryRelationDelete ="DELETE FROM GFU_R_RICHIESTA_PROVV WHERE FK_RICHIESTA = :FK_RICHIESTA  ";

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("FK_RICHIESTA", idRichiesta);

			namedJdbcTemplate.update(queryRelationDelete, paramMap);  	

			LOG.debug("[RichiestaProvvDaoImpl::deleteRichiestaProvv] queryRelationDelete  = " + queryRelationDelete);
			LOG.debug("[RichiestaProvvDaoImpl::deleteRichiestaProvv]  eliminazione effettuata. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaProvvDaoImpl::deleteRichiestaProvv] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaProvvDaoImpl::deleteRichiestaProvv] END ");
		}
	}

}