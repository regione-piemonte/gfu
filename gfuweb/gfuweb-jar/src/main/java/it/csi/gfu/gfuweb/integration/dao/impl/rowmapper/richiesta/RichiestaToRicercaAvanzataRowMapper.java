/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.richiesta;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.richiesta.RichiestaToRicercaAvanzata;
import it.csi.gfu.gfuweb.util.Constants;

public class RichiestaToRicercaAvanzataRowMapper implements RowMapper<RichiestaToRicercaAvanzata>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public RichiestaToRicercaAvanzata mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RichiestaToRicercaAvanzataRowMapper START " );
		RichiestaToRicercaAvanzata richiestaToRicercaAvanzataDto = new RichiestaToRicercaAvanzata();
		richiestaToRicercaAvanzataDto.setIdRichiesta(rs.getLong("ID_RICHIESTA"));
		richiestaToRicercaAvanzataDto.setNumProtocollo(rs.getString("NUM_PROTOCOLLO"));
		richiestaToRicercaAvanzataDto.setDataProtocollo(rs.getString("DATA_PROTOCOLLO"));
		richiestaToRicercaAvanzataDto.setDescRichiedente(rs.getString("DESC_RICHIEDENTE"));
		richiestaToRicercaAvanzataDto.setDescTipoRichiedente(rs.getString("DESC_TIPO_RICHIEDENTE"));
		richiestaToRicercaAvanzataDto.setNumPraticaPraurb(rs.getString("NUM_PRATICA_PRAURB"));

		LOG.debug("RichiestaToRicercaAvanzataRowMapper STOP " );
		return richiestaToRicercaAvanzataDto;
	}
}
