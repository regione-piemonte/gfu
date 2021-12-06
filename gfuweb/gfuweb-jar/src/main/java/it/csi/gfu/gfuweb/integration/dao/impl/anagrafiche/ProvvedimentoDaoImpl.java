/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.anagrafiche;

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
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.provvedimento.Provvedimento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.ProvvedimentoDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.ProvvedimentoRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class ProvvedimentoDaoImpl extends JdbcDaoSupport implements ProvvedimentoDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public ProvvedimentoDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Provvedimento> findAllProvvedimenti(Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PROVVEDIMENTO, DESCRIZIONE, IS_VALID " );
		sql.append("  FROM GFU_D_PROVVEDIMENTO  " );
		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" where IS_VALID IS TRUE  ");
			}else {
				sql.append(" where IS_VALID IS FALSE  ");
			}
		}

		LOG.debug("[ProvvedimentoDaoImpl - findAllProvvedimenti] param  isValid = " + isValid);
		LOG.debug("[ProvvedimentoDaoImpl - findAllProvvedimenti] query =" + sql.toString());

		List<Provvedimento> provvedimentoList = null;
		try {
			provvedimentoList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new ProvvedimentoRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[ProvvedimentoDaoImpl::findAllProvvedimenti]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProvvedimentoDaoImpl::findAllProvvedimenti] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::findAllProvvedimenti] END");
		}
		return (List<Provvedimento>) provvedimentoList;
	}

	@Override
	public Provvedimento findProvvedimentoByPk(Long id) throws DaoException, SystemException {
		LOG.debug("[ProvvedimentoDaoImpl::findProvvedimentoByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_PROVVEDIMENTO, DESCRIZIONE,  IS_VALID ");
		sql.append("FROM GFU_D_PROVVEDIMENTO ");
		sql.append("WHERE  ");
		sql.append("ID_PROVVEDIMENTO = :ID_PROVVEDIMENTO ");

		Provvedimento result = null;

		LOG.debug("[ProvvedimentoDaoImpl - findProvvedimentoByPk] query =" + sql.toString());
		LOG.debug("[ProvvedimentoDaoImpl - findProvvedimentoByPk] param  id = " + id);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_PROVVEDIMENTO", id);

		try {
			result = (Provvedimento) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new ProvvedimentoRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl - findProvvedimentoByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProvvedimentoDaoImpl::findProvvedimentoByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::findProvvedimentoByPk] END");
		}
		return result;
	}

	@Override
	public Provvedimento createProvvedimento(Provvedimento provvedimento) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_D_PROVVEDIMENTO ( DESCRIZIONE, IS_VALID) " + 
					" VALUES(:DESCRIZIONE, :IS_VALID) RETURNING ID_PROVVEDIMENTO ";		

			LOG.debug("[ProvvedimentoDaoImpl - createProvvedimento] query =" + queryInsert.toString());
			LOG.debug("[ProvvedimentoDaoImpl - createProvvedimento] param  PROVVEDIMENTO = " + provvedimento);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("DESCRIZIONE", provvedimento.getDescProvvedimento());
			paramMap.put("IS_VALID", provvedimento.isIsValid());

			LOG.debug("[ProvvedimentoDaoImpl::createProvvedimento]  param [DESCRIZIONE] = " + provvedimento.getDescProvvedimento());
			LOG.debug("[ProvvedimentoDaoImpl::createProvvedimento]  param [IS_VALID] = " + provvedimento.isIsValid());


			Long idProvvedimento = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			provvedimento.setIdProvvedimento(idProvvedimento);

			LOG.debug("[ProvvedimentoDaoImpl::createProvvedimento]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl::createProvvedimento]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProvvedimentoDaoImpl::createProvvedimento] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::createProvvedimento] END ");
		}
		return provvedimento;
	}

	@Override
	public Provvedimento updateProvvedimento(Provvedimento provvedimento) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_D_PROVVEDIMENTO SET DESCRIZIONE = :DESCRIZIONE, IS_VALID = :IS_VALID "+
					" WHERE ID_PROVVEDIMENTO = :ID_PROVVEDIMENTO ";		

			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("DESCRIZIONE", provvedimento.getDescProvvedimento());
			paramMap.put("IS_VALID", provvedimento.isIsValid());
			paramMap.put("ID_PROVVEDIMENTO", provvedimento.getIdProvvedimento());

			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  param [ID_PROVVEDIMENTO] = " + provvedimento.getIdProvvedimento());
			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  param [DESCRIZIONE] = " + provvedimento.getDescProvvedimento());
			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  param [IS_VALID] = " + provvedimento.isIsValid());

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[ProvvedimentoDaoImpl::updateProvvedimento] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[ProvvedimentoDaoImpl::updateProvvedimento] END ");
		}

		return provvedimento;
	}


	@Override
	public void deleteProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		try {
			LOG.debug("[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] idRichiesta = " + idRichiesta);
			LOG.debug("[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] idLeggeProvvDr = " + idLeggeProvvDr);

			String queryRelationDelete ="DELETE FROM GFU_R_RICHIESTA_PROVV WHERE FK_RICHIESTA = :FK_RICHIESTA AND FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR ";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("FK_RICHIESTA", idRichiesta);
			paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);

			namedJdbcTemplate.update(queryRelationDelete, paramMap);  	

			LOG.debug("[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] queryRelationDelete  = " + queryRelationDelete);
					
			String queryRelationRichiedenteProvvDelete ="DELETE FROM GFU_R_RICHIEDENTE_PROVV RP WHERE FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR AND  "+
			"EXISTS (SELECT * FROM GFU_R_RICHIESTA_RICHIEDENTE RR WHERE RR.FK_RICHIEDENTE = RP.FK_RICHIEDENTE AND RR.FK_RICHIESTA = :FK_RICHIESTA) ";
			Map<String, Object> paramMapRelDelete = new HashMap<String, Object>();
			paramMapRelDelete.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
			paramMapRelDelete.put("FK_RICHIESTA", idRichiesta);

			namedJdbcTemplate.update(queryRelationRichiedenteProvvDelete, paramMapRelDelete);

			LOG.debug("[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] queryRelationRichiedenteProvvDelete  = " + queryRelationRichiedenteProvvDelete);
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
					"[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::deleteProvvToRichiedentiToRichiesta] END ");
		}
	}
	
	

	@Override
	public void createRelRichiestaProvv(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		try {
			String sqlInsertRichiestaProvv= "INSERT INTO GFU_R_RICHIESTA_PROVV (FK_RICHIESTA, FK_LEGGE_PROVV_DR) "+
					"VALUES (:ID_RICHIESTA, :ID_LEGGE_PROVV_DR) ";

			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiestaProvv] queryInsertRichiestaProvv =" + sqlInsertRichiestaProvv.toString());
			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiestaProvv] param  idRichiesta = " + idRichiesta);
			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiestaProvv] param  idLeggeProvvDr = " + idLeggeProvvDr);

			Map<String, Object> paramMapRichiestaProvv = new HashMap<String, Object>();

			paramMapRichiestaProvv.put("ID_RICHIESTA", idRichiesta);
			paramMapRichiestaProvv.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr);

			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiestaProvv]  param [ID_RICHIESTA] = " + idRichiesta);
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiestaProvv]  param [ID_LEGGE_PROVV_DR] = " + idLeggeProvvDr);

			namedJdbcTemplate.update(sqlInsertRichiestaProvv, paramMapRichiestaProvv);	

			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiestaProvv]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiestaProvv]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_17_RICHIESTA_GIA_PRESENTE_PER_PROVVEDIMENTO, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProvvedimentoDaoImpl::createRelRichiestaProvv] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiestaProvv] END ");
		}

	}

	@Override
	public void createRelRichiedenteProvv(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		try {

			String queryInsertRichiedenteProvv = " INSERT INTO GFU_R_RICHIEDENTE_PROVV (FK_RICHIEDENTE, FK_LEGGE_PROVV_DR)  " +
					" (SELECT FK_RICHIEDENTE, :ID_LEGGE_PROVV_DR FROM GFU_R_RICHIESTA_RICHIEDENTE WHERE GFU.GFU_R_RICHIESTA_RICHIEDENTE.FK_RICHIESTA = :ID_RICHIESTA) ";		

			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiedenteProvv] queryInsertRichiestaProvv =" + queryInsertRichiedenteProvv.toString());
			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiedenteProvv] param  idRichiesta = " + idRichiesta);
			LOG.debug("[ProvvedimentoDaoImpl - createRelRichiedenteProvv] param  idLeggeProvvDr = " + idLeggeProvvDr);

			Map<String, Object> paramMapRichiedenteProvv = new HashMap<String, Object>();

			paramMapRichiedenteProvv.put("ID_RICHIESTA", idRichiesta);
			paramMapRichiedenteProvv.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr);

			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiedenteProvv]  param [ID_RICHIESTA] = " + idRichiesta);
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiedenteProvv]  param [ID_LEGGE_PROVV_DR] = " + idLeggeProvvDr);

			namedJdbcTemplate.update(queryInsertRichiedenteProvv, paramMapRichiedenteProvv);


			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiedenteProvv]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiedenteProvv]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_16_COMUNE_GIA_PRESENTE_PER_PROVVEDIMENTO, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (Throwable ex) {
			LOG.error(
					"[ProvvedimentoDaoImpl::createRelRichiedenteProvv] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[ProvvedimentoDaoImpl::createRelRichiedenteProvv] END ");
		}

	}

	@Override
	public long countUnivocitaRichiedenteProvvedimento(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		LOG.debug("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimento] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) FROM ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RP, GFU_T_RICHIEDENTE R, GFU_T_RICHIESTA RA, GFU_R_RICHIESTA_RICHIEDENTE RR, ");
		sql.append("GFU_R_RICHIESTA_PROVV RIP LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.ID_FINANZIAMENTO = COALESCE(RIP.FK_FINANZIAMENTO,0) ");
		sql.append("WHERE RP.FK_LEGGE_PROVV_DR = RIP.FK_LEGGE_PROVV_DR ");
		sql.append("AND RR.FK_RICHIEDENTE = R.ID_RICHIEDENTE ");
		sql.append("AND RR.FK_RICHIESTA = RIP.FK_RICHIESTA ");
		sql.append("AND R.ID_RICHIEDENTE = RP.FK_RICHIEDENTE ");
		sql.append("AND RA.ID_RICHIESTA = RR.FK_RICHIESTA ");
		sql.append("AND RA.ID_RICHIESTA = RIP.FK_RICHIESTA ");
		sql.append("AND COALESCE(F.FK_STATO_FINANZIAMENTO,0) NOT IN("+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO+",");
		sql.append(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO+","+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA+") ");
		sql.append("AND RP.FLG_RINUNCIA != "+ Boolean.TRUE+" ");
		sql.append("AND RP.FK_LEGGE_PROVV_DR= :FK_LEGGE_PROVV_DR ");
		sql.append("AND R.ISTAT_COMUNE IN ");
		sql.append("(SELECT ISTAT_COMUNE ");
		sql.append("FROM GFU_T_RICHIEDENTE R2, GFU_T_RICHIESTA RA2, GFU_R_RICHIESTA_RICHIEDENTE RR2  ");
		sql.append("WHERE RA2.ID_RICHIESTA=RR2.FK_RICHIESTA  ");
		sql.append("AND RR2.FK_RICHIEDENTE=R2.ID_RICHIEDENTE  ");
		sql.append("AND RA2.ID_RICHIESTA=:ID_RICHIESTA) ");

		long conteggio = 0;

		LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimento] query = " + sql.toString());
		LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimento] param FK_LEGGE_PROVV_DR = " + idLeggeProvvDr);
		LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimento] param ID_RICHIESTA = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
		paramMap.put("ID_RICHIESTA", idRichiesta);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimento] END");
		}

		return conteggio;	
	}
	
	@Override
	public long countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento(Long idFinanziamento)
			throws DaoException, SystemException {
		LOG.debug("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append("SELECT COUNT(*) FROM ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RP, GFU_T_RICHIEDENTE R, GFU_T_RICHIESTA RA, GFU_R_RICHIESTA_RICHIEDENTE RR, ");
		sql.append("GFU_R_RICHIESTA_PROVV RIP LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.ID_FINANZIAMENTO = COALESCE(RIP.FK_FINANZIAMENTO,0) ");
		sql.append("WHERE RP.FK_LEGGE_PROVV_DR = RIP.FK_LEGGE_PROVV_DR ");
		sql.append("AND RR.FK_RICHIEDENTE = R.ID_RICHIEDENTE ");
		sql.append("AND RR.FK_RICHIESTA = RIP.FK_RICHIESTA ");
		sql.append("AND R.ID_RICHIEDENTE = RP.FK_RICHIEDENTE ");
		sql.append("AND RA.ID_RICHIESTA = RR.FK_RICHIESTA ");
		sql.append("AND RA.ID_RICHIESTA = RIP.FK_RICHIESTA ");
		sql.append("AND COALESCE(F.FK_STATO_FINANZIAMENTO,0) NOT IN("+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO+",");
		sql.append(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO+","+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA+") ");
		sql.append("AND RP.FLG_RINUNCIA != "+ Boolean.TRUE+" ");
		sql.append("AND RP.FK_LEGGE_PROVV_DR = (SELECT  RIP2.FK_LEGGE_PROVV_DR FROM GFU_R_RICHIESTA_PROVV RIP2 WHERE RIP2.FK_FINANZIAMENTO = :ID_FINANZIAMENTO) ");
		sql.append("AND RA.ID_RICHIESTA != (SELECT  RIP2.FK_RICHIESTA FROM GFU_R_RICHIESTA_PROVV RIP2 WHERE RIP2.FK_FINANZIAMENTO = :ID_FINANZIAMENTO) ");
		sql.append("AND R.ISTAT_COMUNE IN ");
		sql.append("(SELECT ISTAT_COMUNE ");
		sql.append("FROM GFU_T_RICHIEDENTE R2, GFU_R_RICHIESTA_RICHIEDENTE RR2, GFU_T_FINANZIAMENTO F2, GFU_R_RICHIESTA_PROVV RIP2, ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RP2 ");
		sql.append("WHERE  RR2.FK_RICHIEDENTE=R2.ID_RICHIEDENTE ");
		sql.append("AND F2.ID_FINANZIAMENTO = RIP2.FK_FINANZIAMENTO  ");
		sql.append("AND RIP2.FK_RICHIESTA= RR2.FK_RICHIESTA  ");
		sql.append("and RIP2.fk_legge_provv_dr = RP2.fk_legge_provv_dr   ");
		sql.append("and RP2.fk_richiedente = RR2.FK_RICHIEDENTE ");
		sql.append("AND F2.ID_FINANZIAMENTO = :ID_FINANZIAMENTO) ");            


		long conteggio = 0;

		LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] query = " + sql.toString());
		LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] param ID_FINANZIAMENTO = " + idFinanziamento);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_FINANZIAMENTO", idFinanziamento);	 

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[ProvvedimentoDaoImpl - countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[ProvvedimentoDaoImpl::countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento] END");
		}

		return conteggio;	
	}

}