/*******************************************************************************
* © Copyright Regione Piemonte – 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.statofinanziamento;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.statofinanziamento.StatoFinanziamento;
import it.csi.gfu.gfuweb.util.Constants;

public class StatoFinanziamentoRowMapper implements RowMapper<StatoFinanziamento>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public StatoFinanziamento mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("StatoFinanziamentoRowMapper START " );
		StatoFinanziamento statoFinanziamentoDto = new StatoFinanziamento();
		statoFinanziamentoDto.setIdStatoFinanziamento(rs.getLong("ID_STATO_FINANZIAMENTO"));
		statoFinanziamentoDto.setDescrizione(rs.getString("DESC_STATO_FINANZIAMENTO"));
		statoFinanziamentoDto.setCodStatoFinanziamento(rs.getString("COD_STATO_FINANZIAMENTO"));
		LOG.debug("StatoFinanziamentoRowMapper STOP " );
		return statoFinanziamentoDto;

	}

}
