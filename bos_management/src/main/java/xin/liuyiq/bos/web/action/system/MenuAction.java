package xin.liuyiq.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.system.Menu;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.MenuService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu>{

    @Autowired
    private MenuService menuService;
    //menu_save
    @Action(value = "menu_query",results = {@Result(name = "success",type = "json")})
    public String query(){
        List<Menu> menus = menuService.query();
        pushObjectToValueStack(menus);
        return SUCCESS;
    }

    @Action(value = "menu_save",results = {@Result(name = "success",type = "redirect",location = "./pages/system/menu.html")})
    public String save(){
        menuService.save(model);
        return SUCCESS;
    }
    
    // menu_showmenu
    @Action(value = "menu_showmenu",results = {@Result(name = "success",type = "json")})
    public String showMenu(){
    	Subject subject = SecurityUtils.getSubject();
    	User user = (User) subject.getPrincipal();
    	List<Menu> menus = menuService.findByUser(user);
    	pushObjectToValueStack(menus);
    	return SUCCESS;
    }
}
