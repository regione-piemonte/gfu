/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.comuneregpie;

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

import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.comuneregpie.ComuneRegPieDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.comuneregpie.ComuneRegPieRowMapper;
import it.csi.gfu.gfuweb.util.Constants;

@Repository
public class ComuneRegPieDaoImpl extends JdbcDaoSupport implements ComuneRegPieDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public ComuneRegPieDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Comune> findAllComuni(String descComune, Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT   A.ISTAT_COMUNE, DESC_COMUNE,  SIGLA_PROV, DESC_PROVINCIA, POP_TOTALE " );
		sql.append("  FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A, YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE B  " );
		sql.append(" WHERE ");  
		sql.append(" (:DESC_COMUNE::VARCHAR IS NULL OR UPPER(DESC_COMUNE) LIKE UPPER(:DESC_COMUNE::VARCHAR))  ");    
		sql.append(" AND   A.ISTAT_COMUNE = B.ISTAT_COMUNE ");
		sql.append(" AND B.ANNO = (SELECT MAX(ANNO) FROM YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE) ");
		sql.append(" AND ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'") ;


		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" AND R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
				sql.append(" AND D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");
			}else {
				//TODO
			}
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (descComune != null) {
			paramMap.put("DESC_COMUNE", "%"+ descComune + "%");
		} else {
			paramMap.put("DESC_COMUNE", descComune );
		}

		LOG.debug("[ComuneRegPieDaoImpl - findAllComuni] param  isValid = " + isValid);
		LOG.debug("[ComuneRegPieDaoImpl - findAllComuni] query =" + sql.toString());

		List<Comune> comuneList = null;
		try {		
			comuneList = namedJdbcTemplate.query(sql.toString(), paramMap, new ComuneRegPieRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[ComuneRegPieDaoImpl::findAllComuni]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ComuneRegPieDaoImpl::findAllComuni] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ComuneRegPieDaoImpl::findAllComuni] END");
		}
		return (List<Comune>) comuneList;
	}

	@Override
	public Comune findComuneByPk(String codIstat) throws DaoException, SystemException {
		LOG.debug("[ComuneRegPieDaoImpl::findComuneByPk] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT   A.ISTAT_COMUNE, DESC_COMUNE,  SIGLA_PROV, DESC_PROVINCIA, POP_TOTALE " );
		sql.append(" FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A, YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE B  " );
		sql.append(" WHERE A.ISTAT_COMUNE = :ISTAT_COMUNE " );
		sql.append(" AND A.ISTAT_COMUNE = B.ISTAT_COMUNE   " );
		sql.append(" AND B.ANNO = (SELECT MAX(ANNO) FROM YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE)  " );
		sql.append(" AND R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
		sql.append(" AND D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");

		Comune result = null;

		LOG.debug("[ComuneRegPieDaoImpl - findComuneByPk] query =" + sql.toString());
		LOG.debug("[ComuneRegPieDaoImpl - findComuneByPk] param  codIstat = " + codIstat);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ISTAT_COMUNE", codIstat);

		try {
			result = (Comune) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new ComuneRegPieRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[ComuneRegPieDaoImpl - findComuneByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ComuneRegPieDaoImpl::findComuneByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ComuneRegPieDaoImpl::findComuneByPk] END");
		}
		return result;
	}
}