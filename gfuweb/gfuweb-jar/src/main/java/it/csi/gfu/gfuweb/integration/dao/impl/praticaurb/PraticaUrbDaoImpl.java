/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.praticaurb;

import java.util.HashMap;
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

import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.praticaurb.PraticaUrbDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class PraticaUrbDaoImpl extends JdbcDaoSupport implements PraticaUrbDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public PraticaUrbDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public PraticaUrbGfu createPraticaUrbGfu(Long idFinanziamento, PraticaUrbGfu praticaUrbGfu)
			throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_T_PRATICA_URB(NUM_PRATICA_URB, FK_FINANZIAMENTO, NUM_ATTO_APPROVAZIONE_URB, DATA_ATTO_APPROVAZIONE_URB) " + 
					" VALUES(:NUM_PRATICA_URB, :FK_FINANZIAMENTO, :NUM_ATTO_APPROVAZIONE_URB, TO_DATE(:DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY')) RETURNING ID_PRATICA_URB ";		

			LOG.debug("[PraticaUrbDaoImpl - createPraticaUrbGfu] query =" + queryInsert.toString());
			LOG.debug("[PraticaUrbDaoImpl - createPraticaUrbGfu] param  idFinanziamento = " + idFinanziamento);
			LOG.debug("[PraticaUrbDaoImpl - createPraticaUrbGfu] param  praticaUrbGfu = " + praticaUrbGfu);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("NUM_PRATICA_URB",  praticaUrbGfu.getNumPraticaUrb());
			paramMap.put("FK_FINANZIAMENTO" , idFinanziamento);
			paramMap.put("NUM_ATTO_APPROVAZIONE_URB", praticaUrbGfu.getNumAttoApprovazioneUrb());
			paramMap.put("DATA_ATTO_APPROVAZIONE_URB", praticaUrbGfu.getDataAttoApprovazioneUrb());

			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  param [NUM_PRATICA_URB"+ praticaUrbGfu.getNumPraticaUrb());
			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  param [FK_FINANZIAMENTO] = " + idFinanziamento);
			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  param [NUM_ATTO_APPROVAZIONE_URB] = " + praticaUrbGfu.getNumAttoApprovazioneUrb());
			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  param [DATA_ATTO_APPROVAZIONE_URB] = " + praticaUrbGfu.getDataAttoApprovazioneUrb());

			Long idPraticaUrb = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			praticaUrbGfu.setIdPraticaUrb(idPraticaUrb);

			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[PraticaUrbDaoImpl::createPraticaUrbGfu] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[PraticaUrbDaoImpl::createPraticaUrbGfu] END ");
		}
		return praticaUrbGfu;
	}

	@Override
	public void deletePraticaUrbGfu(Long idFinanziamento) throws DaoException, SystemException {
		try {
			LOG.debug("[PraticaUrbDaoImpl::deletePraticaUrbGfu] idFinanziamento = " + idFinanziamento);

			String queryDelete ="DELETE FROM GFU_T_PRATICA_URB WHERE FK_FINANZIAMENTO = :FK_FINANZIAMENTO ";
			Map<String, Object> paramMapDelete = new HashMap<String, Object>();
			paramMapDelete.put("FK_FINANZIAMENTO", idFinanziamento);

			namedJdbcTemplate.update(queryDelete, paramMapDelete);

			LOG.debug("[PraticaUrbDaoImpl::deletePraticaUrbGfu] queryDelete  = " + queryDelete);
			LOG.debug("[PraticaUrbDaoImpl::deletePraticaUrbGfu]  eliminazione effettuata. Stato = SUCCESS ");
		} 
		catch(DataIntegrityViolationException ex)
		{
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[PraticaUrbDaoImpl::deletePraticaUrbGfu] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[PraticaUrbDaoImpl::deletePraticaUrbGfu] END ");
		}
	}

	@Override
	public long countUnivocitaFinanziamento(Long idFinanziamento) throws DaoException, SystemException {
		LOG.debug("[PraticaUrbDaoImpl::countUnivocitaNumProtDataProt] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append(" SELECT COUNT(*) FROM GFU_T_PRATICA_URB ");
		sql.append(" WHERE FK_FINANZIAMENTO = :FK_FINANZIAMENTO ");

		long conteggio = 0;

		LOG.debug("[PraticaUrbDaoImpl - countUnivocitaFinanziamento] query = " + sql.toString());
		LOG.debug("[PraticaUrbDaoImpl - countUnivocitaFinanziamento] param FK_FINANZIAMENTO = " + idFinanziamento);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_FINANZIAMENTO", idFinanziamento);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[PraticaUrbDaoImpl - countUnivocitaFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[PraticaUrbDaoImpl::countUnivocitaFinanziamento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[PraticaUrbDaoImpl::countUnivocitaFinanziamento] END");
		}

		return conteggio;
	}
}