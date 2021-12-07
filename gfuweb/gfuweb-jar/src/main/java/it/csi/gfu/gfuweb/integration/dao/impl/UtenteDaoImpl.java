/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl;

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

import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.UtenteDao;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.UtenteRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche.DrRowMapper;
import it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.utente.UtenteAnagRowMapper;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;


@Repository
public class UtenteDaoImpl  extends JdbcDaoSupport implements UtenteDao {

	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);
	private NamedParameterJdbcTemplate namedJdbcTemplate; 

	@Autowired
	public UtenteDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}

	@Override
	public List<Utente>  findUtenteByFilter(UtenteFilter utenteFilter) throws DaoException, SystemException {
		LOG.debug("[UtenteDaoImpl::findUtenteByFilter] BEGIN");
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  codice fiscale = " + utenteFilter.getCodiceFiscale());
		System.out.println("param   MMM INT 1 codice fiscale = \" + utenteFilter.getCodiceFiscale() "  + utenteFilter.getCodiceFiscale());

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append(" ID_UTENTE, COGNOME, NOME, P.DESCRIZIONE DESCRIZIONE_PROFILO, ");
		sql.append(" CODICE_FISCALE, FK_PROFILO, COD_PROFILO	");
		sql.append(" FROM ");
		sql.append(" gfu_d_profilo  P, gfu_t_utente U ");
		sql.append(" WHERE ");
		sql.append(" U.fk_profilo = P.id_profilo AND ");
		sql.append(" (:CODICE_FISCALE::VARCHAR IS NULL OR UPPER(CODICE_FISCALE) = UPPER(:CODICE_FISCALE::VARCHAR))   ");

		List<Utente> result = null;

		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] query =" + sql.toString());
		System.out.println("[UtenteDaoImpl - findUtenteByFilter] query = "  + sql.toString());

		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  nome = " + utenteFilter.getNome());
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  cognome = " + utenteFilter.getCognome());
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  codice fiscale = " + utenteFilter.getCodiceFiscale());
        System.out.println(" MMM INT 2  codFiscale " + utenteFilter.getCodiceFiscale());

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("CODICE_FISCALE", utenteFilter.getCodiceFiscale());

		try {
			result = (List<Utente>) namedJdbcTemplate.query(sql.toString(), paramMap, new UtenteRowMapper());
		}
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[UtenteDaoImpl::findUtenteByFilter] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[UtenteDaoImpl::findUtenteByFilter] END");
		}
		return result;
	}
	
	@Override
	public Utente  findUtenteAuth(UtenteFilter utenteFilter) throws DaoException, SystemException {
		LOG.debug("[UtenteDaoImpl::findUtenteByFilter] BEGIN");
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  codice fiscale = " + utenteFilter.getCodiceFiscale());
		System.out.println("param   MMM INT 1 codice fiscale = \" + utenteFilter.getCodiceFiscale() "  + utenteFilter.getCodiceFiscale());

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append(" ID_UTENTE, COGNOME, NOME, P.DESCRIZIONE DESCRIZIONE_PROFILO, ");
		sql.append(" CODICE_FISCALE, FK_PROFILO, COD_PROFILO	");
		sql.append(" FROM ");
		sql.append(" gfu_d_profilo  P, gfu_t_utente U ");
		sql.append(" WHERE ");
		sql.append(" U.fk_profilo = P.id_profilo AND ");
		sql.append(" (:CODICE_FISCALE::VARCHAR IS NULL OR UPPER(CODICE_FISCALE) = UPPER(:CODICE_FISCALE::VARCHAR))   ");

		Utente result = null;

		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] query =" + sql.toString());
		System.out.println("[UtenteDaoImpl - findUtenteByFilter] query = "  + sql.toString());

		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  nome = " + utenteFilter.getNome());
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  cognome = " + utenteFilter.getCognome());
		LOG.debug("[UtenteDaoImpl - findUtenteByFilter] param  codice fiscale = " + utenteFilter.getCodiceFiscale());
        System.out.println(" MMM INT 2  codFiscale " + utenteFilter.getCodiceFiscale());

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("CODICE_FISCALE", utenteFilter.getCodiceFiscale());

		try {
			result = (Utente) namedJdbcTemplate.queryForObject(sql.toString(), paramMap, new UtenteRowMapper());		
		}
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[UtenteDaoImpl::findUtenteByFilter] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[UtenteDaoImpl::findUtenteByFilter] END");
		}
		return result;
	}

	@Override
	public List<Utente> findAllUtenti() throws DaoException, SystemException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT U.ID_UTENTE, U.COGNOME, U.NOME, U.CODICE_FISCALE, U.EMAIL, U.FK_PROFILO,  P.DESCRIZIONE  DESCRIZIONE_PROFILO, P.COD_PROFILO,  " );
		sql.append("TO_CHAR(U.DATA_INSERIMENTO, 'DD/MM/YYYY')  DATA_INSERIMENTO, TO_CHAR(U.DATA_CANCELLAZIONE, 'DD/MM/YYYY') DATA_CANCELLAZIONE   " );
		sql.append("FROM GFU_T_UTENTE U , GFU_D_PROFILO P ");
		sql.append("WHERE P.ID_PROFILO = U.FK_PROFILO ");
		sql.append("ORDER BY U.ID_UTENTE ");

		LOG.debug("[UtenteDaoImpl - findAllUtenti] query =" + sql.toString());

		List<Utente> utenteList = null;
		try {
			utenteList = getJdbcTemplate().query(sql.toString(),
					new Object[] { }, new UtenteAnagRowMapper());

		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("\"[UtenteDaoImpl::findAllUtenti]  NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[UtenteDaoImpl::findAllUtenti] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[UtenteDaoImpl::findAllUtenti] END");
		}
		return (List<Utente>) utenteList;
	}

	@Override
	public Utente findUtenteByPk(Long idUtente)
			throws DaoException, SystemException {
		LOG.debug("[UtenteDaoImpl::findDrByPk] BEGIN");


		StringBuilder sql = new StringBuilder();
		sql.append("SELECT U.ID_UTENTE, U.COGNOME, U.NOME, U.CODICE_FISCALE, U.EMAIL, U.FK_PROFILO,  P.DESCRIZIONE  DESCRIZIONE_PROFILO, P.COD_PROFILO,  " );
		sql.append("TO_CHAR(U.DATA_INSERIMENTO, 'DD/MM/YYYY')  DATA_INSERIMENTO, TO_CHAR(U.DATA_CANCELLAZIONE, 'DD/MM/YYYY') DATA_CANCELLAZIONE   " );
		sql.append("FROM GFU_T_UTENTE U , GFU_D_PROFILO P ");
		sql.append("WHERE P.ID_PROFILO = U.FK_PROFILO ");
		sql.append("AND U.ID_UTENTE = :ID_UTENTE ");

		Utente result = null;

		LOG.debug("[UtenteDaoImpl - findUtenteByPk] query =" + sql.toString());
		LOG.debug("[UtenteDaoImpl - findUtenteByPk] param  idUtente = " + idUtente);

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID_UTENTE", idUtente);

		try {
			result = (Utente) namedJdbcTemplate.queryForObject(sql.toString(), paramMap , new UtenteAnagRowMapper());
		} 
		catch(EmptyResultDataAccessException ex)
		{
			LOG.debug("[UtenteDaoImpl - findUtenteByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[UtenteDaoImpl::findUtenteByPk] esecuzione query",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[UtenteDaoImpl::findUtenteByPk] END");
		}
		return result;
	}

	@Override
	public Utente createUtente(Utente utente)
			throws DaoException, SystemException {
		try {

			String queryInsert = " INSERT INTO GFU_T_UTENTE (COGNOME, NOME, CODICE_FISCALE, EMAIL, FK_PROFILO,DATA_INSERIMENTO ) " + 
					" VALUES(:COGNOME, :NOME, :CODICE_FISCALE, :EMAIL, :FK_PROFILO, CURRENT_DATE  ) RETURNING ID_UTENTE ";		

			LOG.debug("[UtenteDaoImpl - createUtente] query =" + queryInsert.toString());
			LOG.debug("[UtenteDaoImpl - createUtente] param  UTENTE = " + utente);

			Map<String, Object> paramMap = new HashMap<String, Object>();

			paramMap.put("COGNOME", utente.getCognome());
			paramMap.put("NOME" , utente.getNome());
			paramMap.put("CODICE_FISCALE", utente.getCodiceFiscale());
			paramMap.put("EMAIL", utente.getEmail());
			paramMap.put("FK_PROFILO", utente.getProfilo().getIdProfilo());

			LOG.debug("[UtenteDaoImpl::createUtente]  param [COGNOME] = " + utente.getCognome());
			LOG.debug("[UtenteDaoImpl::createUtente]  param [NOME] = " + utente.getNome());
			LOG.debug("[UtenteDaoImpl::createUtente]  param [CODICE_FISCALE] = " + utente.getCodiceFiscale());
			LOG.debug("[UtenteDaoImpl::createUtente]  param [EMAIL] = " + utente.getEmail());
			LOG.debug("[UtenteDaoImpl::createUtente]  param [FK_PROFILO] = " + utente.getProfilo().getIdProfilo());

			Long idUtente = namedJdbcTemplate.queryForObject(queryInsert, paramMap, Long.class);

			utente.setIdUtente(idUtente);

			LOG.debug("[UtenteDaoImpl::createUtente]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[UtenteDaoImpl::createUtente]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());		
		}
		catch (Throwable ex) {
			LOG.error(
					"[UtenteDaoImpl::createUtente] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[UtenteDaoImpl::createUtente] END ");
		}
		return utente;
	}

	@Override
	public Utente updateUtente(Utente utente)throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_UTENTE SET COGNOME = :COGNOME, NOME = :NOME, CODICE_FISCALE = :CODICE_FISCALE, EMAIL = :EMAIL, "
					+ "FK_PROFILO = :FK_PROFILO "+
					" WHERE ID_UTENTE = :ID_UTENTE ";		

			LOG.debug("[UtenteDaoImpl::updateUtente]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("COGNOME", utente.getCognome());
			paramMap.put("NOME", utente.getNome());
			paramMap.put("CODICE_FISCALE", utente.getCodiceFiscale());
			paramMap.put("EMAIL", utente.getEmail());
			paramMap.put("FK_PROFILO", utente.getProfilo().getIdProfilo());
			paramMap.put("ID_UTENTE", utente.getIdUtente());

			LOG.debug("[UtenteDaoImpl::updateUtente]  param [ID_UTENTE] = " + utente.getIdUtente());
			LOG.debug("[UtenteDaoImpl::updateUtente]  param [COGNOME] = " + utente.getCognome());
			LOG.debug("[UtenteDaoImpl::updateUtente]  param [NOME] = " + utente.getNome());
			LOG.debug("[UtenteDaoImpl::updateUtente]  param [CODICE_FISCALE] = " + utente.getCodiceFiscale());
			LOG.debug("[UtenteDaoImpl::updateUtente]  param [EMAIL] = " + utente.getEmail());
			LOG.debug("[UtenteDaoImpl::updateUtente]  param [FK_PROFILO] = " + utente.getProfilo().getIdProfilo());

			namedJdbcTemplate.update(queryUpdate, paramMap);  

			LOG.debug("[UtenteDaoImpl::updateUtente]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[UtenteDaoImpl::updateUtente]  Integrity Keys Violation");
			throw new DaoException(ErrorMessages.CODE_11_CONSTRAINT_VIOLATED, ex.getMostSpecificCause());
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[UtenteDaoImpl::updateUtente] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[UtenteDaoImpl::updateUtente] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[UtenteDaoImpl::updateUtente] END ");
		}
		return utente;
	}

	@Override
	public void deleteUtenteByPk(Long idUtente) throws DaoException, SystemException {
		try
		{
			String queryUpdate = " UPDATE GFU_T_UTENTE SET DATA_CANCELLAZIONE = CURRENT_DATE "+
					" WHERE ID_UTENTE = :ID_UTENTE ";		

			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk]  queryUpdate: = " + queryUpdate.toString());

			Map<String, Object> paramMap = new HashMap<String, Object>();	

			paramMap.put("ID_UTENTE", idUtente);

			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk]  param [ID_UTENTE] = " + idUtente);

			int updateResult= namedJdbcTemplate.update(queryUpdate, paramMap);  
			if(updateResult == 0) {
				throw new EmptyResultDataAccessException(ErrorMessages.NOT_FOUND, 1);
			}

			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk]  Inserimento effettuato. Stato = SUCCESS ");} 

		catch (DataIntegrityViolationException ex) {
			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk]  Integrity Keys Violation");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (EmptyResultDataAccessException ex) {
			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk] NESSUN RISULTATO");
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error("[UtenteDaoImpl::deleteUtenteByPk] esecuzione query Failed ", ex);
			throw new SystemException("Errore di sistema", ex);
		}
		finally {
			LOG.debug("[UtenteDaoImpl::deleteUtenteByPk] END ");
		}

	}
}

