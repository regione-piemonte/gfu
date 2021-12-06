/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.associazione;

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
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.associazione.Associazione;
import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.associazione.AssociazioneRegPieDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class AssociazioneRegPieDaoImpl extends JdbcDaoSupport implements AssociazioneRegPieDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public AssociazioneRegPieDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Associazione> findAllAssociazioni(String descAssociazione, String tipoFormaAss, Boolean isValid)
			throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT FA.ID_FORMA_ASSOCIATIVA, FA.DESCRIZIONE DESC_ASSOCIAZIONE, TFA.COD_TIPO, FA.COD_FORMA_ASSOCIATIVA ISTAT_ASSOCIAZIONE, TFA.DESCRIZIONE DESC_TIPO_FORMA_ASSOCIATIVA , CFA.ISTAT_COMUNE, YLA.DESC_COMUNE,  YLA.SIGLA_PROV, YLA.DESC_PROVINCIA, YP.POP_TOTALE ");
		sql.append(" FROM GFU_T_FORMA_ASSOCIATIVA FA, ");
		sql.append(" GFU_R_COMUNI_FORMA_ASSOCIATIVA CFA, ");
		sql.append(" GFU_D_TIPO_FORMA_ASSOCIATIVA TFA, ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI YLA, ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE YP ");
		sql.append(" WHERE (:DESCRIZIONE::VARCHAR IS NULL OR UPPER(FA.DESCRIZIONE) LIKE UPPER(:DESCRIZIONE::VARCHAR)) ");
		sql.append(" and (:COD_TIPO::VARCHAR IS NULL OR UPPER(TFA.COD_TIPO) LIKE UPPER(:COD_TIPO::VARCHAR)) ");
		sql.append(" AND TFA.ID_TIPO_FORMA_ASSOCIATIVA = FA.FK_TIPO_FORMA_ASSOCIATIVA ");
		sql.append(" AND TFA.COD_TIPO != '"+Constants.DB.TIPO_FORMA_ASSOCIATIVA.COD_TIPO.UNIONE_COMUNI_DATI_PIEMONTE+"'");
		sql.append(" AND FA.ID_FORMA_ASSOCIATIVA = CFA.FK_FORMA_ASSOCIATIVA ");

		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" AND YLA.R_STATUS = "+ Constants.PARAMETER.REG_PIE.R_STATUS_1);
				sql.append(" AND YLA.D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");
			}else {

			}
		}

		sql.append(" AND YLA.ISTAT_COMUNE = CFA.ISTAT_COMUNE ");
		sql.append(" AND YLA.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'");
		sql.append(" AND YP.ANNO = (SELECT MAX(ANNO) FROM YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE) ");
		sql.append(" AND CFA.ISTAT_COMUNE = YP.ISTAT_COMUNE ");
		sql.append(" UNION ");
		sql.append(" SELECT NULL, YUC.DESC_UNIONE DESC_ASSOCIAZIONE ,  YUC.COD_TIPO , YUC.COD_ISTAT_UNIONE ISTAT_ASSOCIAZIONE, TFA.DESCRIZIONE DESC_TIPO_FORMA_ASSOCIATIVA, YAUC.ISTAT_COMUNE, YLA.DESC_COMUNE,  YLA.SIGLA_PROV, YLA.DESC_PROVINCIA, YP.POP_TOTALE ");
		sql.append(" FROM YUCCA_T_REGPIE_UNIONI_COMUNI YUC, ");
		sql.append(" YUCCA_R_REGPIE_ASSOCIAZIONE_UNIONI_COMUNI YAUC, ");
		sql.append(" GFU_D_TIPO_FORMA_ASSOCIATIVA TFA, ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI YLA, ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE YP ");
		sql.append(" WHERE (:DESCRIZIONE::VARCHAR IS NULL OR UPPER(YUC.DESC_UNIONE) LIKE UPPER(:DESCRIZIONE::VARCHAR)) ");
		sql.append(" and (:COD_TIPO::VARCHAR IS NULL OR UPPER(TFA.COD_TIPO) LIKE UPPER(:COD_TIPO::VARCHAR)) ");

		if(isValid != null) {
			if(isValid.equals(Boolean.TRUE) ){
				sql.append(" AND YUC.R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
				sql.append(" AND YUC.D_STOP_UNIONE = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");
				sql.append(" AND YAUC.R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
				sql.append(" AND YAUC.D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");
				sql.append(" AND YLA.R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
				sql.append(" AND YLA.D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY') ");
			}else {
				//TODO
			}
		}

		sql.append(" AND YUC.COD_ISTAT_UNIONE = YAUC.COD_ISTAT_UNIONE ");		
		sql.append(" AND YUC.COD_TIPO = TFA.COD_TIPO ");	
		sql.append(" AND YLA.ISTAT_COMUNE = YAUC.ISTAT_COMUNE ");
		sql.append(" AND YLA.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'");
		sql.append(" AND YP.ANNO = (SELECT MAX(ANNO) FROM YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE) ");
		sql.append(" AND YAUC.ISTAT_COMUNE = YP.ISTAT_COMUNE ");
		sql.append(" ORDER BY ISTAT_ASSOCIAZIONE, SIGLA_PROV, DESC_COMUNE ");
		try
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();

			if (descAssociazione != null) {
				paramMap.put("DESCRIZIONE", "%"+ descAssociazione + "%");
			} else {
				paramMap.put("DESCRIZIONE", descAssociazione );
			}

			paramMap.put("COD_TIPO", tipoFormaAss );

			LOG.debug("[AssociazioneRegPieDaoImpl - findAllAssociazioni] param  descAssociazione = " + descAssociazione);
			LOG.debug("[AssociazioneRegPieDaoImpl - findAllAssociazioni] param  tipoFormaAss = " + tipoFormaAss);
			LOG.debug("[AssociazioneRegPieDaoImpl - findAllAssociazioni] param  isValid = " + isValid);
			LOG.debug("[AssociazioneRegPieDaoImpl - findAllAssociazioni] query =" + sql.toString());

			return namedJdbcTemplate.query(sql.toString(), paramMap,
					new ResultSetExtractor<List<Associazione>>()
			{
				@Override
				public List<Associazione> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<Associazione> list = new ArrayList<Associazione>();
					String lastKey = null;
					List<Comune> elencoComune = null;
					while (rs.next())
					{
						String istatAssociazione =  rs.getString("ISTAT_ASSOCIAZIONE");	
						if (lastKey == null || !lastKey.equals(istatAssociazione))
						{
							Associazione associazione = new Associazione();
							associazione.setIstatAssociazione(rs.getString("ISTAT_ASSOCIAZIONE"));
							associazione.setIdAssociazione(rs.getLong("ID_FORMA_ASSOCIATIVA"));
							associazione.setDescAssociazione(rs.getString("DESC_ASSOCIAZIONE"));
							associazione.setCodTipoFormaAssociativa(rs.getString("COD_TIPO"));
							associazione.setDescTipoFormaAssociativa(rs.getString("DESC_TIPO_FORMA_ASSOCIATIVA"));              	

							elencoComune = new ArrayList<Comune>();
							associazione.setComuni(elencoComune);

							list.add(associazione);
							lastKey = istatAssociazione;
						}
						elencoComune.add(new Comune(rs.getString("ISTAT_COMUNE"), rs.getString("DESC_COMUNE"), rs.getString("DESC_PROVINCIA"),rs.getString("SIGLA_PROV"), rs.getInt("POP_TOTALE")));
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
			LOG.debug("[AssociazioneRegPieDaoImpl::findAllAssociazioni]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[AssociazioneRegPieDaoImpl::findAllAssociazioni] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[AssociazioneRegPieDaoImpl::findAllAssociazioni] END");
		}

	}

	@Override
	public List<Associazione> findAssociazioneByPk(Long idAssociazione) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT FA.ID_FORMA_ASSOCIATIVA, FA.DESCRIZIONE DESC_ASSOCIAZIONE, TFA.COD_TIPO, FA.COD_FORMA_ASSOCIATIVA ISTAT_ASSOCIAZIONE, TFA.DESCRIZIONE DESC_TIPO_FORMA_ASSOCIATIVA , CFA.ISTAT_COMUNE, YLA.DESC_COMUNE,  YLA.SIGLA_PROV, YLA.DESC_PROVINCIA , YP.POP_TOTALE ");
		sql.append(" FROM GFU_T_FORMA_ASSOCIATIVA FA,  ");
		sql.append(" GFU_R_COMUNI_FORMA_ASSOCIATIVA CFA, ");
		sql.append(" GFU_D_TIPO_FORMA_ASSOCIATIVA TFA,  ");
		sql.append(" YUCCA_T_REGPIE_ISTAT_LIMITI_AMMINISTRATIVI YLA,  ");
		sql.append("  YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE YP ");
		sql.append(" WHERE TFA.ID_TIPO_FORMA_ASSOCIATIVA = FA.FK_TIPO_FORMA_ASSOCIATIVA  ");
		sql.append(" AND FA.ID_FORMA_ASSOCIATIVA = CFA.FK_FORMA_ASSOCIATIVA  ");
		sql.append(" AND YLA.R_STATUS = "+Constants.PARAMETER.REG_PIE.R_STATUS_1);
		sql.append(" AND YLA.D_STOP = TO_DATE('"+Constants.PARAMETER.REG_PIE.D_STOP_9999+"', 'DD/MM/YYYY')  ");
		sql.append(" AND YLA.ISTAT_COMUNE = CFA.ISTAT_COMUNE 	  ");
		sql.append(" AND YLA.ISTAT_REGIONE = '"+Constants.PARAMETER.REG_PIE.ISTAT_REGIONE_01+"'");
		sql.append(" AND YP.ANNO = (SELECT MAX(ANNO) FROM YUCCA_T_REGPIE_ISTAT_POPOLAZIONE_RESIDENTE) ");
		sql.append(" AND CFA.ISTAT_COMUNE = YP.ISTAT_COMUNE ");				       	         
		sql.append(" AND ID_FORMA_ASSOCIATIVA = :ID_FORMA_ASSOCIATIVA ");

		try
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("ID_FORMA_ASSOCIATIVA", idAssociazione);

			LOG.debug("[AssociazioneRegPieDaoImpl - findAssociazioneByPk] param  idAssociazione = " + idAssociazione);
			LOG.debug("[AssociazioneRegPieDaoImpl - findAssociazioneByPk] query =" + sql.toString());		

			return  namedJdbcTemplate.query(sql.toString(), paramMap,
					new ResultSetExtractor<List<Associazione>>()
			{
				@Override
				public List<Associazione> extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					List<Associazione> list = new ArrayList<Associazione>();
					String lastKey = null;
					List<Comune> elencoComune = null;

					while (rs.next())
					{
						String istatAssociazione = rs.getString("ISTAT_ASSOCIAZIONE");

						if (lastKey == null || !lastKey.equals(istatAssociazione))
						{

							Associazione associazione = new Associazione();
							associazione.setIstatAssociazione(rs.getString("ISTAT_ASSOCIAZIONE"));
							associazione.setIdAssociazione(rs.getLong("ID_FORMA_ASSOCIATIVA"));
							associazione.setDescAssociazione(rs.getString("DESC_ASSOCIAZIONE"));
							associazione.setCodTipoFormaAssociativa(rs.getString("COD_TIPO"));
							associazione.setDescTipoFormaAssociativa(rs.getString("DESC_TIPO_FORMA_ASSOCIATIVA"));


							elencoComune = new ArrayList<Comune>();
							associazione.setComuni(elencoComune);

							list.add(associazione);
							lastKey = istatAssociazione;
						}
						elencoComune.add(new Comune(rs.getString("ISTAT_COMUNE"), rs.getString("DESC_COMUNE"), rs.getString("DESC_PROVINCIA"),rs.getString("SIGLA_PROV"),rs.getInt("POP_TOTALE")));

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
			LOG.debug("[AssociazioneRegPieDaoImpl::findAssociazioneByPk]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[AssociazioneRegPieDaoImpl::findAssociazioneByPk] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[AssociazioneRegPieDaoImpl::findAssociazioneByPk] END");
		}
	}

	@Override
	public Associazione createAssociazione(Associazione associazione) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();	
		Long seqToPkAssociazione = null;

		try
		{
			sql.append(" SELECT nextval('SEQ_ID_FORMA_ASSOCIATIVA')");

			seqToPkAssociazione = getJdbcTemplate().queryForObject(sql.toString(), new Object[] {}, Long.class);

			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione] seqToPkAssociazione = " + seqToPkAssociazione);
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione] query sequence  = " + sql.toString());


			String queryInsert = " INSERT INTO GFU_T_FORMA_ASSOCIATIVA (ID_FORMA_ASSOCIATIVA, FK_TIPO_FORMA_ASSOCIATIVA, COD_FORMA_ASSOCIATIVA, DESCRIZIONE ) " + 
					" VALUES (:ID_FORMA_ASSOCIATIVA, (SELECT ID_TIPO_FORMA_ASSOCIATIVA FROM GFU_D_TIPO_FORMA_ASSOCIATIVA WHERE COD_TIPO = :COD_TIPO_FORMA_ASSOCIATIVA) , :COD_FORMA_ASSOCIATIVA, :DESC_ASSOCIAZIONE) ";		

			LOG.debug("[AssociazioneRegPieDaoImpl - createAssociazione] query =" + queryInsert.toString());
			LOG.debug("[AssociazioneRegPieDaoImpl - createAssociazione] param  associazione = " + associazione);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("ID_FORMA_ASSOCIATIVA", seqToPkAssociazione);
			paramMap.put("COD_TIPO_FORMA_ASSOCIATIVA" , associazione.getCodTipoFormaAssociativa());
			if(associazione.getCodTipoFormaAssociativa().equals(Constants.DB.TIPO_FORMA_ASSOCIATIVA.COD_TIPO.UNIONE_COMUNI_DATI_PIEMONTE)) {
				paramMap.put("COD_FORMA_ASSOCIATIVA", associazione.getIstatAssociazione());		
			}else {
				String codFormaAssociativa = associazione.getCodTipoFormaAssociativa()+"_"+seqToPkAssociazione;
				paramMap.put("COD_FORMA_ASSOCIATIVA", codFormaAssociativa);	
				associazione.setIstatAssociazione(codFormaAssociativa);
			}

			paramMap.put("DESC_ASSOCIAZIONE", associazione.getDescAssociazione());

			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [ID_FORMA_ASSOCIATIVA] = " + seqToPkAssociazione);
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [COD_TIPO_FORMA_ASSOCIATIVA] = " + associazione.getCodTipoFormaAssociativa());
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [COD_FORMA_ASSOCIATIVA] = " + associazione.getIstatAssociazione());
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [DESC_ASSOCIAZIONE] = " + associazione.getDescAssociazione());


			namedJdbcTemplate.update(queryInsert, paramMap);  

			if(associazione.getComuni()!=null) {
				for (int i = 0; i < associazione.getComuni().size(); i++) {
					Comune comune = associazione.getComuni().get(i);
					StringBuilder sqlInsertComuni = new StringBuilder();

					sqlInsertComuni.append("INSERT INTO GFU_R_COMUNI_FORMA_ASSOCIATIVA (FK_FORMA_ASSOCIATIVA, ISTAT_COMUNE ) ");
					sqlInsertComuni.append( "VALUES (:FK_FORMA_ASSOCIATIVA, :ISTAT_COMUNE )");		

					paramMap.put("FK_FORMA_ASSOCIATIVA",seqToPkAssociazione);
					paramMap.put("ISTAT_COMUNE", comune.getIstatComune());

					LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [FK_FORMA_ASSOCIATIVA] = " +seqToPkAssociazione);
					LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  param [ISTAT_COMUNE] = " + comune.getIstatComune());

					namedJdbcTemplate.update(sqlInsertComuni.toString(), paramMap); 
					LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  update effettuato. Stato = SUCCESS ");} 
			}

			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[AssociazioneRegPieDaoImpl::createAssociazione] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[AssociazioneRegPieDaoImpl::createAssociazione] END ");
		}

		associazione.setIdAssociazione(seqToPkAssociazione);

		return associazione;
	}
}