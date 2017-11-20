package seehope.service;

import seehope.model.ResultModel;
import seehope.model.UserModel;

public interface IUserService {

	/**
	 * 根据用户名取得用户
	 */
	ResultModel getUserByUsername(String username);
	
	/**
	 * 删除用户
	 * @param user
	 * @return
	 */
	ResultModel deleteUser(String[] ids);

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	ResultModel addUser(UserModel user);

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	ResultModel editUser(UserModel user);

	/**
	 * 根据用户ID获取用户
	 * @param id
	 * @return
	 */
	ResultModel getUserById(String id);

	/**
	 * 获取所有的用户数据
	 * @param id
	 * @return
	 */
	ResultModel getAllUser();

	/**
	 * 根据账号或者密码获取用户
	 * @param username
	 * @param password
	 * @return
	 */
	UserModel getUserModel(String username, String password);

	ResultModel setRole(String id, String role);

	ResultModel setStatus(String id, String status);

	ResultModel setPassword(String id, String oldp, String newp);

}
