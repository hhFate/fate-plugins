package cn.reinforce.utils;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

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
