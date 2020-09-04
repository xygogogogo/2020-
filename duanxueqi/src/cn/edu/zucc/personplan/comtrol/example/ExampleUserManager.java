package cn.edu.zucc.personplan.comtrol.example;

import java.sql.SQLException;
import java.util.Date;

import com.mysql.jdbc.Connection;

import cn.edu.zucc.personplan.itf.IUserManager;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.BusinessException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

public class ExampleUserManager implements IUserManager {

	@Override
	public BeanUser reg(String userid, String pwd,String pwd2) throws BaseException {
		// TODO Auto-generated method stub
		if(userid==null || "".equals(userid)) throw new BusinessException("账号不能为空");
		if(pwd==null || "".equals(pwd)) throw new BusinessException("密码不能为空");
		if(!pwd.equals(pwd2)) throw new BusinessException("两次输入密码不一致");
		java.sql.Connection conn=null;
		//检查用户是否存在
		try {
			conn=DBUtil.getConnection();
			String sql = "select user_id from tbl_user where user_id = ?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid); 
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) {
				rs.close();
				pst.close();
				throw new BusinessException("用户已经存在");
			}
			rs.close();
			pst.close();
			sql = "insert into tbl_user(user_id,user_pwd,register_time) values(?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, pwd);
			pst.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
			pst.execute();
			BeanUser user = new BeanUser();
			user.setRegister_time(new Date(0));
			user.setUser_id(userid);
			return user;
			
		}catch(SQLException ex) {
			throw new DbException(ex);
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		//return null;
	}

	
	@Override
	public BeanUser login(String userid, String pwd) throws BaseException {
		// TODO Auto-generated method stub
		java.sql.Connection conn=null;
		BeanUser user=null;
		if(pwd==null) {
			
		}
		try {
			conn=DBUtil.getConnection();
			String sql = "select register_time "
					+ " from tbl_user where user_id = ? and user_pwd = ?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
			pst.setString(2, pwd);
			java.sql.ResultSet rs=pst.executeQuery();
			if(rs.next()) {
				
				user=new BeanUser();
				user.setUser_id(userid);
				user.setRegister_time(rs.getTimestamp(1));
				return user;	
			}else {
				throw new BusinessException("用户不存在或密码错误");
			}
		}catch(SQLException ex) {
			throw new DbException(ex);
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		//return null;
	}


	@Override
	public void changePwd(BeanUser user, String oldPwd, String newPwd,
			String newPwd2) throws BaseException {
		// TODO Auto-generated method stub
		
	}

}
