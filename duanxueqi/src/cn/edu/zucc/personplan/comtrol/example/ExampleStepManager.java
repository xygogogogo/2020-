package cn.edu.zucc.personplan.comtrol.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personplan.itf.IStepManager;
import cn.edu.zucc.personplan.model.BeanPlan;
import cn.edu.zucc.personplan.model.BeanStep;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;
import cn.edu.zucc.personplan.util.BusinessException;
import cn.edu.zucc.personplan.util.DBUtil;
import cn.edu.zucc.personplan.util.DbException;

public class ExampleStepManager implements IStepManager {

	@Override
	public void add(BeanPlan plan, String name, String planstartdate,
			String planfinishdate) throws BaseException {
		
		
	}

	@Override
	public List<BeanStep> loadSteps(BeanPlan plan) throws BaseException {
		List<BeanStep> result=new ArrayList<BeanStep>();
		BeanStep p=new BeanStep();
		result.add(p);
		return result;
	}

	@Override
	public void deleteStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		int step_id = 1;
		java.sql.Connection conn = null;
		try {
			conn =DBUtil.getConnection();
			String sql="select step_id,user_id,tbl_plan.plan_id"
					+ " from tbl_step,tbl_plan "
					+ " where tbl_plan.plan_id = tbl_step.plan_id "
					+ " and step_id = "+step_id;
			java.sql.Statement st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery(sql);
			int step_order = 0;
			String plan_user_id = null;
			int plan_id=0;
			if(rs.next()) {
				step_order = rs.getInt(1);
				plan_user_id= rs.getString(2);
				plan_id = rs.getInt(3);
			}else {
				rs.close();
				st.close();
				throw new BusinessException("该步骤不存在");
			}
			rs.close();
			if(BeanUser.currentLoginUser.getUser_id().equals(plan_user_id)) {
				st.close();
				throw new BusinessException("不能删除别人的步骤");
			}		
			sql="delete from tbl_step where step_id = "+step_id;
			st.execute(sql);
			st.close();
			
			sql="update tbl_step set step_order = step_order-1 where plan_id = ? and step_order >"+step_order;
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, plan_id);;
			pst.execute();
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
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
		
		
		
	}

	@Override
	public void startStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishStep(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUp(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown(BeanStep step) throws BaseException {
		// TODO Auto-generated method stub
		
	}

}
