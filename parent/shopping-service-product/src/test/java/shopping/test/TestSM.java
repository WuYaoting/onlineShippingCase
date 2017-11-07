package shopping.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wyt.shopping.mapper.test.UserTestMapper;
import com.wyt.shopping.pojo.test.UserTest;
import com.wyt.shopping.service.test.UserTestService;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSM {

	@Resource
	private UserTestMapper userTestMapper;
	@Resource
	private UserTestService userTestService;
	
	@Test
	public void testMybatis(){
		UserTest userTest = new UserTest();
		userTest.setName("liudehuaMybatis");
		userTest.setBirthday(new Date());
		userTestMapper.insertUser(userTest);
	}
	
	@Test
	public void testSpring(){
		UserTest userTest = new UserTest();
		userTest.setName("liudehuaSpring在测试");
		userTest.setBirthday(new Date());
		userTestService.insertUser(userTest);
	}
	
	
}
