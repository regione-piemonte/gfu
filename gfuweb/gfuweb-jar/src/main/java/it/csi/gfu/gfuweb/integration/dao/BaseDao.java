/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseDao extends JdbcDaoSupport
{
	public void addStringToBigDecimalValues(StringBuilder query, List<String> codes)
	{
		if (codes != null)
		{
			boolean first = true;
			for (String code : codes)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					query.append(", ");
				}
				if (code != null)
				{
					query.append(Long.valueOf(code));
				}
				else
				{
					query.append("null");
				}
			}
		}
	}

	public void addBigDecimalToStringValues(StringBuilder query, List<BigDecimal> codes)
	{
		if (codes != null)
		{
			boolean first = true;
			for (BigDecimal code : codes)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					query.append(", ");
				}
				if (code != null)
				{
					query.append(code);
				}
				else
				{
					query.append("null");
				}
			}
		}
	}
}