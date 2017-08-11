package xin.liuyiq.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import xin.liuyiq.bos.domain.system.Permission;
import xin.liuyiq.bos.service.system.PermissionService;
import xin.liuyiq.bos.web.action.common.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PermissionAction extends BaseAction<Permission> {

	@Autowired
	private PermissionService permissionService;

	// permission_query
	@Action(value = "permission_query", results = { @Result(name = "success", type = "json") })
	public String query() {
		List<Permission> permissions = permissionService.query();
		pushObjectToValueStack(permissions);
		return SUCCESS;
	}

	@Action(value = "permission_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/system/permission.html") })
	public String save() {
		permissionService.save(model);
		return SUCCESS;
	}
}
