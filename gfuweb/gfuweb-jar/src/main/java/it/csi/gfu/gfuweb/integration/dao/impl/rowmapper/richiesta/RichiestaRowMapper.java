/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.richiesta.Richiesta;
import it.csi.gfu.gfuweb.util.Constants;

public class RichiestaRowMapper implements RowMapper<Richiesta>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public Richiesta mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RichiestaRowMapper START " );
		Richiesta richiestaDto = new Richiesta();
		richiestaDto.setIdRichiesta(rs.getLong("ID_RICHIESTA"));
		richiestaDto.setNumProtocollo(rs.getString("NUM_PROTOCOLLO"));
		richiestaDto.setDataProtocollo(rs.getString("DATA_PROTOCOLLO"));
		richiestaDto.setNote(rs.getString("NOTE"));
		Integer idAssociazione = rs.getInt("ID_ASSOCIAZIONE");
		/**
		 * Verifico idAssociazione: se e' null imposto null (altrimenti di default viene settato 0)
		 */
		richiestaDto.setIdAssociazione(rs.wasNull() ? null : idAssociazione);
		richiestaDto.setDescAssociazione(rs.getString("DESC_ASSOCIAZIONE"));
		richiestaDto.setIsValid(rs.getBoolean("IS_VALID"));
		richiestaDto.setCodTipoFormaAssociativa(rs.getString("COD_TIPO_FORMA_ASSOCIATIVA"));
		richiestaDto.setDescTipoFormaAssociativa(rs.getString("DESC_TIPO_FORMA_ASSOCIATIVA"));
		LOG.debug("RichiestaRowMapper STOP " );
		return richiestaDto;

	}

}
