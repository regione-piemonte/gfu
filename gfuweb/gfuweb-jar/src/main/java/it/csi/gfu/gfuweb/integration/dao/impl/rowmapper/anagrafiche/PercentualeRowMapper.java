/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.percentuale.Percentuale;
import it.csi.gfu.gfuweb.util.Constants;

public class PercentualeRowMapper implements RowMapper<Percentuale>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Percentuale mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("PercentualeRowMapper START " );
		Percentuale percentualeDto = new Percentuale();
		percentualeDto.setIdPercentuale(rs.getLong("ID_PERCENTUALE"));
		percentualeDto.setValorePercentuale(rs.getLong("VALORE_PERCENTUALE"));
		percentualeDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("PercentualeRowMapper STOP " );
		return percentualeDto;

	}

}