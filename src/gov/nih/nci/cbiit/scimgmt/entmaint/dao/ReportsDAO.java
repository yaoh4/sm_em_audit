package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.TransferredAuditAccountsVO;

/**
 * DAO for Admin reports.
 * @author tembharenbd
 */
@Component
public class ReportsDAO {

	public static Logger log = Logger.getLogger(ReportsDAO.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private NciUser nciUser;
		
	/**
	 * Search Transferred Audit Accounts
	 * 
	 * @param paginatedList
	 * @param searchVO
	 * @param all
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginatedListImpl<TransferredAuditAccountsVO> searchTransferredAccounts(PaginatedListImpl paginatedList, final AuditSearchVO searchVO, Boolean all) throws Exception {
		log.debug("searching for Transferred accounts: " + searchVO);
		PaginatedListImpl<TransferredAuditAccountsVO> pListImpl = null;
		
		try {		
			//Union of i2eVwLeftJoinAuditVw and auditVwLeftJoinI2eVw
			StringBuffer transferredAccountsQuery = new StringBuffer();
			
			//Left outer join of i2eVw and AuditVw
			StringBuffer i2eVwLeftJoinAuditVw = new StringBuffer();
			
			//Left outer join of AuditVw and i2eVw
			StringBuffer auditVwLeftJoinI2eVw = new StringBuffer();				
			
			i2eVwLeftJoinAuditVw.append("select i2eVw.last_name AS lastName, i2eVw.first_name AS firstName, i2eVw.nih_network_id AS nihNetworkId, i2eVw.ned_org_path AS nedOrgPath, ");
			i2eVwLeftJoinAuditVw.append("auditVw.impacii_active_status_flag AS impaciiActiveStatusFlag, i2eVw.i2e_active_status_flag AS i2eActiveStatusFlag, ");
			i2eVwLeftJoinAuditVw.append("i2eVw.transfer_to_org_path AS transferToNedOrgPath, i2eVw.transfer_from_org_path AS transferFromNedOrgPath, ");
			i2eVwLeftJoinAuditVw.append("auditVw.deleted_transfer_to_org_path AS deletedTransferToNedOrgPath, auditVw.deleted_transfer_from_org_path AS deletedTransferFromNedOrgPath, auditVw.status_code AS statusCode ");
			i2eVwLeftJoinAuditVw.append("from em_i2e_audit_accounts_vw i2eVw left outer join em_audit_accounts_vw auditVw on i2eVw.nih_network_id = auditVw.nih_network_id and i2eVw.audit_id = auditVw.audit_id ");
			i2eVwLeftJoinAuditVw.append("where i2eVw.audit_id = :auditId and i2eVw.transfer_to_org_path is not null ");			
			
			auditVwLeftJoinI2eVw.append("select auditVw.last_name AS lastName, auditVw.first_name AS firstName, auditVw.nih_network_id AS nihNetworkId, auditVw.ned_org_path AS nedOrgPath, ");
			auditVwLeftJoinI2eVw.append("auditVw.impacii_active_status_flag AS impaciiActiveStatusFlag, i2eVw.i2e_active_status_flag AS i2eActiveStatusFlag, ");
			auditVwLeftJoinI2eVw.append("auditVw.transfer_to_org_path AS transferToNedOrgPath, auditVw.transfer_from_org_path AS transferFromNedOrgPath, ");
			auditVwLeftJoinI2eVw.append("auditVw.deleted_transfer_to_org_path AS deletedTransferToNedOrgPath, auditVw.deleted_transfer_from_org_path AS deletedTransferFromNedOrgPath, auditVw.status_code AS statusCode ");
			auditVwLeftJoinI2eVw.append("from em_audit_accounts_vw auditVw left outer join em_i2e_audit_accounts_vw  i2eVw on auditVw.nih_network_id = i2eVw.nih_network_id and i2eVw.audit_id = auditVw.audit_id ");
			auditVwLeftJoinI2eVw.append("where auditVw.audit_id = :auditId and (auditVw.transfer_to_org_path is not null or auditVw.deleted_transfer_to_org_path is not null)");
			
			transferredAccountsQuery.append("select lastName, firstName, nihNetworkId, nedOrgPath, impaciiActiveStatusFlag, i2eActiveStatusFlag, transferToNedOrgPath, transferFromNedOrgPath, deletedTransferToNedOrgPath, deletedTransferFromNedOrgPath, statusCode from ( ")	;
			transferredAccountsQuery.append(i2eVwLeftJoinAuditVw.toString());
			transferredAccountsQuery.append("union ");
			transferredAccountsQuery.append(auditVwLeftJoinI2eVw.toString());
			transferredAccountsQuery.append(") order by " + addSortOrder(paginatedList));					
			
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(transferredAccountsQuery.toString());
			query.setParameter("auditId",searchVO.getAuditId());		
			
			pListImpl =  getPaginatedListResult(paginatedList, query, all, searchVO.getAuditId());			
			return pListImpl;

		} catch (Throwable e) {			
			EmAppUtil.logUserID(nciUser, log);
			log.error("Pass-in parameters: paginatedList - " + paginatedList + ", searchVO - " + searchVO + ", all - " + all);
			log.error("Outgoing parameters: pListImpl - " + (pListImpl == null ? "NULL" : "object ID=" + pListImpl.toString()));
			String errorString = "Error while searching for Transferred accounts. ";
			log.error(errorString, e);
			throw new Exception(errorString,e);			
		}
	}
	
	/**
	 * Add Sort Order
	 * 
	 * @param criteria
	 * @param paginatedList
	 * @return
	 */
	private String addSortOrder(PaginatedListImpl paginatedList) {
		
		String sortOrderCriterion = paginatedList.getSortCriterion();
		String sortOrder = paginatedList.getSqlSortDirection();
		String orderBy = "lastName asc, firstName asc";
		
		if (!StringUtils.isBlank(sortOrderCriterion)) {
			if (sortOrderCriterion.equalsIgnoreCase("fullName")) {
				if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
					orderBy = "lastName asc, firstName asc";
				} else {
					orderBy = "lastName desc, firstName desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("nihNetworkId")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "nihNetworkId asc";
				}else{
					orderBy = "nihNetworkId desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("nedOrgPath")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "nedOrgPath asc";
				}else{
					orderBy = "nedOrgPath desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("impaciiActiveStatusFlag")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "impaciiActiveStatusFlag asc";
				}else{
					orderBy = "impaciiActiveStatusFlag desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("i2eActiveStatusFlag")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "i2eActiveStatusFlag asc";
				}else{
					orderBy = "i2eActiveStatusFlag desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("transferToNedOrgPath")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "transferToNedOrgPath asc";
				}else{
					orderBy = "transferToNedOrgPath desc";
				}
			}else if(sortOrderCriterion.equalsIgnoreCase("transferFromNedOrgPath")){
				if(StringUtils.equalsIgnoreCase(sortOrder, "asc")){
					orderBy = "transferFromNedOrgPath asc";
				}else{
					orderBy = "transferFromNedOrgPath desc";
				}
			}
		}
		return orderBy;
	}
	
	/**
	 * Get paginated list
	 * @param paginatedList
	 * @param criteria
	 * @param all
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PaginatedListImpl getPaginatedListResult(PaginatedListImpl paginatedList, SQLQuery query, boolean all, Long auditId) throws Exception {
		final int objectsPerPage = paginatedList.getObjectsPerPage();
		final int firstResult = objectsPerPage * paginatedList.getIndex();
			
		List results = null;		
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		if (all) {
			results = query.list();
			paginatedList.setTotal(results.size());
		} else {
			results = query.setFirstResult(firstResult)
				.setMaxResults(objectsPerPage)
				.list();
		}
		List<TransferredAuditAccountsVO> transferredAccountsList = populateTransferredAccountVOs(results);
		paginatedList.setList(transferredAccountsList);
		
		if(!all && paginatedList.getFullListSize() == 0) {
			SQLQuery resultCountQuery = sessionFactory.getCurrentSession().createSQLQuery("select count(*) from ( "+query.getQueryString()+")");
			resultCountQuery.setParameter("auditId",auditId);
			paginatedList.setTotal(getTotalResultCount(resultCountQuery));
		}
		return paginatedList;
	}	
	
	/**
	 * Gets the total result count.	 * 
	 * @param SQLQuery the resultCountQuery
	 * @return the total result count
	 * @throws Exception 
	 */
	private int getTotalResultCount(SQLQuery resultCountQuery) throws Exception {
		try{			
			return  ( (BigDecimal) resultCountQuery.uniqueResult() ).intValue();	
		} catch (Throwable e) {			
			EmAppUtil.logUserID(nciUser, log);	
			log.error("Pass-in parameters: resultCountQuery - " + resultCountQuery.getQueryString());	
			String errorString = "Error while getting total result count for Transferred accounts list. ";
			log.error(errorString, e);
			throw new Exception(errorString,e);			
		}
	}
	
	/**
	 * Populates TransferredAuditAccountsVO from query results.
	 * @param results
	 * @return transferredAccountsList
	 */
	@SuppressWarnings("rawtypes")
	public List<TransferredAuditAccountsVO> populateTransferredAccountVOs(List results){
		List<TransferredAuditAccountsVO> transferredAccountsList  = new ArrayList<TransferredAuditAccountsVO>();
		
		for(Object object: results){			
			Map row = (Map)object;
			TransferredAuditAccountsVO accountVo = new TransferredAuditAccountsVO();
			
			accountVo.setFirstName((String)row.get("FIRSTNAME"));
			accountVo.setLastName((String)row.get("LASTNAME"));
			accountVo.setNihNetworkId((String)row.get("NIHNETWORKID"));
			accountVo.setNedOrgPath((String)row.get("NEDORGPATH"));
			accountVo.setTransferFromNedOrgPath((String)row.get("TRANSFERFROMNEDORGPATH"));
			accountVo.setTransferToNedOrgPath((String)row.get("TRANSFERTONEDORGPATH"));	
			accountVo.setDeletedTransferToNedOrgPath((String)row.get("DELETEDTRANSFERTONEDORGPATH"));	
			accountVo.setDeletedTransferFromNedOrgPath((String)row.get("DELETEDTRANSFERFROMNEDORGPATH"));
			if ((BigDecimal) row.get("STATUSCODE") != null) {
				accountVo.setStatusCode(((BigDecimal) row.get("STATUSCODE")).longValue());
			}
			
			if(row.get("IMPACIIACTIVESTATUSFLAG") == null){
				accountVo.setImpaciiActiveStatusFlag(null);
			}
			else if("Y".equalsIgnoreCase((String)row.get("IMPACIIACTIVESTATUSFLAG"))){
				accountVo.setImpaciiActiveStatusFlag(true);
			}
			else{
				accountVo.setImpaciiActiveStatusFlag(false);
			}			
			if(row.get("I2EACTIVESTATUSFLAG") == null){
				accountVo.setI2eActiveStatusFlag(null);
			}
			else if("Y".equalsIgnoreCase((String)row.get("I2EACTIVESTATUSFLAG"))){
				accountVo.setI2eActiveStatusFlag(true);
			}
			else{
				accountVo.setI2eActiveStatusFlag(false);
			}			
			transferredAccountsList.add(accountVo);
		}		
		
		return transferredAccountsList;
	}
}
