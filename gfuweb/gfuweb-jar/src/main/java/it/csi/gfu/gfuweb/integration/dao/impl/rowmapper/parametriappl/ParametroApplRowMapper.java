/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.parametriappl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.parametriappl.ParametroAppl;
import it.csi.gfu.gfuweb.util.Constants;

public class ParametroApplRowMapper implements RowMapper<ParametroAppl>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public ParametroAppl mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ParametroApplRowMapper START " );
		ParametroAppl parametroApplDto = new ParametroAppl();
		parametroApplDto.setIdParametro(rs.getLong("ID_PARAMETRO"));
		parametroApplDto.setKey(rs.getString("KEY"));
		parametroApplDto.setDescrizione(rs.getString("DESCRIZIONE"));
		parametroApplDto.setValore(rs.getString("VALORE"));
		LOG.debug("ParametroApplRowMapper STOP " );
		return parametroApplDto;
	}
}
