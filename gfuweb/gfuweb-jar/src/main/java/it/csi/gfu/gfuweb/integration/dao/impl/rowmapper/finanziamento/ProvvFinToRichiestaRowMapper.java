/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.finanziamento;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.richiestaprovv.RichiestaProvv;
import it.csi.gfu.gfuweb.util.Constants;

public class ProvvFinToRichiestaRowMapper implements RowMapper<RichiestaProvv>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public RichiestaProvv mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ProvvFinToRichiestaRowMapper START " );
		RichiestaProvv richiestaProvvDto = new RichiestaProvv();
		richiestaProvvDto.setIdRichiesta(rs.getLong("ID_RICHIESTA"));
		richiestaProvvDto.setNumProtocollo(rs.getString("NUM_PROTOCOLLO"));
		richiestaProvvDto.setDataProtocollo(rs.getString("DATA_PROTOCOLLO"));
		richiestaProvvDto.setIdLeggeProvvDr(rs.getLong("ID_LEGGE_PROVV_DR"));
		richiestaProvvDto.setDescLeggeProvvDr(rs.getString("DESC_LEGGE_PROVV_DR"));
		richiestaProvvDto.setIdFinanziamento(rs.getBigDecimal("ID_FINANZIAMENTO"));
		LOG.debug("ProvvFinToRichiestaRowMapper STOP " );
		return richiestaProvvDto;

	}

}
