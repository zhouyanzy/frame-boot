package top.zhouy.frameboot.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.zhouy.frameboot.mapper.UserMapper;
import top.zhouy.frameboot.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现security的获取用户信息方法
 */
@Service(value = "myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        User user = userMapper.selectByPhone(phone);
        // 可以有多个角色，先只设定一个
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        grantedAuthorities.add(grantedAuthority);
        //创建一个用户，用于判断权限，请注意此用户名和方法参数中的username一致；BCryptPasswordEncoder是用来演示加密使用。
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getPhone(), new BCryptPasswordEncoder().encode(user.getPassword()), grantedAuthorities);
        return userDetails;
    }
}
