package cn.com.minstone.eBusiness.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PushUtilTest {

	@Before
	public void setUp() throws Exception {
		PushUtil.init("oHbCrrSDZv6eQ3txFFmmj1", "mvxPwI0r1D8yf5fCl64Bv7", "EZBy8z8DC08Mr8TDTEYnu");
	}

	@Test
	public void testPushTransmissionToList() throws Exception {
		List<String> array = new ArrayList<String>();
		array.add("leader");
		PushUtil.pushTransmissionToList(array, "test");
	}
	
	@Test
	public void test1(){
		PushUtil.pushAll("Hello, I'm a test");
	}
	
	@Test
	public void stest2(){
		List<String> array = new ArrayList<String>();
		array.add("leader");
		try {
			PushUtil.pushNotifyToTagert(array, "Target test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public class Student extends Object{
		
		
		private String idcard;
		
		@SuppressWarnings("unused")
		private long time;

		public String getIdcard() {
			return idcard;
		}

		public void setIdcard(String idcard) {
			this.idcard = idcard;
		}

		@Override
		public String toString() {
			return "Student [idcard=" + idcard + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((idcard == null) ? 0 : idcard.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Student other = (Student) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (idcard == null) {
				if (other.idcard != null)
					return false;
			} else if (!idcard.equals(other.idcard))
				return false;
			return true;
		}

		private PushUtilTest getOuterType() {
			return PushUtilTest.this;
		}

		

		
		
		
	}
	
}
