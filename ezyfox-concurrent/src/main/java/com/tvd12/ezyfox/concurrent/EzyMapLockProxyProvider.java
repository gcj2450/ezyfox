package com.tvd12.ezyfox.concurrent;

import java.util.Map;
import java.util.concurrent.locks.Lock;

public abstract class EzyMapLockProxyProvider implements EzyMapLockProvider {

	protected final Map<Object, EzyLockProxy> locks;
	
	public EzyMapLockProxyProvider() {
		this.locks = newLockMap();
	}
	
	protected abstract Map<Object, EzyLockProxy> newLockMap();
	
	@Override
	public Lock provideLock(Object key) {
		synchronized (locks) {
			EzyLockProxy lock = locks.get(key);
			if(lock == null) {
				lock = new EzyLockProxy();
				locks.put(key, lock);
			}
			lock.retain();
			return lock;
		}
	}
	
	@Override
	public void removeLock(Object key) {
		synchronized (locks) {
			EzyLockProxy lock = locks.get(key);
			if(lock != null) {
				lock.release();
				if(lock.isReleasable())
					locks.remove(key);
			}
		}
	}
	
}
