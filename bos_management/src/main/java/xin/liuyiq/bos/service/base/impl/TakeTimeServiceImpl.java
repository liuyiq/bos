package xin.liuyiq.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.liuyiq.bos.dao.base.TakeTimeRepository;
import xin.liuyiq.bos.domain.base.TakeTime;
import xin.liuyiq.bos.service.base.TakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

    @Autowired
    private TakeTimeRepository takeTimeRepository;

    @Override
    public Page<TakeTime> findAll(Pageable pageable) {
        return takeTimeRepository.findAll(pageable);
    }

    @Override
    public List<TakeTime> findAll() {
        List<TakeTime> list = takeTimeRepository.findAll();
        return list;
    }
}
