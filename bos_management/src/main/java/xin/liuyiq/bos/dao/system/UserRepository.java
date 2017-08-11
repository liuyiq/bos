package xin.liuyiq.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;

import xin.liuyiq.bos.domain.system.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);

}
