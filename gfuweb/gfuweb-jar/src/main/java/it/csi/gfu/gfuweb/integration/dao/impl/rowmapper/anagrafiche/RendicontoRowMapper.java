/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.anagrafiche;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.rendiconto.Rendiconto;
import it.csi.gfu.gfuweb.util.Constants;

public class RendicontoRowMapper implements RowMapper<Rendiconto>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Rendiconto mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RendicontoRowMapper START " );
		Rendiconto rendicontoDto = new Rendiconto();
		rendicontoDto.setIdRendiconto(rs.getLong("ID_RENDICONTO"));
		rendicontoDto.setDescrizione(rs.getString("DESCRIZIONE"));
		rendicontoDto.setIsValid(rs.getBoolean("IS_VALID"));
		LOG.debug("RendicontoRowMapper STOP " );
		return rendicontoDto;

	}

}