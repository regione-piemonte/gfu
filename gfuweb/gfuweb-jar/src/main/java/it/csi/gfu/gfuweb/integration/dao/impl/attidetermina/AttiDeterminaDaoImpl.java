/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.attidetermina;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.attidetermina.AttiDeterminaDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.determina.DeterminaRowMapper;
import it.csi.gfu.gfuweb.util.Constants;

@Repository
public class AttiDeterminaDaoImpl extends JdbcDaoSupport implements AttiDeterminaDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public AttiDeterminaDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public Long countDeterminaByNumAndDataDetermina(String numDetermina, String dataDetermina)
			throws DaoException, SystemException {
		LOG.debug("[AttiDeterminaDaoImpl::countDeterminaByNumAndDataDetermina] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("select COUNT(*) from atti_t_determina atti where atti.numero = :NUM_DETERMINA::NUMERIC and atti.data= TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY')  ");

		long conteggio = 0;

		LOG.debug("[AttiDeterminaDaoImpl - countDeterminaByNumAndDataDetermina] query =" + sql.toString());
		LOG.debug("[AttiDeterminaDaoImpl - countDeterminaByNumAndDataDetermina] param  numDetermina = " + numDetermina);
		LOG.debug("[AttiDeterminaDaoImpl - countDeterminaByNumAndDataDetermina] param  dataDetermina = " + dataDetermina);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("NUM_DETERMINA", Integer.parseInt(numDetermina));
		paramMap.put("DATA_DETERMINA", dataDetermina);

		try {
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[AttiDeterminaDaoImpl - countDeterminaByNumAndDataDetermina] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[AttiDeterminaDaoImpl::countDeterminaByNumAndDataDetermina] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[AttiDeterminaDaoImpl::countDeterminaByNumAndDataDetermina] END");
		}
		return conteggio;
	}

	@Override
	public Determina findDeterminaByNumAndData(String numDetermina, String dataDetermina)
			throws DaoException, SystemException {
		LOG.debug("[AttiDeterminaDaoImpl::findDeterminaByNumAndData] BEGIN");


		StringBuilder sql = new StringBuilder();

		sql.append("SELECT D.NUMERO NUMERO_DETERMINA, TO_CHAR(D.DATA, 'DD/MM/YYYY') DATA_DETERMINA, D.URL ");
		sql.append("FROM ATTI_T_DETERMINA D ");
		sql.append("WHERE  ");
		sql.append("D.NUMERO = :NUMERO_DETERMINA AND D.DATA = TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY') ");

		Determina determina = null;

		LOG.debug("[AttiDeterminaDaoImpl - findDeterminaByNumAndData] query =" + sql.toString());
		LOG.debug("[AttiDeterminaDaoImpl - findDeterminaByNumAndData] param  numDetermina = " + numDetermina);
		LOG.debug("[AttiDeterminaDaoImpl - findDeterminaByNumAndData] param  dataDetermina = " + dataDetermina);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("NUMERO_DETERMINA", Integer.parseInt(numDetermina));
		paramMap.put("DATA_DETERMINA", dataDetermina);

		try {
			determina = (Determina) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new DeterminaRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[AttiDeterminaDaoImpl - findDeterminaByNumAndData] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[AttiDeterminaDaoImpl::findDeterminaByNumAndData] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[AttiDeterminaDaoImpl::findDeterminaByNumAndData] END");
		}
		return determina;
	}
}
