package cn.itcast;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.pojo.SuperPojo;

/**
 * 测试json
 * 
 * @author Administrator
 *
 */
public class TestJson {

	@Test
	public void testJson1()
			throws JsonGenerationException, JsonMappingException, IOException {

		Buyer buyer = new Buyer();
		buyer.setUsername("范冰冰");
		
		SuperPojo superPojo = new SuperPojo();
		superPojo.setProperty("username", "大毛");
		superPojo.setProperty("password", "123456");

		ObjectMapper om = new ObjectMapper();

		/*
		 * Writer w = new StringWriter();
		 * 
		 * om.writeValue(w, buyer);
		 * 
		 * System.out.println(w.toString());
		 */

		// 生成json字符串的时候忽略空
		om.setSerializationInclusion(Include.NON_NULL);
		
		//将对象转成json字符串
		String str = om.writeValueAsString(superPojo);
		System.out.println(str);
		
		//将json字符串转成对象
		//Buyer buyer2 = om.readValue(str, Buyer.class);
		SuperPojo sp = om.readValue(str, SuperPojo.class);
		
		System.out.println(sp.get("username"));
		System.out.println(sp.get("password"));

	}

}
