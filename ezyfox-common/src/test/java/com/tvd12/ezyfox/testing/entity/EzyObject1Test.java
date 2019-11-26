package com.tvd12.ezyfox.testing.entity;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.builder.EzyObjectBuilder;
import com.tvd12.ezyfox.entity.EzyHashMap;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;

import static org.testng.Assert.*;

public class EzyObject1Test extends EzyEntityTest {

	@Test
	public void test() {
	}
	
	@Test
	public void test1() {
		Map<String, Object> map = new HashMap<>();
		EzyObjectBuilder firstBuilder 
			= newObjectBuilder().append("1'", "a'");
		map.put("1", "a");
		EzyObject object = newObjectBuilder()
				.append(map)
				.append("2", firstBuilder)
				.append("3", "c")
				.append("4", "d")
				.append("5", "e")
				.build();
		assertEquals(object.get("1"), "a");
		assertEquals(object.getWithDefault("1", "b"), "a");
		assertEquals(object.get("3", String.class), "c");
		assertEquals(object.remove("4"), "d");
		assertEquals(object.compute("6", (k,v) -> v != null ? v : "f"), "f");
		assertEquals(object.size(), 5);
		assertEquals(object.isEmpty(), false);
		assertEquals(object.containsKey("6"), true);
		assertEquals(object.keySet().containsAll(Sets.newHashSet("1", "2", "3", "5", "6")), true);
		object.entrySet();
		object.toMap();
		object.toString();
		EzyObject clone = object.duplicate();
		assertEquals(object, clone);
		assertTrue(clone.keySet().containsAll(Sets.newHashSet("1", "2", "3", "5", "6")));
		object.clear();
		assertEquals(object.getWithDefault("1", "b"), "b");
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void test2() {
		EzyObject object = new EzyHashMap(null, null) {
			private static final long serialVersionUID = -4366815253239566713L;

			@Override
			public Object clone() throws CloneNotSupportedException {
				throw new CloneNotSupportedException();
			}
		};
		object.duplicate();
	}
	
	@Test
	public void equalsAndHashCodeTest() {
		EzyObject a = EzyEntityFactory.newObjectBuilder()
				.append("a", 1)
				.append("b", 2)
				.build();
		assert !a.equals(null);
		assert a.equals(a);
		assert !a.equals(new Object());
		EzyObject b = EzyEntityFactory.newObjectBuilder()
				.append("a", 1)
				.append("b", 2)
				.build();
		assert a.equals(b);
		EzyObject c = EzyEntityFactory.newObjectBuilder()
				.append("a", 1)
				.build();
		assert !a.equals(c);
	}
}
