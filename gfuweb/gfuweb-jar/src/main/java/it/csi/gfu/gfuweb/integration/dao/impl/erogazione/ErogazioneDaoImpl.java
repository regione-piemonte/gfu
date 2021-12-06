/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.erogazione;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.erogazione.DeterminaToErogazioni;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.BaseDao;
import it.csi.gfu.gfuweb.integration.dao.erogazione.ErogazioneDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.erogazione.ErogazioneRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class ErogazioneDaoImpl extends BaseDao implements ErogazioneDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public ErogazioneDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Erogazione> findAllErogazioniToFinanziamento(Long idFinanziamento)
			throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_EROGAZIONE, FK_TIPO_EROGAZIONE, TE.DESCRIZIONE DESC_TIPO_EROGAZIONE, FK_FINANZIAMENTO, " );
		sql.append("IMPORTO_EROGAZIONE, VALUTA, NUM_DETERMINA, TO_CHAR(DATA_DETERMINA, 'DD/MM/YYYY') DATA_DETERMINA  " );
		sql.append("FROM GFU_T_EROGAZIONE E LEFT OUTER JOIN GFU_D_TIPO_EROGAZIONE TE ON E.FK_TIPO_EROGAZIONE = TE.ID_TIPO_EROGAZIONE ");
		sql.append(" WHERE FK_FINANZIAMENTO = :FK_FINANZIAMENTO ");

		LOG.debug("[ErogazioneDaoImpl - findAllErogazioniToFinanziamento] param  idFinanziamento = " + idFinanziamento);
		LOG.debug("[ErogazioneDaoImpl - findAllErogazioniToFinanziamento] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("FK_FINANZIAMENTO", idFinanziamento);

		List<Erogazione> erogazioneList = null;
		try {		
			erogazioneList = namedJdbcTemplate.query(sql.toString(), paramMap, new ErogazioneRowMapper());

			if(erogazioneList.isEmpty()) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[ErogazioneDaoImpl::findAllErogazioniToFinanziamento]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ErogazioneDaoImpl::findAllErogazioniToFinanziamento] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ErogazioneDaoImpl::findAllErogazioniToFinanziamento] END");
		}
		return (List<Erogazione>) erogazioneList;
	}

	@Override
	public void createErogazione(Long idFinanziamento, Erogazione erogazione) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_T_EROGAZIONE (FK_FINANZIAMENTO, FK_TIPO_EROGAZIONE, IMPORTO_EROGAZIONE, VALUTA, NUM_DETERMINA, DATA_DETERMINA) " + 
					" VALUES(:FK_FINANZIAMENTO, :FK_TIPO_EROGAZIONE, :IMPORTO_EROGAZIONE, '"+Constants.DB.VALUTA_EURO+"', :NUM_DETERMINA, TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY'))  RETURNING ID_EROGAZIONE ";		

			LOG.debug("[ErogazioneDaoImpl - createErogazione] query =" + queryInsert.toString());
			LOG.debug("[ErogazioneDaoImpl - createErogazione] param  idFinanziamento = " + idFinanziamento);
			LOG.debug("[ErogazioneDaoImpl - createErogazione] param  Erogazione = " + erogazione);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("FK_FINANZIAMENTO", idFinanziamento);
			paramMap.put("FK_TIPO_EROGAZIONE" , erogazione.getIdTipoErogazione());
			paramMap.put("IMPORTO_EROGAZIONE", erogazione.getImportoErogazione());
			paramMap.put("NUM_DETERMINA", erogazione.getNumDetermina());
			paramMap.put("DATA_DETERMINA", erogazione.getDataDetermina());

			LOG.debug("[ErogazioneDaoImpl::createErogazione]  param [FK_FINANZIAMENTO"+ idFinanziamento);
			LOG.debug("[ErogazioneDaoImpl::createErogazione]  param [FK_TIPO_EROGAZIONE] = " + erogazione.getIdTipoErogazione());
			LOG.debug("[ErogazioneDaoImpl::createErogazione]  param [IMPORTO_EROGAZIONE] = " + erogazione.getImportoErogazione());
			LOG.debug("[ErogazioneDaoImpl::createErogazione]  param [NUM_DETERMINA] = " + erogazione.getNumDetermina());
			LOG.debug("[ErogazioneDaoImpl::createErogazione]  param [DATA_DETERMINA] = " + erogazione.getDataDetermina());


			Long idErogazione = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			erogazione.setIdErogazione(idErogazione);

			LOG.debug("[ErogazioneDaoImpl::createErogazione]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ErogazioneDaoImpl::createErogazione]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[ErogazioneDaoImpl::createErogazione] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ErogazioneDaoImpl::createErogazione] END ");
		}

	}

	@Override
	public void updateAllErogazioniToDetermina(DeterminaToErogazioni determinaToErogazioni) throws DaoException, SystemException {
		try
		{
			List<BigDecimal> listIdFinanziamentoDaAssociare = new ArrayList<BigDecimal>();
			for (int i = 0; i < determinaToErogazioni.getFinanziamentiDaAssociare().size(); i++) {			
				determinaToErogazioni.getFinanziamentiDaAssociare().get(i).getIdFinanziamento();
				listIdFinanziamentoDaAssociare.add(determinaToErogazioni.getFinanziamentiDaAssociare().get(i).getIdFinanziamento());
			}
			if (listIdFinanziamentoDaAssociare == null || listIdFinanziamentoDaAssociare.size() == 0) {
				throw new EmptyResultDataAccessException("Nessun dato in base alla richiesta", 1);
			}

			StringBuilder queryUpdate = new StringBuilder();
			queryUpdate.append("UPDATE GFU_T_EROGAZIONE SET NUM_DETERMINA = :NUM_DETERMINA,  ");
			queryUpdate.append(" DATA_DETERMINA = TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY') ");
			queryUpdate.append("  WHERE FK_FINANZIAMENTO IN ( ");
			addBigDecimalToStringValues(queryUpdate, listIdFinanziamentoDaAssociare);
			queryUpdate.append(" ) AND ");
			queryUpdate.append(" (CASE ");
			queryUpdate.append("WHEN ( SELECT F.FK_STATO_FINANZIAMENTO FROM GFU_T_FINANZIAMENTO F WHERE F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO ) =  "+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO);
			queryUpdate.append(" THEN  ");
			queryUpdate.append(" FK_TIPO_EROGAZIONE =  "+Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.ACCONTO);
			queryUpdate.append(" WHEN (SELECT F.FK_STATO_FINANZIAMENTO FROM GFU_T_FINANZIAMENTO F WHERE F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO ) = "+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO);
			queryUpdate.append(" THEN ");
			queryUpdate.append("FK_TIPO_EROGAZIONE = "+Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.SALDO);
			queryUpdate.append(" END) ");

			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	
			paramMap.put("NUM_DETERMINA", determinaToErogazioni.getNumDetermina());
			paramMap.put("DATA_DETERMINA", determinaToErogazioni.getDataDetermina());
			
			paramMap.put("ID_FINANZIAMENTO", determinaToErogazioni.getFinanziamentiDaAssociare().get(0).getIdFinanziamento());

			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina]  param [NUM_DETERMINA] = " + determinaToErogazioni.getNumDetermina());
			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina]  param [DATA_DETERMINA] = " + determinaToErogazioni.getDataDetermina());

			namedJdbcTemplate.update(queryUpdate.toString(), paramMap);  

			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);

		}
		catch (Throwable ex) {
			LOG.error("[ErogazioneDaoImpl::updateAllErogazioniToDetermina] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[ErogazioneDaoImpl::updateAllErogazioniToDetermina] END ");
		}

	}

	@Override
	public void updateErogazione(Long idFinanziamento, Long idErogazione, Erogazione erogazione) throws DaoException, SystemException {
		try {
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  ");	
			StringBuilder sql = new StringBuilder();	

			LOG.debug("[ErogazioneDaoImpl::updateSeduta]  " );

			sql.append("UPDATE GFU_T_EROGAZIONE SET FK_FINANZIAMENTO=:FK_FINANZIAMENTO, FK_TIPO_EROGAZIONE = :FK_TIPO_EROGAZIONE, ");
			sql.append("IMPORTO_EROGAZIONE = :IMPORTO_EROGAZIONE,  NUM_DETERMINA = :NUM_DETERMINA,  DATA_DETERMINA = TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY') ");
			sql.append( " WHERE ID_EROGAZIONE=:ID_EROGAZIONE ");		

			Map<String, Object> paramMap = new HashMap<String, Object>();		

			paramMap.put("FK_FINANZIAMENTO", idFinanziamento);
			paramMap.put("FK_TIPO_EROGAZIONE", erogazione.getIdTipoErogazione());
			paramMap.put("IMPORTO_EROGAZIONE", erogazione.getImportoErogazione());
			paramMap.put("NUM_DETERMINA", erogazione.getNumDetermina());
			paramMap.put("DATA_DETERMINA", erogazione.getDataDetermina());
			paramMap.put("ID_EROGAZIONE", idErogazione);

			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  queryUpdate: = " + sql.toString());
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [FK_FINANZIAMENTO] = " + idFinanziamento);
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [FK_TIPO_EROGAZIONE] = " + erogazione.getIdTipoErogazione());
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [IMPORTO_EROGAZIONE] = " + erogazione.getImportoErogazione());
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [NUM_DETERMINA] = " + erogazione.getNumDetermina());
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [DATA_DETERMINA] = " + erogazione.getDataDetermina());
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  param [ID_EROGAZIONE] = " + idErogazione);

			namedJdbcTemplate.update(sql.toString(), paramMap);  
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  UPDATE effettuato. Stato = SUCCESS ");
		} 
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ErogazioneDaoImpl::updateErogazione]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[ErogazioneDaoImpl::updateErogazione] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ErogazioneDaoImpl::updateErogazione] END ");
		}

	}
}