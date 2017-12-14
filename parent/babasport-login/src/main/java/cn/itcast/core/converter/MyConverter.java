package cn.itcast.core.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 自定义转换器
 * @author Administrator
 * 
 * @param <S>
 *            转换前的类型
 * @param <T>
 *            转换后的类型
 *
 */
public class MyConverter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		if(source!=null)
		{
			//去空格
			source = source.trim();  //"      "  ""
			//如果发现去完空格之后，不是"",就证明该参数是合法的
			if(!source.equals(""))
			{
				return source;
			}
			
		}
		return null;
	}

}
