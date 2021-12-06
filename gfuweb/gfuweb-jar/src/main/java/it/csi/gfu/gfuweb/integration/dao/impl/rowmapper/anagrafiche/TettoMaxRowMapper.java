/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.tetto.TettoMax;
import it.csi.gfu.gfuweb.util.Constants;

public class TettoMaxRowMapper implements RowMapper<TettoMax>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public TettoMax mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("TettoMaxRowMapper START " );
		TettoMax tettoMaxDto = new TettoMax();
		tettoMaxDto.setIdTettoMax(rs.getLong("ID_TETTO_MAX"));
		tettoMaxDto.setImporto(rs.getBigDecimal("IMPORTO"));
		tettoMaxDto.setValuta(rs.getString("VALUTA"));
		tettoMaxDto.setDataInizio(rs.getString("DATA_INIZIO"));
		tettoMaxDto.setDataFine(rs.getString("DATA_FINE"));
		LOG.debug("TettoMaxRowMapper STOP " );
		return tettoMaxDto;

	}

}
