/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.logAudit;

import java.math.BigDecimal;

import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;

public interface CsiLogAuditDao {
	
	public BigDecimal insertCsiLogAudit(CsiLogAuditDto csiLogAuditDto)throws DaoException, SystemException;
}