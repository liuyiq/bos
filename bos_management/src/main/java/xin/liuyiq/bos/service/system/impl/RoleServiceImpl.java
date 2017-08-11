package xin.liuyiq.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.system.MenuRepository;
import xin.liuyiq.bos.dao.system.PermissionRepository;
import xin.liuyiq.bos.dao.system.RoleRepository;
import xin.liuyiq.bos.domain.system.Menu;
import xin.liuyiq.bos.domain.system.Permission;
import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.PermissionService;
import xin.liuyiq.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	@Override
	public List<Role> findByUser(User user) {
		if(user.getType().intValue() == 1){
			return roleRepository.findAll();
		}else{
			return roleRepository.findByName(user.getId());		
		}
		
	}

	@Override
	public List<Role> query() {
		return roleRepository.findAll();
	}

	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		roleRepository.save(role);
		
		if(permissionIds!= null && permissionIds.length != 0){
			for (String permsId : permissionIds) {
				Permission permission = permissionRepository.findOne(Integer.parseInt(permsId));
				role.getPermissions().add(permission);
			}
		}
		
		if(StringUtils.isNotBlank(menuIds)){
			for (String menuId : menuIds.split(",")) {
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				role.getMenus().add(menu);
			}
		}
	}

}
