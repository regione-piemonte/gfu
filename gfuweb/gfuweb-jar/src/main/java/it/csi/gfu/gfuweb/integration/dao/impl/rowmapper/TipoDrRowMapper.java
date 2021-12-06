/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.dr.TipoDr;
import it.csi.gfu.gfuweb.util.Constants;

public class TipoDrRowMapper implements RowMapper<TipoDr>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public TipoDr mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("TipoDrRowMapper START " );
		TipoDr tipoDrDto = new TipoDr();
		tipoDrDto.setIdTipoDr(rs.getLong("ID_TIPO_DR"));
		tipoDrDto.setCodTipoDr(rs.getString("COD_TIPO_DR"));
		tipoDrDto.setDescTipoDr(rs.getString("DESCRIZIONE"));
		LOG.debug("TipoDrRowMapper STOP " );
		return tipoDrDto;

	}

}
