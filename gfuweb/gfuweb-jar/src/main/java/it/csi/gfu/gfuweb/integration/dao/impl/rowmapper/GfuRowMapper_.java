/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.rowmapper;

public class GfuRowMapper_
{
	public Long checkLongNull(Long number)
	{
		if (number == null || number.longValue() == 0) return null;
		
		return number;
	}
}