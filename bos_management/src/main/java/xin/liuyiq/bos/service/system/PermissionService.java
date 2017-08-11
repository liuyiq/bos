package xin.liuyiq.bos.service.system;

import java.util.List;

import xin.liuyiq.bos.domain.system.Permission;
import xin.liuyiq.bos.domain.system.User;

public interface PermissionService {

	public List<Permission> findByUser(User user);

	public List<Permission> query();

	public void save(Permission model);
	
}
