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
import seehope.model.UserModel;
import seehope.security.SecurityContext;
public class UserDao {
	

	/**
	 * 根据ID删除指定的用户
	 * 
	 * @param id
	 * @return
	 */
	public static boolean deleteUser(String[] ids) {
		boolean result = false;
		String sql = "DELETE FROM SEC_USER WHERE ID = ?";
		try {
			Connection conn = DBUtil.getInstance().getConnection();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String id : ids) {
				
				UserModel user = UserDao.getUserById(id);
				if(user.getUsername().equals("admin"))
					continue;
				stmt.setString(1, id);
				stmt.addBatch();
			}
			int[] c = stmt.executeBatch();
			conn.commit();
			if (c.length > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取所有的用户数据
	 * 
	 * @return
	 */
	public static List<UserModel> getUsers() {
		List<UserModel> list = new LinkedList<UserModel>();
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_USER U WHERE U.STATUS = 001";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("ID");
				String username = rs.getString("USERNAME");
				String password = rs.getString("PASSWORD");
				String sex = rs.getString("SEX");
				String phone = rs.getString("PHONE");
				String status = rs.getString("STATUS");
				String remark = rs.getString("REMARK");
				String creator = rs.getString("CREATOR");
				Date createdTime = rs.getDate("CREATED_TIME");
				String lastUpdate = rs.getString("LAST_UPDATE");
				Date lastUpdateTime = rs.getDate("LAST_UPDATE_TIME");
				String role = UserRoleMapDao.getRole(id);
				UserModel user = new UserModel(username, password);
				user.setId(id);
				user.setSex(sex);
				user.setPhone(phone);
				user.setStatus(status);
				user.setRemark(remark);
				user.setCreator(creator);
				user.setCreatedTime(createdTime);
				user.setLastUpdate(lastUpdate);
				user.setLastUpdateTime(lastUpdateTime);
				user.setRole(role);
				list.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return list;
	}

	/**
	 * 获取所有的用户数据总量
	 * 
	 * @return
	 */
	public static int getUsersMapCount(Map param) {
		int count = 0;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT COUNT(1) FROM SEC_USER U";
		String sSearch = (String) param.get("sSearch");
		String key = (String) param.get("key");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			if(key!=null&&!key.trim().equals("")&&sSearch!=null&&!sSearch.trim().equals("")){
				sql = "SELECT COUNT(1) FROM SEC_USER U LEFT JOIN SEC_URMAP M ON U.ID=M.USERID WHERE U.USERNAME=? AND M.ROLE=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
				stmt.setString(2, sSearch.trim());
			}
			else if(key!=null&&!key.trim().equals("")){
				sql = "SELECT COUNT(1) FROM SEC_USER WHERE USERNAME=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
			}
			else if(sSearch!=null&&!sSearch.trim().equals("")){
				sql = "SELECT COUNT(1) FROM SEC_USER U LEFT JOIN SEC_URMAP M ON U.ID=M.USERID WHERE M.ROLE=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, sSearch.trim());
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
	public static List<Map> getUsersMap(Map param) {
		List<Map> list = new LinkedList<Map>();
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * from (SELECT *,@rownum:=@rownum+1 as rownum2 FROM (SELECT U.*FROM SEC_USER U) A,(select @rownum:=0) R) A where A.rownum2>? and A.rownum2<?;";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer start = (Integer) param.get("start");
		Integer pageSize = (Integer) param.get("pageSize");
		String sSearch = (String) param.get("sSearch");
		String key = (String) param.get("key");
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, start);
			stmt.setInt(2, start+pageSize+1);
			if(key!=null&&!key.trim().equals("")&&sSearch!=null&&!sSearch.trim().equals("")){
				sql = "SELECT*FROM(SELECT T.*,ROW_COUNT()+2 RN FROM (SELECT * FROM SEC_USER U LEFT JOIN SEC_URMAP M ON U.ID=M.USERID WHERE U.USERNAME=? AND M.ROLE=?) T) A WHERE A.RN>? AND A.RN<?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
				stmt.setString(2, sSearch.trim());
				stmt.setInt(3, start);
				stmt.setInt(4, start+pageSize+1);
			}
			else if(key!=null&&!key.trim().equals("")){
				sql = "SELECT*FROM(SELECT T.*,ROW_COUNT()+2 RN FROM (SELECT * FROM SEC_USER WHERE USERNAME=?) T) A WHERE A.RN>? AND A.RN<?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, key.trim());
				stmt.setInt(2, start);
				stmt.setInt(3, start+pageSize+1);
			}
			else if(sSearch!=null&&!sSearch.trim().equals("")){
				sql = "SELECT*FROM(SELECT T.*,ROW_COUNT()+2 RN FROM (SELECT * FROM SEC_USER U LEFT JOIN SEC_URMAP M ON U.ID=M.USERID WHERE M.ROLE=?) T) A WHERE A.RN>? AND A.RN<?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, sSearch.trim());
				stmt.setInt(2, start);
				stmt.setInt(3, start+pageSize+1);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("ID");
				String username = rs.getString("USERNAME");
				String password = rs.getString("PASSWORD");
				String sex = rs.getString("SEX");
				String phone = rs.getString("PHONE");
				String status = rs.getString("STATUS");
				String remark = rs.getString("REMARK");
				String creator = rs.getString("CREATOR");
				Date createdTime = rs.getDate("CREATED_TIME");
				String lastUpdate = rs.getString("LAST_UPDATE");
				Date lastUpdateTime = rs.getDate("LAST_UPDATE_TIME");
				String role = UserRoleMapDao.getRole(id);
				Map m = new HashedMap();
				m.put("id", id);
				m.put("username", username);
				m.put("password", password);
				m.put("sex", sex);
				m.put("phone", phone);
				m.put("status", status);
				m.put("remark", remark);
				m.put("creator", creator);
				m.put("createdTime", createdTime);
				m.put("lastUpdate", lastUpdate);
				m.put("lastUpdateTime", lastUpdateTime);
				m.put("role", role);
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return list;
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean addUser(UserModel user) {
		boolean result = false;
		
		if(UserDao.hasUser(user.getUsername()))
			return result;
		
		
		
		String sql = "INSERT INTO SEC_USER (ID,USERNAME,PASSWORD,SEX,PHONE,STATUS,REMARK,CREATOR,CREATED_TIME,LAST_UPDATE,LAST_UPDATE_TIME)"
				+ "VALUES(?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE)";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getId());
			stmt.setString(2, user.getUsername());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getSex());
			stmt.setString(5, user.getPhone());
			stmt.setString(6, "002");
			stmt.setString(7, user.getRemark());
			stmt.setString(8, user.getCreator());
			stmt.setString(9, user.getLastUpdate());
			int c = stmt.executeUpdate();
			if (c>0){
				if(!UserRoleMapDao.addMap(user.getId())){
					String[] ids={user.getId()};
					deleteUser(ids);
					c=0;
				}
			}
			if (c > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 修改用户状态
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateUserStatus(String id,String status) {
		boolean result = false;
		String sql = "UPDATE SEC_USER SET STATUS=?,LAST_UPDATE_TIME=SYSDATE " + "WHERE ID = ?";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, id);
			int c = stmt.executeUpdate();
			if (c > 0) {
				if(status=="001")
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateUserPassword(String id,String newp) {
		boolean result = false;
		String sql = "UPDATE SEC_USER SET PASSWORD=?,LAST_UPDATE_TIME=SYSDATE WHERE ID = ?";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, SecurityContext.encryptPassword(newp));
			stmt.setString(2, id);
			int c = stmt.executeUpdate();
			if (c > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean isOldp(String id,String oldp){
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_USER U WHERE U.ID = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			while(rs.next()){
				String password = rs.getString("PASSWORD");
				if(password.equals(SecurityContext.encryptPassword(oldp)))
					return true;
				else return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return false;
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	public static boolean updateUser(UserModel user) {
		boolean result = false;
		String sql = "UPDATE SEC_USER SET SEX=?,PHONE=?,REMARK=?,LAST_UPDATE=?,LAST_UPDATE_TIME=SYSDATE"
				+ " WHERE ID = ?";
		Connection conn = DBUtil.getInstance().getConnection();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getSex());
			stmt.setString(2, user.getPhone());
			stmt.setString(3, user.getRemark());
			stmt.setString(4, user.getLastUpdate());
			stmt.setString(5, user.getId());
			int c = stmt.executeUpdate();
			if (c > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据ID获取用户
	 * 
	 * @param id
	 * @return
	 */
	public static UserModel getUserById(String id) {
		UserModel user = null;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_USER U WHERE U.ID = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString("USERNAME");
				String password = rs.getString("PASSWORD");
				String sex = rs.getString("SEX");
				String phone = rs.getString("PHONE");
				String status = rs.getString("STATUS");
				String remark = rs.getString("REMARK");
				String creator = rs.getString("CREATOR");
				Date createdTime = rs.getDate("CREATED_TIME");
				String lastUpdate = rs.getString("LAST_UPDATE");
				Date lastUpdateTime = rs.getDate("LAST_UPDATE_TIME");
				String role = UserRoleMapDao.getRole(id);
				user = new UserModel(username, password);
				user.setId(id);
				user.setSex(sex);
				user.setPhone(phone);
				user.setStatus(status);
				user.setRemark(remark);
				user.setCreator(creator);
				user.setCreatedTime(createdTime);
				user.setLastUpdate(lastUpdate);
				user.setLastUpdateTime(lastUpdateTime);
				user.setRole(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return user;
	}

	/**
	 * 根据账号和密码获取用户数据
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static UserModel getUser(String username, String password) {
		UserModel user = null;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_USER U WHERE U.USERNAME = ? AND U.PASSWORD = ? AND U.STATUS = 001";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String id = rs.getString("ID");
				String sex = rs.getString("SEX");
				String phone = rs.getString("PHONE");
				String status = rs.getString("STATUS");
				String remark = rs.getString("REMARK");
				String creator = rs.getString("CREATOR");
				Date createdTime = rs.getDate("CREATED_TIME");
				String lastUpdate = rs.getString("LAST_UPDATE");
				Date lastUpdateTime = rs.getDate("LAST_UPDATE_TIME");
				String role = UserRoleMapDao.getRole(id);
				user = new UserModel(username, password);
				user.setId(id);
				user.setSex(sex);
				user.setPhone(phone);
				user.setStatus(status);
				user.setRemark(remark);
				user.setCreator(creator);
				user.setCreatedTime(createdTime);
				user.setLastUpdate(lastUpdate);
				user.setLastUpdateTime(lastUpdateTime);
				user.setRole(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return user;
	}
	
	public static boolean hasUser(String username) {
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT * FROM SEC_USER U WHERE U.USERNAME = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return false;
	}

	public static boolean setRole(String id, String role) {
		boolean result = false;
		result = UserRoleMapDao.updateRole(id,role);
		if (result) {
			result = true;
		}
		return result;
	}

	public static boolean  hasRole(String role){
		boolean count = false;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT COUNT(1) FROM SEC_URMAP WHERE ROLE=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, role);
			rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt(1)>0)
					count=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return count;
	}
	
	public static boolean  hasSex(String sex){
		boolean count = false;
		Connection conn = DBUtil.getInstance().getConnection();
		String sql = "SELECT COUNT(1) FROM SEC_USER WHERE SEX=?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sex);
			rs = stmt.executeQuery();
			while (rs.next()) {
				if(rs.getInt(1)>0)
					count=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.getInstance().close(rs, stmt);
		}
		return count;
	}
	
	public static String getRoleContext(UserModel user){
		
		String rolename = UserRoleMapDao.getRole(user.getId());
		return RoleDao.getRole(rolename).getContext();
	}
}
