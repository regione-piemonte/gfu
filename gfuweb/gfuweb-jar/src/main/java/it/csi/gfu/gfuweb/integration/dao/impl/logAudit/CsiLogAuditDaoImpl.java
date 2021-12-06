/*******************************************************************************
* Copyright CSI-Piemonte -2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
******************************************************************************/
package it.csi.gfu.gfuweb.integration.dao.impl.logAudit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import it.csi.gfu.gfuweb.dto.logAudit.CsiLogAuditDto;
import it.csi.gfu.gfuweb.exception.DaoException;
import it.csi.gfu.gfuweb.exception.SystemException;
import it.csi.gfu.gfuweb.integration.dao.logAudit.CsiLogAuditDao;
import it.csi.gfu.gfuweb.util.Constants;
import it.csi.gfu.gfuweb.util.ErrorMessages;

@Repository
public class CsiLogAuditDaoImpl  extends JdbcDaoSupport implements CsiLogAuditDao {
	
	protected transient Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME);
	private NamedParameterJdbcTemplate namedJdbcTemplate; 
	
	@Autowired
	public CsiLogAuditDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate  namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
		setDataSource(dataSource);
	}
		

	@Override
	public BigDecimal insertCsiLogAudit(CsiLogAuditDto csiLogAuditDto)
			throws DaoException, SystemException {
		
		StringBuilder sql = new StringBuilder();	
		BigDecimal idAudit = null;
		
		try {

		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit] query sequence  = " + sql.toString());

		String queryInsert = " INSERT INTO CSI_LOG_AUDIT (DATA_ORA, ID_APP,  IP_ADDRESS ,UTENTE, OPERAZIONE, OGG_OPER, KEY_OPER ) " + 				
				"VALUES(:DATA_ORA , :ID_APP , :IP_ADDRESS, :UTENTE, :OPERAZIONE, :OGG_OPER, :KEY_OPER) RETURNING AUDIT_ID ";		
	
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("DATA_ORA", csiLogAuditDto.getDataOra());
        paramMap.put("ID_APP", csiLogAuditDto.getIdApp());      
        paramMap.put("IP_ADDRESS", csiLogAuditDto.getIpAddress());
        paramMap.put("UTENTE", csiLogAuditDto.getUtente());
        paramMap.put("OPERAZIONE", csiLogAuditDto.getOperazione());
        paramMap.put("OGG_OPER", csiLogAuditDto.getOggOper());
        paramMap.put("KEY_OPER", csiLogAuditDto.getKeyOper());
        
        
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [DATA_ORA] = " + csiLogAuditDto.getDataOra());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [ID_APP] = " + csiLogAuditDto.getIdApp());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [IP_ADDRESS] = " + csiLogAuditDto.getIpAddress());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [UTENTE] = " + csiLogAuditDto.getUtente());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [OPERAZIONE] = " + csiLogAuditDto.getOperazione());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [OGG_OPER] = " + csiLogAuditDto.getOggOper());
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  param [KEY_OPER] = " + csiLogAuditDto.getKeyOper());

		
        idAudit = namedJdbcTemplate.queryForObject(queryInsert, paramMap, BigDecimal.class);

			
		LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  Inserimento effettuato. Stato = SUCCESS ");} 
		
		catch(DataIntegrityViolationException ex)
		{
			LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit]  Integrity Keys Violation ");
			if (ex instanceof DuplicateKeyException) {
				  throw new DaoException(ErrorMessages.CODE_1_CHIAVE_DUPLICATA, ex.getMostSpecificCause());
			}
			throw new DaoException("Nessun dato in base alla richiesta", ex);
		}
		catch (Throwable ex) {
			LOG.error(
					"[CsiLogAuditDaoImpl::insertCsiLogAudit] esecuzione query Failed ",
					ex);
			throw new SystemException("Errore di sistema", ex);
		} finally {
			LOG.debug("[CsiLogAuditDaoImpl::insertCsiLogAudit] END ");
		}
		
		return idAudit;
	}
}