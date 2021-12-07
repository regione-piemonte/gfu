/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.richiesta;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaRichiedenteDao;
import it.csi.gfu.gfuweb.util.Constants;

@Repository
public class RichiestaRichiedenteDaoImpl extends JdbcDaoSupport implements RichiestaRichiedenteDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RichiestaRichiedenteDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public Long countRichiedentiToRichiesta(Long idRichiesta, Boolean rinuncia) throws DaoException, SystemException {
		LOG.debug("[RichiestaRichiedenteDaoImpl::countRichiedentiToRichiesta] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) ");
		sql.append("FROM GFU_R_RICHIESTA_RICHIEDENTE RR ,  ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RP ");
		sql.append("WHERE RR.FK_RICHIESTA = :ID_RICHIESTA  ");
		sql.append("AND RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE ");
		sql.append("AND RP.FLG_RINUNCIA = "+ rinuncia);

		long conteggio = 0;

		LOG.debug("[RichiestaRichiedenteDaoImpl - countRichiedentiToRichiesta] query = " + sql.toString());
		LOG.debug("[RichiestaRichiedenteDaoImpl - countRichiedentiToRichiesta] param idRichiesta = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaRichiedenteDaoImpl - countRichiedentiToRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiestaRichiedenteDaoImpl::countRichiedentiToRichiesta] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiestaRichiedenteDaoImpl::countRichiedentiToRichiesta] END");
		}

		return conteggio;			
	}

	@Override
	public long countUltimaRichiestaRichiedente(Long idRichiesta) throws DaoException, SystemException {
		LOG.debug("[RichiestaRichiedenteDaoImpl::countUltimaRichiestaRichiedente] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) ");
		sql.append("FROM GFU_R_RICHIESTA_RICHIEDENTE  ");
		sql.append("WHERE FK_RICHIESTA = :FK_RICHIESTA  ");

		long conteggio = 0;

		LOG.debug("[RichiestaRichiedenteDaoImpl - countUltimaRichiestaRichiedente] query = " + sql.toString());
		LOG.debug("[RichiestaRichiedenteDaoImpl - countUltimaRichiestaRichiedente] param idRichiesta = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_RICHIESTA", idRichiesta);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaRichiedenteDaoImpl - countUltimaRichiestaRichiedente] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiestaRichiedenteDaoImpl::countUltimaRichiestaRichiedente] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiestaRichiedenteDaoImpl::countUltimaRichiestaRichiedente] END");
		}

		return conteggio;	
	}
}