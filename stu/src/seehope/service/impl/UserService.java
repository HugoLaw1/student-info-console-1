package seehope.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import seehope.dao.UserDao;
import seehope.model.ResultModel;
import seehope.model.UserModel;
import seehope.security.SecurityContext;
import seehope.service.IUserService;

public class UserService implements IUserService {

	
	
	
	@Override
	public ResultModel addUser(UserModel user) {
		user.setId(UUID.randomUUID().toString());
		user.setPassword(SecurityContext.encryptPassword(user.getUsername()));
		if (!UserDao.addUser(user)) {
			return new ResultModel("000", "新增用户失败！");
		}
		return new ResultModel("001", "新增用户成功！",user);
	}

	@Override
	public ResultModel getUserById(String id) {
		UserModel user = UserDao.getUserById(id);
		ResultModel result = null;
		if(user!=null){
			result = new ResultModel("001", user);
		}else{
			result = new ResultModel("000", user);
		}
		return result;
	}

	@Override
	public ResultModel getAllUser() {
		List<UserModel> users = UserDao.getUsers();
		return new ResultModel("001", users);
	}

	public List<Map> getAllUserByPage(Map param) {
		return UserDao.getUsersMap(param);
	}

	public int getAllUserByPageCount(Map param) {
		return UserDao.getUsersMapCount(param);
	}

	@Override
	public UserModel getUserModel(String username, String password) {
		return UserDao.getUser(username, SecurityContext.encryptPassword(password));
	}

	@Override
	public ResultModel editUser(UserModel user) {
		boolean result = UserDao.updateUser(user);
		if (!result) {
			return new ResultModel("000", "修改用户失败！");
		}
		return new ResultModel("001", "修改用户成功！",user);
	}

	@Override
	public ResultModel deleteUser(String[] ids) {
		if(UserDao.deleteUser(ids)){
			return new ResultModel("001","删除成功！");
		}
		return new ResultModel("000","删除失败！");
	}

	@Override
	public ResultModel setRole(String id, String role) {
		boolean result = UserDao.setRole(id,role);
		if (!result) {
			return new ResultModel("000", "修改用户角色失败！");
		}
		return new ResultModel("001", "修改用户角色成功！");
	}

	@Override
	public ResultModel setStatus(String id, String status) {
		boolean result = UserDao.updateUserStatus(id,status);
		if (!result) {
			return new ResultModel("000", "操作失败！");
		}
		return new ResultModel("001", "操作成功！");
	}

	@Override
	public ResultModel setPassword(String id, String oldp, String newp) {
//		System.out.println(id);
//		System.out.println(oldp);
//		System.out.println(newp);
		boolean result = UserDao.isOldp(id, oldp);
		if(!result){
			return new ResultModel("000","原先密码错误！");
		}
		result = UserDao.updateUserPassword(id, newp);
		if(!result){
			return new ResultModel("000","操作失败！");
		}
		return new ResultModel("001", "操作成功！");
	}

	@Override
	public ResultModel getUserByUsername(String username) {
		
		return null;
	}
	
	
}
