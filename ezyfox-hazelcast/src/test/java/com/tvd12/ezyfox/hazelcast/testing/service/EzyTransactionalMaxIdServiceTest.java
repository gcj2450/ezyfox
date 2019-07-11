package com.tvd12.ezyfox.hazelcast.testing.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.TransactionalMap;
import com.tvd12.ezyfox.function.EzyExceptionFunction;
import com.tvd12.ezyfox.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezyfox.hazelcast.service.EzyTransactionalMaxIdService;
import com.tvd12.ezyfox.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezyfox.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezyfox.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezyfox.hazelcast.transaction.EzyTransactionOptions;

public class EzyTransactionalMaxIdServiceTest extends HazelcastBaseTest {

	@Test
	public void test() throws Exception {
		final EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		
		List<Long> nums = new ArrayList<>();
		Thread[] threads = new Thread[1000];
		for(int i = 0 ; i < threads.length ; ++i) {
			threads[i] = new Thread(() -> {
				nums.add(service.incrementAndGet("something"));
			});
		}
		for(int i = 0 ; i < threads.length ; ++i) {
			threads[i].start();
//			threads[i].join();
		}
		
		Thread.sleep(2000L);
		
		System.out.println(nums);
		for(int i = 0 ; i < nums.size() - 1 ; ++i) {
			if(nums.get(i + 1) != nums.get(i) + 1) {
				System.err.println("transaction xxx: error in " + i);
			}
		}
		
	}
	
	@Test
	public void test11() throws Exception {
		final EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(HZ_INSTANCE);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		
		List<Long> nums = new ArrayList<>();
		Thread[] threads = new Thread[1000];
		for(int i = 0 ; i < threads.length ; ++i) {
			threads[i] = new Thread(() -> {
				nums.add(service.incrementAndGet("somethingx", 2));
			});
		}
		for(int i = 0 ; i < threads.length ; ++i) {
			threads[i].start();
//			threads[i].join();
		}
		
		Thread.sleep(2000L);
		
		System.out.println(nums);
		for(int i = 0 ; i < nums.size() - 1 ; ++i) {
			if(nums.get(i + 1) != nums.get(i) + 2) {
				System.err.println("transaction yyy: error in " + i);
			}
		}
		
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test2() {
		EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService();
		service.setHazelcastInstance(HZ_INSTANCE);
		service.setMapTransactionFactory(new EzyMapTransactionFactory() {
			
			@Override
			public <K, V, R> EzyMapReturnTransaction<K, V, R> newReturnTransaction(String mapName,
					EzyTransactionOptions options) {
				return new EzyMapReturnTransaction<K, V, R>() {
					@Override
					public R apply(EzyExceptionFunction<TransactionalMap<K, V>, R> func) throws Exception {
						throw new RuntimeException();
					}

					@Override
					public void begin() {
					}

					@Override
					public void commit() {
					}

					@Override
					public void rollback() {
					}
				};
			}
			
			@Override
			public <K, V> EzyMapApplyTransaction<K, V> newApplyTransaction(String mapName, EzyTransactionOptions options) {
				return null;
			}
		});
		service.incrementAndGet("d");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test3() {
		IMap map = mock(IMap.class);
		HazelcastInstance hzInstance = mock(HazelcastInstance.class);
		when(hzInstance.getMap(anyString())).thenReturn(map);
		EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(hzInstance);
		service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
		service.loadAll();
	}
	
}
