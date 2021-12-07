/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.legge.Legge;
import it.csi.gfu.gfuweb.util.Constants;

public class LeggeRowMapper implements RowMapper<Legge>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Legge mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("LeggeRowMapper START " );
		Legge leggeDto = new Legge();
		leggeDto.setIdLegge(rs.getLong("ID_LEGGE"));
		leggeDto.setDescrizione(rs.getString("DESCRIZIONE"));
		leggeDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("LeggeRowMapper STOP " );
		return leggeDto;

	}

}