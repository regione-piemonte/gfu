/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.util.Constants;

	public class UtenteRowMapper implements RowMapper<Utente>
	{
		protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);
		
		public Utente mapRow(ResultSet rs, int rowNum) throws SQLException {
			LOG.debug("UtenteRowMapper START " );
			
			Utente utente = new Utente();
			utente.setCognome(rs.getString("COGNOME"));
			utente.setNome(rs.getString("NOME"));
			utente.setIdUtente(rs.getLong("ID_UTENTE"));
			utente.setCodiceFiscale(rs.getString("CODICE_FISCALE"));
			
			Profilo profilo = new Profilo();
			profilo.setDescrizioneProfilo(rs.getString("DESCRIZIONE_PROFILO"));			
			profilo.setIdProfilo(rs.getBigDecimal("FK_PROFILO"));
			profilo.setCodProfilo(rs.getString("COD_PROFILO"));
			utente.setProfilo(profilo);
			
			LOG.debug("UtenteRowMapper STOP " );
			return utente;
		}
	}