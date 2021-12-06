/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.erogazione;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.erogazione.Erogazione;
import it.csi.gfu.gfuweb.util.Constants;

public class ErogazioneRowMapper implements RowMapper<Erogazione>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Erogazione mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("ErogazioneRowMapper START " );
		Erogazione erogazioneDto = new Erogazione();
		erogazioneDto.setIdErogazione(rs.getLong("ID_EROGAZIONE"));
		erogazioneDto.setIdTipoErogazione(rs.getBigDecimal("FK_TIPO_EROGAZIONE"));
		erogazioneDto.setDescTipoErogazione(rs.getString("DESC_TIPO_EROGAZIONE"));
		erogazioneDto.setIdFinanziamento(rs.getBigDecimal("FK_FINANZIAMENTO"));
		erogazioneDto.setImportoErogazione(rs.getBigDecimal("IMPORTO_EROGAZIONE"));
		erogazioneDto.setValuta(rs.getString("VALUTA"));
		erogazioneDto.setNumDetermina(rs.getString("NUM_DETERMINA"));
		erogazioneDto.setDataDetermina(rs.getString("DATA_DETERMINA"));
		LOG.debug("ErogazioneRowMapper STOP " );
		return erogazioneDto;

	}

}
