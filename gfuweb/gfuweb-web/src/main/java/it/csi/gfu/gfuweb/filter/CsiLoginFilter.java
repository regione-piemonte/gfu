/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.filter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.gfu.gfuweb.business.SpringApplicationContextHelper;
import it.csi.gfu.gfuweb.business.service.impl.BusinessLogicManager;
import it.csi.gfu.gfuweb.dto.Profilo;
import it.csi.gfu.gfuweb.dto.user.utente.Utente;
import it.csi.gfu.gfuweb.dto.user.utente.UtenteFilter;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.exceptions.MalformedIdTokenException;

public class CsiLoginFilter implements Filter {

	public static final String IRIDE_ID_SESSIONATTR = "iride2_id";
	public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";
	private static Logger LOG = Logger.getLogger(".CsiLoginFilter");

	@Autowired
	private BusinessLogicManager businessLogicm;

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fchn)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;

		if (hreq.getSession().getAttribute(Constants.IRIDE_ID_SESSIONATTR) == null) {

			String marker = getToken(hreq);
			if (marker != null) {
				try {
					Identita identita = new Identita(normalizeToken(marker));
					LOG.debug("[CsiLoginFilter]  doFilter: Identita Digitale.codFiscale = " + identita.getCodFiscale());
					
					hreq.getSession().setAttribute(Constants.IRIDE_ID_SESSIONATTR, identita);
					
					hreq.getSession().setAttribute("CODICE_FISCALE", identita.getCodFiscale());
					
					UtenteFilter u = new UtenteFilter();
					u.setCodiceFiscale(identita.getCodFiscale());				

					BusinessLogicManager bl = (BusinessLogicManager) SpringApplicationContextHelper
							.getBean("businessLogic");				
					Utente utenteCollegato = bl.getUtenteCollegato(u);
					
					if(utenteCollegato == null) {	
						String CF = (String ) hreq.getHeader("CF");
						if( CF != null && CF != "")
						{
							UtenteFilter utenteFilterFittizio = new UtenteFilter();
							utenteFilterFittizio.setCodiceFiscale(CF);
							BusinessLogicManager blFiff = (BusinessLogicManager) SpringApplicationContextHelper
								.getBean("businessLogic");
							List<Utente> uteFittizio = blFiff.getUtenteByFilter(utenteFilterFittizio);
							hreq.getSession().setAttribute("PROFILO", (Profilo) uteFittizio.get(0).getProfilo());
							hreq.getSession().setAttribute("UTENTE", (Utente) uteFittizio.get(0));
						}
					}
					else { 
						hreq.getSession().setAttribute("PROFILO", (Profilo) utenteCollegato.getProfilo());
						hreq.getSession().setAttribute("UTENTE", utenteCollegato);						
					}

				} catch (MalformedIdTokenException e) {
					LOG.error("[IrideIdAdapterFilter::doFilter] " + e.toString(), e);
					throw new ServletException("Tentativo di accesso a pagina con token di sicurezza corrotto", e);
				} catch (DaoException e) {
					e.printStackTrace();
				} catch (SystemException e) {
					e.printStackTrace();
				}
			} 
		}
		fchn.doFilter(req, resp);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	public String getToken(HttpServletRequest httpreq) {
		String marker = (String) httpreq.getHeader(AUTH_ID_MARKER);
		LOG.debug("\n\n\n\n\n\nMarker [" + marker + "]");
		if (marker == null)
		{
			return null;
		}
		try {
			String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
			return decodedMarker;
		} catch (java.io.UnsupportedEncodingException e) {
			return marker;
		}
	}

	private String normalizeToken(String token) {
		return token;
	}
}
