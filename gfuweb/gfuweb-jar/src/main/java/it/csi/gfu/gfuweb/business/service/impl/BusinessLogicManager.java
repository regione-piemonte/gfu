/*******************************************************************************
 * © Copyright Regione Piemonte – 2021
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 ******************************************************************************/
package it.csi.gfu.gfuweb.business.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.business.service.BusinessLogic;
import it.csi.gfu.gfuweb.dto.Error;
import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.dto.erogazione.DeterminaToErogazioni;
import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.dto.excel.RichiesteForExcelRowDto;
import it.csi.gfu.gfuweb.dto.finanziamento.Finanziamento;
import it.csi.gfu.gfuweb.dto.filter.RichiestaFilter;
import it.csi.gfu.gfuweb.dto.associazione.Associazione;
import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.dto.popolazione.VincoloPopolazione;
import it.csi.gfu.gfuweb.dto.praticaurb.PraticaUrbGfu;
import it.csi.gfu.gfuweb.dto.provvedimento.Provvedimento;
import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.dto.richiedente.RichiedenteProvv;
import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.dto.rinuncia.Rinuncia;
import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.dto.tetto.TettoMax;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;
import it.csi.gfu.gfuweb.dto.richiesta.RichiestaToRicercaAvanzata;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.DatiInputErratiException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.LeggeDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.ParereDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.PercentualeDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.RendicontoDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.TettoMaxDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.DrDao;
import it.csi.gfu.gfuweb.integration.dao.ProfiloDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.ProvvedimentoDao;
import it.csi.gfu.gfuweb.integration.dao.UtenteDao;
import it.csi.gfu.gfuweb.integration.dao.associazione.AssociazioneRegPieDao;
import it.csi.gfu.gfuweb.integration.dao.comuneregpie.ComuneRegPieDao;
import it.csi.gfu.gfuweb.integration.dao.erogazione.ErogazioneDao;
import it.csi.gfu.gfuweb.integration.dao.finanziamento.FinanziamentoDao;
import it.csi.gfu.gfuweb.integration.dao.finanziamento.FinanziamentoRinunciaDao;
import it.csi.gfu.gfuweb.integration.dao.leggeprovvdr.LeggeProvvDrDao;
import it.csi.gfu.gfuweb.integration.dao.praticaurb.PraticaUrbDao;
import it.csi.gfu.gfuweb.integration.dao.richiedente.RichiedenteDao;
import it.csi.gfu.gfuweb.integration.dao.richiedente.RichiedenteProvvDao;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaDao;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaProvvDao;
import it.csi.gfu.gfuweb.integration.dao.richiesta.RichiestaRichiedenteDao;
import it.csi.gfu.gfuweb.integration.dao.statofinanziamento.StatoFinanziamentoDao;
import it.csi.gfu.gfuweb.integration.dao.anagrafiche.VincoloPopolazioneDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;
import it.csi.gfu.gfuweb.util.ValidatorDto;


@Component("businessLogic")
public class BusinessLogicManager extends BusinessLogicExtServiceManager implements BusinessLogic
{
	protected transient Logger LOG = Logger.getLogger(Constants.LOGGER);

	@Context 
	private ServletContext context;

	@Autowired
	private UtenteDao utenteDao;
	@Autowired
	private ProfiloDao profiloDao;
	@Autowired
	private DrDao drDao;
	@Autowired
	private ProvvedimentoDao provvedimentoDao;
	@Autowired
	private LeggeDao leggeDao;
	@Autowired
	private PercentualeDao percentualeDao;
	@Autowired
	private ParereDao parereDao;
	@Autowired
	private RendicontoDao rendicontoDao;
	@Autowired
	private ComuneRegPieDao comuneRegPieDao;
	@Autowired
	private AssociazioneRegPieDao associazioneRegPieDao;
	@Autowired
	private RichiestaDao richiestaDao;
	@Autowired
	private VincoloPopolazioneDao vincoloPopolazioneDao;
	@Autowired
	private RichiedenteDao richiedenteDao;
	@Autowired
	private LeggeProvvDrDao leggeProvvDrDao;
	@Autowired
	private FinanziamentoDao finanziamentoDao;
	@Autowired
	private RichiestaProvvDao richiestaProvvDao;
	@Autowired
	private RichiestaRichiedenteDao richiestaRichiedenteDao;
	@Autowired
	private ErogazioneDao erogazioneDao;
	@Autowired
	private StatoFinanziamentoDao statoFinanziamentoDao;
	@Autowired
	private RichiedenteProvvDao richiedenteProvvDao;
	@Autowired
	private TettoMaxDao tettoMaxDao;
	@Autowired
	private FinanziamentoRinunciaDao finanziamentoRinunciaDao;
	@Autowired
	private PraticaUrbDao praticaUrbDao;


	@Override
	public List<Utente> getUtenteByFilter(UtenteFilter utenteFilter)throws DaoException, SystemException {
		List<Utente>  listUtenteDto;
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param nome = " + utenteFilter.getNome());
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param cognome = " + utenteFilter.getCognome());
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param codiceFiscale = " + utenteFilter.getCodiceFiscale());
		listUtenteDto = utenteDao.findUtenteByFilter(utenteFilter);
		System.out.println("listUtenteDto size MMM 8 " + listUtenteDto.size());
		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 5 " + listUtenteDto.get(0).getCognome());
		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 6 " + listUtenteDto.get(0).getProfilo().getCodProfilo());
		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 7 " + listUtenteDto.get(0).getCodiceFiscale());
		return listUtenteDto;
	}

	@Override
	public Utente getUtenteCollegato(UtenteFilter utenteFilter)throws DaoException, SystemException {
		Utente  utenteDto;
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param nome = " + utenteFilter.getNome());
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param cognome = " + utenteFilter.getCognome());
		LOG.debug("[BusinessLogicManager : getUtenteByFilter] param codiceFiscale = " + utenteFilter.getCodiceFiscale());
		utenteDto = utenteDao.findUtenteAuth(utenteFilter);

		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 5 " + utenteDto.getCognome());
		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 6 " + utenteDto.getProfilo().getCodProfilo());
		System.out.println("listUtenteDto dentro a BusinessLogicManager getUtenteByFilter MMM 7 " + utenteDto.getCodiceFiscale());
		return utenteDto;
	}

	@Override
	public Profilo getProfiloById(BigDecimal idProfilo) throws DaoException, SystemException
	{
		Profilo profilo;
		LOG.debug("[BusinessLogicManager : getProfiloById ]  param idProfilo = " + idProfilo);
		profilo = profiloDao.findProfiloById(idProfilo);
		return profilo;
	}
	@Override
	public List<Profilo> getProfilo() throws DaoException, SystemException {
		List<Profilo> profiloList;
		LOG.debug("[BusinessLogicManager : getProfilo ] ");
		profiloList = profiloDao.findProfilo();
		return profiloList;
	}	

	@Override
	public List<Dr> readAllDr(Boolean isValid) throws DaoException, SystemException {
		List<Dr> drList;
		LOG.debug("[BusinessLogicManager : readAllDr ] ");
		drList = drDao.findAllDr(isValid);
		return drList;

	}
	@Override
	public Dr readDrByPk(Long id) throws DaoException, SystemException {
		Dr dr;
		LOG.debug("[BusinessLogicManager : readDrByPk ]  param id = " + id);
		dr = drDao.findDrByPk(id);
		return dr;

	}
	@Override
	public Dr createDr(Dr dr) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createDr] param dr = " + dr);
		dr = drDao.createDr(dr);
		return drDao.findDrByPk(dr.getIdDr());

	}
	@Override
	public Dr updateDr(Dr dr) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateDr] param dr = " + dr);
		drDao.updateDr(dr);	
		leggeProvvDrDao.findAndUpdateLeggeProvvDr(null, null, dr.getIdDr());
		return drDao.findDrByPk(dr.getIdDr());	
	}

	@Override
	public List<Provvedimento> readAllProvvedimenti(Boolean isValid) throws DaoException, SystemException {
		List<Provvedimento> provvedimentoList;
		LOG.debug("[BusinessLogicManager : readAllProvvedimenti ] ");
		provvedimentoList = provvedimentoDao.findAllProvvedimenti(isValid);
		return provvedimentoList;
	}

	@Override
	public Provvedimento readProvvedimentoByPk(Long id) throws DaoException, SystemException {
		Provvedimento provvedimento;
		LOG.debug("[BusinessLogicManager : readProvvedimentoByPk ]  param id = " + id);
		provvedimento = provvedimentoDao.findProvvedimentoByPk(id);
		return provvedimento;
	}
	@Override
	public Provvedimento createProvvedimento(Provvedimento provvedimento) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createProvvedimento] param provvedimento = " + provvedimento);
		provvedimento = provvedimentoDao.createProvvedimento(provvedimento);
		return provvedimentoDao.findProvvedimentoByPk(provvedimento.getIdProvvedimento());
	}
	@Override
	public Provvedimento updateProvvedimento(Provvedimento provvedimento) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateProvvedimento] param provvedimento = " + provvedimento);
		provvedimentoDao.updateProvvedimento(provvedimento);	
		leggeProvvDrDao.findAndUpdateLeggeProvvDr(null, provvedimento.getIdProvvedimento(), null);
		return provvedimentoDao.findProvvedimentoByPk(provvedimento.getIdProvvedimento());	
	}

	@Override
	public List<LeggeProvvDr> readAllLeggeProvvDr(String descLeggiProvvDr, Boolean isValid) throws DaoException, SystemException {
		List<LeggeProvvDr> leggeProvvDrList;
		LOG.debug("[BusinessLogicManager : readAllLeggeProvvDr ] ");
		leggeProvvDrList = leggeDao.findAllLeggeProvvDr(descLeggiProvvDr, isValid);
		return leggeProvvDrList;
	}
	@Override
	public LeggeProvvDr readLeggeProvvDrByPk(Long id) throws DaoException, SystemException {
		LeggeProvvDr leggeProvvDr;
		LOG.debug("[BusinessLogicManager : readLeggeProvvDrByPk ]  param id = " + id);
		leggeProvvDr = leggeDao.findLeggeProvvDrByPk(id);
		return leggeProvvDr;
	}
	@Override
	public LeggeProvvDr createLeggeProvvDr(LeggeProvvDr leggeProvvDr) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createLeggeProvvDr] param leggeProvvDr = " + leggeProvvDr);

		long conteggio = leggeProvvDrDao.countUnivocitaLeggeProvvDr(leggeProvvDr.getIdLegge(), leggeProvvDr.getIdProvvedimento(), leggeProvvDr.getIdDr(), null);

		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_45_LEGGE_PROVV_DR_GIA_PRESENTE);
			error.setMessage(ErrorMessages.MESSAGE_45_LEGGE_PROVV_DR_GIA_PRESENTE);
			throw new DatiInputErratiException(error) ;
		}	

		String descLeggeProvvDr = setDescLeggeProvvDr(leggeProvvDr);

		leggeProvvDr.setDescLeggeProvvDr(descLeggeProvvDr);
		leggeProvvDr = leggeDao.createLeggeProvvDr(leggeProvvDr);
		return leggeDao.findLeggeProvvDrByPk(leggeProvvDr.getIdLeggeProvvDr());

	}	

	@Override
	public LeggeProvvDr updateLeggeProvvDr(LeggeProvvDr leggeProvvDr) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateLeggeProvvDr] param leggeProvvDr = " + leggeProvvDr);
		long conteggio = leggeProvvDrDao.countUnivocitaLeggeProvvDr(leggeProvvDr.getIdLegge(), leggeProvvDr.getIdProvvedimento(), leggeProvvDr.getIdDr(), leggeProvvDr.getIdLeggeProvvDr());

		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_45_LEGGE_PROVV_DR_GIA_PRESENTE);
			error.setMessage(ErrorMessages.MESSAGE_45_LEGGE_PROVV_DR_GIA_PRESENTE);
			throw new DatiInputErratiException(error) ;
		}	

		String descLeggeProvvDr = setDescLeggeProvvDr(leggeProvvDr);
		leggeProvvDr.setDescLeggeProvvDr(descLeggeProvvDr);
		leggeProvvDr = leggeDao.updateLeggeProvvDr(leggeProvvDr);
		return leggeDao.findLeggeProvvDrByPk(leggeProvvDr.getIdLeggeProvvDr());	
	}

	private String setDescLeggeProvvDr(LeggeProvvDr leggeProvvDr) {		
		String descLeggeProvvDr = leggeProvvDr.getDescLegge();
		descLeggeProvvDr += (leggeProvvDr.getDescProvvedimento()==null || leggeProvvDr.getDescProvvedimento().equals(""))?"":" # "+leggeProvvDr.getDescProvvedimento();
		descLeggeProvvDr += (leggeProvvDr.getDescDr()==null || leggeProvvDr.getDescDr().equals(""))?"":" # "+leggeProvvDr.getDescDr() ;
		return descLeggeProvvDr;
	}

	@Override
	public List<Comune> readAllComuni(String descComune, Boolean isValid)
			throws DaoException, SystemException {
		List<Comune> comuneList;
		LOG.debug("[BusinessLogicManager : readAllComuni ] ");
		comuneList = comuneRegPieDao.findAllComuni(descComune,isValid);
		return comuneList;
	}

	@Override
	public Comune readComuneByPk(String codIstat) throws DaoException, SystemException {
		Comune comune;
		LOG.debug("[BusinessLogicManager : readComuneByPk ]  param codIstat = " + codIstat);
		comune = comuneRegPieDao.findComuneByPk(codIstat);
		return comune;
	}
	@Override
	public List<Associazione> readAllAssociazioni(String descAssociazione, String tipoFormaAss, Boolean isValid)
			throws DaoException, SystemException {
		List<Associazione> associazioneList;
		LOG.debug("[BusinessLogicManager : readAllAssociazioni ] ");
		associazioneList = associazioneRegPieDao.findAllAssociazioni(descAssociazione, tipoFormaAss, isValid);
		return associazioneList;
	}

	@Override
	public Associazione readAssociazioneByPk(Long idAssociazione) throws DaoException, SystemException {
		Associazione associazione;
		LOG.debug("[BusinessLogicManager : readAssociazioneByPk ]  param idAssociazione = " + idAssociazione);
		associazione = associazioneRegPieDao.findAssociazioneByPk(idAssociazione).get(0);
		return associazione;
	}
	@Override
	public Associazione createAssociazione(Associazione associazione) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createAssociazione] param associazione = " + associazione);
		return associazioneRegPieDao.createAssociazione(associazione);
	}
	@Override
	public List<Legge> readAllLeggi(Boolean isValid) throws DaoException, SystemException {
		List<Legge> leggeList;
		LOG.debug("[BusinessLogicManager : readAllLeggi ] ");
		leggeList = leggeDao.findAllLeggi(isValid);
		return leggeList;
	}

	@Override
	public Legge readLeggeByPk(Long id) throws DaoException, SystemException {
		Legge legge;
		LOG.debug("[BusinessLogicManager : readLeggeByPk ]  param id = " + id);
		legge = leggeDao.findLeggeByPk(id);
		return legge;
	}

	@Override
	public Legge createLegge(Legge legge)	throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createLegge] param legge = " + legge);
		return leggeDao.createLegge(legge);
	}

	@Override
	public Legge updateLegge(Legge legge) throws DaoException, SystemException
	{
		LOG.debug("[BusinessLogicManager: updateLegge] param legge = " + legge);
		leggeDao.updateLegge(legge);	
		leggeProvvDrDao.findAndUpdateLeggeProvvDr(legge.getIdLegge(), null, null);
		return leggeDao.findLeggeByPk(legge.getIdLegge());		
	}

	@Override
	public List<Percentuale> readAllPercentuali(Boolean isValid) throws DaoException, SystemException {
		List<Percentuale> percentualeList;
		LOG.debug("[BusinessLogicManager : readAllPercentuali ] ");
		percentualeList = percentualeDao.findAllPercentuali(isValid);
		return percentualeList;
	}

	@Override
	public Percentuale readPercentualeByPk(Long id) throws DaoException, SystemException {
		Percentuale percentuale;
		LOG.debug("[BusinessLogicManager : readPercentualeByPk ]  param id = " + id);
		percentuale = percentualeDao.findPercentualeByPk(id);
		return percentuale;
	}

	@Override
	public Percentuale createPercentuale(Percentuale percentuale) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createPercentuale] param percentuale = " + percentuale);
		return percentualeDao.createPercentuale(percentuale);
	}

	@Override
	public Percentuale updatePercentuale(Percentuale percentuale) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updatePercentuale] param percentuale = " + percentuale);
		percentualeDao.updatePercentuale(percentuale);	
		return percentualeDao.findPercentualeByPk(percentuale.getIdPercentuale());	
	}

	@Override
	public List<Parere> readAllPareri(Boolean isValid) throws DaoException, SystemException {
		List<Parere> parereList;
		LOG.debug("[BusinessLogicManager : readAllParere ] ");
		parereList = parereDao.findAllPareri(isValid);
		return parereList;
	}

	@Override
	public Parere readParereByPk(Long idParere) throws DaoException, SystemException {
		Parere parere;
		LOG.debug("[BusinessLogicManager : readParereByPk ]  param id = " + idParere);
		parere = parereDao.findParereByPk(idParere);
		return parere;
	}

	@Override
	public Parere createParere(Parere parere) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createParere] param parere = " + parere);
		return parereDao.createParere(parere);
	}

	@Override
	public Parere updateParere(Parere parere) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateParere] param parere = " + parere);
		parereDao.updateParere(parere);
		return parereDao.findParereByPk(parere.getIdParere());
	}

	@Override
	public List<Rendiconto> readAllRendiconti(Boolean isValid) throws DaoException, SystemException {
		List<Rendiconto> rendicontoList;
		LOG.debug("[BusinessLogicManager : readAllRendiconti ] ");
		rendicontoList = rendicontoDao.findAllRendiconti(isValid);
		return rendicontoList;
	}
	@Override
	public Rendiconto readRendicontoByPk(Long idRendiconto) throws DaoException, SystemException {
		Rendiconto rendiconto;
		LOG.debug("[BusinessLogicManager : readRendicontoByPk ]  param idRendiconto = " + idRendiconto);
		rendiconto = rendicontoDao.findRendicontoByPk(idRendiconto);
		return rendiconto;
	}
	@Override
	public Rendiconto createRendiconto(Rendiconto rendiconto) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createRendiconto] param rendiconto = " + rendiconto);
		return rendicontoDao.createRendiconto(rendiconto);
	}
	@Override
	public Rendiconto updateRendiconto(Rendiconto rendiconto) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateRendiconto] param rendiconto = " + rendiconto);
		rendicontoDao.updateRendiconto(rendiconto);
		return rendicontoDao.findRendicontoByPk(rendiconto.getIdRendiconto());
	}
	@Override
	public List<TipoDr> readAllTipiDr() throws DaoException, SystemException {
		List<TipoDr> tipoDrList;
		LOG.debug("[BusinessLogicManager : readAllTipiDr ] ");
		tipoDrList = drDao.findAllTipiDr();
		return tipoDrList;
	}
	@Override
	public List<Richiesta> readAllRichieste(Boolean isValid) throws DaoException, SystemException {
		List<Richiesta> richiestaList;
		LOG.debug("[BusinessLogicManager : readAllRichieste ] ");
		richiestaList = richiestaDao.findAllRichieste(isValid);
		return richiestaList;
	}
	@Override
	public Richiesta readRichiestaByPk(Long idRichiesta) throws DaoException, SystemException {
		Richiesta richiesta;
		LOG.debug("[BusinessLogicManager : readRichiestaByPk ]  param idRichiesta = " + idRichiesta);
		richiesta = richiestaDao.findRichiestaByPk(idRichiesta);
		return richiesta;
	}
	@Override
	public Richiesta createRichiesta(Richiesta richiesta) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createRichiesta] param richiesta = " + richiesta);

		long conteggio = richiestaDao.countUnivocitaNumProtDataProt(richiesta);
		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_27_NUM_E_DATA_PROT_GIA_PRESENTE);
			error.setMessage(ErrorMessages.MESSAGE_27_NUM_E_DATA_PROT_GIA_PRESENTE);
			throw new DatiInputErratiException(error) ;
		}

		richiesta = richiestaDao.createRichiesta(richiesta);
		return richiestaDao.findRichiestaByPk(richiesta.getIdRichiesta());
	}
	@Override
	public Richiesta updateRichiesta(Long idRichiesta, Richiesta richiesta) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateRichiesta] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: updateRichiesta] param richiesta = " + richiesta);		
		long conteggio = richiedenteDao.countRichiedentiToRichiesta(idRichiesta);

		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_46_RICHIEDENTI_ASSOCIATI_RICHIESTA_MODIFICA_PROTOCOLLO_NON_AMMESSO);
			error.setMessage(ErrorMessages.MESSAGE_46_RICHIEDENTI_ASSOCIATI_RICHIESTA_MODIFICA_PROTOCOLLO_NON_AMMESSO);
			throw new DatiInputErratiException(error) ;
		}
		richiestaDao.updateRichiesta(idRichiesta, richiesta);
		return richiestaDao.findRichiestaByPk(idRichiesta);
	}
	@Override
	public void deleteRichiesta(Long idRichiesta) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: deleteRichiesta] param idRichiesta = " + idRichiesta);

		richiestaDao.deleteRichiesta(idRichiesta);

	}
	@Override
	public void updateRichiestaToFormaAssociativa(Long idRichiesta, Long idAssociazione)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateRichiestaToFormaAssociativa] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: updateRichiestaToFormaAssociativa] param idAssociazione = " + idAssociazione);
		long conteggio = richiedenteDao.countRichiedentiToRichiesta(idRichiesta);

		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_12_RICHIEDENTI_ASSOCIATI_RICHIESTA);
			error.setMessage(ErrorMessages.MESSAGE_12_RICHIEDENTI_ASSOCIATI_RICHIESTA);
			throw new DatiInputErratiException(error) ;
		}
		richiestaDao.updateRichiestaToFormaAssociativa(idRichiesta, idAssociazione);
	}

	@Override
	public List<Richiedente> readAllRichiedenti(Long idRichiesta) throws DaoException, SystemException {
		List<Richiedente> richiedenteList;
		LOG.debug("[BusinessLogicManager : readAllRichiedenti ] ");
		richiedenteList = richiedenteDao.findAllRichiedenti(idRichiesta);
		return richiedenteList;
	}	
	@Override
	public List<Richiedente> createRichiedente(Long idRichiesta, List<Richiedente> listRichiedente)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createRichiedente] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager : createRichiedente] param listRichiedente = " + listRichiedente);

		controlloSeEsisteAssociazioneConStatoNotInCorso(idRichiesta, null,ErrorMessages.CODE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA, ErrorMessages.MESSAGE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA);

		listRichiedente = richiedenteDao.createRichiedente(idRichiesta, listRichiedente);

		List<RichiestaProvv> richiestaProvvList = richiestaProvvDao.findAllRichiestaProvvToRichiesta(idRichiesta);
		if(!richiestaProvvList.isEmpty()) {
			richiedenteProvvDao.createRichiedenteProvv(richiestaProvvList, listRichiedente.get(0).getIdRichiedente());
		}
		return listRichiedente;
	}

	@Override
	public Richiedente readRichiedenteByPk(Long idRichiedente) throws DaoException, SystemException {
		Richiedente richiedente;
		LOG.debug("[BusinessLogicManager : readRichiedenteByPk ]  param idRichiedente = " + idRichiedente);
		richiedente = richiedenteDao.findRichiedenteByPk(idRichiedente);
		return richiedente;
	}

	@Override
	public void deleteRichiedente(Long idRichiesta, Long idRichiedente) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: deleteRichiedente] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: deleteRichiedente] param idRichiedente = " + idRichiedente);

		controlloSeEsisteAssociazioneConStatoNotInCorso(idRichiesta, null,ErrorMessages.CODE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA, ErrorMessages.MESSAGE_13_PROVVEDIMENTI_ASSOCIATI_RICHIESTA);

		long conteggio =  richiedenteProvvDao.countRichiedenteProvv(idRichiedente);
		if(conteggio > 0) {
			richiedenteProvvDao.deleteRichiedenteProvv(idRichiedente);
		}
		long conteggioUltimaRichiestaRichiedente = richiestaRichiedenteDao.countUltimaRichiestaRichiedente(idRichiesta);
		if(conteggioUltimaRichiestaRichiedente == 1) {
			richiestaDao.updateFormaAssociativaAtNullToRichiesta(idRichiesta);
			List<Long> idFinanziamentoList = richiestaProvvDao.findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta(idRichiesta, null);
			richiestaProvvDao.deleteRichiestaProvv(idRichiesta);
			if(idFinanziamentoList != null && !idFinanziamentoList.isEmpty()) {
				for (int i = 0; i < idFinanziamentoList.size(); i++) {
					finanziamentoDao.deleteFinanziamento(idFinanziamentoList.get(i));
				}
			}
		}
		richiedenteDao.deleteRichiedente(idRichiesta, idRichiedente);
	}

	@Override
	public List<LeggeProvvDr> readAllProvvedimentiToRichiesta(Long idRichiesta) throws DaoException, SystemException {
		List<LeggeProvvDr> provvedimentiList;
		LOG.debug("[BusinessLogicManager : readAllProvvedimentiToRichiesta ] ");
		provvedimentiList = leggeProvvDrDao.findAllProvvedimentiToRichiesta(idRichiesta);
		return provvedimentiList;
	}

	@Override
	public void deleteProvvToRichiedentiToRichiesta(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: deleteProvvToRichiedentiToRichiesta] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: deleteProvvToRichiedentiToRichiesta] param idLeggeProvvDr = " + idLeggeProvvDr);

		controlloSeEsisteAssociazioneConStatoNotInCorso(idRichiesta, idLeggeProvvDr,ErrorMessages.CODE_15_PROVVEDIMENTO_CON_FINANZIAMENTO_ASSOCIATO, ErrorMessages.MESSAGE_15_PROVVEDIMENTO_CON_FINANZIAMENTO_ASSOCIATO);

		List<Long> idFinanziamentoList = richiestaProvvDao.findFinanziamentiToDeleteRelProvvToRichiedentiToRichiesta(idRichiesta, idLeggeProvvDr);
		provvedimentoDao.deleteProvvToRichiedentiToRichiesta(idRichiesta, idLeggeProvvDr);

		if(idFinanziamentoList != null && !idFinanziamentoList.isEmpty()) {
			for (int i = 0; i < idFinanziamentoList.size(); i++) {
				finanziamentoDao.deleteFinanziamento(idFinanziamentoList.get(i));
			}
		}

	}
	@Override
	public List<RichiedenteProvv> createLeggeProvvDrToRichiesta(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: createLeggeProvvDrToRichiesta] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: createLeggeProvvDrToRichiesta] param idLeggeProvvDr = " + idLeggeProvvDr);
		long conteggioLeggeProvvDrValid = leggeProvvDrDao.countLeggeProvvDrValid(idLeggeProvvDr);
		if(conteggioLeggeProvvDrValid == 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_48_LEGGE_PROVV_DR_NON_VALIDA);
			error.setMessage(ErrorMessages.MESSAGE_48_LEGGE_PROVV_DR_NON_VALIDA);
			throw new DatiInputErratiException(error) ;
		}

		long conteggio = richiedenteDao.countRichiedentiToRichiesta(idRichiesta);
		if(conteggio == 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_18_RICHIESTA_SENZA_RICHIEDENTI_ASSOCIATI);
			error.setMessage(ErrorMessages.MESSAGE_18_RICHIESTA_SENZA_RICHIEDENTI_ASSOCIATI);
			throw new DatiInputErratiException(error) ;
		}
		controlloSeEsisteAssociazioneConStatoNotInCorso(idRichiesta, idLeggeProvvDr,ErrorMessages.CODE_14_PROVVEDIMENTO_CON_FINANZIAMENTO_PRESENTE_PER_COMUNE, ErrorMessages.MESSAGE_14_PROVVEDIMENTO_CON_FINANZIAMENTO_PRESENTE_PER_COMUNE);

		long conteggioUnivocita = provvedimentoDao.countUnivocitaRichiedenteProvvedimento(idRichiesta, idLeggeProvvDr);
		if(conteggioUnivocita > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_33_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO);
			error.setMessage(ErrorMessages.MESSAGE_33_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO);
			throw new DatiInputErratiException(error) ;
		}

		provvedimentoDao.createRelRichiestaProvv(idRichiesta, idLeggeProvvDr);
		provvedimentoDao.createRelRichiedenteProvv(idRichiesta, idLeggeProvvDr);		

		return richiedenteDao.findAllRichiedenteProvv(idRichiesta);
	}

	@Override
	public List<RichiedenteProvv> readAllRichiedenteProvv(Long idRichiesta) throws DaoException, SystemException {
		List<RichiedenteProvv> richiedenteProvvList;
		LOG.debug("[BusinessLogicManager : readAllRichiedenteProvv ] ");
		richiedenteProvvList = richiedenteDao.findAllRichiedenteProvv(idRichiesta);
		return richiedenteProvvList;
	}
	@Override
	public void updateRichiedenteProvv(Long idRichiesta, RichiedenteProvv richiedenteProvv) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateRichiedenteProvv] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: updateRichiedenteProvv] param richiedenteProvv = " + richiedenteProvv);

		if(!richiedenteProvv.getProvvedimentiToRichiedente().isEmpty()) {
			if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr()!= null) {
				Finanziamento finanziamento = finanziamentoDao.findFinanziamentoToProvRich(idRichiesta, richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr());
				if(finanziamento != null) {	
					if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue()||
							finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.CONCLUSO.longValue()||
							finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO.longValue()||
							finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO.longValue()||	
							finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA.longValue())) {
						Error error = new Error();
						error.setCode(ErrorMessages.CODE_24_RINUNCIA_DOCUMENTAZIONE_FINANZIAMENTO_PROVV_NON_AMMESSO);
						error.setMessage(ErrorMessages.MESSAGE_24_RINUNCIA_DOCUMENTAZIONE_FINANZIAMENTO_PROVV_NON_AMMESSO);
						throw new DatiInputErratiException(error) ;
					}
				}


				if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia() != null  && richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia()) {
					if(finanziamento != null) {						

						Long conteggio = richiedenteProvvDao.countRichiedentiCheHanRinunciatoAlProvvedimento(idRichiesta, richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr(), richiedenteProvv.getIdRichiedente(), Boolean.FALSE);
						if(conteggio == 0) {

							if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue())){
								Error error = new Error();
								error.setCode(ErrorMessages.CODE_40_RINUNCIA_ULTIMO_COMUNE_ACCONTO_EROGATO);
								error.setMessage(ErrorMessages.MESSAGE_40_RINUNCIA_ULTIMO_COMUNE_ACCONTO_EROGATO);
								throw new DatiInputErratiException(error) ;
							}else {
								updateStatoFinToFinanziamento(finanziamento.getIdFinanziamento(), Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA.longValue());								
							}
						}
					}
				}	

				if(finanziamento != null) {
					if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()!=Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue())&&
							(finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()!=Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO.longValue())){
						if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagDocumentazione() != null  && !richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagDocumentazione()) {
							Error error = new Error();
							error.setCode(ErrorMessages.CODE_35_DOCUMENTAZIONE_NON_MODIFICABILE_STATO_NOT_IN_CORSO_OR_SOSPESO);
							error.setMessage(ErrorMessages.MESSAGE_35_DOCUMENTAZIONE_NON_MODIFICABILE_STATO_NOT_IN_CORSO_OR_SOSPESO);
							throw new DatiInputErratiException(error) ;
						}
					}
					if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue()||
							finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue())) {						
						if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia() != null  && !richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia()) {
							Error error = new Error();
							error.setCode(ErrorMessages.CODE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO);
							error.setMessage(ErrorMessages.MESSAGE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO);
							throw new DatiInputErratiException(error) ;
						}
					}

					if(richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia() != null  && !richiedenteProvv.getProvvedimentiToRichiedente().get(0).isFlagRinuncia()) {
						if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue()||
								finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO.longValue()||
								finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO.longValue())) {						

							Long conteggio = richiedenteProvvDao.countRichiedentiCheHanRinunciatoAlProvvedimento(idRichiesta, richiedenteProvv.getProvvedimentiToRichiedente().get(0).getIdLeggeProvvDr(), richiedenteProvv.getIdRichiedente(), Boolean.TRUE);
							if(conteggio == 0) {
								if(finanziamento.getRinuncia()!= null) {
									if(finanziamento.getRinuncia().getIdFinanziamentoRinuncia() != null) {
										Long idFinanziamentoRinuncia = finanziamento.getRinuncia().getIdFinanziamentoRinuncia();										
										finanziamentoDao.updateFkFinanziamentoRinunciaToNull(finanziamento.getIdFinanziamento());
										finanziamentoRinunciaDao.deleteFinanziamentoRinuncia(idFinanziamentoRinuncia);
									}				
								}
							}
						}
					}

				}
			}
		}
		richiedenteDao.updateRichiedenteProvv(idRichiesta, richiedenteProvv);
	}

	@Override
	public List<VincoloPopolazione> readAllVincoloPopolazione(Boolean isValid) throws DaoException, SystemException {
		List<VincoloPopolazione> popolazioneList;
		LOG.debug("[BusinessLogicManager : readAllVincoloPopolazione ] ");
		popolazioneList = vincoloPopolazioneDao.findAllVincoloPopolazione(isValid);
		return popolazioneList;
	}
	@Override
	public VincoloPopolazione readVincoloPopolazioneByPk(Long idVincoloPopolazione) throws DaoException, SystemException {
		VincoloPopolazione popolazione;
		LOG.debug("[BusinessLogicManager : readVincoloPopolazioneByPk ]  param idVincoloPopolazione = " + idVincoloPopolazione);
		popolazione = vincoloPopolazioneDao.findVincoloPopolazioneByPk(idVincoloPopolazione);
		return popolazione;
	}
	@Override
	public VincoloPopolazione createVincoloPopolazione(VincoloPopolazione vincoloPopolazione) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createVincoloPopolazione] param vincoloPopolazione = " + vincoloPopolazione);
		String descrizione =  vincoloPopolazione.getSegno()+" " +vincoloPopolazione.getPopolazione();
		vincoloPopolazione.setDescrizione(descrizione);
		vincoloPopolazione = vincoloPopolazioneDao.createVincoloPopolazione(vincoloPopolazione);
		return vincoloPopolazioneDao.findVincoloPopolazioneByPk(vincoloPopolazione.getIdVincoloPopolazione());
	}
	@Override
	public VincoloPopolazione updateVincoloPopolazione(VincoloPopolazione vincoloPopolazione) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateVincoloPopolazione] param vincoloPopolazione = " + vincoloPopolazione);
		vincoloPopolazioneDao.updateVincoloPopolazione(vincoloPopolazione);
		return vincoloPopolazioneDao.findVincoloPopolazioneByPk(vincoloPopolazione.getIdVincoloPopolazione());
	}

	private void controlloSeEsisteAssociazioneConStatoNotInCorso(Long idRichiesta, Long idLeggeProvvDr, String errorMessageCode, String errorMessageDescription) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : controlloSeEsisteAssociazioneConStatoNotInCorso] param idRichiesta = " + idRichiesta);
		long conteggio = finanziamentoDao.countAssociazioneFinanziamentoWithStatoNotInCorso(idRichiesta, idLeggeProvvDr);

		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(errorMessageCode);
			error.setMessage(errorMessageDescription);
			throw new DatiInputErratiException(error) ;
		}			
	}
	@Override
	public Finanziamento readFinanziamentoToProvRich(Long idRichiesta, Long idLeggeProvvDr)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readFinanziamentoToProvRich ]  param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager : readFinanziamentoToProvRich ]  param idLeggeProvvDr = " + idLeggeProvvDr);
		return finanziamentoDao.findFinanziamentoToProvRich(idRichiesta, idLeggeProvvDr);
	}
	@Override
	public List<RichiestaToRicercaAvanzata> readRichiesteByFilter(RichiestaFilter richiestaFilter) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readRichiesteByFilter ] ");
		return richiestaDao.findRichiestaByFilter(richiestaFilter);
	}

	@Override
	public Finanziamento createFinanziamento(Long idRichiesta, Long idLeggeProvvDr, Finanziamento finanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createFinanziamento] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager : createFinanziamento] param idLeggeProvvDr = " + idLeggeProvvDr);
		LOG.debug("[BusinessLogicManager : createFinanziamento] param finanziamento = " + finanziamento);
		long conteggio = richiestaProvvDao.countAssociazioneRichiestaProvv(idRichiesta, idLeggeProvvDr);

		if(conteggio == 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_19_ASSOCIAZIONE_RICHIESTA_PROVV_INESISTENTE);
			error.setMessage(ErrorMessages.MESSAGE_19_ASSOCIAZIONE_RICHIESTA_PROVV_INESISTENTE);
			throw new DatiInputErratiException(error) ;
		}	

		finanziamento = finanziamentoDao.createFinanziamento(idRichiesta, idLeggeProvvDr, finanziamento);

		richiestaProvvDao.updateFkFinanziamento(finanziamento.getIdFinanziamento(), idRichiesta, idLeggeProvvDr);

		if(finanziamento.getRinuncia()!=null) {					
			if(finanziamento.getRinuncia().getIdFinanziamentoRinuncia()==null) {
				if(finanziamento.getRinuncia().getImporto() != null  && finanziamento.getRinuncia().getImporto().compareTo(BigDecimal.ZERO) != 0) {
					Rinuncia rinuncia = finanziamentoRinunciaDao.createFinanziamentoRinuncia(finanziamento.getRinuncia());
					finanziamento.getRinuncia().setIdFinanziamentoRinuncia(rinuncia.getIdFinanziamentoRinuncia());					
					finanziamentoDao.updateFinanziamento(finanziamento.getIdFinanziamento(), finanziamento);	
				}		
			}
		}


		return finanziamentoDao.findFinanziamentoByPk(finanziamento.getIdFinanziamento());
	}
	@Override
	public Finanziamento readFinanziamentoByPk(Long idFinanziamento)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readFinanziamentoByPk ]  param idFinanziamento = " + idFinanziamento);
		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}
	@Override
	public Finanziamento updateFinanziamento(Long idRichiesta, Long idFinanziamento, Finanziamento finanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateFinanziamento] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[BusinessLogicManager: updateFinanziamento] param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager: updateFinanziamento] param Finanziamento = " + finanziamento);
		if(finanziamento.getRinuncia()!=null) {					
			if(finanziamento.getRinuncia().getIdFinanziamentoRinuncia()==null) {	
				if(finanziamento.getRinuncia().getImporto() != null  && finanziamento.getRinuncia().getImporto().compareTo(BigDecimal.ZERO) != 0) {
					Rinuncia rinuncia = finanziamentoRinunciaDao.createFinanziamentoRinuncia(finanziamento.getRinuncia());
					finanziamento.getRinuncia().setIdFinanziamentoRinuncia(rinuncia.getIdFinanziamentoRinuncia());
				}
			}else {
				if(finanziamento.getRinuncia().getImporto() == null  || finanziamento.getRinuncia().getImporto().compareTo(BigDecimal.ZERO) == 0) {
					Error error = new Error();
					error.setCode(ErrorMessages.CODE_39_IMPORTO_RINUNCIA_OBBLIGATORIO);
					error.setMessage(ErrorMessages.MESSAGE_39_IMPORTO_RINUNCIA_OBBLIGATORIO);
					throw new DatiInputErratiException(error) ;
				}

				finanziamentoRinunciaDao.updateFinanziamentoRinuncia(finanziamento);

			}
		}

		finanziamentoDao.updateFinanziamento(idFinanziamento, finanziamento);		
		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}
	@Override
	public List<Erogazione> readAllErogazioniToFinanziamento(Long idFinanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : readAllErogazioniToFinanziamento ] ");
		return erogazioneDao.findAllErogazioniToFinanziamento(idFinanziamento);
	}
	@Override
	public Finanziamento createErogazione(Long idFinanziamento, Erogazione erogazione)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createErogazione] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[BusinessLogicManager : createErogazione] param Erogazione = " + erogazione);

		Finanziamento finanziamento = finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
		if(finanziamento == null) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_20_FINANZIAMENTO_INESISTENTE);
			error.setMessage(ErrorMessages.MESSAGE_20_FINANZIAMENTO_INESISTENTE);
			throw new DatiInputErratiException(error) ;
		}

		if(erogazione != null) {
			erogazioneDao.createErogazione(idFinanziamento, erogazione);	

			if(erogazione.getIdTipoErogazione()!= null) {
				if(erogazione.getIdTipoErogazione().intValue()==(Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.ACCONTO)) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue(), null);
				}else if(erogazione.getIdTipoErogazione().intValue()==(Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.SALDO)) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue(), null);
				}
			}
		}
		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}

	@Override
	public void updateAllErogazioniToDetermina(DeterminaToErogazioni determinaToErogazioni)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateAllErogazioniToDetermina] param DeterminaToErogazioni = " + determinaToErogazioni);

		erogazioneDao.updateAllErogazioniToDetermina(determinaToErogazioni);

		if(determinaToErogazioni.getFinanziamentiDaAssociare() != null) {
			for (int i = 0; i < determinaToErogazioni.getFinanziamentiDaAssociare().size(); i++) {
				Long idFinanziamento = determinaToErogazioni.getFinanziamentiDaAssociare().get(i).getIdFinanziamento().longValue();
				StatoFinanziamento statoFinanziamento= statoFinanziamentoDao.findStatoFinanziamento(idFinanziamento);
				if(statoFinanziamento.getIdStatoFinanziamento() == Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue()) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue(),null);
				}else if(statoFinanziamento.getIdStatoFinanziamento() == Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue()) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.CONCLUSO.longValue(), null);
				}
			}
		}
	}
	@Override
	public StatoFinanziamento readStatoFinanziamento(Long idFinanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : readStatoFinanziamento ] ");
		return statoFinanziamentoDao.findStatoFinanziamento(idFinanziamento);
	}
	@Override
	public Finanziamento updateStatoFinToFinanziamento(Long idFinanziamento, Long idStatoFinanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateStatoFinToFinanziamento] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[BusinessLogicManager: updateStatoFinToFinanziamento] param idStatoFinanziamento = " + idStatoFinanziamento);

		Finanziamento finanziamento = finanziamentoDao.findFinanziamentoByPk(idFinanziamento);

		Long idStatoFinanziamentoDb = null;

		if(finanziamento.getStatoFinanziamento() != null) {
			idStatoFinanziamentoDb = finanziamento.getStatoFinanziamento().getIdStatoFinanziamento();
		}

		Long idStatoFinanziamentoPrec = null;
		if(finanziamento.getStatoFinanziamentoPrec() != null) {
			idStatoFinanziamentoPrec = finanziamento.getStatoFinanziamentoPrec().getIdStatoFinanziamento();
		}

		boolean updateStatoFinanziamento = false;

		if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue())) {
				Long conteggio =  richiedenteProvvDao.countRichiedenteProvvWithFlagDocumentazioneFalse(idFinanziamento, finanziamento.getIdLeggeProvvDr());
				if(conteggio > 0) {
					Error error = new Error();
					error.setCode(ErrorMessages.CODE_21_VALIDAZIONE_NON_EFFETTUATA_DOCUMENTAZIONE_NON_PRESENTE);
					error.setMessage(ErrorMessages.MESSAGE_21_VALIDAZIONE_NON_EFFETTUATA_DOCUMENTAZIONE_NON_PRESENTE);
					throw new DatiInputErratiException(error) ;
				}

				ValidatorDto.validateNullValue(finanziamento.getImportoAmmesso(), Boolean.TRUE, ErrorMessages.MESSAGE_22_VALIDAZIONE_SENZA_IMPORTO_AMMESSO, ErrorMessages.CODE_22_VALIDAZIONE_SENZA_IMPORTO_AMMESSO);

				updateStatoFinanziamento = true;
			}
		}
		else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==(Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue())) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO).longValue()) {
			if(idStatoFinanziamentoDb != null && (idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO.longValue())) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO).longValue()) {
			if(idStatoFinanziamentoDb != null && (idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue())) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA).longValue()) {
			if(idStatoFinanziamentoDb != null && (idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue())) {
				updateStatoFinanziamento = true;

				if(idFinanziamento != null) {
					richiedenteProvvDao.updateFlagRinunciaToAllRichiedenti( idFinanziamento,  Boolean.TRUE);
				}
			}
		}
		else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO.longValue()) {			
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue()) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue()) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.CONCLUSO).longValue()) {
			if(idStatoFinanziamentoDb != null && idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_SALDO.longValue()) {
				updateStatoFinanziamento = true;
			}
		}else if(idStatoFinanziamento == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RIPRISTINA_FINANZIAMENTO).longValue()) {

			if(idStatoFinanziamentoDb != null && (idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA.longValue())) {

				long conteggioUnivocita = provvedimentoDao.countUnivocitaRichiedenteProvvedimentoRipristinaFinanziamento(idFinanziamento);
				if(conteggioUnivocita > 0) {
					Error error = new Error();
					error.setCode(ErrorMessages.CODE_37_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO_RIPRISTINA_FIN);
					error.setMessage(ErrorMessages.MESSAGE_37_RICHIEDENTE_PROVVEDIMENTO_UNIVOCO_RIPRISTINA_FIN);
					throw new DatiInputErratiException(error) ;
				}

				if(idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA.longValue()) {
					if(idFinanziamento != null) {
						richiedenteProvvDao.updateFlagRinunciaToAllRichiedenti( idFinanziamento,  Boolean.FALSE);
						if(finanziamento.getRinuncia()!=null) {
							if(finanziamento.getRinuncia().getIdFinanziamentoRinuncia() != null) {
								Long idFinanziamentoRinuncia = finanziamento.getRinuncia().getIdFinanziamentoRinuncia();										
								finanziamentoDao.updateFkFinanziamentoRinunciaToNull(finanziamento.getIdFinanziamento());
								finanziamentoRinunciaDao.deleteFinanziamentoRinuncia(idFinanziamentoRinuncia);
							}	
						}
					}
				}
			}	

			if(idStatoFinanziamentoDb != null && (idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.SOSPESO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.RESPINTO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.REVOCATO.longValue()||
					idStatoFinanziamentoDb==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ANNULLATO_PER_RINUNCIA.longValue())) {				

				if(idStatoFinanziamentoPrec!=null && idStatoFinanziamentoPrec == (Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.VALIDATO).longValue()) {
					Long conteggio =  richiedenteProvvDao.countRichiedenteProvvWithFlagDocumentazioneFalse(idFinanziamento, finanziamento.getIdLeggeProvvDr());
					if(conteggio > 0) {
						Error error = new Error();
						error.setCode(ErrorMessages.CODE_36_RIPRISTINA_NON_POSSIBILE_DOCUMENTAZIONE_NON_PRESENTE);
						error.setMessage(ErrorMessages.MESSAGE_36_RIPRISTINA_NON_POSSIBILE_DOCUMENTAZIONE_NON_PRESENTE);
						throw new DatiInputErratiException(error) ;
					}
				}
				if(idStatoFinanziamentoPrec == null) {
					idStatoFinanziamento = Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.IN_CORSO.longValue();
				}else {
					idStatoFinanziamento = idStatoFinanziamentoPrec;
				}
				idStatoFinanziamentoDb = null;
				updateStatoFinanziamento = true;
			}
		}

		if(updateStatoFinanziamento) {
			statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, idStatoFinanziamento, idStatoFinanziamentoDb);
		}else {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO);
			error.setMessage(ErrorMessages.MESSAGE_23_AGGIORNAMENTO_STATO_FINANZIAMENTO_NON_AMMESSO);
			throw new DatiInputErratiException(error) ;
		}

		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}
	@Override
	public List<RichiestaProvv> readAllProvvFinToRichiesta(Long idStatoFinanziamento, Long idLeggeProvvDr,
			String dataProtRichiestaDa, String dataProtRichiestaA) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readAllProvvFinToRichiesta ] ");
		return finanziamentoDao.findAllProvvFinToRichiesta(idStatoFinanziamento, idLeggeProvvDr, dataProtRichiestaDa, dataProtRichiestaA);
	}
	@Override
	public List<TettoMax> readAllTettoMax(Boolean isValid) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readAllTettoMax ] ");
		return tettoMaxDao.findAllTettoMax(isValid);
	}
	@Override
	public TettoMax readTettoMaxByPk(Long idTettoMax) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readTettoMaxByPk ]  param idTettoMax = " + idTettoMax);
		return tettoMaxDao.findTettoMaxByPk(idTettoMax);
	}
	@Override
	public TettoMax createTettoMax(TettoMax tettoMax) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createTettoMax] param TettoMax = " + tettoMax);

		tettoMaxDao.updateTettoMaxAttivoToDisattivo();

		tettoMax = tettoMaxDao.createTettoMax(tettoMax);
		return tettoMaxDao.findTettoMaxByPk(tettoMax.getIdTettoMax());
	}

	@Override
	public TettoMax updateTettoMax(TettoMax tettoMax) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateTettoMax] param tettoMax = " + tettoMax);
		tettoMaxDao.updateTettoMax(tettoMax);
		return tettoMaxDao.findTettoMaxByPk(tettoMax.getIdTettoMax());	
	}
	@Override
	public TettoMax deleteTettoMaxByPk(Long idTettoMax) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: deleteTettoMaxByPk] param idTettoMax = " + idTettoMax);
		tettoMaxDao.deleteTettoMaxByPk(idTettoMax);	
		return tettoMaxDao.findTettoMaxByPk(idTettoMax);
	}
	@Override
	public TettoMaxTotRichiedenti readTettoMaxToRichiedenti(Long idRichiesta, Long idLeggeProvDr) throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readTettoMaxToRichiedenti ]  param idRichiesta = " + idRichiesta);
		LOG.debug("[BusinessLogicManager : readTettoMaxToRichiedenti ]  param idLeggeProvDr = " + idLeggeProvDr);
		return tettoMaxDao.findTettoMaxTotRichiedenti(idRichiesta, idLeggeProvDr);
	}
	@Override
	public Finanziamento updateErogazione(Long idFinanziamento, Long idErogazione, Erogazione erogazione)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: updateErogazione] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[BusinessLogicManager: updateErogazione] param idErogazione = " + idErogazione);
		LOG.debug("[BusinessLogicManager: updateErogazione] param erogazione = " + erogazione);
		if(erogazione!= null) {
			Finanziamento finanziamento = finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
			if(finanziamento!=null) {
				if(finanziamento.getStatoFinanziamento() != null && (finanziamento.getStatoFinanziamento().getIdStatoFinanziamento()==Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.PERFEZIONATO_PER_ACCONTO.longValue())) {
					Long conteggioFin =  finanziamentoDao.countFinanziamentoConFlagRinunciaTrue(idFinanziamento);
					if(conteggioFin > 0 ) {
						if(finanziamento.getRinuncia() == null || (finanziamento.getRinuncia() != null && (finanziamento.getRinuncia().getImporto() == null  || finanziamento.getRinuncia().getImporto().compareTo(BigDecimal.ZERO) == 0))) {
							Error error = new Error();
							error.setCode(ErrorMessages.CODE_41_FINANZIAMENTO_SENZA_IMPORTO_RINUNCIA);
							error.setMessage(ErrorMessages.MESSAGE_41_FINANZIAMENTO_SENZA_IMPORTO_RINUNCIA);
							throw new DatiInputErratiException(error) ;
						}					
					}
				}
			}

			erogazioneDao.updateErogazione(idFinanziamento, idErogazione, erogazione);

			if(erogazione.getIdTipoErogazione()!= null) {
				if(erogazione.getIdTipoErogazione().intValue()==(Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.ACCONTO)) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.ACCONTO_EROGATO.longValue(), null);
				}else if(erogazione.getIdTipoErogazione().intValue()==(Constants.DB.TIPO_EROGAZIONE.ID_TIPO_EROGAZIONE.SALDO)) {
					statoFinanziamentoDao.updateStatoFinToFinanziamento(idFinanziamento, Constants.DB.STATO_FINANZIAMENTO.ID_STATO_FINANZIAMENTO.CONCLUSO.longValue(), null);
				}
			}
		}

		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}

	@Override
	public void createExcel(RichiestaFilter filtro, ByteArrayOutputStream baos)
			throws DaoException, SystemException, IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();

		List<RichiesteForExcelRowDto> listRichiesteForExcel = richiestaDao.findElencoRichiesteExcelByFilter(filtro);

		HSSFSheet sheet = workbook.createSheet("export");

		Font headerFont = workbook.createFont();
		headerFont.setBoldweight((short) 10);
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);

		for(int i = 0; i < Constants.EXCEL_RICERCA_AVANZATA.INTESTAZIONE.PRIMA_RIGA.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(Constants.EXCEL_RICERCA_AVANZATA.INTESTAZIONE.PRIMA_RIGA[i]);
			cell.setCellStyle(headerCellStyle);
		}

		int rowNum = 1;
		for(RichiesteForExcelRowDto employee: listRichiesteForExcel) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0)
			.setCellValue(employee.getNumProtocollo());
			row.createCell(1)
			.setCellValue(employee.getDataProtocollo());
			row.createCell(2)
			.setCellValue(employee.getDescRichiedente());
			row.createCell(3)
			.setCellValue(employee.getDescTipoRichiedente());
			row.createCell(4)
			.setCellValue(employee.getDescComune());
			row.createCell(5)
			.setCellValue(employee.getSiglaProv());
			if(employee.getPopolazione()!=null) {
				row.createCell(6)
				.setCellValue(employee.getPopolazione().doubleValue());
			}
			row.createCell(7)
			.setCellValue(employee.getRinuncia());
			row.createCell(8)
			.setCellValue(employee.getDescLeggeProvvDr());
			if(employee.getImportoFinanziabile()!=null) {
				row.createCell(9)
				.setCellValue(employee.getImportoFinanziabile().doubleValue());
			}
			if(employee.getImportoAmmesso() != null) {
				row.createCell(10)
				.setCellValue(employee.getImportoAmmesso().doubleValue());
			}
			if(employee.getImportoMaxPerComune() != null) {
				row.createCell(11).setCellValue(employee.getImportoMaxPerComune().doubleValue());
			}			
			row.createCell(12)
			.setCellValue(employee.getDescParere());
			row.createCell(13)
			.setCellValue(employee.getDescRendiconto());
			row.createCell(14)
			.setCellValue(employee.getNumAttoApprovazioneUrb());
			row.createCell(15)
			.setCellValue(employee.getDataAttoApprovazioneUrb());
			row.createCell(16)
			.setCellValue(employee.getDescStatoFin());
			if(employee.getImportoRinuncia() != null) {
				row.createCell(17)
				.setCellValue(employee.getImportoRinuncia().doubleValue());
			}
			row.createCell(18)
			.setCellValue(employee.getAttoRinuncia());
			row.createCell(19)
			.setCellValue(employee.getNoteFin());
			if(employee.getImportoErogazioneAcc() != null) {
				row.createCell(20)
				.setCellValue(employee.getImportoErogazioneAcc().doubleValue());
			}
			if(employee.getNumDeterminaAcc() != null) {
				row.createCell(21)
				.setCellValue(employee.getNumDeterminaAcc());
			}
			row.createCell(22)
			.setCellValue(employee.getDataDeterminaAcc());
			if(employee.getImportoErogazioneSaldo() != null) {
				row.createCell(23)
				.setCellValue(employee.getImportoErogazioneSaldo().doubleValue());
			}
			if(employee.getNumDeterminaSaldo() != null) {
				row.createCell(24)
				.setCellValue(employee.getNumDeterminaSaldo());
			}
			row.createCell(25)
			.setCellValue(employee.getDataDeterminaSaldo());
			row.createCell(26)
			.setCellValue(employee.getNumPraticaPraurb());

		}

		workbook.write(baos);
	}

	@Override
	public List<Utente> readAllUtenti() throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readAllUtenti ] ");
		return utenteDao.findAllUtenti();
	}
	@Override
	public Utente readUtentiByPk(Long idUtente)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : readUtentiByPk ]  param idUtente = " + idUtente);
		return utenteDao.findUtenteByPk(idUtente);
	}
	@Override
	public Utente createUtente(Utente utente)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager : createUtente] param utente = " + utente);
		utente = utenteDao.createUtente(utente);
		return utenteDao.findUtenteByPk(utente.getIdUtente());
	}
	@Override
	public Utente updateUtente(Utente utente)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: updateUtente] param utente = " + utente);
		utenteDao.updateUtente(utente);
		return utenteDao.findUtenteByPk(utente.getIdUtente());
	}
	@Override
	public Utente deleteUtenteByPk(Long idUtente)
			throws DaoException, SystemException {
		LOG.debug("[BusinessLogicManager: deleteUtenteByPk] param idUtente = " + idUtente);
		utenteDao.deleteUtenteByPk(idUtente);	
		return utenteDao.findUtenteByPk(idUtente);
	}
	@Override
	public Finanziamento readPraticaUrbGfuToFinanziamento(Long idFinanziamento) throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : readPraticaUrbGfuToFinanziamento ] ");
		Finanziamento finanziamentoResult = finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
		return finanziamentoResult;
	}
	@Override
	public Finanziamento createPraticaUrbGfu(Long idFinanziamento, PraticaUrbGfu praticaUrbGfu)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager : createPraticaUrbGfu] param idFinanziamento = " + idFinanziamento);
		LOG.debug("[BusinessLogicManager : createPraticaUrbGfu] param praticaUrbGfu = " + praticaUrbGfu);

		long conteggio = praticaUrbDao.countUnivocitaFinanziamento(idFinanziamento);
		if(conteggio > 0) {
			Error error = new Error();
			error.setCode(ErrorMessages.CODE_44_FINANZIAMENTO_PRATICA_URB_GIA_PRESENTE);
			error.setMessage(ErrorMessages.MESSAGE_44_FINANZIAMENTO_PRATICA_URB_GIA_PRESENTE);
			throw new DatiInputErratiException(error) ;
		}
		praticaUrbDao.createPraticaUrbGfu(idFinanziamento, praticaUrbGfu);		
		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}
	@Override
	public Finanziamento deletePraticaUrbGfu(Long idFinanziamento)
			throws DaoException, SystemException, DatiInputErratiException {
		LOG.debug("[BusinessLogicManager: deletePraticaUrbGfu] param idFinanziamento = " + idFinanziamento);

		praticaUrbDao.deletePraticaUrbGfu(idFinanziamento);

		return finanziamentoDao.findFinanziamentoByPk(idFinanziamento);
	}
}
