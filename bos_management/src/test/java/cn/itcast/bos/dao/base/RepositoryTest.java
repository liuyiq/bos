package cn.itcast.bos.dao.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.StandardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class RepositoryTest {
	@Autowired
	private StandardRepository standardDao;
	
	@Test
	@Transactional
	@Rollback(false)
	public void saveTest(){
		// System.out.println(standardDao.findByName("10-20公斤"));
		// System.out.println(standardDao.findByMinWeight(10));
		// System.out.println(standardDao.findByMinWeightOrMaxWeight(10, 30));
		// System.out.println(standardDao.queryAll(1));
		// System.out.println(standardDao.queryById(1));
		// System.out.println(standardDao.queryById2(1));
		// standardDao.updateMinLength(8, 1);	
	}
}
