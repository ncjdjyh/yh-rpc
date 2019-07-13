package common;

import java.util.HashMap;
import java.util.Map;

public class MessageRegistry {
	private Map<String, Class<?>> clazzes = new HashMap<>();

	public void register(String signature, Class<?> clazz) {
		clazzes.put(signature, clazz);
	}

	public Class<?> get(String type) {
		return clazzes.get(type);
	}
}
