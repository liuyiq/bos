package xin.liuyiq.bos.service.system;

import java.util.List;

import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.domain.system.User;

public interface RoleService {

	public List<Role> findByUser(User user);

	public List<Role> query();

	public void save(Role model, String[] permissionIds, String menuIds);
}
