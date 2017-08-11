package xin.liuyiq.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import xin.liuyiq.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer>,JpaSpecificationExecutor<Standard>{
	
}
