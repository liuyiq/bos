package xin.liuyiq.bos.realm;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xin.liuyiq.bos.domain.system.Permission;
import xin.liuyiq.bos.domain.system.Role;
import xin.liuyiq.bos.domain.system.User;
import xin.liuyiq.bos.service.system.PermissionService;
import xin.liuyiq.bos.service.system.RoleService;
import xin.liuyiq.bos.service.system.UserService;

@Component("bosRealm")
public class BosRealm extends AuthorizingRealm{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private  PermissionService permissionService;
	@Override
	// 认证
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 这里面放了user的登录信息
		UsernamePasswordToken usernamePasswordToken =  (UsernamePasswordToken) token;
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if(user == null){
			// 没有获取到user,用户名密码错误
			return null;
		}else{
			// 取到后和第二个参数 做比较
			return new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
		}
		
	}
	@Override
	// 授权
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		// 查询的是角色
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			simpleAuthorizationInfo.addRole(role.getKeyword());
		}
		
		// 查询的是权限
		List<Permission> perms = permissionService.findByUser(user);
		for (Permission permission : perms) {
			simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
		}
		
		
		return simpleAuthorizationInfo;
	}
}
