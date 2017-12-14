package cn.itcast;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.pojo.TestTb;
import cn.itcast.core.service.TestTbService;

/**
 * Junit + Spring
 * 
 * @author Administrator 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestTbServiceTest {

	@Autowired
	private TestTbService testTbService;

	/**
	 * 测试service
	 */
	@Test
	public void addTestTb() {

		TestTb testTb = new TestTb();
		testTb.setName("范冰冰8");
		testTb.setBirthday(new Date());

		testTbService.add(testTb);
	}

}
