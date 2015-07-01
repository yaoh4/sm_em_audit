package gov.nih.nci.cbiit.scimgmt.entmaint.junit;

import java.sql.Connection;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.common.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.common.DatabaseManager;
import gov.nih.nci.cbiit.scimgmt.entmaint.common.PropertiesManager;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2AuditDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("applicationContext.xml")
public class AuditAccountTest {
	List<String> allOrgs = null;
	Long auditId = Long.parseLong(PropertiesManager.getAuiditId());
	@Autowired 
	protected Impac2AuditDAO impac2AuditDAO;
	
	@Before
	public void setUp(){
		allOrgs = new DatabaseManager().getAllOrganization(auditId);
	}
	
	@After
	public void tearDown(){
		allOrgs = null; //Garbage collection
	}
	
	@Test
	@Transactional
	public void testActiveAccount(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		for(String s : allOrgs){
			int count_from_jdbc = dm.getOrgCount(conn, s, auditId, ApplicationConstants.ACTIVE_TYPE);
			int count_from_dao = getCount(ApplicationConstants.ACTIVE_TYPE, s, auditId);
			if(count_from_jdbc != count_from_dao){
				System.out.println("organization: " + s +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
				isOk = false;
			}
			
		}
		dm.closeConnection(conn);
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test active accounts---");
		}else{
			System.out.println("----Test active accounts successfully---");
		}
	}
	
	@Test
	@Transactional
	public void testNewAccount(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		for(String s : allOrgs){
			int count_from_jdbc = dm.getOrgCount(conn, s, auditId, ApplicationConstants.NEW_TYPE);
			int count_from_dao = getCount(ApplicationConstants.NEW_TYPE, s, auditId);
			if(count_from_jdbc != count_from_dao){
				System.out.println("organization: " + s +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
				isOk = false;
			}
			
		}
		dm.closeConnection(conn);
		
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test new accounts---");
		}else{
			System.out.println("----Test new accounts successfully---");
		}
	}

	@Test
	@Transactional
	public void testDeletedAccount(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		for(String s : allOrgs){
			int count_from_jdbc = dm.getOrgCount(conn, s, auditId, ApplicationConstants.DELETED_TYPE);
			int count_from_dao = getCount(ApplicationConstants.DELETED_TYPE, s, auditId);
			if(count_from_jdbc != count_from_dao){
				System.out.println("organization: " + s +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
				isOk = false;
			}
		}
		dm.closeConnection(conn);
		
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test deleted accounts---");
		}else{
			System.out.println("----Test deleted accounts successfully---");
		}
	}
	
	@Test
	@Transactional
	public void testInactiveAccount(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		for(String s : allOrgs){
			int count_from_jdbc = dm.getOrgCount(conn, s, auditId, ApplicationConstants.INACTIVE_TYPE);
			int count_from_dao = getCount(ApplicationConstants.INACTIVE_TYPE, s, auditId);
			if(count_from_jdbc != count_from_dao){
				System.out.println("organization: " + s +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
				isOk = false;
			}
		}
		dm.closeConnection(conn);
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test inactive accounts---");
		}else{
			System.out.println("----Test inactive accounts successfully---");
		}
	}
	
	private int getCount(String type, String orgName, Long auditId){
		PaginatedListImpl<EmAuditAccountsVw> auditList = null;
		
		PaginatedListImpl<EmAuditAccountsVw> pl = new PaginatedListImpl<EmAuditAccountsVw>();
		AuditSearchVO svo = new AuditSearchVO();
		//svo.setOrganization("OD CBIIT");
		svo.setOrganization(orgName);
		svo.setAuditId(auditId);
		if(type.equalsIgnoreCase(ApplicationConstants.ACTIVE_TYPE)){
			auditList = impac2AuditDAO.searchActiveAccounts(pl, svo, true);
		}else if(type.equalsIgnoreCase(ApplicationConstants.NEW_TYPE)){
			auditList = impac2AuditDAO.searchNewAccounts(pl, svo, true);
		}else if(type.equalsIgnoreCase(ApplicationConstants.DELETED_TYPE)){
			auditList = impac2AuditDAO.searchDeletedAccounts(pl, svo, true);
		}else if(type.equalsIgnoreCase(ApplicationConstants.INACTIVE_TYPE)){
			auditList = impac2AuditDAO.searchInactiveAccounts(pl, svo, true);
		}
		return auditList.getFullListSize();
	}
}
