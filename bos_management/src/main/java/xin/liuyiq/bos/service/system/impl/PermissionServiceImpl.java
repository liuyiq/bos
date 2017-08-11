package xin.liuyiq.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.system.PermissionRepository;
import xin.liuyiq.bos.domain.system.Permission;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService{

	@Autowired
	private PermissionRepository permissionRepository;
	@Override
	public List<Permission> findByUser(User user) {
		if(user.getType().intValue() == 1){
			return permissionRepository.findAll();
		}else{
			return permissionRepository.findByUser(user.getId());
		}
		
	}
	@Override
	public List<Permission> query() {
		return permissionRepository.findAll();
	}
	@Override
	public void save(Permission permission) {
		permissionRepository.save(permission);
	}

}
