package com.salessparrow.api.unit.globalConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import com.salessparrow.api.lib.globalConstants.UserConstants;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserConstantsTest {

	@Test
	void testGetName() {
		assertEquals("SALESFORCE", UserConstants.SALESFORCE_USER_KIND);
	}

	@Test
	void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
		Constructor<UserConstants> constructor = UserConstants.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		assertThrows(InvocationTargetException.class, constructor::newInstance);
	}

}
