/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.dr.Dr;
import it.csi.gfu.gfuweb.util.Constants;

public class DrRowMapper implements RowMapper<Dr>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Dr mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("DrRowMapper START " );
		Dr drDto = new Dr();
		drDto.setIdDr(rs.getLong("ID_DR"));
		drDto.setIdTipoDr(rs.getInt("FK_TIPO_DR"));
		drDto.setDescTipoDr(rs.getString("DESC_TIPO_DR"));
		drDto.setNumeroDr(rs.getString("NUMERO"));
		drDto.setDataDr(rs.getString("DATA_DR"));
		drDto.setDescDr(rs.getString("DESCRIZIONE"));
		drDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("DrRowMapper STOP " );
		return drDto;

	}

}
