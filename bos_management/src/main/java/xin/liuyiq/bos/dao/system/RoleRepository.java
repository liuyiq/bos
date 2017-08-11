package xin.liuyiq.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import xin.liuyiq.bos.domain.system.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	@Query("from Role r inner join fetch r.users u where u.id = ?")
	public List<Role> findByName(Integer id);


	
}
