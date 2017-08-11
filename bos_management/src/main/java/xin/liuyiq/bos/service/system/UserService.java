package xin.liuyiq.bos.service.system;

import java.util.List;

import xin.liuyiq.bos.domain.system.User;

public interface UserService {

	public User findByUsername(String username);

	public List<User> query();

	public void save(User model, String[] roleIds);

}
