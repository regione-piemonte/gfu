/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.parere.Parere;
import it.csi.gfu.gfuweb.util.Constants;

public class ParereRowMapper implements RowMapper<Parere>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Parere mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ParereRowMapper START " );
		Parere parereDto = new Parere();
		parereDto.setIdParere(rs.getLong("ID_PARERE"));
		parereDto.setDescrizione(rs.getString("DESCRIZIONE"));
		parereDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("ParereRowMapper STOP " );
		return parereDto;

	}

}