/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.ProfiloDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.ProfiloRowMapper;
import it.csi.gfu.gfuweb.util.Constants;

@Repository
public class ProfiloDaoImpl extends JdbcDaoSupport implements ProfiloDao {
	
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);
	
	private NamedParameterJdbcTemplate namedJdbcTemplate; 
		
	@Autowired
	public ProfiloDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}
	
	
	@Override
	public Profilo findProfiloById(BigDecimal idProfilo) throws DaoException, SystemException {
		LOG.debug("[ProfiloDaoImpl::findProfiloById] BEGIN");
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id_profilo, descrizione descrizione_profilo, cod_profilo ");
		sql.append("FROM gfu_d_profilo ");
		sql.append("WHERE  ");
		sql.append("id_profilo = :id_profilo ");

		Profilo app = null;
		
		LOG.debug("[ProfiloDaoImpl - findProfiloById] query =" + sql.toString());
		LOG.debug("[ProfiloDaoImpl - findProfiloById] param  idProfilo = " + idProfilo);

		Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id_profilo", idProfilo);
        
		try {
		app = (Profilo) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new ProfiloRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[ProfiloDaoImpl - findProfiloById] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProfiloDaoImpl::findProfiloById] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProfiloDaoImpl::findProfiloById] END");
		}
		return app;
	}


	@Override
	public List<Profilo> findProfilo() throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id_profilo, descrizione descrizione_profilo, cod_profilo ");
		sql.append("FROM gfu_d_profilo ");
		sql.append("ORDER BY descrizione_profilo ");

		LOG.debug("[ProfiloDaoImpl - findProfilo] query =" + sql.toString());

		List<Profilo> profiloList = null;
		try {
			profiloList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new ProfiloRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[ProfiloDaoImpl::findProfilo]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProfiloDaoImpl::findProfilo] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProfiloDaoImpl::findProfilo] END");
		}
		return (List<Profilo>) profiloList;
	}
	
}

