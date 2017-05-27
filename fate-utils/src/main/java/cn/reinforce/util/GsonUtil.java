package cn.reinforce.util;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * 对Gson进行简单的配置
 * 默认Date类型转换成时间戳
 * 加上@Expose的字段才有效
 * @author hhFate
 * @create 2016-08-17
 * @since 1.0.0
 */
public class GsonUtil {

	private final static Gson gson;

	private GsonUtil() {
	}

	static {
		JsonSerializer<Date> ser = (src, typeOfSrc, context)->(src ==null ? null : new JsonPrimitive(src.getTime()));

		JsonDeserializer<Date> deser = (json,  typeOfT,  context)->json == null ? null : new Date(json.getAsLong());

		gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Date.class, ser)
				.registerTypeAdapter(Date.class, deser)
				.excludeFieldsWithoutExposeAnnotation().create();
	}

	public static Gson getGson() {
		return gson;
	}

}
