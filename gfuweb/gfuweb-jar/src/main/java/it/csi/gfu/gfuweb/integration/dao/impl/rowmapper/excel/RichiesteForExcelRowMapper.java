/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper.excel;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import it.csi.gfu.gfuweb.dto.excel.RichiesteForExcelRowDto;
import it.csi.gfu.gfuweb.util.Constants;

public class RichiesteForExcelRowMapper implements RowMapper<RichiesteForExcelRowDto>
{
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);

	public RichiesteForExcelRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		LOG.debug("RichiesteForExcelRowMapper START " );
		RichiesteForExcelRowDto richiesteForExcelRowDto = new RichiesteForExcelRowDto();
		richiesteForExcelRowDto.setNumProtocollo(rs.getString("NUM_PROTOCOLLO"));
		richiesteForExcelRowDto.setDataProtocollo(rs.getString("DATA_PROTOCOLLO"));
		richiesteForExcelRowDto.setDescRichiedente(rs.getString("DESC_RICHIEDENTE"));
		richiesteForExcelRowDto.setDescTipoRichiedente(rs.getString("DESC_TIPO_RICHIEDENTE"));
		richiesteForExcelRowDto.setDescComune(rs.getString("DESC_COMUNE"));
		richiesteForExcelRowDto.setSiglaProv(rs.getString("SIGLA_PROV"));
		richiesteForExcelRowDto.setPopolazione(rs.getBigDecimal("POPOLAZIONE"));
		richiesteForExcelRowDto.setRinuncia(rs.getString("RINUNCIA"));
		richiesteForExcelRowDto.setDescLeggeProvvDr(rs.getString("DESC_LEGGE_PROVV_DR"));
		richiesteForExcelRowDto.setImportoFinanziabile(rs.getBigDecimal("IMPORTO_FINANZIABILE"));
		richiesteForExcelRowDto.setImportoAmmesso(rs.getBigDecimal("IMPORTO_AMMESSO"));
		richiesteForExcelRowDto.setImportoMaxPerComune(rs.getBigDecimal("IMPORTO_MAX_TOTALE_PER_COMUNE"));
		richiesteForExcelRowDto.setDescParere(rs.getString("DESC_PARERE"));
		richiesteForExcelRowDto.setDescRendiconto(rs.getString("DESC_RENDICONTO"));
		richiesteForExcelRowDto.setDescStatoFin(rs.getString("DESC_STATO_FIN"));
		richiesteForExcelRowDto.setImportoRinuncia(rs.getBigDecimal("IMPORTO_RINUNCIA"));
		richiesteForExcelRowDto.setAttoRinuncia(rs.getString("ATTO_RINUNCIA"));
		richiesteForExcelRowDto.setNoteFin(rs.getString("NOTE_FIN"));
		richiesteForExcelRowDto.setImportoErogazioneAcc(rs.getBigDecimal("IMPORTO_EROGAZIONE_ACC"));
		richiesteForExcelRowDto.setNumDeterminaAcc(rs.getString("NUM_DETERMINA_ACC"));
		richiesteForExcelRowDto.setDataDeterminaAcc(rs.getString("DATA_DETERMINA_ACC"));
		richiesteForExcelRowDto.setImportoErogazioneSaldo(rs.getBigDecimal("IMPORTO_EROGAZIONE_SALDO"));
		richiesteForExcelRowDto.setNumDeterminaSaldo(rs.getString("NUM_DETERMINA_SALDO"));
		richiesteForExcelRowDto.setDataDeterminaSaldo(rs.getString("DATA_DETERMINA_SALDO"));
		richiesteForExcelRowDto.setNumPraticaPraurb(rs.getString("NUM_PRATICA_URB"));
		richiesteForExcelRowDto.setNumAttoApprovazioneUrb(rs.getString("NUM_ATTO_APPROVAZIONE_URB"));
		richiesteForExcelRowDto.setDataAttoApprovazioneUrb(rs.getString("DATA_ATTO_APPROVAZIONE_URB"));
		LOG.debug("RichiesteForExcelRowMapper STOP " );
		return richiesteForExcelRowDto;
	}
}
