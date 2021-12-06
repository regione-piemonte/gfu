/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.comuneregpie;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.comune.Comune;
import it.csi.gfu.gfuweb.util.Constants;

public class ComuneRegPieRowMapper implements RowMapper<Comune>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Comune mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ComuneRegPieRowMapper START " );
		Comune comuneDto = new Comune();
		comuneDto.setIstatComune(rs.getString("ISTAT_COMUNE"));
		comuneDto.setDescComune(rs.getString("DESC_COMUNE"));
		comuneDto.setSiglaProvincia(rs.getString("SIGLA_PROV"));
		comuneDto.setDescProvincia(rs.getString("DESC_PROVINCIA"));
		comuneDto.setPopolazione(rs.getInt("POP_TOTALE"));
		LOG.debug("ComuneRegPieRowMapper STOP " );
		return comuneDto;

	}

}
