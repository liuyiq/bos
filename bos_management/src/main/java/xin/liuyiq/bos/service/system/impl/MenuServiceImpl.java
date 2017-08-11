package xin.liuyiq.bos.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.system.MenuRepository;
import xin.liuyiq.bos.domain.system.Menu;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Override
    @CacheEvict(value="menu",allEntries=true)
    public List<Menu> query() {
        return menuRepository.findAll();
    }
    @Override
    public void save(Menu menu) {
    	// 如果页面上的父菜单项没有选中,会发生瞬时对象异常
    	// 有两种解决办法,一个是加下面的判断把ParentMenu设置为null
    	// 或者是在页面上加校验
    	
    	if(menu.getParentMenu() != null && menu.getParentMenu().getId() == 0){
    		menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }

	@Override
	@Cacheable(value="menu")
	public List<Menu> findByUser(User user) {
		if(user.getType().intValue() == 1){
			return menuRepository.findAll();
		}else{
			return menuRepository.findByUser(user.getId());
		}
	}
}
