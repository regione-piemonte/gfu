/*******************************************************************************
 * © Copyright Regione Piemonte – 2021
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 ******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.richiedente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.provvedimento.ProvvedimentiToRichiedente;
import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.dto.richiedente.RichiedenteProvv;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiedente.RichiedenteRowMapper;
import it.csi.gfu.gfuweb.integration.dao.richiedente.RichiedenteDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class RichiedenteDaoImpl extends JdbcDaoSupport implements RichiedenteDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RichiedenteDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Richiedente> findAllRichiedenti(Long idRichiesta) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT RD.ID_RICHIEDENTE, RD.ISTAT_COMUNE,  " );
		sql.append("DECODE  (A.DESC_COMUNE, NULL, " );
		sql.append("(SELECT A2.DESC_COMUNE FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 WHERE A2.ISTAT_COMUNE = RD.ISTAT_COMUNE AND "  );
		sql.append("A2.D_STOP = (SELECT MAX(D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A3 WHERE A3.ISTAT_COMUNE = A2.ISTAT_COMUNE  )),A.DESC_COMUNE)DESC_COMUNE , "  ); 
		sql.append(" RD.POPOLAZIONE, A.DESC_PROVINCIA, A.SIGLA_PROV " );
		sql.append("FROM GFU_T_RICHIEDENTE RD,	GFU_R_RICHIESTA_RICHIEDENTE RR, " );
		sql.append("GFU_T_RICHIESTA RS,	YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A " );
		sql.append("WHERE " );
		sql.append("RD.ID_RICHIEDENTE = RR.FK_RICHIEDENTE " );
		sql.append("AND RR.FK_RICHIESTA = RS.ID_RICHIESTA " );
		sql.append("AND A.ISTAT_COMUNE = RD.ISTAT_COMUNE " );
		sql.append("AND RS.ID_RICHIESTA = :ID_RICHIESTA  ");
		sql.append(" AND A.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"' ") ;
		sql.append("AND A.D_STOP = ( ");	
		sql.append("SELECT MAX(A2.D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2  ");	
		sql.append("WHERE A2.ISTAT_COMUNE = A.ISTAT_COMUNE AND  ");	
		sql.append("A2.D_START <=  ");	
		sql.append("(SELECT R2.DATA_PROTOCOLLO FROM GFU_T_RICHIESTA R2 WHERE R2.ID_RICHIESTA = RR.FK_RICHIESTA  ))  ");		

		LOG.debug("[RichiedenteDaoImpl - findAllRichiedenti] param  idRichiesta = " + idRichiesta);
		LOG.debug("[RichiedenteDaoImpl - findAllRichiedenti] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);

		List<Richiedente> richiedenteList = null;
		try {
			richiedenteList = namedJdbcTemplate.query(sql.toString(),paramMap, new RichiedenteRowMapper());

			if(richiedenteList.isEmpty()) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
		} 

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RichiedenteDaoImpl::findAllRichiedenti]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteDaoImpl::findAllRichiedenti] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteDaoImpl::findAllRichiedenti] END");
		}
		return (List<Richiedente>) richiedenteList;
	}

	@Override
	public Long countRichiedentiToRichiesta(Long idRichiesta) throws DaoException, SystemException {
		LOG.debug("[RichiedenteDaoImpl::findRichiedentiToRichiesta] BEGIN");

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_R_RICHIESTA_RICHIEDENTE ");
		sql.append(" WHERE FK_RICHIESTA = :ID_RICHIESTA ");

		long conteggio = 0;

		LOG.debug("[RichiedenteDaoImpl - findRichiedentiToRichiesta] query = " + sql.toString());
		LOG.debug("[RichiedenteDaoImpl - findRichiedentiToRichiesta] param idRichiesta = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteDaoImpl - findRichiedentiToRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiedenteDaoImpl::findRichiedentiToRichiesta] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiedenteDaoImpl::findRichiedentiToRichiesta] END");
		}

		return conteggio;			
	}

	@Override
	public List<Richiedente> createRichiedente(Long idRichiesta, List<Richiedente> listRichiedente)
			throws DaoException, SystemException {
		try {
			LOG.debug("[RichiedenteDaoImpl::createRichiedente]  ");			

			for (int i = 0; i < listRichiedente.size(); i++) {

				Map<String, Object> paramMapRichiedente = new HashMap<String, Object>();
				Map<String, Object> paramMapRichiestaRichiedente = new HashMap<String, Object>();
				Richiedente richiedente = listRichiedente.get(i);

				String queryInsertRichiedente = " INSERT INTO GFU_T_RICHIEDENTE (ISTAT_COMUNE, POPOLAZIONE) " + 
						" VALUES(:ISTAT_COMUNE, :POPOLAZIONE) RETURNING ID_RICHIEDENTE ";		

				paramMapRichiedente.put("ISTAT_COMUNE", richiedente.getIstatComune());
				paramMapRichiedente.put("POPOLAZIONE", richiedente.getPopolazione());

				LOG.debug("[RichiedenteDaoImpl::createRichiedente]  param [idRichiesta] = " + idRichiesta);
				LOG.debug("[RichiedenteDaoImpl::createRichiedente]  param [IstatComune] = " + richiedente.getIstatComune());
				LOG.debug("[RichiedenteDaoImpl::createRichiedente]  param [Popolazione] = " + richiedente.getPopolazione());

				Long idRichiedente = namedJdbcTemplate.queryForObject(queryInsertRichiedente, paramMapRichiedente, Long.class);

				String queryInsertRichiestaRichiedente = " INSERT INTO GFU_R_RICHIESTA_RICHIEDENTE(FK_RICHIESTA, FK_RICHIEDENTE) VALUES(:FK_RICHIESTA,:FK_RICHIEDENTE) ";		

				paramMapRichiestaRichiedente.put("FK_RICHIESTA", idRichiesta);
				paramMapRichiestaRichiedente.put("FK_RICHIEDENTE", idRichiedente);

				LOG.debug("[RichiedenteDaoImpl::createRichiedente]  param [idRichiesta] = " + idRichiesta);
				LOG.debug("[RichiedenteDaoImpl::createRichiedente]  param [idRichiedente] = " + idRichiedente);

				namedJdbcTemplate.update(queryInsertRichiestaRichiedente, paramMapRichiestaRichiedente);  

				listRichiedente.get(i).setIdRichiedente(idRichiedente);
			}

			LOG.debug("[RichiedenteDaoImpl::createRichiedente]  Inserimento effettuato. Stato = SUCCESS ");} 
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[RichiedenteDaoImpl::createRichiedente]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteDaoImpl::createRichiedente] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteDaoImpl::createRichiedente] END ");
		}

		return listRichiedente;
	}

	@Override
	public Richiedente findRichiedenteByPk(Long idRichiedente) throws DaoException, SystemException {
		LOG.debug("[RichiedenteDaoImpl::findRichiedenteByPk] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT	RD.ID_RICHIEDENTE,RD.ISTAT_COMUNE, " );
		sql.append("	DECODE (A.DESC_COMUNE, NULL, "); 
		sql.append("	(SELECT A2.DESC_COMUNE FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 WHERE A2.ISTAT_COMUNE = RD.ISTAT_COMUNE AND " ); 
		sql.append("	A2.D_STOP = (SELECT MAX(D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A3 WHERE A3.ISTAT_COMUNE = A2.ISTAT_COMUNE  )),A.DESC_COMUNE)DESC_COMUNE , " ); 
		sql.append("RD.POPOLAZIONE, " );
		sql.append("A.DESC_PROVINCIA,A.SIGLA_PROV " );
		sql.append("FROM  " );
		sql.append("GFU_T_RICHIEDENTE RD,	YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A, GFU_R_RICHIESTA_RICHIEDENTE RR, GFU_T_RICHIESTA RS " );
		sql.append("WHERE A.ISTAT_COMUNE = RD.ISTAT_COMUNE ");
		sql.append("AND ID_RICHIEDENTE = :ID_RICHIEDENTE ");
		sql.append(" AND A.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"' ") ;
		sql.append("AND RD.ID_RICHIEDENTE = RR.FK_RICHIEDENTE  ");
		sql.append("AND RR.FK_RICHIESTA = RS.ID_RICHIESTA ");
		sql.append("AND A.D_STOP = ( ");
		sql.append("SELECT MAX(A2.D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 ");
		sql.append("WHERE A2.ISTAT_COMUNE = A.ISTAT_COMUNE AND ");
		sql.append("A2.D_START <=  ");
		sql.append("(SELECT R2.DATA_PROTOCOLLO FROM GFU_T_RICHIESTA R2 WHERE R2.ID_RICHIESTA = RR.FK_RICHIESTA  ))  ");

		Richiedente result = null;

		LOG.debug("[RichiedenteDaoImpl - findRichiedenteByPk] query =" + sql.toString());
		LOG.debug("[RichiedenteDaoImpl - findRichiedenteByPk] param  idRichiedente = " + idRichiedente);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIEDENTE", idRichiedente);

		try {
			result = (Richiedente) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new RichiedenteRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteDaoImpl - findRichiedenteByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch(IncorrectResultSizeDataAccessException ex)
		{
			LOG.debug("[RichiedenteDaoImpl - findRichiedenteByPk] RISULTATO ERRATO");
			throw new DaoException("Risultato errato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteDaoImpl::findRichiedenteByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteDaoImpl::findRichiedenteByPk] END");
		}
		return result;
	}

	@Override
	public void deleteRichiedente(Long idRichiesta, Long idRichiedente) throws DaoException, SystemException {

		try {
			LOG.debug("[RichiedenteDaoImpl::deleteRichiedente] idRichiesta = " + idRichiesta);
			LOG.debug("[RichiedenteDaoImpl::deleteRichiedente] idRichiedente = " + idRichiedente);

			String queryRelationDelete ="DELETE FROM GFU_R_RICHIESTA_RICHIEDENTE WHERE FK_RICHIESTA =:FK_RICHIESTA AND FK_RICHIEDENTE =:FK_RICHIEDENTE ";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("FK_RICHIESTA", idRichiesta);
			paramMap.put("FK_RICHIEDENTE", idRichiedente);

			namedJdbcTemplate.update(queryRelationDelete, paramMap);  	

			LOG.debug("[RichiedenteDaoImpl::deleteRichiedente] queryRelationDelete  = " + queryRelationDelete);

			String queryDelete ="DELETE FROM GFU_T_RICHIEDENTE WHERE ID_RICHIEDENTE =:ID_RICHIEDENTE ";
			Map<String, Object> paramMapDelete = new HashMap<String, Object>();
			paramMapDelete.put("ID_RICHIEDENTE", idRichiedente);

			namedJdbcTemplate.update(queryDelete, paramMapDelete);

			LOG.debug("[RichiedenteDaoImpl::deleteRichiedente]  eliminazione effettuata. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());	
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiedenteDaoImpl::deleteRichiedente] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiedenteDaoImpl::deleteRichiedente] END ");
		}
	}

	@Override
	public List<RichiedenteProvv> findAllRichiedenteProvv(Long idRichiesta) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT RP.FK_RICHIEDENTE, R.ISTAT_COMUNE, ");
		sql.append("DECODE  (A.DESC_COMUNE, NULL, " );
		sql.append("		(SELECT A2.DESC_COMUNE FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 WHERE A2.ISTAT_COMUNE = R.ISTAT_COMUNE AND " );
		sql.append("		A2.D_STOP = (SELECT MAX(D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A3 WHERE A3.ISTAT_COMUNE = A2.ISTAT_COMUNE  )),A.DESC_COMUNE)DESC_COMUNE , " ); 
		sql.append(" A.SIGLA_PROV, R.POPOLAZIONE, ");
		sql.append(" LPDR.ID_LEGGE_PROVV_DR, LPDR.DESCRIZIONE DESC_LEGGE_PROVV_DR, RP.FLG_DOCUMENTAZIONE, ");
		sql.append(" RP.FLG_RINUNCIA, VP.SEGNO || ' ' || VP.POPOLAZIONE DESC_VINCOLO_POPOLAZIONE ");
		sql.append(" FROM GFU_R_RICHIEDENTE_PROVV RP, ");
		sql.append(" GFU_R_RICHIESTA_RICHIEDENTE RR, ");
		sql.append(" GFU_T_LEGGE_PROVV_DR LPDR LEFT OUTER JOIN GFU_D_VINCOLO_POPOLAZIONE VP ON VP.ID_VINCOLO_POPOLAZIONE = LPDR.FK_VINCOLO_POPOLAZIONE , ");
		sql.append(" GFU_T_RICHIEDENTE R, ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A ");
		sql.append(" WHERE RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE ");
		sql.append(" AND LPDR.ID_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append(" AND R.ID_RICHIEDENTE = RR.FK_RICHIEDENTE ");
		sql.append(" AND RP.FK_RICHIEDENTE = R.ID_RICHIEDENTE ");
		sql.append(" AND A.ISTAT_COMUNE = R.ISTAT_COMUNE ");
		sql.append(" AND RR.FK_RICHIESTA= :ID_RICHIESTA ");
		sql.append(" AND A.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"' ") ;
		sql.append("AND A.D_STOP = ( ");	
		sql.append("SELECT MAX(A2.D_STOP) FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2  ");	
		sql.append("WHERE A2.ISTAT_COMUNE = A.ISTAT_COMUNE AND  ");	
		sql.append("A2.D_START <=  ");	
		sql.append("(SELECT R2.DATA_PROTOCOLLO FROM GFU_T_RICHIESTA R2 WHERE R2.ID_RICHIESTA = RR.FK_RICHIESTA  ))  ");	
		sql.append(" ORDER BY RP.FK_RICHIEDENTE ");

		try
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("ID_RICHIESTA", idRichiesta );

			LOG.debug("[RichiedenteDaoImpl - findAllRichiedenteProvv] param  idRichiesta = " + idRichiesta);
			LOG.debug("[RichiedenteDaoImpl - findAllRichiedenteProvv] query =" + sql.toString());

			return namedJdbcTemplate.query(sql.toString(), paramMap,
					new ResultSetExtractor<List<RichiedenteProvv>>()
			{
				@Override
				public List<RichiedenteProvv> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<RichiedenteProvv> list = new ArrayList<RichiedenteProvv>();
					Long lastKey = null;
					List<ProvvedimentiToRichiedente> elencoProvvedimentiToRichiedente = null;
					while (rs.next())
					{
						long idRichiedente = rs.getLong("FK_RICHIEDENTE");
						if (lastKey == null || lastKey != idRichiedente)
						{
							RichiedenteProvv richiedenteProvv = new RichiedenteProvv();
							richiedenteProvv.setIdRichiedente(rs.getLong("FK_RICHIEDENTE"));
							richiedenteProvv.setIstatComune(rs.getString("ISTAT_COMUNE"));
							richiedenteProvv.setDescComune(rs.getString("DESC_COMUNE"));
							richiedenteProvv.setSiglaProvincia(rs.getString("SIGLA_PROV"));
							richiedenteProvv.setPopolazione(rs.getInt("POPOLAZIONE"));

							elencoProvvedimentiToRichiedente = new ArrayList<ProvvedimentiToRichiedente>();
							richiedenteProvv.setProvvedimentiToRichiedente(elencoProvvedimentiToRichiedente);

							list.add(richiedenteProvv);
							lastKey = idRichiedente;
						}
						elencoProvvedimentiToRichiedente.add(new ProvvedimentiToRichiedente(rs.getLong("ID_LEGGE_PROVV_DR"), rs.getBoolean("FLG_DOCUMENTAZIONE"), rs.getBoolean("FLG_RINUNCIA"), rs.getString("DESC_LEGGE_PROVV_DR"), rs.getString("DESC_VINCOLO_POPOLAZIONE")));
					}				

					if (list == null || list.size() == 0)
					{
						throw new EmptyResultDataAccessException(ErrorMessages.MESSAGE_7_RICERCA_NON_HA_PRODOTTO_RISULTATI, 1);
					}
					return list;
				}
			});
		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiedenteDaoImpl::findAllRichiedenteProvv]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiedenteDaoImpl::findAllRichiedenteProvv] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiedenteDaoImpl::findAllRichiedenteProvv] END");
		}
	}

	@Override
	public void updateRichiedenteProvv(Long idRichiesta, RichiedenteProvv richiedenteProvv)
			throws DaoException, SystemException {
		try {

			String queryUpdate = " UPDATE GFU_R_RICHIEDENTE_PROVV SET FLG_DOCUMENTAZIONE = :FLG_DOCUMENTAZIONE , FLG_RINUNCIA = :FLG_RINUNCIA " +
					" WHERE FK_RICHIEDENTE = :FK_RICHIEDENTE AND FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR  ";

			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagDocumentazione() == null) {
				paramMap.put("FLG_DOCUMENTAZIONE", Boolean.FALSE);
			}else {
				paramMap.put("FLG_DOCUMENTAZIONE", richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagDocumentazione());
			}

			if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia() == null) {
				paramMap.put("FLG_RINUNCIA", Boolean.FALSE);
			}else {
				paramMap.put("FLG_RINUNCIA", richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia());
			}
			paramMap.put("FK_RICHIEDENTE", richiedenteProvv.getIdRichiedente());
			paramMap.put("FK_LEGGE_PROVV_DR", richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr());

			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  param [FLG_DOCUMENTAZIONE] = " + richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagDocumentazione());
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  param [FLG_RINUNCIA] = " + richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia());
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  param [FK_RICHIEDENTE] = " + richiedenteProvv.getIdRichiedente());
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  param [FK_LEGGE_PROVV_DR] = " + richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  

			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  Inserimento effettuato. Stato = SUCCESS ");
		} 
		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiedenteDaoImpl::updateRichiedenteProvv] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiedenteDaoImpl::updateRichiedenteProvv] END ");
		}

	}	
}