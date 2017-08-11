package xin.liuyiq.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import xin.liuyiq.bos.domain.take_delivery.PageBean;
import xin.liuyiq.bos.domain.take_delivery.Promotion;


public interface PromotionService {

    public void save(Promotion model);

    public Page findAll(Pageable pageable);
    
    @Path("/pageQuery/{page}/{rows}")
    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public PageBean pageQuery(@PathParam("page")  int page,@PathParam("rows")  int rows);

	public void updateStatus(Date date);
    
}
