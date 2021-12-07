/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.finanziamento;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.rinuncia.Rinuncia;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.BaseDao;
import it.csi.gfu.gfuweb.integration.dao.finanziamento.FinanziamentoRinunciaDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class FinanziamentoRinunciaDaoImpl extends BaseDao implements FinanziamentoRinunciaDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public FinanziamentoRinunciaDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public Rinuncia createFinanziamentoRinuncia(Rinuncia rinuncia)
			throws DaoException, SystemException {

		Long idFinanziamentoRinuncia = null;
		try {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::createFinanziamentoRinuncia]  ");			
			Map<String, Object> paramMap = new HashMap<String, Object>();

			String queryInsert = " INSERT INTO GFU_T_FINANZIAMENTO_RINUNCIA ( IMPORTO, VALUTA, ATTO_RINUNCIA ) " + 
					"VALUES (:IMPORTO, '"+Constants.DB.VALUTA_EURO+"', :ATTO_RINUNCIA ) RETURNING ID_FINANZIAMENTO_RINUNCIA ";

			paramMap.put("IMPORTO", rinuncia.getImporto());
			paramMap.put("ATTO_RINUNCIA", rinuncia.getAttoRinuncia());

			idFinanziamentoRinuncia = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);
			rinuncia.setIdFinanziamentoRinuncia(idFinanziamentoRinuncia);

			LOG.debug("[FinanziamentoRinunciaDaoImpl::createFinanziamentoRinuncia]  Inserimento effettuato. Stato = SUCCESS ");} 
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[FinanziamentoRinunciaDaoImpl::createFinanziamentoRinuncia]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[FinanziamentoRinunciaDaoImpl::createFinanziamentoRinuncia] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::createFinanziamentoRinuncia] END ");
		}

		return rinuncia;
	}

	@Override
	public void updateFinanziamentoRinuncia(Finanziamento finanziamento) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_FINANZIAMENTO_RINUNCIA SET IMPORTO = :IMPORTO, ATTO_RINUNCIA = :ATTO_RINUNCIA "
					+ " WHERE ID_FINANZIAMENTO_RINUNCIA = :ID_FINANZIAMENTO_RINUNCIA ";		

			LOG.debug("[FinanziamentoRinunciaDaoImpl::updateFinanziamentoRinuncia]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("IMPORTO", finanziamento.getRinuncia().getImporto());
			paramMap.put("ATTO_RINUNCIA", finanziamento.getRinuncia().getAttoRinuncia());
			paramMap.put("ID_FINANZIAMENTO_RINUNCIA", finanziamento.getRinuncia().getIdFinanziamentoRinuncia());

			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamentoRinuncia]  param [ID_FINANZIAMENTO_RINUNCIA] = " + finanziamento.getRinuncia().getIdFinanziamentoRinuncia());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamentoRinuncia]  param [IMPORTO] = " + finanziamento.getRinuncia().getImporto());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamentoRinuncia]  param [ATTO_RINUNCIA] = " + finanziamento.getRinuncia().getAttoRinuncia());

			int updateResult = namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[FinanziamentoRinunciaDaoImpl::updateFinanziamentoRinuncia]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::updateFinanziamento]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::updateFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[FinanziamentoRinunciaDaoImpl::updateFinanziamento] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::updateFinanziamento] END ");
		}		
	}

	@Override
	public void deleteFinanziamentoRinuncia(Long idFinanziamentoRinuncia) throws DaoException, SystemException {
		try {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::deleteFinanziamentoRinuncia] idFinanziamentoRinuncia = " + idFinanziamentoRinuncia);
		
			String queryDelete ="DELETE FROM GFU_T_FINANZIAMENTO_RINUNCIA WHERE ID_FINANZIAMENTO_RINUNCIA = :ID_FINANZIAMENTO_RINUNCIA  ";
			Map<String, Object> paramMapDelete = new HashMap<String, Object>();
			paramMapDelete.put("ID_FINANZIAMENTO_RINUNCIA", idFinanziamentoRinuncia);

			namedJdbcTemplate.update(queryDelete, paramMapDelete);

			LOG.debug("[FinanziamentoRinunciaDaoImpl::deleteFinanziamentoRinuncia] queryDelete  = " + queryDelete);
			LOG.debug("[FinanziamentoRinunciaDaoImpl::deleteFinanziamentoRinuncia]  eliminazione effettuata. Stato = SUCCESS ");
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
					"[FinanziamentoRinunciaDaoImpl::deleteFinanziamentoRinuncia] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[FinanziamentoRinunciaDaoImpl::deleteFinanziamentoRinuncia] END ");
		}
		
	}
}