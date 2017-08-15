package xin.liuyiq.bos.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import xin.liuyiq.bos.service.take_delivery.WayBillService;

public class WayBillJob implements Job{
	
	@Autowired
	private WayBillService wayBillService;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		wayBillService.syncIndex();
	}

}
