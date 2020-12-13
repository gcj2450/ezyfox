package com.tvd12.ezyfox.binding.testing;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.binding.annotation.EzyReader;
import com.tvd12.ezyfox.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfox.binding.testing.scan2.Scan2Object;
import com.tvd12.ezyfox.binding.testing.scan2.Scan2ObjectReader;
import com.tvd12.ezyfox.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class EzyArrayReaderBuilderTest extends BaseTest {

	@Test
	public void test() {
		EzyArrayReaderBuilder builder 
			= new EzyArrayReaderBuilder(new EzyClass(ClassA.class));
		builder.build();
	}
	
	@Test
	public void testConstructorCase() {
		EzyArrayReaderBuilder.setDebug(true);
		EzyArrayReaderBuilder builder = new EzyArrayReaderBuilder(new EzyClass(ClassB.class));
		builder.build();
	}
	
	@Test
	public void testConstructorNoFieldsCase() {
		EzyArrayReaderBuilder.setDebug(true);
		EzyArrayReaderBuilder builder = new EzyArrayReaderBuilder(new EzyClass(ClassC.class));
		builder.build();
	}

	@SuppressWarnings("rawtypes")
	public static class ClassA {
		public Map map = new HashMap<>();
		@EzyReader(Scan2ObjectReader.class)
		public Scan2Object object = new Scan2Object();
		public ClassB classB;
		
		public void setValue(String value) {
			
		}
	}
	
	@SuppressWarnings({ "rawtypes", "serial" })
	public static class ClassB extends HashMap {
		
	}
	
	@Getter
	@AllArgsConstructor
	public static class ClassC {
		private String name;
		private String value;
	}
	
	@Getter
	public static class ClassD {
		public ClassD(
				boolean booleanValue,
				byte byteValue,
				char charValue,
				double doubleValue,
				float floatValue,
				int intValue,
				long longValue,
				short shortValue,
				String stringValue) {}
	}
	
}
