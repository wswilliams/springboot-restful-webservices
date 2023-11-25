package net.wssouza.springboot;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		UserControllerTest.class,
		// Outras classes de teste aqui
})
//@SpringBootTest
class SpringbootRestfulWebservicesApplicationTests {

	@BeforeClass
	public static void setup() {
		try {
			UserControllerTest.testUploadFile();
			UserControllerTest.testGetUserById();
			UserControllerTest.testSearch();
			UserControllerTest.testGetAllUsers();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
