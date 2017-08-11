package xin.liuyiq.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import xin.liuyiq.bos.domain.base.TakeTime;

public interface TakeTimeService {
	
    public Page<TakeTime> findAll(Pageable pageable);

    public List<TakeTime> findAll();
}
