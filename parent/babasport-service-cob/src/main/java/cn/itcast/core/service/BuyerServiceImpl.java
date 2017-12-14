package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;

import cn.itcast.core.dao.BuyerDAO;
import cn.itcast.core.pojo.Buyer;

/**
 * @author Administrator 用户服务实现类
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDAO buyerDAO;

	@Override
	public Buyer findByUsername(String username) {

		Buyer buyer = new Buyer();
		buyer.setUsername(username);

		// 返回唯一结果
		return buyerDAO.selectOne(buyer);
	}

}
