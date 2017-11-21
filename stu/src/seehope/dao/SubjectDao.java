package seehope.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import seehope.db.DBUtil;
import seehope.model.SubjectModel;

public class SubjectDao {
	/**
	 * 更新学生数据
	 */
	public static String editSubject(SubjectModel subject){
		String result ="002";
		
		if(!SubjectDao.hasSubjectByName(subject.getSub_pre())){
			return result="004";
		}
		
		String sql="UPDATE sec_subject set sub_name=?,sub_hours=?,sub_weight=?,sub_pre=?,sub_remark=?,sub_last_update_time=SYSDATE()  where sub_id=?; ";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, subject.getSub_name());
			stmt.setFloat(2, subject.getSub_hours());
			stmt.setFloat(3, subject.getSub_weight());
			stmt.setString(4, subject.getSub_pre());
			stmt.setString(5, subject.getSub_remark());
			stmt.setString(6, subject.getSub_id());
			int c = stmt.executeUpdate();
			if(c<0){
				conn.rollback();
				conn.commit();
			}
			result = "001";
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获取学生
	 */
	public static SubjectModel getSubjectById(String sub_id){
		String sql = "SELECT * FROM SEC_subject WHERE sub_id=?";
		Connection conn = DBUtil.getInstance().getConnection();
		SubjectModel subject = new SubjectModel();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sub_id );
			rs = stmt.executeQuery();
			while(rs.next()){
				String sub_name = rs.getString("sub_name");
				Float sub_hours = rs.getFloat("sub_hours");
				Float sub_weight = rs.getFloat("sub_weight");
				String sub_pre = rs.getString("sub_pre");
				String sub_remark = rs.getString("sub_remark");
				
				subject.setSub_id(sub_id);
				subject.setSub_name(sub_name);
				subject.setSub_hours(sub_hours);
				subject.setSub_weight(sub_weight);
				subject.setSub_pre(sub_pre);
				subject.setSub_remark(sub_remark);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return subject;
	}

	/**
	 * 删除学生
	 */
	public static boolean deleteSubject(String[] sub_ids) {
		boolean result = true;
		String sql = "DELETE FROM SEC_subject WHERE sub_id = ?";
		try {
			Connection conn = DBUtil.getInstance().getConnection();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String id : sub_ids) {
				stmt.setString(1, id);
				stmt.addBatch();
			}
			int[] c = stmt.executeBatch();
			conn.commit();
			System.out.println(c);
			for(int cc :c){
				if(cc <0){
					conn.rollback();
					conn.commit();
					result =false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 增添学生
	 */
	public static String addSubject(SubjectModel subject) {
		String result = "002";
//		if (SubjectDao.hasSubject(subject.getSub_id())) {
//			return result = "003";
//		}
//		if(SubjectDao.hasSubjectByName(subject.getSub_name())){
//			return result = "004";
//		
		if(!SubjectDao.hasSubjectByName(subject.getSub_pre())){
			return result = "004";
		}
		
		String sql = "INSERT INTO SEC_subject (sub_id,sub_name,sub_hours,sub_weight,sub_pre,sub_remark,sub_created_time,sub_last_update_time) VALUES(?,?,?,?,?,?,SYSDATE(),SYSDATE())";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, subject.getSub_id());
			stmt.setString(2, subject.getSub_name());
			stmt.setFloat(3, subject.getSub_hours());
			stmt.setFloat(4, subject.getSub_weight());
			stmt.setString(5, subject.getSub_pre());
			stmt.setString(6, subject.getSub_remark());
			int c = stmt.executeUpdate();
			if (c > 0) {
				result = "001";
				conn.commit();
			} else {
				conn.rollback();
				conn.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查询学生是否存在
	 */
	public static boolean hasSubject(String sub_id) {
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_subject U WHERE U.sub_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sub_id);
			rs = stmt.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return false;
	}
	
	public static boolean hasSubjectByName(String sub_name) {
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_subject U WHERE U.sub_name = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sub_name);
			rs = stmt.executeQuery();
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return false;
	}
	
	
	/**
	 * 获取所有的用户数据总量
	 * 
	 * @return
	 */
	public static int getSubjectMapCount(Map param) {
		int count = 0;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT COUNT(1) FROM sec_subject U";
		String sSearch = (String) param.get("sSearch");
		PreparedStatement stmt = null;
		ResultSet rs = null;

		/**
		 * 分离key
		 */
		String kc = (String) param.get("key");
		String[] two = null;
		String key = null;
		String content = null;
		if (kc != null) {
			two = kc.split(",");
			key = two[0];
			content = two[1];
		}
		try {
			stmt = conn.prepareStatement(sql);
			if (key != null && !key.trim().equals("") && content != null && !content.trim().equals("")
					&& content.trim().equals("name")) {
				sql = "select count(1) from sec_subject where sub_name=?;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
			} else if (key != null && !key.trim().equals("") && content != null && !content.trim().equals("")
					&& content.trim().equals("id")) {
				sql = "SELECT COUNT(1) FROM sec_subject WHERE sub_id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return count;
	}

	/**
	 * 获取所有的用户数据
	 * 
	 * @return
	 */
	public static List<Map> getSubjectMap(Map param) {
		List<Map> list = new LinkedList<Map>();
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * from (SELECT *,@rownum:=@rownum+1 as rownum2 FROM (SELECT U.* FROM SEC_subject U) A,(select @rownum:=0) R) A where A.rownum2>? and A.rownum2<?;";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer start = (Integer) param.get("start");
		Integer pageSize = (Integer) param.get("pageSize");
		String sSearch = (String) param.get("sSearch");
		/**
		 * 分离key
		 */
		String kc = (String) param.get("key");
		String[] two = null;
		String key = null;
		String content = null;
		if (kc != null) {
			two = kc.split(",");
			key = two[0];
			content = two[1];
		}
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, start + pageSize + 1);
			if (key != null && !key.trim().equals("") && content != null && !content.trim().equals("")
					&& content.trim().equals("name")) {
				sql = "SELECT * from (SELECT *,@rownum:=@rownum+1 as rownum2 FROM(SELECT U.* FROM SEC_subject U where sub_name=?) A,(select @rownum:=0) R) A where A.rownum2>? and A.rownum2<?;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
				stmt.setInt(2, start);
				stmt.setInt(3, start + pageSize + 1);
			} else if (key != null && !key.trim().equals("") && content != null && !content.trim().equals("")
					&& content.trim().equals("id")) {
				sql = "SELECT * from (SELECT *,@rownum:=@rownum+1 as rownum2 FROM (SELECT U.* FROM SEC_subject U where sub_id = ?) A,(select @rownum:=0) R) A where A.rownum2>? and A.rownum2<?;";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
				stmt.setInt(2, start);
				stmt.setInt(3, start + pageSize + 1);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String sub_id = rs.getString("sub_id");
				String sub_name = rs.getString("sub_name");
				float sub_hours = rs.getFloat("sub_hours");
				float sub_weight = rs.getFloat("sub_weight");
				String sub_pre = rs.getString("sub_pre");
				String sub_remark = rs.getString("sub_remark");
				Date sub_createdTime = rs.getDate("sub_CREATED_TIME");
				Date sub_lastUpdateTime = rs.getDate("sub_LAST_UPDATE_TIME");
				Map m = new HashedMap();
				m.put("sub_id", sub_id);
				m.put("sub_name", sub_name);
				m.put("sub_hours", sub_hours);
				m.put("sub_weight", sub_weight);
				m.put("sub_pre", sub_pre);
				m.put("sub_remark", sub_remark);
				m.put("sub_createdTime", sub_createdTime);
				m.put("sub_lastUpdateTime", sub_lastUpdateTime);
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return list;
	}
}
