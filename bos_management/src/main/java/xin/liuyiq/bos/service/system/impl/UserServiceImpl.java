package xin.liuyiq.bos.service.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.system.RoleRepository;
import xin.liuyiq.bos.dao.system.UserRepository;
import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private RoleRepository roleRepository;
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	@Override
	public List<User> query() {
		return userRepository.findAll();
	}
	@Override
	public void save(User user, String[] roleIds) {
		userRepository.save(user);
		if(roleIds != null && roleIds.length != 0){
			for (String roleId : roleIds) {
				Role role = roleRepository.findOne(Integer.parseInt(roleId));
				user.getRoles().add(role);
			}
		}
	}

}
