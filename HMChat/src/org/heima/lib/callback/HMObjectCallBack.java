package org.heima.lib.callback;

import java.lang.reflect.ParameterizedType;

public abstract class HMObjectCallBack<T> {
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public HMObjectCallBack() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	public abstract void onSuccess(T t);

	public abstract void onError(int error, String msg);

	public Class<T> getClazz() {
		return clazz;
	}

}
