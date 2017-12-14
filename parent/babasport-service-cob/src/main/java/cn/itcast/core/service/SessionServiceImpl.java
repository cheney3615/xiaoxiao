package cn.itcast.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

/**
 * session服务实现类
 *
 * 模拟官方session功能，主要是将session数据存放到redis中
 * 
 * @author Administrator
 *
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {
	
	@Autowired
	private Jedis jedis;

	@Override
	public void addUsernameToRedis(String key, String value) {
		// key是uuid（maosessionid） value是用户名
	    // 因为考虑到以后可能还有验证码，所以在key后面加个username再保存到redis中
		jedis.set(key+":username", value);
		
		// 设置失效时间 单位秒
		jedis.expire(key+":username", 1800);
	}

	@Override
	public String getUsernameForRedis(String key) {
		// TODO Auto-generated method stub
		String username = jedis.get(key+":username");
		
		if(username!=null)
		{
			//重新设置失效时间
			jedis.expire(key+":username", 1800);
		}
		return username;
	}

}
