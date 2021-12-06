/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.determina;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.determina.Determina;
import it.csi.gfu.gfuweb.util.Constants;

public class DeterminaRowMapper implements RowMapper<Determina>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Determina mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("DeterminaRowMapper START " );
		Determina determinaDto = new Determina();
		determinaDto.setNumDetermina(rs.getInt("NUMERO_DETERMINA"));
		determinaDto.setDataDetermina(rs.getString("DATA_DETERMINA"));
		determinaDto.setUrl(rs.getString("URL"));
		LOG.debug("DeterminaRowMapper STOP " );
		return determinaDto;
	}
}
