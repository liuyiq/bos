package xin.liuyiq.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.service.system.RoleService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction extends BaseAction<Role>{

    @Autowired
    private RoleService roleService ;
    
    //role_query
    @Action(value = "role_query",results = {@Result(name = "success",type = "json")})
    public String query(){
    	List<Role> roles = roleService.query();
    	pushObjectToValueStack(roles);
        return SUCCESS;
    }
    
    private String[] permissionIds;
    private String menuIds;
    
    public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	@Action(value = "role_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/system/role.html") })
	public String save() {
		
    	roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
