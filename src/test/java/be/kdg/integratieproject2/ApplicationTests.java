package be.kdg.integratieproject2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/*
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"testcontext.xml"})
*/

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = {"classpath*:testcontext.xml"})
public class ApplicationTests {

	@Test
	public void simpleTest() {
		assertThat("lol" , is("lol"));

	}

}
