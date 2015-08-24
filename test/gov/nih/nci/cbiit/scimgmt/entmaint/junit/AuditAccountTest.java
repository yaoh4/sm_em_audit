package gov.nih.nci.cbiit.scimgmt.entmaint.junit;
import gov.nih.nci.cbiit.scimgmt.entmaint.actions.AdminDashboardAction;import gov.nih.nci.cbiit.scimgmt.entmaint.common.ApplicationConstants;import gov.nih.nci.cbiit.scimgmt.entmaint.common.DatabaseManager;import gov.nih.nci.cbiit.scimgmt.entmaint.common.PropertiesManager;import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2eAuditDAO;import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2ePortfolioDAO;import gov.nih.nci.cbiit.scimgmt.entmaint.dao.Impac2AuditDAO;import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmAuditAccountsVw;import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2eAuditAccountsVw;import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmI2ePortfolioVw;import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DashboardData;import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;import java.sql.Connection;import java.util.Date;import java.util.HashMap;import java.util.List;import java.util.Set;import org.junit.After;import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.test.annotation.Rollback;import org.springframework.test.context.ContextConfiguration;import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;import org.springframework.transaction.annotation.Transactional;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("applicationContext.xml")
public class AuditAccountTest {	private static final String ACTIVE = "active";	private static final String NEW = "new";	private static final String DELETED = "deleted";	private static final String INACTIVE = "inactive";	private static final String I2E = "i2e";	//for dashboard data testing	private HashMap<String, HashMap<String, DashboardData>> orgData;	private HashMap<String, HashMap<String, DashboardData>> otherOrgData;	Set<String> orgKeys;	Set<String> otherKeys;	
	List<String> allOrgs = null;		//since adminDashboardAction use latest audit ID, please sync the test.properties file with latest audit ID for completed account testing
	Long auditId = Long.parseLong(PropertiesManager.getAuiditId());
	
	@Autowired 
	protected Impac2AuditDAO impac2AuditDAO;
	@Autowired
	protected I2ePortfolioDAO i2ePortfolioDAO;
	@Autowired
	protected I2eAuditDAO i2eAuditDAO;
	@Autowired	protected AdminDashboardAction adminDashboardAction;	
	@Before
	public void setUp(){
		allOrgs = new DatabaseManager().getAllOrganization(auditId);
	}
	
	@After
	public void tearDown(){
		allOrgs = null; //Garbage collection
	}
			@Test	@Transactional	public void testDashboardActiveCompletedAccount(){		getDashboardData();		Date impaciiToDate = adminDashboardAction.getEmAuditsVO().getImpaciiToDate();		Date impaciiFromDate = adminDashboardAction.getEmAuditsVO().getImpaciiFromDate();		Long audit_id = adminDashboardAction.getAuditId();		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		boolean isOk = true;				for(String s : orgKeys){			HashMap<String, DashboardData> org = orgData.get(s);			Long count = dm.getDashboardActiveCompletedCount(conn, impaciiToDate, impaciiFromDate, s, audit_id);			if(count !=  org.get(ACTIVE).getActiveCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + org.get(ACTIVE).getActiveCompleteCount());				isOk = false;			}		}				for(String s : otherKeys){			HashMap<String, DashboardData> other = otherOrgData.get(s);			Long count = dm.getDashboardActiveCompletedCount(conn, impaciiToDate, impaciiFromDate, s, auditId);			if(count !=  other.get(ACTIVE).getActiveCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + other.get(ACTIVE).getActiveCompleteCount());				isOk = false;			}		}		dm.closeConnection(conn);		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test active completed accounts---");		}else{			System.out.println("----Test active completed accounts successfully---");		}	}		@Test	@Transactional	public void testDashboardNewCompletedAccount(){		getDashboardData();		Date impaciiToDate = adminDashboardAction.getEmAuditsVO().getImpaciiToDate();		Date impaciiFromDate = adminDashboardAction.getEmAuditsVO().getImpaciiFromDate();		Long audit_id = adminDashboardAction.getAuditId();		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		boolean isOk = true;				for(String s : orgKeys){			HashMap<String, DashboardData> org = orgData.get(s);			Long count = dm.getDashboardNewCompletedCount(conn, impaciiToDate, impaciiFromDate, s, audit_id);			if(count !=  org.get(NEW).getNewCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + org.get(NEW).getNewCompleteCount());				isOk = false;			}		}				for(String s : otherKeys){			HashMap<String, DashboardData> other = otherOrgData.get(s);			Long count = dm.getDashboardNewCompletedCount(conn, impaciiToDate, impaciiFromDate, s, auditId);			if(count !=  other.get(NEW).getNewCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + other.get(NEW).getNewCompleteCount());				isOk = false;			}		}		dm.closeConnection(conn);		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test new completed accounts---");		}else{			System.out.println("----Test new completed accounts successfully---");		}	}	@Test	@Transactional	public void testDashboardDeletedCompletedAccount(){		getDashboardData();		Date impaciiToDate = adminDashboardAction.getEmAuditsVO().getImpaciiToDate();		Date impaciiFromDate = adminDashboardAction.getEmAuditsVO().getImpaciiFromDate();		Long audit_id = adminDashboardAction.getAuditId();		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		boolean isOk = true;				for(String s : orgKeys){			HashMap<String, DashboardData> org = orgData.get(s);			Long count = dm.getDashboardDeletedCompletedCount(conn, impaciiToDate, impaciiFromDate, s, audit_id);			if(count !=  org.get(DELETED).getDeletedCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + org.get(DELETED).getDeletedCompleteCount());				isOk = false;			}		}				for(String s : otherKeys){			HashMap<String, DashboardData> other = otherOrgData.get(s);			Long count = dm.getDashboardDeletedCompletedCount(conn, impaciiToDate, impaciiFromDate, s, auditId);			if(count !=  other.get(DELETED).getDeletedCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + other.get(DELETED).getDeletedCompleteCount());				isOk = false;			}		}		dm.closeConnection(conn);		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test deleted completed accounts---");		}else{			System.out.println("----Test deleted completed accounts successfully---");		}	}	@Test	@Transactional	public void testDashboardInactiveCompletedAccount(){		getDashboardData();		Long audit_id = adminDashboardAction.getAuditId();		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		boolean isOk = true;				for(String s : orgKeys){			HashMap<String, DashboardData> org = orgData.get(s);			Long count = dm.getDashboardInactiveCompletedCount(conn, s, audit_id);			if(count !=  org.get(INACTIVE).getInactiveCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + org.get(INACTIVE).getInactiveCompleteCount());				isOk = false;			}		}				for(String s : otherKeys){			HashMap<String, DashboardData> other = otherOrgData.get(s);			Long count = dm.getDashboardInactiveCompletedCount(conn, s, auditId);			if(count !=  other.get(INACTIVE).getInactiveCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + other.get(INACTIVE).getActiveCompleteCount());				isOk = false;			}		}		dm.closeConnection(conn);		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test inactive completed accounts---");		}else{			System.out.println("----Test inactive completed accounts successfully---");		}	}		@Test	@Transactional	public void testDashboardI2ECompletedAccount(){		getDashboardData();				Long audit_id = adminDashboardAction.getAuditId();		DatabaseManager dm = new  DatabaseManager();		Connection conn = dm.getConnection();		boolean isOk = true;				for(String s : orgKeys){			HashMap<String, DashboardData> org = orgData.get(s);			Long count = dm.getDashboardI2eCompletedCount(conn, s, audit_id);			if(count !=  org.get(I2E).getI2eCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + org.get(I2E).getI2eCompleteCount());				isOk = false;			}		}				for(String s : otherKeys){			HashMap<String, DashboardData> other = otherOrgData.get(s);			Long count = dm.getDashboardI2eCompletedCount(conn, s, auditId);			if(count !=  other.get(I2E).getI2eCompleteCount()){				System.out.println("organization: " + s +"-----" + " jdbc:" + count + " --- " + "dao:" + other.get(I2E).getI2eCompleteCount());				isOk = false;			}		}		dm.closeConnection(conn);		if(!isOk){			Assert.assertTrue(isOk);			System.out.println("----Failed to test i2e completed accounts---");		}else{			System.out.println("----Test i2e completed accounts successfully---");		}	}	
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
	}		private void getDashboardData(){		adminDashboardAction.gotoDashboard();		orgData = adminDashboardAction.getOrgsData();		otherOrgData = adminDashboardAction.getOtherOrgsData();		orgKeys = orgData.keySet();		otherKeys = otherOrgData.keySet();	}}
