/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import it.csi.gfu.gfuweb.dto.tetto.TettoMaxTotRichiedenti;
import it.csi.gfu.gfuweb.util.Constants;

public class TettoMaxTotRichiedentiRowMapper implements RowMapper<TettoMaxTotRichiedenti>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public TettoMaxTotRichiedenti mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("TettoMaxTotRichiedentiRowMapper START " );
		TettoMaxTotRichiedenti tettoMaxTotRichiedentiDto = new TettoMaxTotRichiedenti();
		tettoMaxTotRichiedentiDto.setIdTettoMax(rs.getInt("ID_TETTO_MAX"));
		tettoMaxTotRichiedentiDto.setImportoTettoMax(rs.getInt("IMPORTO_TETTO_MAX"));
		tettoMaxTotRichiedentiDto.setImportoTettoMaxTotRichiedenti(rs.getInt("IMPORTO_MAX_PER_RICHIEDENTI"));
		tettoMaxTotRichiedentiDto.setValuta(rs.getString("VALUTA"));
		LOG.debug("TettoMaxTotRichiedentiRowMapper STOP " );
		return tettoMaxTotRichiedentiDto;
	}
}