/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.util.rest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import it.csi.gfu.gfuweb.util.Constants;
import org.jboss.resteasy.util.Base64;


public class RestCaller {
	
	private Logger LOG = Logger.getLogger(Constants.LOGGER);
	
	private HttpMethod httpMethod;
	private String baseUrl;
	private String path;
	private HttpHeaders headers;
	private Map<String, String> queryParams;
	private Object body;
	private ResponseErrorHandler responseErrorHandler;
	private boolean mapObject = true;
	private boolean encodingUTF8 = false;
	
	public RestCaller(HttpMethod httpMethod, String baseUrl) {
		httpMethod(httpMethod);
		baseUrl(baseUrl);
	}

	public RestCaller(HttpMethod httpMethod, String baseUrl, String path) {
		httpMethod(httpMethod);
		baseUrl(baseUrl);
		path(path);
	}
	
	
	public RestCaller headersBasicAuth(String user, String password) {
		String auth = user + ":" + password;
		String encodedAuth = Base64.encodeBytes(auth.getBytes( ));
        String authHeader = "Basic " + new String( encodedAuth );
       headerParam("Authorization", authHeader );
        
        return this;
	}
	
	public RestCaller httpMethod(HttpMethod httpMethod) {
		if(httpMethod==null) {
			throw new IllegalArgumentException("Occorre specificare un valore per HttpMethod.");
		}
		
		this.httpMethod = httpMethod;
		return this;
	}
	
	public RestCaller mapObject(boolean mapObject) {
		this.mapObject = false;
		return this;
	}
	
	public RestCaller encodingUTF8(boolean encoding) {
		encodingUTF8 = encoding;
		return this;
	}
	
	private RestCaller baseUrl(String baseUrl) {
		this.baseUrl = cleanLastSlash(baseUrl);
		return this;
	}
	
	
	public RestCaller responseErrorHandler(ResponseErrorHandler responseErrorHandler) {
		this.responseErrorHandler = responseErrorHandler;
		return this;
	}
	
	
	private RestCaller path(String path) {
		this.path=cleanFirstSlash(path);
		return this;
	}
	
	public RestCaller body(Object body) {
		this.body = body;
		return this;
	}
	
	public RestCaller headers(HttpHeaders headers) {
		this.headers = headers;
		return this;
	}
	
	public RestCaller headerParam(String key, String value) {
		headerParam(key, value, true);
		return this;
	}
	
	public RestCaller contentType(MediaType mediaType) {
		if(this.headers==null) {
			this.headers = new HttpHeaders();
		}
		headers.setContentType(mediaType);
		return this;
	}
	
	public RestCaller headerParam(String key, String value, boolean add) {
		if(this.headers==null) {
			this.headers = new HttpHeaders();
		}
		if(add) {
			this.headers.add(key, value);
		} else {
			this.headers.set(key, value);
		}
		return this;
	}
	
	public RestCaller headers(String xRequestId, String xCodiceServizio) {
		headerParam("X-Request-ID", xRequestId, false);
		headerParam("X-Codice-Servizio", xCodiceServizio, false);
		
		return this;
	}
	
	public RestCaller headers(String xRequestId, String xCodiceServizio, String shibIdentitaCodiceFiscale) {
		headers(xRequestId, xCodiceServizio);
		headerParam("Shib-Identita-CodiceFiscale", shibIdentitaCodiceFiscale, false);
		return this;
	}
	
	public RestCaller queryParam(String key, String value) {
		if(this.queryParams == null) {
			this.queryParams = new HashMap<>();
		}
		if(StringUtils.isNotBlank(value)) {
			this.queryParams.put(key, value);
		}
		return this;
	}
	
	
	private String queryParams() {
		if(this.queryParams==null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder("");
		
		this.queryParams.entrySet().forEach(e -> 
			sb.append("&")
				.append(urlEncode(e.getKey()))
				.append("=")
				.append(urlEncode(e.getValue()))
		);
		
		
		if(sb.length()>0) {
			sb.replace(0, 1, "?");
		}
		return sb.toString();
	}
	
	
	public <T> T call(TypeReference<T> type) {
		final String METHOD_NAME = "call";
		
		RestTemplate restTemplate = new RestTemplate();
		
		if(this.responseErrorHandler != null) {
			restTemplate.setErrorHandler(this.responseErrorHandler);
		}
				
		if(headers==null) {
			headers = new HttpHeaders();
		}
		
		String jsonFromBody = null;
		try {
			if(body != null) {
				ObjectMapper mapper = new ObjectMapper();
				jsonFromBody = mapper.writeValueAsString(body);
				LOG.info(METHOD_NAME+ "jsonFromBody: %s"+ jsonFromBody);
			}
		}catch(Exception e) {
			String msg =  String.format("Error in body class %s ", body!= null?body.getClass():null);
			LOG.error(METHOD_NAME + msg, e);
			throw new RestClientException(msg, e);
		}
		
		headers.keySet().stream().forEach(c -> LOG.debug(METHOD_NAME+ "header: " + c + headers.get(c)));
		HttpEntity<String> httpEntity = new HttpEntity<>(jsonFromBody, this.headers);
				
		String url = this.baseUrl + pathParams() + queryParams();
		LOG.debug(METHOD_NAME+ "url: %s"+ url);
		LOG.info(METHOD_NAME+ "url: " + url);
		ResponseEntity<String> re = null;
		try {
			if(encodingUTF8) {
				restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			}
			URI uri = new URI(url);
			re = restTemplate.exchange(uri, httpMethod, httpEntity, String.class);
		} catch (URISyntaxException e1) {
			throw new RestClientException(e1.getMessage(), e1);
		}
		String json = re.getBody();	
		if (json == null) {
			return null;
		}
		
		if(!mapObject) {
			return (T)json;
		}
		T result;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			result = mapper.readValue(json ,type);
		} catch(Exception e) {
			String msg = String.format("Error mapping in type: %s - json: %s - url: %s", type!=null?type.getType():null, json, url);
			LOG.error(METHOD_NAME+ msg, e);
			throw new RestClientException(msg, e);
		}
		return result;
	}
	
	public <T> T callGeneric(TypeReference<T> type, Class<T> serviceClass) {
		final String METHOD_NAME = "call";
		
		RestTemplate restTemplate = new RestTemplate();
		
		if(this.responseErrorHandler != null) {
			restTemplate.setErrorHandler(this.responseErrorHandler);
		}
		
		/**
		 * Gli intercetor pare rispondano in un tempo assai inferiore a quello reale quindi loggo con stopwatch 
		 * alla chiamata relae del servizio
		 * interceptors.add(new StopWatchRestInterceptor());
		 * interceptors.add(new TraceRestInterceptor());
		 */
		
		
		if(headers==null) {
			headers = new HttpHeaders();
		}
		
		String jsonFromBody = null;
		try {
			if(body != null) {
				ObjectMapper mapper = new ObjectMapper();
				jsonFromBody = mapper.writeValueAsString(body);
				LOG.debug(METHOD_NAME+ "jsonFromBody: %s"+ jsonFromBody);
			}
		}catch(Exception e) {
			String msg =  String.format("Error in body class %s ", body!= null?body.getClass():null);
			LOG.error(METHOD_NAME+ msg, e);
			throw new RestClientException(msg, e);
		}
		
		headers.keySet().stream().forEach(c -> LOG.debug(METHOD_NAME+ "header: " + c + headers.get(c)));
		HttpEntity<String> httpEntity = new HttpEntity<>(jsonFromBody, this.headers);
				
		String url = this.baseUrl + pathParams() + queryParams();
		LOG.debug(METHOD_NAME+ "url: %s"+ url);
		LOG.info(METHOD_NAME+ "url: " + url);
		ResponseEntity<T> re = null;
		try {
			if(encodingUTF8) {
				restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
			}
			URI uri = new URI(url);
			re = restTemplate.exchange(uri, httpMethod, httpEntity, serviceClass);
			T jsonRe = re.getBody();
			return jsonRe;
		} catch (URISyntaxException e1) {
			throw new RestClientException(e1.getMessage(), e1);
		}
	}

	private String pathParams() {
		if(StringUtils.isNotBlank(this.path)) {
			return "/" + this.path;
		}
		
		return "";
	}
	
	private static String urlEncode(String filter) {
		try {
			return URLEncoder.encode(filter!=null?filter:"", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("UTF-8 encoding not supported.",e);
		}
	}
	
	private static String cleanLastSlash(String url) {
		if(url==null) {
			return null;
		}
		if(url.endsWith("/")) {
			url = url.substring(0, url.length()-1);
		}
		return url;
	}
	
	private static String cleanFirstSlash(String url) {
		if(url==null) {
			return null;
		}
		if(url.startsWith("/")) {
			url = url.substring(1, url.length());
		}
		return url;
	}
}
