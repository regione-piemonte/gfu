/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.provvedimento.Provvedimento;
import it.csi.gfu.gfuweb.util.Constants;

public class ProvvedimentoRowMapper implements RowMapper<Provvedimento>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Provvedimento mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ProvvedimentoRowMapper START " );
		Provvedimento provvedimentoDto = new Provvedimento();
		provvedimentoDto.setIdProvvedimento(rs.getLong("ID_PROVVEDIMENTO"));
		provvedimentoDto.setDescProvvedimento(rs.getString("DESCRIZIONE"));
		provvedimentoDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("ProvvedimentoRowMapper STOP " );
		return provvedimentoDto;

	}

}
