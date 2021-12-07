/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiedente;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.richiedente.Richiedente;
import it.csi.gfu.gfuweb.util.Constants;

public class RichiedenteRowMapper implements RowMapper<Richiedente>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Richiedente mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RichiedenteRowMapper START " );
		Richiedente richiedenteDto = new Richiedente();
		richiedenteDto.setIdRichiedente(rs.getLong("ID_RICHIEDENTE"));
		richiedenteDto.setIstatComune(rs.getString("ISTAT_COMUNE"));
		richiedenteDto.setDescComune(rs.getString("DESC_COMUNE"));
		richiedenteDto.setPopolazione(rs.getInt("POPOLAZIONE"));
		richiedenteDto.setDescProvincia(rs.getString("DESC_PROVINCIA"));
		richiedenteDto.setSiglaProvincia(rs.getString("SIGLA_PROV"));
		LOG.debug("RichiedenteRowMapper STOP " );
		return richiedenteDto;

	}

}
