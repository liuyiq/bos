package xin.liuyiq.bos.service.system;

import java.util.List;

import xin.liuyiq.bos.domain.system.Menu;
import xin.liuyiq.bos.domain.system.User;

public interface MenuService {

    public List<Menu> query();

    public void save(Menu model);

	public List<Menu> findByUser(User user);
}
