/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.statofinanziamento;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.BaseDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.statofinanziamento.StatoFinanziamentoRowMapper;
import it.csi.gfu.gfuweb.integration.dao.statofinanziamento.StatoFinanziamentoDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class StatoFinanziamentoDaoImpl extends BaseDao implements StatoFinanziamentoDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public StatoFinanziamentoDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public StatoFinanziamento findStatoFinanziamento(Long idFinanziamento)
			throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT SF.ID_STATO_FINANZIAMENTO, SF.DESCRIZIONE DESC_STATO_FINANZIAMENTO, SF.COD_STATO_FINANZIAMENTO " );
		sql.append("FROM GFU_T_FINANZIAMENTO F LEFT OUTER JOIN GFU_D_STATO_FINANZIAMENTO SF ON F.FK_STATO_FINANZIAMENTO = SF.ID_STATO_FINANZIAMENTO " );
		sql.append("WHERE ID_FINANZIAMENTO = :ID_FINANZIAMENTO ");

		LOG.debug("[StatoFinanziamentoDaoImpl - findStatoFinanziamento] param  idFinanziamento = " + idFinanziamento);
		LOG.debug("[StatoFinanziamentoDaoImpl - findStatoFinanziamento] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("ID_FINANZIAMENTO", idFinanziamento);

		StatoFinanziamento statoFinanziamento = null;
		try {		
			statoFinanziamento = (StatoFinanziamento) namedJdbcTemplate.queryForObject(sql.toString(), paramMap, new StatoFinanziamentoRowMapper());
			if(statoFinanziamento == null ) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[StatoFinanziamentoDaoImpl::findStatoFinanziamento]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[StatoFinanziamentoDaoImpl::findStatoFinanziamento] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[StatoFinanziamentoDaoImpl::findStatoFinanziamento] END");
		}
		return (StatoFinanziamento) statoFinanziamento;
	}

	@Override
	public void updateStatoFinToFinanziamento(Long idFinanziamento, Long idStatoFinanziamento, Long idStatoFinanziamentoPrec)
			throws DaoException, SystemException {
		try
		{
			StringBuilder sql = new StringBuilder();	

			sql.append(" UPDATE GFU_T_FINANZIAMENTO SET FK_STATO_FINANZIAMENTO = :FK_STATO_FINANZIAMENTO , FK_STATO_FINANZ_PREC = :FK_STATO_FINANZ_PREC ");
			sql.append(" WHERE ID_FINANZIAMENTO = :ID_FINANZIAMENTO ");

			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  queryUpdate: = " + sql.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FK_STATO_FINANZIAMENTO", idStatoFinanziamento);
			paramMap.put("ID_FINANZIAMENTO", idFinanziamento);
			paramMap.put("FK_STATO_FINANZ_PREC", idStatoFinanziamentoPrec);

			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  param [idFinanziamento] = " + idFinanziamento);
			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  param [ID_STATO_FINANZIAMENTO] = " + idStatoFinanziamento);
			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  param [FK_STATO_FINANZ_PREC] = " + idStatoFinanziamentoPrec);

			namedJdbcTemplate.update(sql.toString(), paramMap);  

			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[StatoFinanziamentoDaoImpl::updateStatoFinToFinanziamento] END ");
		}
	}
}