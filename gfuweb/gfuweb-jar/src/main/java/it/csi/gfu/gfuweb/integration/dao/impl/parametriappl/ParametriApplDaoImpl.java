/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.parametriappl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.parametriappl.ParametroAppl;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.parametriappl.ParametroApplRowMapper;
import it.csi.gfu.gfuweb.integration.dao.parametriappl.ParametriApplDao;
import it.csi.gfu.gfuweb.util.Constants;

@Repository
public class ParametriApplDaoImpl extends JdbcDaoSupport implements ParametriApplDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public ParametriApplDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public ParametroAppl findParametroApplByKey(String key) throws DaoException, SystemException {
		LOG.debug("[ParametriApplDaoImpl::findParametroApplByKey] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PARAMETRO, KEY, VALORE, DESCRIZIONE FROM GFU_T_PARAMETRI_APPL  WHERE KEY = :KEY ");

		ParametroAppl result = null;

		LOG.debug("[ParametriApplDaoImpl - findParametroApplByKey] query =" + sql.toString());
		LOG.debug("[ParametriApplDaoImpl - findParametroApplByKey] param  key = " + key);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("KEY", key);

		try {
			result = (ParametroAppl) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new ParametroApplRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[ParametriApplDaoImpl - findParametroApplByKey] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch(IncorrectResultSizeDataAccessException ex)
		{
			LOG.debug("[ParametriApplDaoImpl - findParametroApplByKey] RISULTATO ERRATO");
			throw new DaoException("Risultato errato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ParametriApplDaoImpl::findParametroApplByKey] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ParametriApplDaoImpl::findParametroApplByKey] END");
		}
		return result;
	}

}