package com.example.demo.config;



import com.example.demo.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.security.auth.login.AccountException;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MyRealm implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new MyInterceptor()).addPathPatterns("/get");
    }

    //    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        Set<String> stringSet = new HashSet<>();
//        stringSet.add("role");
//        stringSet.add("user:admin");
//        info.setStringPermissions(stringSet);
//        info.setRoles(stringSet);
//        return info;
//    }
//
//    /**
//     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码
//     * private UserService userService;
//     * <p>
//     * 获取即将需要认证的信息
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        System.out.println("-------身份认证方法--------");
//        String userName = (String) authenticationToken.getPrincipal();
//        String userPwd = new String((char[]) authenticationToken.getCredentials());
//        //根据用户名从数据库获取密码
//        String password = "123";
//        if (userName == null) {
//            throw new AccountException("用户名不正确");
//        } else if (!userPwd.equals(password )) {
//            throw new AccountException("密码不正确");
//        }
//        return new SimpleAuthenticationInfo(userName, password,getName());
//    }
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(AuthenticationToken authenticationToken) {
//        return false;
//    }
//
//    @Override
//    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String userName = (String) authenticationToken.getPrincipal();
//        //Authenticate User admin And password 123456 are correct
//        if (!"admin".equals(userName)) {
//            throw new RuntimeException("Account does not exist!");
//        }
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, "123456", getName());
//        return authenticationInfo;
//    }
}
