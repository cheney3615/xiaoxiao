package cn.itcast;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.dao.TestTbDAO;
import cn.itcast.core.pojo.TestTb;

/**
 * Junit + Spring
 * 
 * @author Administrator
 * 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class TestTbTest {

	@Autowired
	private TestTbDAO testTbDAO;
	
	/**
	 * 测试添加  alt + shift + j
	 */
	@Test
	public void testAdd() {
		
		TestTb testTb = new TestTb();
		testTb.setName("范冰冰");
		testTb.setBirthday(new Date());
		
		
		testTbDAO.addTestTb(testTb);

	}

}
