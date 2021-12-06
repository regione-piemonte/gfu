/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.leggeprovvdr;

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

import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.leggeprovvdr.LeggeProvvDrDescRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.leggeprovvdr.LeggeProvvDrRowMapper;
import it.csi.gfu.gfuweb.integration.dao.leggeprovvdr.LeggeProvvDrDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class LeggeProvvDrDaoImpl extends JdbcDaoSupport implements LeggeProvvDrDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public LeggeProvvDrDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<LeggeProvvDr> findAllProvvedimentiToRichiesta(Long idRichiesta) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  LPD.ID_LEGGE_PROVV_DR, LPD.DESCRIZIONE DESC_LEGGE_PROVV_DR , LPD.FK_LEGGE, ");
		sql.append("(select L.DESCRIZIONE from gfu_d_legge l where l.id_legge = lpd.fk_legge) DESC_LEGGE, ");
		sql.append("LPD.FK_PROVVEDIMENTO, ");
		sql.append("(select P.DESCRIZIONE from gfu_d_provvedimento p where p.id_provvedimento = lpd.fk_provvedimento) DESC_PROVVEDIMENTO, ");
		sql.append("LPD.FK_DR, ");
		sql.append("(select DR.DESCRIZIONE from gfu_d_dr dr where dr.id_dr = lpd.fk_dr) DESC_DR, ");
		sql.append("LPD.FK_VINCOLO_POPOLAZIONE, ");
		sql.append("(select VP.SEGNO || ' ' || VP.POPOLAZIONE from gfu_d_vincolo_popolazione vp "); 
		sql.append("where vp.id_vincolo_popolazione = lpd.fk_vincolo_popolazione)  DESC_VINCOLO_POPOLAZIONE, ");
		sql.append("LPD.IS_VALID ");
		sql.append("FROM GFU_R_RICHIESTA_PROVV RP, ");
		sql.append("gfu_t_legge_provv_dr LPD ");
		sql.append("WHERE RP.fk_legge_provv_dr = lpd.id_legge_provv_dr ");
		sql.append("and RP.FK_RICHIESTA = :FK_RICHIESTA ");

		LOG.debug("[LeggeProvvDrDaoImpl - findAllProvvedimentiToRichiesta] param  idRichiesta = " + idRichiesta);
		LOG.debug("[LeggeProvvDrDaoImpl - findAllProvvedimentiToRichiesta] query =" + sql.toString());

		List<LeggeProvvDr> provvedimentiList = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("FK_RICHIESTA", idRichiesta );

			LOG.debug("[LeggeProvvDrDaoImpl - findAllProvvedimentiToRichiesta] param  idRichiesta = " + idRichiesta);

			provvedimentiList = namedJdbcTemplate.query(sql.toString(),paramMap, new LeggeProvvDrRowMapper());
			if(provvedimentiList.isEmpty()) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
		} 

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[LeggeProvvDrDaoImpl::findAllProvvedimentiToRichiesta]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeProvvDrDaoImpl::findAllProvvedimentiToRichiesta] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeProvvDrDaoImpl::findAllProvvedimentiToRichiesta] END");
		}
		return (List<LeggeProvvDr>) provvedimentiList;
	}

	@Override
	public long countLeggeProvvDrValid(Long idLeggeProvvDr) throws DaoException, SystemException {
		LOG.debug("[LeggeProvvDrDaoImpl::countLeggeProvvDrValid] BEGIN");

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_T_LEGGE_PROVV_DR ");
		sql.append(" WHERE ID_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR AND IS_VALID IS "+Boolean.TRUE);

		long conteggio = 0;

		LOG.debug("[LeggeProvvDrDaoImpl - countLeggeProvvDrValid] query = " + sql.toString());
		LOG.debug("[LeggeProvvDrDaoImpl - countLeggeProvvDrValid] param idLeggeProvvDr = " + idLeggeProvvDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[LeggeProvvDrDaoImpl - countLeggeProvvDrValid] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[LeggeProvvDrDaoImpl::countLeggeProvvDrValid] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[LeggeProvvDrDaoImpl::countLeggeProvvDrValid] END");
		}

		return conteggio;		
	}

	@Override
	public long countUnivocitaLeggeProvvDr(Integer idLegge, Integer idProvvedimento, Integer idDr, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		LOG.debug("[LeggeProvvDrDaoImpl::countUnivocitaLeggeProvvDr] BEGIN");

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_T_LEGGE_PROVV_DR ");
		sql.append(" WHERE FK_LEGGE= :FK_LEGGE ");
		if(idProvvedimento==null) {
			sql.append(" AND FK_PROVVEDIMENTO IS NULL ");
		}else {
			sql.append(" AND FK_PROVVEDIMENTO= :FK_PROVVEDIMENTO ");
		}

		if(idDr==null) {
			sql.append(" AND  FK_DR IS NULL ");
		}else {
			sql.append(" AND  FK_DR = :FK_DR ");
		}
		if(idLeggeProvvDr != null) {
			sql.append("AND ID_LEGGE_PROVV_DR != :ID_LEGGE_PROVV_DR ");
		}

		long conteggio = 0;

		LOG.debug("[LeggeProvvDrDaoImpl - countUnivocitaLeggeProvvDr] query = " + sql.toString());
		LOG.debug("[LeggeProvvDrDaoImpl - countUnivocitaLeggeProvvDr] param FK_LEGGE = " + idLegge);
		LOG.debug("[LeggeProvvDrDaoImpl - countUnivocitaLeggeProvvDr] param FK_PROVVEDIMENTO = " + idProvvedimento);
		LOG.debug("[LeggeProvvDrDaoImpl - countUnivocitaLeggeProvvDr] param FK_DR = " + idDr);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_LEGGE", idLegge);
		if(idProvvedimento != null) {
			paramMap.put("FK_PROVVEDIMENTO", idProvvedimento);
		}
		if(idDr != null) {
			paramMap.put("FK_DR", idDr);
		}
		if(idLeggeProvvDr != null) {
			paramMap.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr);
		}

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[LeggeProvvDrDaoImpl - countUnivocitaLeggeProvvDr] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[LeggeProvvDrDaoImpl::countUnivocitaLeggeProvvDr] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[LeggeProvvDrDaoImpl::countUnivocitaLeggeProvvDr] END");
		}

		return conteggio;	
	}

	@Override
	public void findAndUpdateLeggeProvvDr(Long idLegge, Long idProvvedimento, Long idDr) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT LPDR.ID_LEGGE_PROVV_DR,  (SELECT L.DESCRIZIONE FROM GFU_D_LEGGE L WHERE L.ID_LEGGE = LPDR.FK_LEGGE) ||  ");
		sql.append("DECODE(  (SELECT P.DESCRIZIONE FROM GFU_D_PROVVEDIMENTO P WHERE P.ID_PROVVEDIMENTO = LPDR.FK_PROVVEDIMENTO), NULL, '',(SELECT ' # '|| P.DESCRIZIONE FROM GFU_D_PROVVEDIMENTO P WHERE P.ID_PROVVEDIMENTO = LPDR.FK_PROVVEDIMENTO)) || ");
		sql.append(" DECODE( (SELECT DR.DESCRIZIONE FROM GFU_D_DR DR WHERE DR.ID_DR = LPDR.FK_DR ) ,NULL,'',(SELECT ' # '||  DR.DESCRIZIONE FROM GFU_D_DR DR WHERE DR.ID_DR = LPDR.FK_DR ) ) DESC_LEGGE_PROVV_DR ");
		sql.append("FROM GFU_T_LEGGE_PROVV_DR LPDR WHERE  ");
		if(idLegge != null) {
			sql.append("FK_LEGGE = :FK_LEGGE ");
		}else if(idProvvedimento != null) {
			sql.append("FK_PROVVEDIMENTO = :FK_PROVVEDIMENTO ");
		}else if (idDr != null) {
			sql.append("FK_DR = :FK_DR ");
		}

		LOG.debug("[LeggeProvvDrDaoImpl - findAndUpdateLeggeProvvDr] param  idLegge = " + idLegge);
		LOG.debug("[LeggeProvvDrDaoImpl - findAndUpdateLeggeProvvDr] param  idProvvedimento = " + idProvvedimento);
		LOG.debug("[LeggeProvvDrDaoImpl - findAndUpdateLeggeProvvDr] param  idDr = " + idDr);
		LOG.debug("[LeggeProvvDrDaoImpl - findAndUpdateLeggeProvvDr] query =" + sql.toString());

		List<LeggeProvvDr> provvedimentiList = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if(idLegge != null) {
				paramMap.put("FK_LEGGE", idLegge );
			}else if(idProvvedimento != null) {
				paramMap.put("FK_PROVVEDIMENTO", idProvvedimento );
			}else if (idDr != null) {
				paramMap.put("FK_DR", idDr );
			}

			provvedimentiList = namedJdbcTemplate.query(sql.toString(),paramMap, new LeggeProvvDrDescRowMapper());			

			if(provvedimentiList != null && !provvedimentiList.isEmpty()) {
				for (int i = 0; i < provvedimentiList.size(); i++) {
					StringBuilder sqlUpdateProvvedimenti = new StringBuilder();

					sqlUpdateProvvedimenti.append(" UPDATE GFU_T_LEGGE_PROVV_DR LPDR SET DESCRIZIONE = :DESCRIZIONE	");
					sqlUpdateProvvedimenti.append( "WHERE  ID_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR ");		

					paramMap.put("DESCRIZIONE",provvedimentiList.get(i).getDescLeggeProvvDr());
					paramMap.put("ID_LEGGE_PROVV_DR", provvedimentiList.get(i).getIdLeggeProvvDr());

					LOG.debug("[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr]  param [DESCRIZIONE] = " +provvedimentiList.get(i).getDescLeggeProvvDr());
					LOG.debug("[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr]  param [ID_LEGGE_PROVV_DR] = " + provvedimentiList.get(i).getIdLeggeProvvDr());

					namedJdbcTemplate.update(sqlUpdateProvvedimenti.toString(), paramMap); 
					LOG.debug("[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr]  update effettuato. Stato = SUCCESS ");} 
			} 
		}

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[LeggeProvvDrDaoImpl::findAndUpdateLeggeProvvDr] END");
		}
	}
}
