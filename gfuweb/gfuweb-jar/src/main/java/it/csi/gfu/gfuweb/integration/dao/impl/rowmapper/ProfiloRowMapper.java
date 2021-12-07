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
import it.csi.gfu.gfuweb.util.Constants;

	public class ProfiloRowMapper implements RowMapper<Profilo>
	{
		protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);
		
		public Profilo mapRow(ResultSet rs, int rowNum) throws SQLException {
			LOG.debug("ProfiloRowMapper START " );
			Profilo profiloDto = new Profilo();
			profiloDto.setIdProfilo(rs.getBigDecimal("ID_PROFILO"));
			profiloDto.setDescrizioneProfilo(rs.getString("DESCRIZIONE_PROFILO"));
			profiloDto.setCodProfilo(rs.getString("COD_PROFILO"));		
			LOG.debug("ProfiloRowMapper STOP " );
			return profiloDto;
		}
		
	}