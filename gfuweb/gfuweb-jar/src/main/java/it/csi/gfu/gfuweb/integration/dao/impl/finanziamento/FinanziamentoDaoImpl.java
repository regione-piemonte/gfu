/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.finanziamento;

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
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.dto.rinuncia.Rinuncia;
import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.BaseDao;
import it.csi.gfu.gfuweb.integration.dao.finanziamento.FinanziamentoDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.finanziamento.ProvvFinToRichiestaRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class FinanziamentoDaoImpl extends BaseDao implements FinanziamentoDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public FinanziamentoDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public Finanziamento findFinanziamentoToProvRich(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.ID_FINANZIAMENTO, \n"); 
		sql.append("F.IMPORTO_FINANZIABILE, \n");
		sql.append("F.IMPORTO_AMMESSO, \n");
		sql.append("F.VALUTA, \n");
		sql.append("F.FK_TETTO_MAX, \n");
		sql.append("(SELECT TETTO.IMPORTO FROM GFU_D_TETTO_MAX TETTO WHERE TETTO.ID_TETTO_MAX = F.FK_TETTO_MAX) IMPORTO_TETTO_MAX, \n");
		sql.append("F.ATTO_APPROVAZIONE, \n");
		sql.append("F.NOTE, \n");
		sql.append("F.FK_LEGGE_PROVV_DR, \n");
		sql.append("F.FK_PERCENTUALE, \n");
		sql.append("(SELECT PERC.VALORE_PERCENTUALE FROM GFU_D_PERCENTUALE PERC WHERE PERC.ID_PERCENTUALE = F.FK_PERCENTUALE) VALORE_PERCENTUALE, \n");
		sql.append("F.FK_PARERE ID_PARERE, \n");
		sql.append("(SELECT P.DESCRIZIONE FROM GFU_D_PARERE P WHERE P.ID_PARERE = F.FK_PARERE) DESC_PARERE, \n");
		sql.append("F.FK_RENDICONTO ID_RENDICONTO, \n");
		sql.append("(SELECT R.DESCRIZIONE FROM GFU_D_RENDICONTO R WHERE R.ID_RENDICONTO = F.FK_RENDICONTO) DESC_RENDICONTO, \n");
		sql.append("F.FK_STATO_FINANZIAMENTO ID_STATO_FINANZIAMENTO, \n");
		sql.append("(SELECT SF.DESCRIZIONE FROM GFU_D_STATO_FINANZIAMENTO SF ");
		sql.append("WHERE SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZIAMENTO) DESC_STATO_FINANZIAMENTO, \n");				
		sql.append("(SELECT SF.cod_stato_finanziamento FROM GFU_D_STATO_FINANZIAMENTO SF ");
		sql.append("WHERE SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZIAMENTO) COD_STATO_FINANZIAMENTO, ");
		sql.append("F.FK_FINANZIAMENTO_RINUNCIA ID_FINANZIAMENTO_RINUNCIA, \n");
		sql.append("(SELECT FR.IMPORTO FROM GFU_T_FINANZIAMENTO_RINUNCIA FR WHERE FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA) IMPORTO, \n");
		sql.append("(SELECT FR.VALUTA FROM GFU_T_FINANZIAMENTO_RINUNCIA FR WHERE FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA) VALUTA, \n");
		sql.append("(SELECT FR.ATTO_RINUNCIA FROM GFU_T_FINANZIAMENTO_RINUNCIA FR WHERE \n"); 
		sql.append("FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA) ATTO_RINUNCIA, \n"); 
		sql.append("E.ID_EROGAZIONE, \n"); 
		sql.append("E.FK_TIPO_EROGAZIONE, \n "); 
		sql.append("(SELECT TE.DESCRIZIONE FROM GFU_D_TIPO_EROGAZIONE TE WHERE TE.ID_TIPO_EROGAZIONE = E.FK_TIPO_EROGAZIONE) DESC_TIPO_EROGAZIONE, \n "); 
		sql.append("E.FK_FINANZIAMENTO, \n"); 
		sql.append("E.IMPORTO_EROGAZIONE, \n"); 
		sql.append("E.VALUTA, E.NUM_DETERMINA, "); 
		sql.append("TO_CHAR(E.DATA_DETERMINA, 'DD/MM/YYYY') DATA_DETERMINA , \n"); 
		sql.append("F.FK_STATO_FINANZ_PREC ID_STATO_FINANZIAMENTO_PREC, \n"); 
		sql.append("(SELECT SF.DESCRIZIONE FROM GFU_D_STATO_FINANZIAMENTO SF WHERE SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZ_PREC) \n");
		sql.append("DESC_STATO_FINANZIAMENTO_PREC, \n"); 
		sql.append("(SELECT SF.cod_stato_finanziamento FROM GFU_D_STATO_FINANZIAMENTO SF WHERE SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZ_PREC) ");
		sql.append("COD_STATO_FINANZIAMENTO_PREC , ");
		sql.append("PR_URB.ID_PRATICA_URB, PR_URB.NUM_PRATICA_URB, PR_URB.NUM_ATTO_APPROVAZIONE_URB, TO_CHAR(PR_URB.DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY') DATA_ATTO_APPROVAZIONE_URB, PR_URB.fk_finanziamento FK_FINANZIAMENTO_PRATICA_URB ");
		sql.append("FROM GFU_R_RICHIESTA_PROVV RP LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.ID_FINANZIAMENTO = RP.FK_FINANZIAMENTO \n");
		sql.append("LEFT OUTER JOIN GFU_T_EROGAZIONE E ON E.FK_FINANZIAMENTO = RP.FK_FINANZIAMENTO \n");
		sql.append("LEFT OUTER JOIN GFU_T_PRATICA_URB PR_URB ON PR_URB.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("WHERE RP.FK_LEGGE_PROVV_DR = :ID_LEGGE_PROVV_DR \n");
		sql.append("AND RP.FK_RICHIESTA = :ID_RICHIESTA \n");
		sql.append("AND RP.FK_FINANZIAMENTO is not null \n");

		try
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("ID_LEGGE_PROVV_DR", idLeggeProvvDr );
			paramMap.put("ID_RICHIESTA", idRichiesta );

			LOG.debug("[FinanziamentoDaoImpl - findFinanziamentoToProvRich] param  idRichiesta = " + idRichiesta);
			LOG.debug("[FinanziamentoDaoImpl - findFinanziamentoToProvRich] param  idLeggeProvvDr = " + idLeggeProvvDr);
			LOG.info("[FinanziamentoDaoImpl - findFinanziamentoToProvRich] query =" + sql.toString());

			return namedJdbcTemplate.query(sql.toString(), paramMap,
					new ResultSetExtractor<Finanziamento>()
			{
				@Override
				public Finanziamento extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					Long lastKey = null;
					List<Erogazione> elencoErogazione = null;
					Finanziamento finanziamentoDto = new Finanziamento();
					while (rs.next())
					{
						long idFinanziamento = rs.getLong("ID_FINANZIAMENTO");
						if (lastKey == null || lastKey != idFinanziamento)
						{
							finanziamentoDto.setIdFinanziamento(rs.getLong("ID_FINANZIAMENTO"));
							finanziamentoDto.setImportoFinanziabile(rs.getBigDecimal("IMPORTO_FINANZIABILE"));
							finanziamentoDto.setImportoAmmesso(rs.getBigDecimal("IMPORTO_AMMESSO"));
							finanziamentoDto.setValuta(rs.getString("VALUTA"));
							finanziamentoDto.setFkImportoTettoMax(rs.getBigDecimal("fk_tetto_max"));
							finanziamentoDto.setNote(rs.getString("NOTE"));
							finanziamentoDto.setIdLeggeProvvDr(rs.getBigDecimal("FK_LEGGE_PROVV_DR"));
							Long idPercentuale = rs.getLong("FK_PERCENTUALE");	
							if(!rs.wasNull()) {
								Percentuale percentuale = new Percentuale();
								percentuale.setIdPercentuale(idPercentuale);
								percentuale.setValorePercentuale(rs.getLong("VALORE_PERCENTUALE"));
								finanziamentoDto.setPercentuale(percentuale);
							}
							Long idParere = rs.getLong("ID_PARERE");
							if(!rs.wasNull()) {
								Parere parere = new Parere();
								parere.setIdParere(idParere);
								parere.setDescrizione(rs.getString("DESC_PARERE"));
								finanziamentoDto.setParere(parere);
							}
							Long idRendiconto = rs.getLong("ID_RENDICONTO");
							if(!rs.wasNull()) {
								Rendiconto rendiconto = new Rendiconto();
								rendiconto.setIdRendiconto(idRendiconto);
								rendiconto.setDescrizione(rs.getString("DESC_RENDICONTO"));
								finanziamentoDto.setRendiconto(rendiconto);
							}
							Long idStatoFinanziamento = rs.getLong("ID_STATO_FINANZIAMENTO");	
							if(!rs.wasNull()) {
								StatoFinanziamento statoFinanziamento = new StatoFinanziamento();
								statoFinanziamento.setIdStatoFinanziamento(idStatoFinanziamento);
								statoFinanziamento.setDescrizione(rs.getString("DESC_STATO_FINANZIAMENTO"));
								statoFinanziamento.setCodStatoFinanziamento(rs.getString("COD_STATO_FINANZIAMENTO"));
								finanziamentoDto.setStatoFinanziamento(statoFinanziamento);						
							}
							Long idFinanziamentoRinuncia = rs.getLong("ID_FINANZIAMENTO_RINUNCIA");	
							if(!rs.wasNull()) {
								Rinuncia rinuncia = new Rinuncia();
								rinuncia.setIdFinanziamentoRinuncia(idFinanziamentoRinuncia);
								rinuncia.setAttoRinuncia(rs.getString("ATTO_RINUNCIA"));
								rinuncia.setImporto(rs.getBigDecimal("IMPORTO"));
								rinuncia.setValuta(rs.getString("VALUTA"));
								finanziamentoDto.setRinuncia(rinuncia);
							}
							Long idStatoFinanziamentoPrec = rs.getLong("ID_STATO_FINANZIAMENTO_PREC");	
							if(!rs.wasNull()) {
								StatoFinanziamento statoFinanziamentoPrec = new StatoFinanziamento();
								statoFinanziamentoPrec.setIdStatoFinanziamento(idStatoFinanziamentoPrec);
								statoFinanziamentoPrec.setDescrizione(rs.getString("DESC_STATO_FINANZIAMENTO_PREC"));
								statoFinanziamentoPrec.setCodStatoFinanziamento(rs.getString("COD_STATO_FINANZIAMENTO_PREC"));
								finanziamentoDto.setStatoFinanziamentoPrec(statoFinanziamentoPrec);
							}
							Long idPraticaUrb = rs.getLong("ID_PRATICA_URB");	
							if(!rs.wasNull()) {
								PraticaUrbGfu praticaUrbGfu = new PraticaUrbGfu();
								praticaUrbGfu.setIdPraticaUrb(idPraticaUrb);
								praticaUrbGfu.setNumPraticaUrb(rs.getString("NUM_PRATICA_URB"));
								praticaUrbGfu.setNumAttoApprovazioneUrb(rs.getString("NUM_ATTO_APPROVAZIONE_URB"));
								praticaUrbGfu.setDataAttoApprovazioneUrb(rs.getString("DATA_ATTO_APPROVAZIONE_URB"));
								praticaUrbGfu.setFkFinanziamento(rs.getBigDecimal("FK_FINANZIAMENTO_PRATICA_URB"));
								finanziamentoDto.setPraticaUrbGfu(praticaUrbGfu);
							}

							elencoErogazione = new ArrayList<Erogazione>();
							finanziamentoDto.setErogazioni(elencoErogazione);						

							lastKey = idFinanziamento;
						}
						if(rs.getLong("ID_EROGAZIONE")!=0) {
							elencoErogazione.add(new Erogazione(rs.getLong("ID_EROGAZIONE"), rs.getBigDecimal("FK_TIPO_EROGAZIONE"), rs.getString("DESC_TIPO_EROGAZIONE"), rs.getBigDecimal("FK_FINANZIAMENTO"),
									rs.getBigDecimal("IMPORTO_EROGAZIONE"), rs.getString("VALUTA"), rs.getString("NUM_DETERMINA"), rs.getString("DATA_DETERMINA")));
						}
					}

					if (finanziamentoDto.getIdFinanziamento() == null)
					{
						return null;

					}

					return finanziamentoDto;
				}
			});
		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[FinanziamentoDaoImpl::findFinanziamentoToProvRich]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[FinanziamentoDaoImpl::findFinanziamentoToProvRich] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[FinanziamentoDaoImpl::findFinanziamentoToProvRich] END");
		}
	}

	@Override
	public Finanziamento createFinanziamento(Long idRichiesta, Long idLeggeProvvDr, Finanziamento finanziamento)
			throws DaoException, SystemException {
		try {
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  ");			

			Map<String, Object> paramMap = new HashMap<String, Object>();

			String queryInsertRichiedente = " INSERT INTO GFU_T_FINANZIAMENTO (IMPORTO_FINANZIABILE, IMPORTO_AMMESSO, VALUTA, FK_TETTO_MAX , " + 
					"NOTE, FK_LEGGE_PROVV_DR, FK_PERCENTUALE, FK_PARERE, FK_RENDICONTO, FK_STATO_FINANZIAMENTO, FK_STATO_FINANZ_PREC, FK_FINANZIAMENTO_RINUNCIA ) "+
					"VALUES (:IMPORTO_FINANZIABILE, :IMPORTO_AMMESSO, '"+Constants.DB.VALUTA_EURO+"', :FK_TETTO_MAX, :NOTE, :FK_LEGGE_PROVV_DR, "+
					":FK_PERCENTUALE, :FK_PARERE, :FK_RENDICONTO, "+Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO +", :FK_STATO_FINANZIAMENTO_PREC, :FK_FINANZIAMENTO_RINUNCIA) RETURNING ID_FINANZIAMENTO";

			paramMap.put("IMPORTO_FINANZIABILE", finanziamento.getImportoFinanziabile());
			paramMap.put("IMPORTO_AMMESSO", finanziamento.getImportoAmmesso());
			paramMap.put("FK_TETTO_MAX", finanziamento.getFkImportoTettoMax());
			paramMap.put("NOTE", finanziamento.getNote());
			paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
			paramMap.put("FK_PERCENTUALE", finanziamento.getPercentuale() != null ? finanziamento.getPercentuale().getIdPercentuale(): null);
			paramMap.put("FK_PARERE", finanziamento.getParere() !=null ? finanziamento.getParere().getIdParere(): null);
			paramMap.put("FK_RENDICONTO", finanziamento.getRendiconto() != null ? finanziamento.getRendiconto().getIdRendiconto():null);
			paramMap.put("FK_STATO_FINANZIAMENTO_PREC", finanziamento.getStatoFinanziamentoPrec() != null ? finanziamento.getStatoFinanziamentoPrec().getIdStatoFinanziamento() : null);
			paramMap.put("FK_FINANZIAMENTO_RINUNCIA", finanziamento.getRinuncia() != null ? finanziamento.getRinuncia().getIdFinanziamentoRinuncia() : null);


			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [IMPORTO_FINANZIABILE] = " + finanziamento.getImportoFinanziabile());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [IMPORTO_AMMESSO] = " + finanziamento.getImportoAmmesso());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_TETTO_MAX] = " + finanziamento.getFkImportoTettoMax());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [NOTE] = " + finanziamento.getNote());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_LEGGE_PROVV_DR] = " + idLeggeProvvDr);
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_PERCENTUALE] = " + finanziamento.getPercentuale());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_PARERE] = " + finanziamento.getParere());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_RENDICONTO] = " + finanziamento.getRendiconto()  );
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_STATO_FINANZIAMENTO_PREC] = " + finanziamento.getStatoFinanziamentoPrec());
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  param [FK_FINANZIAMENTO_RINUNCIA] = " + finanziamento.getRinuncia());

			Long idFinanziamento = namedJdbcTemplate.queryForObject(queryInsertRichiedente, paramMap, Long.class);

			finanziamento.setIdFinanziamento(idFinanziamento);	
			StatoFinanziamento statoFinanziamento = new StatoFinanziamento ();
			statoFinanziamento.setIdStatoFinanziamento(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue());
			finanziamento.setStatoFinanziamento(statoFinanziamento);
			finanziamento.setValuta(Constants.DB.VALUTA_EURO);

			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  Inserimento effettuato. Stato = SUCCESS ");} 
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento]  Integrity Keys Violation ");		
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}		
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[FinanziamentoDaoImpl::createFinanziamento] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[FinanziamentoDaoImpl::createFinanziamento] END ");
		}

		return finanziamento;
	}

	/**
	 * Inserisco una count sulla richiesta provvedimenti per capire se esistono per la richiesta selezionata dei provvedimenti con stato finanziamento diverso da "IN CORSO"
	 * 
	 */
	@Override
	public Long countAssociazioneFinanziamentoWithStatoNotInCorso(Long idRichiesta, Long idLeggeProvvDr) throws DaoException, SystemException {
		LOG.debug("[FinanziamentoDaoImpl::countAssociazioneFinanziamentoWithStatoNotInCorso] BEGIN");

		StringBuilder sql = new StringBuilder();		

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_T_FINANZIAMENTO F, ");
		sql.append(" GFU_R_RICHIESTA_PROVV RP, ");
		sql.append(" GFU_D_STATO_FINANZIAMENTO SF ");
		sql.append(" WHERE ");
		sql.append(" RP.FK_RICHIESTA = :ID_RICHIESTA ");
		sql.append(" AND F.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append( "and rp.fk_finanziamento = f.id_finanziamento ");
		sql.append(" AND SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZIAMENTO ");
		sql.append(" AND COD_STATO_FINANZIAMENTO != '"+Constants.DB.STATO_FINANZIAMENTO.COD_STATO_FINANZIAMENTO.IN_CORSO+"'");		

		if(idLeggeProvvDr != null) {
			sql.append(" AND RP.FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR ");	
		}

		long conteggio = 0;

		LOG.debug("[FinanziamentoDaoImpl - countAssociazioneFinanziamentoWithStatoNotInCorso] query = " + sql.toString());
		LOG.debug("[FinanziamentoDaoImpl - countAssociazioneFinanziamentoWithStatoNotInCorso] param idRichiesta = " + idRichiesta);


		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_RICHIESTA", idRichiesta);
		if(idLeggeProvvDr != null) {
			paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
		}

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[FinanziamentoDaoImpl - countAssociazioneFinanziamentoWithStatoNotInCorso] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[FinanziamentoDaoImpl::countAssociazioneFinanziamentoWithStatoNotInCorso] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[FinanziamentoDaoImpl::countAssociazioneFinanziamentoWithStatoNotInCorso] END");
		}

		return conteggio;			
	}

	@Override
	public Finanziamento findFinanziamentoByPk(Long idFinanziamento) throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT F.ID_FINANZIAMENTO, F.IMPORTO_FINANZIABILE, F.IMPORTO_AMMESSO, F.VALUTA VALUTA_FINANZIAMENTO, F.fk_tetto_max, ");
		sql.append("F.NOTE, F.FK_LEGGE_PROVV_DR,PERC.ID_PERCENTUALE, PERC.VALORE_PERCENTUALE VALORE_PERCENTUALE, ");
		sql.append("P.ID_PARERE,P.DESCRIZIONE DESC_PARERE, R.ID_RENDICONTO, R.DESCRIZIONE DESC_RENDICONTO,  F.FK_STATO_FINANZIAMENTO ID_STATO_FINANZIAMENTO, ");
		sql.append("SF.DESCRIZIONE DESC_STATO_FINANZIAMENTO, SF.cod_stato_finanziamento, FR.ID_FINANZIAMENTO_RINUNCIA, FR.IMPORTO, FR.VALUTA VALUTA_RINUNCIA, FR.ATTO_RINUNCIA, ");
		sql.append("E.ID_EROGAZIONE, E.FK_TIPO_EROGAZIONE, TE.DESCRIZIONE DESC_TIPO_EROGAZIONE, E.FK_FINANZIAMENTO, E.IMPORTO_EROGAZIONE, ");
		sql.append("E.VALUTA VALUTA_EROGAZIONE, E.NUM_DETERMINA, TO_CHAR(E.DATA_DETERMINA, 'DD/MM/YYYY') DATA_DETERMINA, F.FK_STATO_FINANZ_PREC ID_STATO_FINANZIAMENTO_PREC, SF2.DESCRIZIONE DESC_STATO_FINANZIAMENTO_PREC, SF2.cod_stato_finanziamento COD_STATO_FINANZIAMENTO_PREC, ");
		sql.append("PR_URB.ID_PRATICA_URB, PR_URB.NUM_PRATICA_URB, PR_URB.NUM_ATTO_APPROVAZIONE_URB, TO_CHAR(PR_URB.DATA_ATTO_APPROVAZIONE_URB, 'DD/MM/YYYY') DATA_ATTO_APPROVAZIONE_URB, PR_URB.fk_finanziamento FK_FINANZIAMENTO_PRATICA_URB ");
		sql.append("FROM GFU_T_FINANZIAMENTO F ");
		sql.append("LEFT OUTER JOIN GFU_D_PERCENTUALE PERC ON PERC.ID_PERCENTUALE = F.FK_PERCENTUALE ");
		sql.append("LEFT OUTER JOIN GFU_D_PARERE P ON P.ID_PARERE = F.FK_PARERE ");
		sql.append("LEFT OUTER JOIN GFU_D_RENDICONTO R ON R.ID_RENDICONTO = F.FK_RENDICONTO ");
		sql.append("LEFT OUTER JOIN GFU_D_STATO_FINANZIAMENTO SF ON SF.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZIAMENTO  ");
		sql.append("LEFT OUTER JOIN GFU_D_STATO_FINANZIAMENTO SF2 ON SF2.ID_STATO_FINANZIAMENTO = F.FK_STATO_FINANZ_PREC ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO_RINUNCIA FR ON FR.ID_FINANZIAMENTO_RINUNCIA = F.FK_FINANZIAMENTO_RINUNCIA ");
		sql.append("LEFT OUTER JOIN GFU_T_EROGAZIONE E ON E.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("LEFT OUTER JOIN GFU_D_TIPO_EROGAZIONE TE ON TE.ID_TIPO_EROGAZIONE = E.FK_TIPO_EROGAZIONE ");
		sql.append("LEFT OUTER JOIN GFU_T_PRATICA_URB PR_URB ON PR_URB.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO ");
		sql.append("WHERE F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO ");

		try
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("ID_FINANZIAMENTO", idFinanziamento );

			LOG.debug("[FinanziamentoDaoImpl - findFinanziamentoByPk] param  idFinanziamento = " + idFinanziamento);
			LOG.debug("[FinanziamentoDaoImpl - findFinanziamentoByPk] query =" + sql.toString());

			return namedJdbcTemplate.query(sql.toString(), paramMap,
					new ResultSetExtractor<Finanziamento>()
			{
				@Override
				public Finanziamento extractData(ResultSet rs) throws SQLException, DataAccessException
				{
					Long lastKey = null;
					List<Erogazione> elencoErogazione = null;
					Finanziamento finanziamentoDto = new Finanziamento();
					while (rs.next())
					{
						long idFinanziamento = rs.getLong("ID_FINANZIAMENTO");
						if (lastKey == null || lastKey != idFinanziamento)
						{
							finanziamentoDto.setIdFinanziamento(rs.getLong("ID_FINANZIAMENTO"));
							finanziamentoDto.setImportoFinanziabile(rs.getBigDecimal("IMPORTO_FINANZIABILE"));
							finanziamentoDto.setImportoAmmesso(rs.getBigDecimal("IMPORTO_AMMESSO"));
							finanziamentoDto.setValuta(rs.getString("VALUTA_FINANZIAMENTO"));
							finanziamentoDto.setFkImportoTettoMax(rs.getBigDecimal("fk_tetto_max"));
							finanziamentoDto.setNote(rs.getString("NOTE"));
							finanziamentoDto.setIdLeggeProvvDr(rs.getBigDecimal("FK_LEGGE_PROVV_DR"));
							Long idPercentuale = rs.getLong("ID_PERCENTUALE");	
							if(!rs.wasNull()) {
								Percentuale percentuale = new Percentuale();
								percentuale.setIdPercentuale(idPercentuale);
								percentuale.setValorePercentuale(rs.getLong("VALORE_PERCENTUALE"));
								finanziamentoDto.setPercentuale(percentuale);
							}
							Long idParere = rs.getLong("ID_PARERE");
							if(!rs.wasNull()) {
								Parere parere = new Parere();
								parere.setIdParere(idParere);
								parere.setDescrizione(rs.getString("DESC_PARERE"));
								finanziamentoDto.setParere(parere);
							}
							Long idRendiconto = rs.getLong("ID_RENDICONTO");
							if(!rs.wasNull()) {
								Rendiconto rendiconto = new Rendiconto();
								rendiconto.setIdRendiconto(idRendiconto);
								rendiconto.setDescrizione(rs.getString("DESC_RENDICONTO"));
								finanziamentoDto.setRendiconto(rendiconto);
							}
							Long idStatoFinanziamento = rs.getLong("ID_STATO_FINANZIAMENTO");	
							if(!rs.wasNull()) {
								StatoFinanziamento statoFinanziamento = new StatoFinanziamento();
								statoFinanziamento.setIdStatoFinanziamento(idStatoFinanziamento);
								statoFinanziamento.setDescrizione(rs.getString("DESC_STATO_FINANZIAMENTO"));
								statoFinanziamento.setCodStatoFinanziamento(rs.getString("COD_STATO_FINANZIAMENTO"));
								finanziamentoDto.setStatoFinanziamento(statoFinanziamento);						
							}
							Long idFinanziamentoRinuncia = rs.getLong("ID_FINANZIAMENTO_RINUNCIA");	
							if(!rs.wasNull()) {
								Rinuncia rinuncia = new Rinuncia();
								rinuncia.setIdFinanziamentoRinuncia(idFinanziamentoRinuncia);
								rinuncia.setAttoRinuncia(rs.getString("ATTO_RINUNCIA"));
								rinuncia.setImporto(rs.getBigDecimal("IMPORTO"));
								rinuncia.setValuta(rs.getString("VALUTA_RINUNCIA"));
								finanziamentoDto.setRinuncia(rinuncia);
							}
							Long idStatoFinanziamentoPrec = rs.getLong("ID_STATO_FINANZIAMENTO_PREC");	
							if(!rs.wasNull()) {
								StatoFinanziamento statoFinanziamentoPrec = new StatoFinanziamento();
								statoFinanziamentoPrec.setIdStatoFinanziamento(idStatoFinanziamentoPrec);
								statoFinanziamentoPrec.setDescrizione(rs.getString("DESC_STATO_FINANZIAMENTO_PREC"));
								statoFinanziamentoPrec.setCodStatoFinanziamento(rs.getString("COD_STATO_FINANZIAMENTO_PREC"));
								finanziamentoDto.setStatoFinanziamentoPrec(statoFinanziamentoPrec);
							}
							Long idPraticaUrb = rs.getLong("ID_PRATICA_URB");	
							if(!rs.wasNull()) {
								PraticaUrbGfu praticaUrbGfu = new PraticaUrbGfu();
								praticaUrbGfu.setIdPraticaUrb(idPraticaUrb);
								praticaUrbGfu.setNumPraticaUrb(rs.getString("NUM_PRATICA_URB"));
								praticaUrbGfu.setNumAttoApprovazioneUrb(rs.getString("NUM_ATTO_APPROVAZIONE_URB"));
								praticaUrbGfu.setDataAttoApprovazioneUrb(rs.getString("DATA_ATTO_APPROVAZIONE_URB"));
								praticaUrbGfu.setFkFinanziamento(rs.getBigDecimal("FK_FINANZIAMENTO_PRATICA_URB"));
								finanziamentoDto.setPraticaUrbGfu(praticaUrbGfu);
							}

							elencoErogazione = new ArrayList<Erogazione>();
							finanziamentoDto.setErogazioni(elencoErogazione);						

							lastKey = idFinanziamento;
						}
						rs.getLong("ID_EROGAZIONE");
						if (!rs.wasNull()) {
							elencoErogazione.add(new Erogazione(rs.getLong("ID_EROGAZIONE"), rs.getBigDecimal("FK_TIPO_EROGAZIONE"), rs.getString("DESC_TIPO_EROGAZIONE"), rs.getBigDecimal("FK_FINANZIAMENTO"),
									rs.getBigDecimal("IMPORTO_EROGAZIONE"), rs.getString("VALUTA_EROGAZIONE"), rs.getString("NUM_DETERMINA"), rs.getString("DATA_DETERMINA")));
						}
					}

					if (finanziamentoDto.getIdFinanziamento() == null)
					{
						return null;
					}

					return finanziamentoDto;
				}
			});
		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[FinanziamentoDaoImpl::findFinanziamentoByPk]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[FinanziamentoDaoImpl::findFinanziamentoByPk] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[FinanziamentoDaoImpl::findFinanziamentoByPk] END");
		}
	}

	@Override
	public Finanziamento updateFinanziamento(Long idFinanziamento, Finanziamento finanziamento)
			throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_FINANZIAMENTO SET IMPORTO_FINANZIABILE = :IMPORTO_FINANZIABILE, IMPORTO_AMMESSO = :IMPORTO_AMMESSO, "
					+ " fk_tetto_max = :fk_tetto_max, NOTE = :NOTE, "
					+ " FK_LEGGE_PROVV_DR = :FK_LEGGE_PROVV_DR, FK_PERCENTUALE = :FK_PERCENTUALE, FK_PARERE = :FK_PARERE, FK_RENDICONTO = :FK_RENDICONTO,"
					+ " FK_STATO_FINANZIAMENTO = :FK_STATO_FINANZIAMENTO, FK_STATO_FINANZ_PREC = :FK_STATO_FINANZ_PREC, FK_FINANZIAMENTO_RINUNCIA = :FK_FINANZIAMENTO_RINUNCIA "
					+ " WHERE ID_FINANZIAMENTO = :ID_FINANZIAMENTO ";		

			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("IMPORTO_FINANZIABILE", finanziamento.getImportoFinanziabile());
			paramMap.put("IMPORTO_AMMESSO", finanziamento.getImportoAmmesso());
			paramMap.put("fk_tetto_max", finanziamento.getFkImportoTettoMax());
			paramMap.put("NOTE", finanziamento.getNote());
			paramMap.put("FK_LEGGE_PROVV_DR", finanziamento.getIdLeggeProvvDr());
			paramMap.put("FK_PERCENTUALE", finanziamento.getPercentuale() != null ? finanziamento.getPercentuale().getIdPercentuale() : null);
			paramMap.put("FK_PARERE",finanziamento.getParere() != null ? finanziamento.getParere().getIdParere() : null);
			paramMap.put("FK_RENDICONTO", finanziamento.getRendiconto() != null ? finanziamento.getRendiconto().getIdRendiconto() : null);
			paramMap.put("FK_STATO_FINANZIAMENTO", finanziamento.getStatoFinanziamento() != null ? finanziamento.getStatoFinanziamento().getIdStatoFinanziamento() : null);
			paramMap.put("FK_STATO_FINANZ_PREC", finanziamento.getStatoFinanziamentoPrec() != null ? finanziamento.getStatoFinanziamentoPrec().getIdStatoFinanziamento() : null);
			paramMap.put("FK_FINANZIAMENTO_RINUNCIA", finanziamento.getRinuncia() != null ? finanziamento.getRinuncia().getIdFinanziamentoRinuncia() : null);
			paramMap.put("ID_FINANZIAMENTO", idFinanziamento);

			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [idFinanziamento] = " + idFinanziamento);
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [IMPORTO_FINANZIABILE] = " + finanziamento.getImportoFinanziabile());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [IMPORTO_AMMESSO] = " + finanziamento.getImportoAmmesso());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [fk_tetto_max] = " + finanziamento.getFkImportoTettoMax());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [NOTE] = " + finanziamento.getNote());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_LEGGE_PROVV_DR] = " + finanziamento.getIdLeggeProvvDr());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_PERCENTUALE"+ finanziamento.getPercentuale());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_PARERE] = " + finanziamento.getParere());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_RENDICONTO] = " + finanziamento.getRendiconto());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_STATO_FINANZIAMENTO] = " +  finanziamento.getStatoFinanziamento());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_STATO_FINANZ_PREC] = " + finanziamento.getStatoFinanziamentoPrec());
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  param [FK_FINANZIAMENTO_RINUNCIA] = " + finanziamento.getRinuncia() );        

			int updateResult = namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[FinanziamentoDaoImpl::updateFinanziamento] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamento] END ");
		}
		return finanziamento;
	}

	@Override
	public List<RichiestaProvv> findAllProvvFinToRichiesta(Long idStatoFinanziamento, Long idLeggeProvvDr,
			String dataProtRichiestaDa, String dataProtRichiestaA) throws DaoException, SystemException {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT R.ID_RICHIESTA, R.NUM_PROTOCOLLO,TO_CHAR(R.DATA_PROTOCOLLO, 'DD/MM/YYYY') DATA_PROTOCOLLO, LPDR.ID_LEGGE_PROVV_DR, " );
		sql.append("LPDR.DESCRIZIONE DESC_LEGGE_PROVV_DR, F.ID_FINANZIAMENTO " );
		sql.append("FROM GFU_T_RICHIESTA R LEFT OUTER JOIN GFU_R_RICHIESTA_PROVV RP ON R.ID_RICHIESTA = RP.FK_RICHIESTA ");
		sql.append("LEFT OUTER JOIN GFU_T_LEGGE_PROVV_DR LPDR ON LPDR.ID_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR ");
		sql.append("LEFT OUTER JOIN GFU_T_FINANZIAMENTO F ON F.FK_LEGGE_PROVV_DR = LPDR.ID_LEGGE_PROVV_DR and f.id_finanziamento = RP.fk_finanziamento ");
		sql.append("WHERE F.FK_STATO_FINANZIAMENTO = :FK_STATO_FINANZIAMENTO ");
		sql.append("AND (:FK_LEGGE_PROVV_DR::NUMERIC IS NULL OR (F.FK_LEGGE_PROVV_DR)  = (:FK_LEGGE_PROVV_DR::NUMERIC)) ");
		sql.append("AND R.IS_VALID IS TRUE ");
		if(dataProtRichiestaDa != null && !dataProtRichiestaDa.isEmpty()) {
			if(dataProtRichiestaA == null || dataProtRichiestaA.equals("")) {
				sql.append(" and R.DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND CURRENT_DATE ");
			}else {
				sql.append(" and R.DATA_PROTOCOLLO BETWEEN TO_DATE(:DATA_PROTOCOLLO_DA, 'DD/MM/YYYY') AND TO_DATE(:DATA_PROTOCOLLO_A, 'DD/MM/YYYY') ");
			} 
		}
		sql.append(" ORDER BY R.ID_RICHIESTA ");

		LOG.debug("[FinanziamentoDaoImpl - findAllProvvFinToRichiesta] param  idStatoFinanziamento = " + idStatoFinanziamento);
		LOG.debug("[FinanziamentoDaoImpl - findAllProvvFinToRichiesta] param  idLeggeProvvDr = " + idLeggeProvvDr);
		LOG.debug("[FinanziamentoDaoImpl - findAllProvvFinToRichiesta] param  dataProtRichiestaDa = " + dataProtRichiestaDa);
		LOG.debug("[FinanziamentoDaoImpl - findAllProvvFinToRichiesta] param  dataProtRichiestaA = " + dataProtRichiestaA);
		LOG.debug("[FinanziamentoDaoImpl - findAllProvvFinToRichiesta] query =" + sql.toString());

		Map<String, Object> paramMap = new HashMap<String, Object>();

		paramMap.put("FK_STATO_FINANZIAMENTO", idStatoFinanziamento);
		paramMap.put("FK_LEGGE_PROVV_DR", idLeggeProvvDr);
		if(dataProtRichiestaDa != null && !dataProtRichiestaDa.isEmpty()) {
			paramMap.put("DATA_PROTOCOLLO_DA", dataProtRichiestaDa);

			if(dataProtRichiestaA != null && !dataProtRichiestaA.isEmpty()) {
				paramMap.put("DATA_PROTOCOLLO_A", dataProtRichiestaA);
			}			
		}

		List<RichiestaProvv> provvFinToRichiestaList = null;
		try {		
			provvFinToRichiestaList = namedJdbcTemplate.query(sql.toString(), paramMap, new ProvvFinToRichiestaRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[FinanziamentoDaoImpl::findAllProvvFinToRichiesta]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[FinanziamentoDaoImpl::findAllProvvFinToRichiesta] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[FinanziamentoDaoImpl::findAllProvvFinToRichiesta] END");
		}
		return (List<RichiestaProvv>) provvFinToRichiestaList;
	}

	public void deleteFinanziamento(Long idFinanziamento) throws DaoException, SystemException {
		try {
			LOG.debug("[FinanziamentoDaoImpl::deleteFinanziamento] idFinanziamento = " + idFinanziamento);

			String queryDelete ="DELETE FROM GFU_T_FINANZIAMENTO WHERE ID_FINANZIAMENTO = :ID_FINANZIAMENTO ";
			Map<String, Object> paramMapDelete = new HashMap<String, Object>();
			paramMapDelete.put("ID_FINANZIAMENTO", idFinanziamento);

			namedJdbcTemplate.update(queryDelete, paramMapDelete);

			LOG.debug("[FinanziamentoDaoImpl::deleteFinanziamento] queryDelete  = " + queryDelete);
			LOG.debug("[FinanziamentoDaoImpl::deleteFinanziamento]  eliminazione effettuata. Stato = SUCCESS ");
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
					"[FinanziamentoDaoImpl::deleteFinanziamento] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[FinanziamentoDaoImpl::deleteFinanziamento] END ");
		}
	}

	@Override
	public Long countFinanziamentoConFlagRinunciaTrue(Long idFinanziamento) throws DaoException, SystemException {
		LOG.debug("[FinanziamentoDaoImpl::countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] BEGIN");

		StringBuilder sql = new StringBuilder();	

		sql.append(" SELECT COUNT(*) ");
		sql.append(" FROM GFU_T_FINANZIAMENTO F , GFU_R_RICHIEDENTE_PROVV RP, GFU_R_RICHIESTA_PROVV RIC_P, GFU_R_RICHIESTA_RICHIEDENTE RIC_RIC  ");
		sql.append(" WHERE F.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR  ");
		sql.append(" AND RIC_P.FK_LEGGE_PROVV_DR = RP.FK_LEGGE_PROVV_DR  ");
		sql.append(" AND RIC_P.FK_FINANZIAMENTO = F.ID_FINANZIAMENTO  ");
		sql.append(" AND RP.FLG_RINUNCIA IS "+Boolean.TRUE);
		sql.append(" AND F.ID_FINANZIAMENTO = :ID_FINANZIAMENTO ");
		sql.append(" AND RIC_RIC.FK_RICHIESTA = RIC_P.FK_RICHIESTA ");
		sql.append(" AND RIC_RIC.FK_RICHIEDENTE = RP.FK_RICHIEDENTE ");


		long conteggio = 0;

		LOG.debug("[FinanziamentoDaoImpl - countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] query = " + sql.toString());
		LOG.debug("[FinanziamentoDaoImpl - countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] param idFinanziamento = " + idFinanziamento);


		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_FINANZIAMENTO", idFinanziamento);

		try
		{
			conteggio = namedJdbcTemplate.queryForObject(sql.toString(), paramMap, Long.class);

		} 
		catch (EmptyResultDataAccessException ex)
		{
			LOG.debug("[FinanziamentoDaoImpl - countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex)
		{
			LOG.error("[FinanziamentoDaoImpl::countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] esecuzione query", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally
		{
			LOG.debug("[FinanziamentoDaoImpl::countFinanziamentoSenzaRinunciaConFlagRinunciaTrue] END");
		}

		return conteggio;			
	}

	@Override
	public void updateFkFinanziamentoRinunciaToNull(Long idFinanziamento) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_FINANZIAMENTO SET FK_FINANZIAMENTO_RINUNCIA = NULL WHERE ID_FINANZIAMENTO  = :ID_FINANZIAMENTO  ";

			LOG.debug("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("ID_FINANZIAMENTO", idFinanziamento);

			LOG.debug("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull]  param [idFinanziamento] = " + idFinanziamento);

			int updateResult = namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[FinanziamentoDaoImpl::updateFinanziamentoRinunciaToNull]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[FinanziamentoDaoImpl::updateFkFinanziamentoRinunciaToNull] END ");
		}
	}
}