package cn.itcast.core.dao;

import cn.itcast.core.pojo.TestTb;

/**
 * 测试实体类的DAO
 * @author Administrator
 *
 */
public interface TestTbDAO {
	
	/**
	 * 添加测试类到数据库中
	 * @param testTb
	 */
	public void addTestTb(TestTb testTb);
	
}
