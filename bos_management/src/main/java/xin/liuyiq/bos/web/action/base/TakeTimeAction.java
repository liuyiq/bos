package xin.liuyiq.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.base.TakeTime;
import xin.liuyiq.bos.service.base.TakeTimeService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime>{

    @Autowired
    private TakeTimeService takeTime;

    //takeTime_pageQuery
    @Action(value = "takeTime_pageQuery",results = {@Result(name = SUCCESS,type = "json")})
    public String pageQuery(){
        // 创建Pageable
        Pageable pageable = new PageRequest(page-1,rows);
        Page<TakeTime> page = takeTime.findAll(pageable);
        responseJsonDataToPage(page);
        return SUCCESS;
    };

    // taketime_findAll
    @Action(value = "taketime_findAll",results = {@Result(name = SUCCESS,type = "json")})
    public String findAll(){
        List<TakeTime> list = takeTime.findAll();
        pushObjectToValueStack(list);
        return SUCCESS;
    };
}
