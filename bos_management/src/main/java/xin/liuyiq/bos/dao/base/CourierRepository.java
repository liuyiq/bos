package xin.liuyiq.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import xin.liuyiq.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier,Integer>,JpaSpecificationExecutor<Courier> {
	
	@Query(value = "update Courier set deltag = '1' where id = ?")
	@Modifying
	public void updateDeltag(Integer id);

	@Query(value = "update Courier set deltag = null where id = ?")
	@Modifying
	public void updateDeltagToNull(Integer id);
}
