package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.dao.BrandDAO;
import cn.itcast.core.pojo.Brand;
import cn.itcast.core.tools.PageHelper;
import cn.itcast.core.tools.PageHelper.Page;
import redis.clients.jedis.Jedis;

/**
 * 品牌管理服务实现类
 * 
 * @author Administrator
 *
 */
@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDAO brandDAO;

	@Autowired
	private Jedis jedis;

	@Override
	public Page<Brand> findByExample(Brand brand, Integer pageNum,
			Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);// 开始分页
		List<Brand> brands = brandDAO.findByExample(brand);
		Page pageBrands = PageHelper.endPage();// 结束分页

		return pageBrands;
	}

	@Override
	public Brand findById(Long brandId) {
		return brandDAO.findById(brandId);
	}

	@Override
	public void updateById(Brand brand) {
		// 将品牌信息同步修改到redis中
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());

		brandDAO.updateById(brand);
	}

	@Override
	public void deleteByIds(String ids) {
		brandDAO.deleteByIds(ids);
	}

	@Override
	public List<Brand> findAllFromRedis() {
		// 从redis中获得brand的所有数据
		Map<String, String> hgetAll = jedis.hgetAll("brand");

		Set<Entry<String, String>> entrySet = hgetAll.entrySet();

		//将map中的品牌数据 转换成  List<Brand> 
		List<Brand> brands = new ArrayList<Brand>();
		for (Entry<String, String> entry : entrySet) {
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
		return brands;
	}
}
