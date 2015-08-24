package gov.nih.nci.cbiit.scimgmt.entmaint.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Restrictions;

public class DatabaseManager {
	private String url = PropertiesManager.getUrl();
	private String userId = PropertiesManager.getUserId();
	private String password = PropertiesManager.getPassword();
	public static final String NON_NCI = "Non-NCI";
	
	public Connection getConnection(){
		Connection connection = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection(url, userId, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return connection;
	}
	
	public void closeConnection(Connection conn){
		try{
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<String> getAllOrganization(Long auditId){
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "select * from EM_AUDIT_ACCOUNTS_VW where audit_id = " + auditId;
		List<String> allOrganization = null;
		
		try{
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			Set<String> set = new HashSet<String>();
			while(rs.next()){
				String org = rs.getString("PARENT_NED_ORG_PATH");
				if(org != null && org.length() > 0){
					set.add(org);
				}
			}
			allOrganization = new ArrayList<String>(set);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Collections.sort(allOrganization);
		return allOrganization;
	}
	
	public int getOrgCount(Connection conn, String org, Long auditId, String type){
		int count = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		
		String sql_active = "select count(*) as numCount from em_audit_accounts_vw eaa, em_audits_vw ea where eaa.audit_id = ea.id " +
				     "and ea.id = ? and (eaa.deleted_date is null or trunc(eaa.deleted_date) >= ea.impacii_to_date) " +
				     "and trunc(eaa.created_date) <= ea.impacii_to_date and eaa.parent_ned_org_path = ?";
		
		String sql_new = "select count(*) as numCount from em_audit_accounts_vw eaa, em_audits_vw ea where eaa.audit_id = ea.id " +
					 "and ea.id = ? and trunc(eaa.created_date) >= ea.impacii_from_date " +
					 "and trunc(eaa.created_date) <= ea.impacii_to_date and eaa.parent_ned_org_path = ?";
	
		String sql_deleted = "select count(*) as numCount from em_audit_accounts_vw eaa, em_audits_vw ea where eaa.audit_id = ea.id " +
					"and ea.id = ? and trunc(eaa.deleted_date) >= ea.impacii_from_date " +
					"and trunc(eaa.deleted_date) <= ea.impacii_to_date and eaa.DELETED_BY_PARENT_ORG_PATH = ?";
	
		String sql_inactive = "select count(*) as numCount from em_audit_accounts_vw eaa, em_audits_vw ea where eaa.audit_id = ea.id " +
					"and ea.id = ? and eaa.inactive_user_flag = 'Y' and eaa.parent_ned_org_path = ?";
		
		String sql_i2e = "select count(*) as numCount from em_i2e_audit_accounts_vw eaa, em_audits_vw ea where eaa.audit_id = ea.id " +
				"and ea.id = ? and eaa.parent_ned_org_path = ?";
		
		if(type.equalsIgnoreCase(ApplicationConstants.ACTIVE_TYPE)){
			sql = sql_active;
		}else if(type.equalsIgnoreCase(ApplicationConstants.NEW_TYPE)){
			sql = sql_new;
		}else if(type.equalsIgnoreCase(ApplicationConstants.DELETED_TYPE)){
			sql = sql_deleted;
		}else if(type.equalsIgnoreCase(ApplicationConstants.INACTIVE_TYPE)){
			sql = sql_inactive;
		}else if(type.equalsIgnoreCase(ApplicationConstants.I2EACCOUNT)){
			sql = sql_i2e;
		}
		
		try{
			ps = conn.prepareStatement(sql);
			ps.setLong(1, auditId);
			ps.setString(2, org);
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt("numCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}
	
	public int getPortfolioI2eCount(Connection conn, String orgName, Long cateId){
		int count = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = null;
		String name = orgName.toUpperCase();
		
		String sqlI2eAccount = "select count(*) as i2eCount from em_i2e_portfolio_vw where parent_ned_org_path = ?";
		String sqlI2eDiscrepancy = "select count(*) as i2eCount from em_i2e_portfolio_vw where parent_ned_org_path = ? and " + 
							"(SOD_FLAG = 'Y' or NED_INACTIVE_FLAG = 'Y' or NO_ACTIVE_ROLE_FLAG = 'Y' or I2E_ONLY_FLAG = 'Y' or Active_ROLE_REMAINDER_FLAG = 'Y')";
		if(cateId == 37){
			sql = sqlI2eAccount;
		}else{
			sql = sqlI2eDiscrepancy;
		}
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			rs = ps.executeQuery();
			rs.next();
			count = rs.getInt("i2eCount");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
	
	public Long getNpnId(Connection conn){
		Long id = null;
		Statement st = null;
		ResultSet rs = null;
		List<Long> ids = new ArrayList<Long>();
		
		String sql = "select npn_id as npnId from em_portfolio_notes_t where npn_id is not null";
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()){
				Long npnId = rs.getLong("npnId");
				if(npnId != null){
					ids.add(npnId);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		if(ids != null && ids.size() > 0){
			id = ids.get(0);
		}else{
			id = null;
		}
		
		return id;
	}
	
	public Long getEiaaId(Connection conn, Long auditId){
		Long id = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Long> ids = new ArrayList<Long>();
		
		String sql = "select id as id from em_i2e_audit_accounts_t where id is not null and eau_id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setLong(1, auditId);
			rs = ps.executeQuery();
			while(rs.next()){
				Long eiaaId = rs.getLong("id");
				if(eiaaId != null){
					ids.add(eiaaId);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		if(ids != null && ids.size() > 0){
			id = ids.get(0);
		}else{
			id = null;
		}
		
		return id;
	}
	
	public String getNoteById(Connection conn, Long id){
		String note = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from em_portfolio_notes_t where npn_id = ?";
		try{
			ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();
			note = rs.getString("notes");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				rs.close();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return note;
	}
	
	public Long getDashboardActiveCompletedCount(Connection conn, Date impaciiToDate, Date impaciiFromDate, String orgName, Long auditId){
		Long count = 0L;
		Statement st = null;
		ResultSet rs = null;
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(impaciiToDate);
		String sql = null;
		if(!orgName.equalsIgnoreCase(NON_NCI)){
			sql = "select count(*) as myCount from em_audit_accounts_vw where (deleted_date is null or trunc(deleted_date) > to_date('" + dateStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateStr+ "', 'yyyy-MM-dd')) " + 
					" and created_date is not null and (trunc(created_date) < to_date('" + dateStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateStr+ "', 'yyyy-MM-dd')) " +  
					" and active_submitted_date is not null and parent_ned_org_path = '" + orgName+ "' and audit_id = " + auditId;
		}else{
			sql = "select count(*) as myCount from em_audit_accounts_vw where (deleted_date is null or trunc(deleted_date) > to_date('" + dateStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateStr+ "', 'yyyy-MM-dd')) " + 
					" and created_date is not null and (trunc(created_date) < to_date('" + dateStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateStr+ "', 'yyyy-MM-dd')) " +  
					" and active_submitted_date is not null and NCI_DOC = 'OTHER' and ned_ic <> 'NCI' and audit_id = " + auditId;
		}
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			count = rs.getLong("myCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	public Long getDashboardNewCompletedCount(Connection conn, Date impaciiToDate, Date impaciiFromDate, String orgName, Long auditId){
		Long count = 0L;
		Statement st = null;
		ResultSet rs = null;
		String dateFromStr = new SimpleDateFormat("yyyy-MM-dd").format(impaciiFromDate);
		String dateToStr = new SimpleDateFormat("yyyy-MM-dd").format(impaciiToDate);
		
		String sql = null;
		if(!orgName.equalsIgnoreCase(NON_NCI)){
			sql = "select count(*) as myCount from em_audit_accounts_vw where (trunc(created_date) > to_date('" + dateFromStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateFromStr+ "', 'yyyy-MM-dd')) " + 
					" and (trunc(created_date) < to_date('" + dateToStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateToStr+ "', 'yyyy-MM-dd')) " +  
					" and new_submitted_date is not null and parent_ned_org_path = '" + orgName+ "' and audit_id = " + auditId;
		}else{
			sql = "select count(*) as myCount from em_audit_accounts_vw where (trunc(created_date) > to_date('" + dateFromStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateFromStr+ "', 'yyyy-MM-dd')) " + 
					" and (trunc(created_date) < to_date('" + dateToStr+ "', 'yyyy-MM-dd') or trunc(created_date) = to_date('" + dateToStr+ "', 'yyyy-MM-dd')) " +  
					" and new_submitted_date is not null and NCI_DOC = 'OTHER' and ned_ic <> 'NCI' and audit_id = " + auditId;
		}
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			count = rs.getLong("myCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	public Long getDashboardDeletedCompletedCount(Connection conn, Date impaciiToDate, Date impaciiFromDate, String orgName, Long auditId){
		Long count = 0L;
		Statement st = null;
		ResultSet rs = null;
		String dateFromStr = new SimpleDateFormat("yyyy-MM-dd").format(impaciiFromDate);
		String dateToStr = new SimpleDateFormat("yyyy-MM-dd").format(impaciiToDate);
		
		String sql = null;
		if(!orgName.equalsIgnoreCase(NON_NCI)){
			sql = "select count(*) as myCount from em_audit_accounts_vw where (trunc(deleted_date) > to_date('" + dateFromStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateFromStr+ "', 'yyyy-MM-dd')) " + 
					" and (trunc(deleted_date) < to_date('" + dateToStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateToStr+ "', 'yyyy-MM-dd')) " +  
					" and deleted_submitted_date is not null and parent_ned_org_path = '" + orgName+ "' and audit_id = " + auditId;
		}else{
			sql = "select count(*) as myCount from em_audit_accounts_vw where (trunc(deleted_date) > to_date('" + dateFromStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateFromStr+ "', 'yyyy-MM-dd')) " + 
					" and (trunc(deleted_date) < to_date('" + dateToStr+ "', 'yyyy-MM-dd') or trunc(deleted_date) = to_date('" + dateToStr+ "', 'yyyy-MM-dd')) " +  
					" and deleted_submitted_date is not null and NCI_DOC = 'OTHER' and ned_ic <> 'NCI' and audit_id = " + auditId;
		}
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			count = rs.getLong("myCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	public Long getDashboardInactiveCompletedCount(Connection conn, String orgName, Long auditId){
		Long count = 0L;
		Statement st = null;
		ResultSet rs = null;
		
		String sql = null;
		if(!orgName.equalsIgnoreCase(NON_NCI)){
			sql = "select count(*) as myCount from em_audit_accounts_vw where inactive_unsubmitted_flag is not null and inactive_unsubmitted_flag = 'Y' and parent_ned_org_path = '" + orgName+ "' and audit_id = " + auditId;
		}else{
			sql = "select count(*) as myCount from em_audit_accounts_vw where inactive_unsubmitted_flag is not null and inactive_unsubmitted_flag = 'Y' and NCI_DOC = 'OTHER' and ned_ic <> 'NCI' and audit_id = " + auditId;
		}
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			count = rs.getLong("myCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	public Long getDashboardI2eCompletedCount(Connection conn, String orgName, Long auditId){
		Long count = 0L;
		Statement st = null;
		ResultSet rs = null;
		String sql = null;

		if(!orgName.equalsIgnoreCase(NON_NCI)){
			sql = "select count(*) as myCount from em_i2e_audit_accounts_vw where submitted_by is not null and parent_ned_org_path = '" + orgName+ "' and audit_id = " + auditId;
		}else{
			sql = "select count(*) as myCount from em_i2e_audit_accounts_vw where submitted_by is not null and NCI_DOC = 'OTHER' and ned_ic <> 'NCI' and audit_id = " + auditId;
		}
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			count = rs.getLong("myCount");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				st.close();
				rs.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	public static void main(String[] args) throws Exception{
		DatabaseManager dm = new DatabaseManager();
		List<String> myOrgs = dm.getAllOrganization(116L);
		for(String s : myOrgs){
			System.out.println(s);
		}
		System.out.println("=======Finish=======");
	}
}
