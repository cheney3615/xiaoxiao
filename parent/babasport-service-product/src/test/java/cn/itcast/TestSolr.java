package cn.itcast;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试solr
 * 
 * @author Administrator
 *
 * Junit + Spring
 * 
 * @author Administrator 这样就不用写代码来加载applicationContext.xml和getBean了
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestSolr {
	
	@Autowired
	private HttpSolrServer solrServer;

	/**
	 * 创建索引 java 传统方式
	 * 
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@Test
	public void createIndex1() throws SolrServerException, IOException {

		// 创建httpsolr服务器对象
		HttpSolrServer httpSolrServer = new HttpSolrServer(
				"http://192.168.56.101:8080/solr/collection1");

		// 创建字段
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "1");
		document.addField("name_ik", "范冰冰白富美");

		httpSolrServer.add(document);
		httpSolrServer.commit();
	}
	
	/**
	 * 创建索引  使用spring方式
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	@Test
	public void createIndex2() throws SolrServerException, IOException {
		// 创建字段
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "2");
		document.addField("name_ik", "范冰冰白富美222");

		solrServer.add(document);
		solrServer.commit();
	}

}
