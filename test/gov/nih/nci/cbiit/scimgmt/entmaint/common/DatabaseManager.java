package gov.nih.nci.cbiit.scimgmt.entmaint.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseManager {
	String url = PropertiesManager.getUrl();
	String userId = PropertiesManager.getUserId();
	String password = PropertiesManager.getPassword();
	
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
		
		if(type.equalsIgnoreCase(ApplicationConstants.ACTIVE_TYPE)){
			sql = sql_active;
		}else if(type.equalsIgnoreCase(ApplicationConstants.NEW_TYPE)){
			sql = sql_new;
		}else if(type.equalsIgnoreCase(ApplicationConstants.DELETED_TYPE)){
			sql = sql_deleted;
		}else if(type.equalsIgnoreCase(ApplicationConstants.INACTIVE_TYPE)){
			sql = sql_inactive;
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
	
	public static void main(String[] args) throws Exception{
		DatabaseManager dm = new DatabaseManager();
		List<String> myOrgs = dm.getAllOrganization(116L);
		for(String s : myOrgs){
			System.out.println(s);
		}
		System.out.println("=======Finish=======");
	}
}
