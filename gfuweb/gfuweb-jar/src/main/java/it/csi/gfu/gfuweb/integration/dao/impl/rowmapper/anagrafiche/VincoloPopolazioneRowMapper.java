/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.popolazione.VincoloPopolazione;
import it.csi.gfu.gfuweb.util.Constants;

public class VincoloPopolazioneRowMapper implements RowMapper<VincoloPopolazione>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public VincoloPopolazione mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("VincoloPopolazioneRowMapper START " );
		VincoloPopolazione popolazioneDto = new VincoloPopolazione();
		popolazioneDto.setIdVincoloPopolazione(rs.getLong("ID_VINCOLO_POPOLAZIONE"));
		popolazioneDto.setSegno(rs.getString("SEGNO"));
		popolazioneDto.setPopolazione(rs.getBigDecimal("POPOLAZIONE"));
		popolazioneDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("VincoloPopolazioneRowMapper STOP " );
		return popolazioneDto;

	}

}