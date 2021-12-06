/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.util.Constants;

public class RichiestaProvvRowMapper implements RowMapper<RichiestaProvv>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public RichiestaProvv mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RichiestaProvvRowMapper START " );
		RichiestaProvv richiestaProvvDto = new RichiestaProvv();
		richiestaProvvDto.setIdRichiesta(rs.getLong("FK_RICHIESTA"));
		richiestaProvvDto.setIdLeggeProvvDr(rs.getLong("FK_LEGGE_PROVV_DR"));
		LOG.debug("RichiestaProvvRowMapper STOP " );
		return richiestaProvvDto;

	}

}
