/*******************************************************************************
* Copyright Regione Piemonte - 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/

package it.csi.gfu.gfuweb.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import it.csi.gfu.gfuweb.util.Constants;

public class CORSFilter implements Filter {
	
	
	public static final String IRIDE_ID_SESSIONATTR = "iride2_id";
	public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";
	private static Logger LOG = Logger.getLogger(Constants.LOGGER + ".CsiLoginFilter");

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		if (enableCors) {
			HttpServletResponse res = (HttpServletResponse) servletResponse;
			res.setHeader("Access-Control-Allow-Origin", "*");
			res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		}
		chain.doFilter(servletRequest, servletResponse);

	}

	private static final String ENABLECORS_INIT_PARAM = "enablecors";
	private boolean enableCors = false;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String sEnableCors = filterConfig.getInitParameter(ENABLECORS_INIT_PARAM);
		if ("true".equals(sEnableCors)) {
			enableCors = true;
		} else {
			enableCors = false;
		}
	}

	@Override
	public void destroy() {
	}

}