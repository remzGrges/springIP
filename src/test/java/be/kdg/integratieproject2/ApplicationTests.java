package be.kdg.integratieproject2;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"../../../../resources/testcontest.xml"})

public class ApplicationTests {

	@Test
	public void simpleTest() {
		Assert.assertEquals(10,10);
	}

}
