package gov.nih.nci.cbiit.scimgmt.entmaint.junit;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cbiit.scimgmt.entmaint.common.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.common.DatabaseManager;
import gov.nih.nci.cbiit.scimgmt.entmaint.common.PropertiesManager;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2eAuditDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2ePortfolioDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2AuditDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsVw;import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2ePortfolioVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
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
	@Autowired
	protected I2ePortfolioDAO i2ePortfolioDAO;
	@Autowired
	protected I2eAuditDAO i2eAuditDAO;
	
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
	
	@Test
	@Transactional
	public void testI2eActiveAccount(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		for(String s : allOrgs){
			int count_from_jdbc = dm.getOrgCount(conn, s, auditId, ApplicationConstants.I2EACCOUNT);
			int count_from_dao = getCount(ApplicationConstants.I2EACCOUNT, s, auditId);
			if(count_from_jdbc != count_from_dao){				System.out.println("organization: " + s +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
				isOk = false;
			}
			
		}
		dm.closeConnection(conn);
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test i2e active accounts---");
		}else{
			System.out.println("----Test i2e active accounts successfully---");
		}
	}
	
	@Test
	@Transactional
	public void testI2eAccountPortfolioSearch(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		String org = PropertiesManager.getOrgName();
		Long category = Long.parseLong(PropertiesManager.getI2eAccountId());
		int count_from_jdbc = dm.getPortfolioI2eCount(conn, org, category);
		dm.closeConnection(conn);
		int count_from_dao = getI2ePortfolioAccount(org, category);
		if(count_from_jdbc != count_from_dao){
			System.out.println("organization: " + org +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
			isOk = false;
		}
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test portfolio i2e accounts---");
		}else{
			System.out.println("----Test portfolio i2e accounts successfully---");
		}
	}

	@Test
	@Transactional
	public void testI2ediscrepancyPortfolioSearch(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		String org = PropertiesManager.getOrgName();
		Long category = Long.parseLong(PropertiesManager.getI2eDiscrepancyId());
		int count_from_jdbc = dm.getPortfolioI2eCount(conn, org, category);
		dm.closeConnection(conn);
		int count_from_dao = getI2ePortfolioAccount(org, category);
		if(count_from_jdbc != count_from_dao){
			System.out.println("organization: " + org +"-----" + " jdbc:" + count_from_jdbc + " --- " + "dao:" + count_from_dao);
			isOk = false;
		}
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test portfolio i2e discrepancy---");
		}else{
			System.out.println("----Test portfolio i2e discrepancy successfully---");
		}	
	}
	
	@Test
	@Transactional
	public void testI2ePortfolioFetchNotes(){
		boolean isOk = true;
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		Long npnId = dm.getNpnId(conn);
		String note_from_jdbc = dm.getNoteById(conn, npnId);
		dm.closeConnection(conn);
		String note_from_dao = i2ePortfolioDAO.getPortfolioNoteById(npnId);
		
		if(note_from_jdbc != null && !note_from_jdbc.equalsIgnoreCase(note_from_dao)){
			isOk = false;
		}
		
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test portfolio i2e note retrieving---");
		}else{
			System.out.println("----Test portfolio i2e note retrieving successfully---");
		}
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void testI2ePortfolioSaveNotes(){
		boolean isOk = true;
		String note_for_input = "This message is from JUnit test code 1323";
		DatabaseManager dm = new  DatabaseManager();
		Connection conn = dm.getConnection();
		Long npnId = dm.getNpnId(conn);
		dm.closeConnection(conn);
		i2ePortfolioDAO.saveNotes(npnId, note_for_input, new Date());
		try {
			//Wait for data are inserted into database
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String result = i2ePortfolioDAO.getPortfolioNoteById(npnId);
		if(!note_for_input.equalsIgnoreCase(result)){
			isOk = false;
		}
		if(!isOk){
			Assert.assertTrue(isOk);
			System.out.println("----Failed to test portfolio i2e note saving---");
		}else{
			System.out.println("----Test portfolio i2e note saving successfully---");
		}
	}
		@Test	@Transactional	@Rollback(false)	public void testI2eAuditSaveAction(){		boolean isOk = true;		String note_for_input = "This message is from JUnit test code 1324";		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		Long eiaaId = dm.getEiaaId(conn, auditId);		dm.closeConnection(conn);		Long actionId = Long.parseLong(PropertiesManager.getActionId());		i2eAuditDAO.submit(eiaaId, actionId, note_for_input, new Date());		try {			//Wait for data are inserted into database			Thread.sleep(1000);		} catch (InterruptedException e) {			e.printStackTrace();		}		String result = i2eAuditDAO.getAuditAccountById(eiaaId).getNotes();		if(!note_for_input.equalsIgnoreCase(result)){			isOk = false;		}		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test audit i2e action saving---");		}else{			System.out.println("----Test audit i2e action saving successfully---");		}	}	
	private int getI2ePortfolioAccount(String orgName, Long cateId){
		PaginatedListImpl<EmI2ePortfolioVw> pl = new PaginatedListImpl<EmI2ePortfolioVw>();
		AuditSearchVO svo = new AuditSearchVO();
		svo.setOrganization(orgName);
		svo.setCategory(cateId);
		pl = i2ePortfolioDAO.searchI2eAccounts(pl, svo, true);
		return pl.getFullListSize();
	}
	private int getCount(String type, String orgName, Long auditId){
		PaginatedListImpl<EmAuditAccountsVw> auditList = null;
		PaginatedListImpl<EmI2eAuditAccountsVw> i2eAuditList = null;
		PaginatedListImpl<EmAuditAccountsVw> pl = new PaginatedListImpl<EmAuditAccountsVw>();		PaginatedListImpl<EmI2eAuditAccountsVw> plI2e = new PaginatedListImpl<EmI2eAuditAccountsVw>();		int cnt = 0;
		AuditSearchVO svo = new AuditSearchVO();
		svo.setOrganization(orgName);
		svo.setAuditId(auditId);
		if(type.equalsIgnoreCase(ApplicationConstants.ACTIVE_TYPE)){
			auditList = impac2AuditDAO.searchActiveAccounts(pl, svo, true);			cnt = auditList.getFullListSize();
		}else if(type.equalsIgnoreCase(ApplicationConstants.NEW_TYPE)){
			auditList = impac2AuditDAO.searchNewAccounts(pl, svo, true);			cnt = auditList.getFullListSize();
		}else if(type.equalsIgnoreCase(ApplicationConstants.DELETED_TYPE)){
			auditList = impac2AuditDAO.searchDeletedAccounts(pl, svo, true);			cnt = auditList.getFullListSize();
		}else if(type.equalsIgnoreCase(ApplicationConstants.INACTIVE_TYPE)){
			auditList = impac2AuditDAO.searchInactiveAccounts(pl, svo, true);			cnt = auditList.getFullListSize();
		}else if(type.equalsIgnoreCase(ApplicationConstants.I2EACCOUNT)){			i2eAuditList = i2eAuditDAO.searchActiveAccounts(plI2e, svo, true);			cnt = i2eAuditList.getFullListSize();		}
		return cnt;
	}
}
