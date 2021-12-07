/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.richiesta;

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

import it.csi.gfu.gfuweb.dto.excel.RichiesteForExcelRowDto;
import it.csi.gfu.gfuweb.dto.filter.RichiestaFilter;
import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.dto.richiesta.RichiestaToRicercaAvanzata;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.excel.RichiesteForExcelRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta.RichiestaRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta.RichiestaToRicercaAvanzataRowMapper;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class RichiestaDaoImpl extends JdbcDaoSupport implements RichiestaDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public RichiestaDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Richiesta> findAllRichieste(Boolean isValid) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_RICHIESTA, NUM_PROTOCOLLO, TO_CHAR(DATA_PROTOCOLLO, 'DD/MM/YYYY') DATA_PROTOCOLLO, NOTE,	FK_FORMA_ASSOCIATIVA ID_ASSOCIAZIONE, " );
		sql.append("R.IS_VALID,	FA.DESCRIZIONE DESC_ASSOCIAZIONE,TFA.COD_TIPO COD_TIPO_FORMA_ASSOCIATIVA, " );
		sql.append("TFA.DESCRIZIONE DESC_TIPO_FORMA_ASSOCIATIVA " );
		sql.append("FROM " );
		sql.append("GFU_T_RICHIESTA R  LEFT OUTER JOIN GFU_T_FORMA_ASSOCIATIVA FA ON R.FK_FORMA_ASSOCIATIVA = FA.ID_FORMA_ASSOCIATIVA " );
		sql.append("LEFT OUTER JOIN GFU_D_TIPO_FORMA_ASSOCIATIVA TFA  ON FA.FK_TIPO_FORMA_ASSOCIATIVA= TFA.ID_TIPO_FORMA_ASSOCIATIVA " );
		sql.append("WHERE " );
		sql.append("  R.IS_VALID IS TRUE  ");

		LOG.debug("[RichiestaDaoImpl - findAllRichieste] param  isValid = " + isValid);
		LOG.debug("[RichiestaDaoImpl - findAllRichieste] query =" + sql.toString());

		List<Richiesta> richiestaList = null;
		try {
			richiestaList = namedJdbcTemplate.query(sql.toString(), new RichiestaRowMapper());

			if(richiestaList.isEmpty()) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}
		} 

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RichiestaDaoImpl::findAllRichieste]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaDaoImpl::findAllRichieste] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaDaoImpl::findAllRichieste] END");
		}
		return (List<Richiesta>) richiestaList;
	}

	@Override
	public Richiesta findRichiestaByPk(Long idRichiesta) throws DaoException, SystemException {
		LOG.debug("[RichiestaDaoImpl::findRichiestaByPk] BEGIN");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_RICHIESTA, NUM_PROTOCOLLO, TO_CHAR(DATA_PROTOCOLLO, 'DD/MM/YYYY') DATA_PROTOCOLLO, NOTE,	FK_FORMA_ASSOCIATIVA ID_ASSOCIAZIONE, " );
		sql.append("R.IS_VALID,	FA.DESCRIZIONE DESC_ASSOCIAZIONE,TFA.COD_TIPO COD_TIPO_FORMA_ASSOCIATIVA, " );
		sql.append("TFA.DESCRIZIONE DESC_TIPO_FORMA_ASSOCIATIVA " );
		sql.append("FROM  " );
		sql.append("GFU_T_RICHIESTA R  LEFT OUTER JOIN GFU_T_FORMA_ASSOCIATIVA FA ON R.FK_FORMA_ASSOCIATIVA = FA.ID_FORMA_ASSOCIATIVA " );
		sql.append("LEFT OUTER JOIN GFU_D_TIPO_FORMA_ASSOCIATIVA TFA  ON FA.FK_TIPO_FORMA_ASSOCIATIVA= TFA.ID_TIPO_FORMA_ASSOCIATIVA " );
		sql.append("WHERE ID_RICHIESTA = :ID_RICHIESTA ");

		Richiesta result = null;

		LOG.debug("[RichiestaDaoImpl - findRichiestaByPk] query =" + sql.toString());
		LOG.debug("[RichiestaDaoImpl - findRichiestaByPk] param  idRichiesta = " + idRichiesta);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);

		try {
			result = (Richiesta) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new RichiestaRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaDaoImpl - findRichiestaByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaDaoImpl::findRichiestaByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaDaoImpl::findRichiestaByPk] END");
		}
		return result;
	}

	@Override
	public Richiesta createRichiesta(Richiesta richiesta) throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_T_RICHIESTA (DATA_PROTOCOLLO, NUM_PROTOCOLLO, "+
					" FK_FORMA_ASSOCIATIVA,NOTE, IS_VALID) " + 
					" VALUES(TO_DATE(:DATA_PROTOCOLLO, 'DD/MM/YYYY'), :NUM_PROTOCOLLO,  :FK_FORMA_ASSOCIATIVA, :NOTE, "+Boolean.TRUE+") RETURNING ID_RICHIESTA ";		

			LOG.debug("[RichiestaDaoImpl - createRichiesta] query =" + queryInsert.toString());
			LOG.debug("[RichiestaDaoImpl - createRichiesta] param  richiesta = " + richiesta);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("DATA_PROTOCOLLO", richiesta.getDataProtocollo());
			paramMap.put("NUM_PROTOCOLLO", richiesta.getNumProtocollo());
			paramMap.put("FK_FORMA_ASSOCIATIVA", richiesta.getIdAssociazione());
			paramMap.put("NOTE", richiesta.getNote());

			LOG.debug("[RichiestaDaoImpl::createRichiesta]  param [DATA_PROTOCOLLO] = " + richiesta.getDataProtocollo());
			LOG.debug("[RichiestaDaoImpl::createRichiesta]  param [NUM_PROTOCOLLO] = " + richiesta.getNumProtocollo());
			LOG.debug("[RichiestaDaoImpl::createRichiesta]  param [FK_FORMA_ASSOCIATIVA] = " + richiesta.getIdAssociazione());
			LOG.debug("[RichiestaDaoImpl::createRichiesta]  param [NOTE] = " + richiesta.getNote());

			Long idRichiesta = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			richiesta.setIdRichiesta(idRichiesta);

			LOG.debug("[RichiestaDaoImpl::createRichiesta]  Inserimento effettuato. Stato = SUCCESS ");} 


		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[RichiestaDaoImpl::createRichiesta]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaDaoImpl::createRichiesta] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaDaoImpl::createRichiesta] END ");
		}
		return richiesta;
	}

	@Override
	public Richiesta updateRichiesta(Long idRichiesta, Richiesta richiesta) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_RICHIESTA SET DATA_PROTOCOLLO = TO_DATE(:DATA_PROTOCOLLO, 'DD/MM/YYYY'), NUM_PROTOCOLLO = :NUM_PROTOCOLLO, "
					+ " FK_FORMA_ASSOCIATIVA =  :FK_FORMA_ASSOCIATIVA, NOTE = :NOTE "+
					" WHERE ID_RICHIESTA = :ID_RICHIESTA ";		

			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("DATA_PROTOCOLLO", richiesta.getDataProtocollo());
			paramMap.put("NUM_PROTOCOLLO", richiesta.getNumProtocollo());
			paramMap.put("FK_FORMA_ASSOCIATIVA", richiesta.getIdAssociazione());
			paramMap.put("NOTE", richiesta.getNote());
			paramMap.put("ID_RICHIESTA", idRichiesta);

			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  param [ID_RICHIESTA] = " + idRichiesta);
			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  param [DATA_PROTOCOLLO] = " + richiesta.getDataProtocollo());
			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  param [NUM_PROTOCOLLO] = " + richiesta.getNumProtocollo());
			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  param [FK_FORMA_ASSOCIATIVA] = " + richiesta.getIdAssociazione());
			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  param [NOTE] = " + richiesta.getNote());

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiestaDaoImpl::updateRichiesta]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiestaDaoImpl::updateRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiestaDaoImpl::updateRichiesta] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiestaDaoImpl::updateRichiesta] END ");
		}
		return richiesta;
	}

	@Override
	public void updateRichiestaToFormaAssociativa(Long idRichiesta, Long idAssociazione)
			throws DaoException, SystemException {
		try {
			String queryUpdate = " update GFU_T_RICHIESTA set FK_FORMA_ASSOCIATIVA = :FK_FORMA_ASSOCIATIVA where ID_RICHIESTA = :ID_RICHIESTA ";		

			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("FK_FORMA_ASSOCIATIVA", idAssociazione);
			paramMap.put("ID_RICHIESTA", idRichiesta);

			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa]  param [ID_RICHIESTA] = " + idRichiesta);
			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa]  param [FK_FORMA_ASSOCIATIVA] = " + idAssociazione);

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiestaDaoImpl::updateRichiestaToFormaAssociativa] END ");
		}
	}

	@Override
	public void deleteRichiesta(Long idRichiesta) throws DaoException, SystemException {
		try {
			String queryUpdate = " update GFU_T_RICHIESTA set IS_VALID = "+Boolean.FALSE+ " where ID_RICHIESTA = :ID_RICHIESTA ";		

			LOG.debug("[RichiestaDaoImpl::deleteRichiesta]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("ID_RICHIESTA", idRichiesta);

			LOG.debug("[RichiestaDaoImpl::deleteRichiesta]  param [ID_RICHIESTA] = " + idRichiesta);

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[RichiestaDaoImpl::deleteRichiesta]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiestaDaoImpl::deleteRichiesta]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiestaDaoImpl::deleteRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiestaDaoImpl::deleteRichiesta] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiestaDaoImpl::deleteRichiesta] END ");
		}
	}

	@Override
	public List<RichiestaToRicercaAvanzata> findRichiestaByFilter(RichiestaFilter richiestaFilter) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT GFU_PRAURB.ID_RICHIESTA, GFU_PRAURB.NUM_PROTOCOLLO, GFU_PRAURB.DATA_PROTOCOLLO, ");
		sql.append("GFU_PRAURB.DESC_RICHIEDENTE, GFU_PRAURB.DESC_TIPO_RICHIEDENTE, STRING_AGG(GFU_PRAURB.NUM_PRATICA_URB,' - ') NUM_PRATICA_PRAURB    FROM (	SELECT  ");
		sql.append("R.ID_RICHIESTA, R.NUM_PROTOCOLLO, TO_CHAR(R.DATA_PROTOCOLLO, 'DD/MM/YYYY') DATA_PROTOCOLLO,  ");
		sql.append("DECODE(R.FK_FORMA_ASSOCIATIVA, NULL, A.DESC_COMUNE||' ('|| A.SIGLA_PROV ||')', FA.descrizione)  DESC_RICHIEDENTE, ");
		sql.append("TFA.DESCRIZIONE DESC_TIPO_RICHIEDENTE, ");
		sql.append("PRATICA_URB.NUM_PRATICA_URB ");
		sql.append("FROM   ");
		sql.append("GFU_T_RICHIESTA R   ");
		sql.append("LEFT OUTER JOIN GFU_T_FORMA_ASSOCIATIVA FA ON FA.ID_FORMA_ASSOCIATIVA = R.FK_FORMA_ASSOCIATIVA ");
		sql.append("LEFT OUTER JOIN GFU_D_TIPO_FORMA_ASSOCIATIVA TFA ON TFA.ID_TIPO_FORMA_ASSOCIATIVA = FA.FK_TIPO_FORMA_ASSOCIATIVA ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIESTA_PROVV RIC_P ON RIC_P.FK_RICHIESTA = R.ID_RICHIESTA ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIESTA_RICHIEDENTE RR ON RR.FK_RICHIESTA = R.ID_RICHIESTA ");
		sql.append("LEFT OUTER JOIN GFU_T_RICHIEDENTE RD ON RD.ID_RICHIEDENTE = RR.FK_RICHIEDENTE ");
		sql.append("LEFT OUTER JOIN YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A ON A.ISTAT_COMUNE = RD.ISTAT_COMUNE ");
		sql.append("AND A.D_STOP = (SELECT MAX(A2.D_STOP) ");
		sql.append("FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 ");
		sql.append("WHERE A2.ISTAT_COMUNE = A.ISTAT_COMUNE ");
		sql.append("AND A2.ISTAT_PROVINCIA = A.ISTAT_PROVINCIA ");
		sql.append("AND A2.ISTAT_REGIONE = A.ISTAT_REGIONE) ");
		sql.append("AND A.ISTAT_REGIONE =  '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'   ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIEDENTE_PROVV RP ON RP.FK_RICHIEDENTE = RD.ID_RICHIEDENTE AND RP.FK_LEGGE_PROVV_DR = RIC_P.FK_LEGGE_PROVV_DR ");
		sql.append("LEFT OUTER JOIN GFU_T_LEGGE_PROVV_DR LPDR ON LPDR.ID_LEGGE_PROVV_DR = RIC_P.FK_LEGGE_PROVV_DR AND LPDR.ID_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.ID_FINANZIAMENTO = RIC_P.FK_FINANZIAMENTO ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO_RINUNCIA FR ON FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA ");
		sql.append("LEFT OUTER JOIN GFU_T_EROGAZIONE E ON E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("LEFT OUTER JOIN GFU_T_PRATICA_URB PRATICA_URB ON PRATICA_URB.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("WHERE ");
		sql.append("R.IS_VALID IS TRUE  ");
		sql.append("AND (:NUM_PROTOCOLLO::VARCHAR IS NULL OR UPPER(R.NUM_PROTOCOLLO) LIKE UPPER(:NUM_PROTOCOLLO::VARCHAR))   "); 
		if(richiestaFilter.getDataProtocolloDa() != null && !richiestaFilter.getDataProtocolloDa().isEmpty()) {
			if(richiestaFilter.getDataProtocolloA() == null || richiestaFilter.getDataProtocolloA().equals("")) {
				sql.append("and DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND CURRENT_DATE ");
			}else {
				sql.append("and DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND TO_DATE(:DATA_PROTOCOLLO_A, 'DD/MM/YYYY') ");
			} 
		}
		sql.append("AND (:ISTAT_COMUNE::VARCHAR IS NULL OR UPPER(A.ISTAT_COMUNE) LIKE UPPER(:ISTAT_COMUNE::VARCHAR))  ");
		sql.append("AND (:SIGLA_PROV::VARCHAR IS NULL OR UPPER(A.SIGLA_PROV) LIKE UPPER(:SIGLA_PROV::VARCHAR)) ");
		sql.append("AND (:ID_TIPO_FORMA_ASSOCIATIVA::NUMERIC IS NULL OR (TFA.ID_TIPO_FORMA_ASSOCIATIVA)  = (:ID_TIPO_FORMA_ASSOCIATIVA::NUMERIC))  ");
		sql.append("AND (:DESC_FORMA_ASSOCIATIVA::VARCHAR IS NULL OR UPPER(FA.DESCRIZIONE) LIKE UPPER(:DESC_FORMA_ASSOCIATIVA::VARCHAR)) ");   
		sql.append("AND (:FK_LEGGE_PROVV_DR::NUMERIC IS NULL OR (LPDR.ID_LEGGE_PROVV_DR)  = (:FK_LEGGE_PROVV_DR::NUMERIC))  ");
		sql.append("AND (:FK_VINCOLO_POPOLAZIONE::NUMERIC IS NULL OR (LPDR.FK_VINCOLO_POPOLAZIONE)  = (:FK_VINCOLO_POPOLAZIONE::NUMERIC))  ");
		sql.append("AND (:FK_STATO_FINANZIAMENTO::NUMERIC IS NULL OR (F.FK_STATO_FINANZIAMENTO)  = (:FK_STATO_FINANZIAMENTO::NUMERIC))  ");
		sql.append("AND (:NUM_DETERMINA::VARCHAR IS NULL OR UPPER(E.NUM_DETERMINA) = UPPER(:NUM_DETERMINA::VARCHAR))    ");
		if(richiestaFilter.getDataDetermina() != null && !richiestaFilter.getDataDetermina().isEmpty()) {
			sql.append("AND E.DATA_DETERMINA = TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY') ");
		}
		sql.append("AND (:FK_PARERE::NUMERIC IS NULL OR (F.FK_PARERE)  = (:FK_PARERE::NUMERIC))   ");
		sql.append("AND (:FK_RENDICONTO::NUMERIC IS NULL OR (F.FK_RENDICONTO)  = (:FK_RENDICONTO::NUMERIC))  ");
		sql.append("AND (:ATTO_RINUNCIA::VARCHAR IS NULL OR UPPER(FR.ATTO_RINUNCIA) LIKE UPPER(:ATTO_RINUNCIA::VARCHAR))  ");
		sql.append("AND (:NUM_ATTO_APPROVAZIONE_URB::VARCHAR IS NULL OR UPPER(PRATICA_URB.NUM_ATTO_APPROVAZIONE_URB) LIKE UPPER(:NUM_ATTO_APPROVAZIONE_URB::VARCHAR))  "); 
		if(richiestaFilter.getDataAttoApprovazioneUrb() != null && !richiestaFilter.getDataAttoApprovazioneUrb().isEmpty()) {
			sql.append("AND PRATICA_URB.DATA_ATTO_APPROVAZIONE_URB = TO_DATE(:DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY')  ");
		}	
		sql.append("AND (:NUM_PRATICA_URB::VARCHAR IS NULL OR UPPER(PRATICA_URB.NUM_PRATICA_URB) LIKE UPPER(:NUM_PRATICA_URB::VARCHAR))  ");		
		if(richiestaFilter.getFlagPraticaUrbanisticaAssociata() != null) {
			if(richiestaFilter.getFlagPraticaUrbanisticaAssociata().equals(Boolean.TRUE) ){
				sql.append("AND PRATICA_URB.NUM_PRATICA_URB IS NOT NULL ");
			}
		}
		sql.append(" GROUP BY  R.ID_RICHIESTA, R.NUM_PROTOCOLLO,R.DATA_PROTOCOLLO, DESC_RICHIEDENTE, DESC_TIPO_RICHIEDENTE, PRATICA_URB.NUM_PRATICA_URB ");	
		sql.append(") GFU_PRAURB ");
		sql.append("GROUP BY GFU_PRAURB.ID_RICHIESTA, GFU_PRAURB.NUM_PROTOCOLLO, GFU_PRAURB.DATA_PROTOCOLLO, ");
		sql.append(" GFU_PRAURB.DESC_RICHIEDENTE, GFU_PRAURB.DESC_TIPO_RICHIEDENTE ");
		sql.append(" ORDER BY TO_DATE(GFU_PRAURB.DATA_PROTOCOLLO,'DD/MM/YYYY') DESC, GFU_PRAURB.NUM_PROTOCOLLO,GFU_PRAURB.DESC_RICHIEDENTE ");


		LOG.debug("[RichiestaDaoImpl - findRichiestaByFilter] param  richiestaFilter = " + richiestaFilter);
		LOG.debug("[RichiestaDaoImpl - findRichiestaByFilter] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (richiestaFilter.getNumProtocollo() != null && !richiestaFilter.getNumProtocollo().isEmpty()) {
			paramMap.put("NUM_PROTOCOLLO",  "%"+ richiestaFilter.getNumProtocollo() + "%");
		} else {
			paramMap.put("NUM_PROTOCOLLO",  richiestaFilter.getNumProtocollo() );
		}	
		if(richiestaFilter.getDataProtocolloDa() != null && !richiestaFilter.getDataProtocolloDa().isEmpty()) {
			paramMap.put("DATA_PROTOCOLLO_DA", richiestaFilter.getDataProtocolloDa());

			if(richiestaFilter.getDataProtocolloA() != null && !richiestaFilter.getDataProtocolloA().isEmpty()) {
				paramMap.put("DATA_PROTOCOLLO_A", richiestaFilter.getDataProtocolloA());
			}			
		}
		paramMap.put("ISTAT_COMUNE",  richiestaFilter.getIstatComune() );	
		paramMap.put("SIGLA_PROV",  richiestaFilter.getSiglaProv() );
		paramMap.put("ID_TIPO_FORMA_ASSOCIATIVA", richiestaFilter.getIdTipoFormaAssociativa());
		if (richiestaFilter.getDenominazioneAssociazione() != null) {
			paramMap.put("DESC_FORMA_ASSOCIATIVA",  "%"+ richiestaFilter.getDenominazioneAssociazione() + "%");
		} else {
			paramMap.put("DESC_FORMA_ASSOCIATIVA",  richiestaFilter.getDenominazioneAssociazione() );
		}	
		paramMap.put("FK_LEGGE_PROVV_DR", richiestaFilter.getIdLeggeProvvDr());
		paramMap.put("FK_VINCOLO_POPOLAZIONE", richiestaFilter.getIdVincoloPopolazione());
		paramMap.put("FK_STATO_FINANZIAMENTO", richiestaFilter.getIdStatoFinanziamento());
		paramMap.put("NUM_DETERMINA",  richiestaFilter.getNumDetermina() );
		if(richiestaFilter.getDataDetermina() != null && !richiestaFilter.getDataDetermina().isEmpty()) {
			paramMap.put("DATA_DETERMINA", richiestaFilter.getDataDetermina());
		}
		paramMap.put("FK_PARERE", richiestaFilter.getIdParere());
		paramMap.put("FK_RENDICONTO", richiestaFilter.getIdRendiconto());
		if (richiestaFilter.getAttoRinuncia() != null) {
			paramMap.put("ATTO_RINUNCIA",  "%"+ richiestaFilter.getAttoRinuncia() + "%");
		} else {
			paramMap.put("ATTO_RINUNCIA",  richiestaFilter.getAttoRinuncia() );
		}	
		if(richiestaFilter.getDataAttoApprovazioneUrb() != null && !richiestaFilter.getDataAttoApprovazioneUrb().isEmpty()) {
			paramMap.put("DATA_ATTO_APPROVAZIONE_URB", richiestaFilter.getDataAttoApprovazioneUrb());
		}
		paramMap.put("NUM_PRATICA_URB", richiestaFilter.getNumPraticaUrb());
		paramMap.put("NUM_ATTO_APPROVAZIONE_URB", richiestaFilter.getNumAttoApprovazioneUrb());

		List<RichiestaToRicercaAvanzata> richiestaToRicercaAvanzataList = null;
		try {
			richiestaToRicercaAvanzataList = namedJdbcTemplate.query(sql.toString(), paramMap, new RichiestaToRicercaAvanzataRowMapper());			
		} 

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RichiestaDaoImpl::findRichiestaByFilter]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaDaoImpl::findRichiestaByFilter] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaDaoImpl::findRichiestaByFilter] END");
		}
		return (List<RichiestaToRicercaAvanzata>) richiestaToRicercaAvanzataList;
	}

	@Override
	public long countUnivocitaNumProtDataProt(Richiesta richiesta) throws DaoException, SystemException {
		LOG.debug("[RichiestaDaoImpl::countUnivocitaNumProtDataProt] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append(" SELECT COUNT(*) FROM GFU_T_RICHIESTA ");
		sql.append(" WHERE NUM_PROTOCOLLO = :NUM_PROTOCOLLO ");
		sql.append(" AND DATA_PROTOCOLLO = TO_DATE(:DATA_PROTOCOLLO, 'DD/MM/YYYY') ");

		long conteggio = 0;

		LOG.debug("[RichiestaDaoImpl - countUnivocitaNumProtDataProt] query = " + sql.toString());
		LOG.debug("[RichiestaDaoImpl - countUnivocitaNumProtDataProt] param NUM_PROTOCOLLO = " + richiesta.getNumProtocollo());
		LOG.debug("[RichiestaDaoImpl - countUnivocitaNumProtDataProt] param DATA_PROTOCOLLO = " + richiesta.getDataProtocollo());

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("NUM_PROTOCOLLO", richiesta.getNumProtocollo());
		paramMap.put("DATA_PROTOCOLLO", richiesta.getDataProtocollo());

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[RichiestaDaoImpl - countUnivocitaNumProtDataProt] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[RichiestaDaoImpl::countUnivocitaNumProtDataProt] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[RichiestaDaoImpl::countUnivocitaNumProtDataProt] END");
		}

		return conteggio;	
	}

	@Override
	public List<RichiesteForExcelRowDto> findElencoRichiesteExcelByFilter(RichiestaFilter filtro)
			throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT R.NUM_PROTOCOLLO, TO_CHAR(R.DATA_PROTOCOLLO, 'DD/MM/YYYY') DATA_PROTOCOLLO, ");
		sql.append("DECODE(R.FK_FORMA_ASSOCIATIVA, NULL,A.DESC_COMUNE, FA.DESCRIZIONE )DESC_RICHIEDENTE, ");
		sql.append("DECODE (TFA.DESCRIZIONE, NULL,DECODE(A.DESC_COMUNE,NULL,'','COMUNE'),TFA.DESCRIZIONE)DESC_TIPO_RICHIEDENTE,  ");
		sql.append("A.DESC_COMUNE DESC_COMUNE, ");
		sql.append("A.SIGLA_PROV SIGLA_PROV ,  ");
		sql.append("RD.POPOLAZIONE, DECODE(RP.FLG_RINUNCIA,TRUE,'SI','NO')RINUNCIA, LPDR.DESCRIZIONE DESC_LEGGE_PROVV_DR,  ");
		sql.append("F.IMPORTO_FINANZIABILE, F.IMPORTO_AMMESSO,  ");
		sql.append("(SELECT T.IMPORTO*    ");
		sql.append("(SELECT COUNT(*)   "); 
		sql.append("FROM GFU_R_RICHIESTA_RICHIEDENTE RR ,   ");
		sql.append("GFU_R_RICHIEDENTE_PROVV RPR     ");
		sql.append("WHERE   ");
		sql.append("RR.FK_RICHIESTA = R.ID_RICHIESTA    ");
		sql.append("AND RR.FK_RICHIEDENTE = RPR.FK_RICHIEDENTE ");
		sql.append("AND RPR.FLG_RINUNCIA IS FALSE  ");
		sql.append("AND RPR.FK_LEGGE_PROVV_DR = LPDR.ID_LEGGE_PROVV_DR  ");
		sql.append(") IMPORTO_MAX_PER_RICHIEDENTI   ");
		sql.append("FROM GFU_D_TETTO_MAX T,   ");
		sql.append("GFU_T_RICHIESTA RIC   ");
		sql.append("WHERE  ");
		sql.append("RIC.ID_RICHIESTA = R.ID_RICHIESTA   ");
		sql.append("AND ((RIC.DATA_PROTOCOLLO >= T.DATA_INIZIO   AND RIC.DATA_PROTOCOLLO < T.DATA_FINE  ) "); 
		sql.append("OR (RIC.DATA_PROTOCOLLO >= T.DATA_INIZIO   AND T.DATA_FINE IS NULL))  ");
		sql.append(")  IMPORTO_MAX_TOTALE_PER_COMUNE , 	");
		sql.append("P.DESCRIZIONE DESC_PARERE,  ");
		sql.append("RDT.DESCRIZIONE DESC_RENDICONTO,SF.DESCRIZIONE DESC_STATO_FIN,  ");
		sql.append("FR.IMPORTO IMPORTO_RINUNCIA, FR.ATTO_RINUNCIA , F.NOTE NOTE_FIN,  ");
		sql.append("(SELECT E.IMPORTO_EROGAZIONE  FROM GFU_T_EROGAZIONE E WHERE E.FK_TIPO_EROGAZIONE = 1 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) IMPORTO_EROGAZIONE_ACC,   ");
		sql.append("(SELECT   E.NUM_DETERMINA FROM GFU_T_EROGAZIONE E  WHERE E.FK_TIPO_EROGAZIONE = 1 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) NUM_DETERMINA_ACC,   ");
		sql.append("(SELECT  TO_CHAR(E.DATA_DETERMINA, 'DD/MM/YYYY')   FROM GFU_T_EROGAZIONE E  WHERE E.FK_TIPO_EROGAZIONE = 1 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) DATA_DETERMINA_ACC,  ");
		sql.append("(SELECT E.IMPORTO_EROGAZIONE  FROM GFU_T_EROGAZIONE E  WHERE E.FK_TIPO_EROGAZIONE = 2 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) IMPORTO_EROGAZIONE_SALDO,   ");
		sql.append("(SELECT   E.NUM_DETERMINA FROM GFU_T_EROGAZIONE E  WHERE E.FK_TIPO_EROGAZIONE = 2 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) NUM_DETERMINA_SALDO,   ");
		sql.append("(SELECT  TO_CHAR(E.DATA_DETERMINA, 'DD/MM/YYYY')   FROM GFU_T_EROGAZIONE E  WHERE E.FK_TIPO_EROGAZIONE = 2 AND E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ) DATA_DETERMINA_SALDO , ");
		sql.append("PRATICA_URB.NUM_PRATICA_URB, ");
		sql.append("PRATICA_URB.NUM_ATTO_APPROVAZIONE_URB, ");
		sql.append("TO_CHAR(PRATICA_URB.DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY') DATA_ATTO_APPROVAZIONE_URB ");
		sql.append("FROM   	");
		sql.append("GFU_T_RICHIESTA R  ");
		sql.append("LEFT OUTER JOIN GFU_T_FORMA_ASSOCIATIVA FA ON FA.ID_FORMA_ASSOCIATIVA = R.FK_FORMA_ASSOCIATIVA ");
		sql.append("LEFT OUTER JOIN GFU_D_TIPO_FORMA_ASSOCIATIVA TFA ON TFA.ID_TIPO_FORMA_ASSOCIATIVA = FA.FK_TIPO_FORMA_ASSOCIATIVA ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIESTA_PROVV RIC_P ON RIC_P.FK_RICHIESTA = R.ID_RICHIESTA ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIESTA_RICHIEDENTE RR ON RR.FK_RICHIESTA = R.ID_RICHIESTA ");
		sql.append("LEFT OUTER JOIN GFU_T_RICHIEDENTE RD ON RD.ID_RICHIEDENTE = RR.FK_RICHIEDENTE ");
		sql.append("LEFT OUTER JOIN YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A ON A.ISTAT_COMUNE = RD.ISTAT_COMUNE ");
		sql.append("AND A.D_STOP = (SELECT MAX(A2.D_STOP) ");
		sql.append("FROM YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI A2 ");
		sql.append("WHERE A2.ISTAT_COMUNE = A.ISTAT_COMUNE ");
		sql.append("AND A2.ISTAT_PROVINCIA = A.ISTAT_PROVINCIA ");
		sql.append("AND A2.ISTAT_REGIONE = A.ISTAT_REGIONE) ");
		sql.append("AND A.ISTAT_REGIONE =  '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'   ");
		sql.append("LEFT OUTER JOIN GFU_R_RICHIEDENTE_PROVV RP ON RP.FK_RICHIEDENTE = RD.ID_RICHIEDENTE AND RP.FK_LEGGE_PROVV_DR = RIC_P.FK_LEGGE_PROVV_DR ");
		sql.append("LEFT OUTER JOIN GFU_T_LEGGE_PROVV_DR LPDR ON LPDR.ID_LEGGE_PROVV_DR = RIC_P.FK_LEGGE_PROVV_DR AND LPDR.ID_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.ID_FINANZIAMENTO = RIC_P.FK_FINANZIAMENTO ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO_RINUNCIA FR ON FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA ");
		sql.append("LEFT OUTER JOIN GFU_T_EROGAZIONE E ON E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("LEFT OUTER JOIN GFU_D_PARERE P ON P.ID_PARERE = F.FK_PARERE ");		
		sql.append("LEFT OUTER JOIN GFU_D_RENDICONTO RDT ON RDT.ID_RENDICONTO = F.FK_RENDICONTO  ");	
		sql.append("LEFT OUTER JOIN GFU_D_STATO_FINANZIAMENTO SF ON SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZIAMENTO  ");
		sql.append("LEFT OUTER JOIN GFU_T_PRATICA_URB PRATICA_URB ON PRATICA_URB.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("WHERE   ");
		sql.append("R.IS_VALID IS TRUE ");
		sql.append("AND (:NUM_PROTOCOLLO::VARCHAR IS NULL OR UPPER(R.NUM_PROTOCOLLO) LIKE UPPER(:NUM_PROTOCOLLO::VARCHAR))   ");
		if(filtro.getDataProtocolloDa() != null && !filtro.getDataProtocolloDa().isEmpty()) {
			if(filtro.getDataProtocolloA() == null || filtro.getDataProtocolloA().equals("")) {
				sql.append("and DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND CURRENT_DATE ");
			}else {
				sql.append("and DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND TO_DATE(:DATA_PROTOCOLLO_A, 'DD/MM/YYYY') ");
			} 
		}
		sql.append("AND (:ISTAT_COMUNE::VARCHAR IS NULL OR UPPER(A.ISTAT_COMUNE) LIKE UPPER(:ISTAT_COMUNE::VARCHAR)) ");
		sql.append("AND (:ID_TIPO_FORMA_ASSOCIATIVA::NUMERIC IS NULL OR (TFA.ID_TIPO_FORMA_ASSOCIATIVA)  = (:ID_TIPO_FORMA_ASSOCIATIVA::NUMERIC)) ");
		sql.append("AND (:DESC_FORMA_ASSOCIATIVA::VARCHAR IS NULL OR UPPER(FA.DESCRIZIONE) LIKE UPPER(:DESC_FORMA_ASSOCIATIVA::VARCHAR))  ");
		sql.append("AND (:FK_LEGGE_PROVV_DR::NUMERIC IS NULL OR (LPDR.ID_LEGGE_PROVV_DR)  = (:FK_LEGGE_PROVV_DR::NUMERIC)) ");
		sql.append("AND (:FK_VINCOLO_POPOLAZIONE::NUMERIC IS NULL OR (LPDR.FK_VINCOLO_POPOLAZIONE)  = (:FK_VINCOLO_POPOLAZIONE::NUMERIC)) ");
		sql.append("AND (:FK_STATO_FINANZIAMENTO::NUMERIC IS NULL OR (F.FK_STATO_FINANZIAMENTO)  = (:FK_STATO_FINANZIAMENTO::NUMERIC)) ");
		if(filtro.getNumDetermina() != null && !filtro.getNumDetermina().isEmpty()) {
			sql.append("and UPPER(E.NUM_DETERMINA) = UPPER(:NUM_DETERMINA)   ");
		}
		if(filtro.getDataDetermina() != null && !filtro.getDataDetermina().isEmpty()) {
			sql.append("AND E.DATA_DETERMINA = TO_DATE(:DATA_DETERMINA, 'DD/MM/YYYY') ");
		}
		sql.append("AND (:FK_PARERE::NUMERIC IS NULL OR (F.FK_PARERE)  = (:FK_PARERE::NUMERIC)) ");
		sql.append("AND (:FK_RENDICONTO::NUMERIC IS NULL OR (F.FK_RENDICONTO)  = (:FK_RENDICONTO::NUMERIC)) ");
		sql.append("AND (:ATTO_RINUNCIA::VARCHAR IS NULL OR UPPER(FR.ATTO_RINUNCIA) LIKE UPPER(:ATTO_RINUNCIA::VARCHAR))  ");
		sql.append("AND (:NUM_ATTO_APPROVAZIONE_URB::VARCHAR IS NULL OR UPPER(PRATICA_URB.NUM_ATTO_APPROVAZIONE_URB) LIKE UPPER(:NUM_ATTO_APPROVAZIONE_URB::VARCHAR))  "); 
		if(filtro.getDataAttoApprovazioneUrb() != null && !filtro.getDataAttoApprovazioneUrb().isEmpty()) {
			sql.append("AND PRATICA_URB.DATA_ATTO_APPROVAZIONE_URB = TO_DATE(:DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY')  ");
		}	
		sql.append("AND (:NUM_PRATICA_URB::VARCHAR IS NULL OR UPPER(PRATICA_URB.NUM_PRATICA_URB) LIKE UPPER(:NUM_PRATICA_URB::VARCHAR))  ");		
		if(filtro.getFlagPraticaUrbanisticaAssociata() != null) {
			if(filtro.getFlagPraticaUrbanisticaAssociata().equals(Boolean.TRUE) ){
				sql.append("AND PRATICA_URB.NUM_PRATICA_URB IS NOT NULL ");
			}
		}
		sql.append("GROUP BY R.NUM_PROTOCOLLO, R.DATA_PROTOCOLLO, DESC_RICHIEDENTE, DESC_TIPO_RICHIEDENTE, DESC_COMUNE,RD.ISTAT_COMUNE, SIGLA_PROV, POPOLAZIONE, RINUNCIA, DESC_LEGGE_PROVV_DR, F.IMPORTO_FINANZIABILE, F.IMPORTO_AMMESSO, F.ID_FINANZIAMENTO,IMPORTO_MAX_TOTALE_PER_COMUNE, DESC_PARERE, DESC_RENDICONTO, " );
		sql.append(" DESC_STATO_FIN, IMPORTO_RINUNCIA, FR.ATTO_RINUNCIA, NOTE_FIN, IMPORTO_EROGAZIONE_ACC, NUM_DETERMINA_ACC, DATA_DETERMINA_ACC, IMPORTO_EROGAZIONE_SALDO, NUM_DETERMINA_SALDO, DATA_DETERMINA_SALDO , PRATICA_URB.NUM_PRATICA_URB, PRATICA_URB.NUM_ATTO_APPROVAZIONE_URB, PRATICA_URB.DATA_ATTO_APPROVAZIONE_URB ");
		sql.append(" ORDER BY R.DATA_PROTOCOLLO DESC, R.NUM_PROTOCOLLO,DESC_RICHIEDENTE  ");
		
		LOG.debug("[RichiestaDaoImpl - findElencoRichiesteExcelByFilter] param  filtro = " + filtro);
		LOG.debug("[RichiestaDaoImpl - findElencoRichiesteExcelByFilter] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (filtro.getNumProtocollo() != null && !filtro.getNumProtocollo().isEmpty()) {
			paramMap.put("NUM_PROTOCOLLO",  "%"+ filtro.getNumProtocollo() + "%");
		} else {
			paramMap.put("NUM_PROTOCOLLO",  filtro.getNumProtocollo() );
		}	
		if(filtro.getDataProtocolloDa() != null && !filtro.getDataProtocolloDa().isEmpty()) {
			paramMap.put("DATA_PROTOCOLLO_DA", filtro.getDataProtocolloDa());

			if(filtro.getDataProtocolloA() != null && !filtro.getDataProtocolloA().isEmpty()) {
				paramMap.put("DATA_PROTOCOLLO_A", filtro.getDataProtocolloA());
			}			
		}
		paramMap.put("ISTAT_COMUNE",  filtro.getIstatComune() );	
		paramMap.put("ID_TIPO_FORMA_ASSOCIATIVA", filtro.getIdTipoFormaAssociativa());
		if (filtro.getDenominazioneAssociazione() != null) {
			paramMap.put("DESC_FORMA_ASSOCIATIVA",  "%"+ filtro.getDenominazioneAssociazione() + "%");
		} else {
			paramMap.put("DESC_FORMA_ASSOCIATIVA",  filtro.getDenominazioneAssociazione() );
		}	
		paramMap.put("FK_LEGGE_PROVV_DR", filtro.getIdLeggeProvvDr());
		paramMap.put("FK_VINCOLO_POPOLAZIONE", filtro.getIdVincoloPopolazione());
		paramMap.put("FK_STATO_FINANZIAMENTO", filtro.getIdStatoFinanziamento());
		if (filtro.getNumDetermina() != null && !filtro.getNumDetermina().isEmpty() ) {
			paramMap.put("NUM_DETERMINA",  filtro.getNumDetermina() );
		}
		if(filtro.getDataDetermina() != null && !filtro.getDataDetermina().isEmpty()) {
			paramMap.put("DATA_DETERMINA", filtro.getDataDetermina());
		}
		paramMap.put("FK_PARERE", filtro.getIdParere());
		paramMap.put("FK_RENDICONTO", filtro.getIdRendiconto());
		if (filtro.getAttoRinuncia() != null) {
			paramMap.put("ATTO_RINUNCIA",  "%"+ filtro.getAttoRinuncia() + "%");
		} else {
			paramMap.put("ATTO_RINUNCIA",  filtro.getAttoRinuncia() );
		}	
		if(filtro.getDataAttoApprovazioneUrb() != null && !filtro.getDataAttoApprovazioneUrb().isEmpty()) {
			paramMap.put("DATA_ATTO_APPROVAZIONE_URB", filtro.getDataAttoApprovazioneUrb());
		}
		paramMap.put("NUM_PRATICA_URB", filtro.getNumPraticaUrb());
		paramMap.put("NUM_ATTO_APPROVAZIONE_URB", filtro.getNumAttoApprovazioneUrb());

		List<RichiesteForExcelRowDto> richiesteForExcelList = null;
		try {
			richiesteForExcelList = namedJdbcTemplate.query(sql.toString(), paramMap, new RichiesteForExcelRowMapper());
			
		} 		

		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[RichiestaDaoImpl::findElencoRichiesteExcelByFilter]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[RichiestaDaoImpl::findElencoRichiesteExcelByFilter] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[RichiestaDaoImpl::findElencoRichiesteExcelByFilter] END");
		}
		return (List<RichiesteForExcelRowDto>) richiesteForExcelList;
	}

	@Override
	public void updateFormaAssociativaAtNullToRichiesta(Long idRichiesta) throws DaoException, SystemException {
		try {
			String queryUpdate = " UPDATE  GFU_T_RICHIESTA  SET  FK_FORMA_ASSOCIATIVA = NULL WHERE ID_RICHIESTA = :ID_RICHIESTA  ";		

			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("ID_RICHIESTA", idRichiesta);

			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta]  param [ID_RICHIESTA] = " + idRichiesta);

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[RichiestaDaoImpl::updateFormaAssociativaAtNullToRichiesta] END ");
		}
	}
}