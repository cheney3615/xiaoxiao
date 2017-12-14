package cn.itcast.core.service;

/**
 * Session服务类接口
 * 
 * 模拟官方session功能，并将session数据存放到redis中
 * 
 * @author Administrator
 *
 */
public interface SessionService {

	/**
	 * 将用户名配合“maosid”存入到redis中
	 * @param key
	 * @param value
	 */
	public void addUsernameToRedis(String key,String value);
	
	/**
	 * 根据“maosid”取出redis中的用户名
	 * @param key
	 * @return
	 */
	public String getUsernameForRedis(String key);
}
