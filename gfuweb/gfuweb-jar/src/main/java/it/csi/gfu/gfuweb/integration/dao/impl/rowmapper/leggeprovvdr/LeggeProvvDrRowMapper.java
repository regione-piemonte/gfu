/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.leggeprovvdr;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.legge.LeggeProvvDr;
import it.csi.gfu.gfuweb.util.Constants;

public class LeggeProvvDrRowMapper implements RowMapper<LeggeProvvDr>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public LeggeProvvDr mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("LeggeProvvDrRowMapper START " );
		LeggeProvvDr leggeProvvDrDto = new LeggeProvvDr();
		leggeProvvDrDto.setIdLeggeProvvDr(rs.getLong("ID_LEGGE_PROVV_DR"));
		leggeProvvDrDto.setDescLeggeProvvDr(rs.getString("DESC_LEGGE_PROVV_DR"));
		leggeProvvDrDto.setIdLegge(rs.getInt("FK_LEGGE"));
		leggeProvvDrDto.setDescLegge(rs.getString("DESC_LEGGE"));
		leggeProvvDrDto.setIdProvvedimento(rs.getInt("FK_PROVVEDIMENTO"));
		leggeProvvDrDto.setDescProvvedimento(rs.getString("DESC_PROVVEDIMENTO"));
		leggeProvvDrDto.setIdDr(rs.getInt("FK_DR"));
		leggeProvvDrDto.setDescDr(rs.getString("DESC_DR"));
		leggeProvvDrDto.setIdVincoloPopolazione(rs.getInt("FK_VINCOLO_POPOLAZIONE"));
		leggeProvvDrDto.setDescVincoloPopolazione(rs.getString("DESC_VINCOLO_POPOLAZIONE"));		
		leggeProvvDrDto.setIsValid(rs.getBoolean("IS_VALID"));
		
		LOG.debug("LeggeProvvDrRowMapper STOP " );
		return leggeProvvDrDto;
		
	}

}