package xin.liuyiq.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.UserService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User>{
	
	@Autowired
	private UserService userService;
	
    @Action(value = "user_login",results = {@Result(name = SUCCESS,type = "redirect",location = "index.html"),@Result(name = LOGIN,type = "redirect",location = "login.html")})
    public String login(){
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
        
        try {
			subject.login(token);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return LOGIN;
		}
    };
    // user_logout
    @Action(value = "user_logout",results = {
    		@Result(name = LOGIN,type = "redirect",location = "login.html")})
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return LOGIN;
    };
    
    // user_query
    @Action(value = "user_query",results = {@Result(name = "success",type = "json")})
    public String query(){
    	List<User> users = userService.query();
    	pushObjectToValueStack(users);
    	return SUCCESS;
    }
    // user_save
    private String[] roleIds;
    
    public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	@Action(value = "user_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/system/userlist.html") })
	public String save() {
		userService.save(model,roleIds);
		return SUCCESS;
	}
}
