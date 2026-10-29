package ru.KodinMD.NauJava.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.KodinMD.NauJava.entity.AppUser;
import ru.KodinMD.NauJava.service.UserService;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService{
    private final UserService userService;
    @Autowired
    public AppUserDetailsService(UserService userService){
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        AppUser appUser = userService.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (appUser.getPassword() == null || appUser.getRole() == null){
            throw new UsernameNotFoundException("User has no credentials: " + username);
        }
        return new User(appUser.getLogin(), appUser.getPassword(), mapRoles(appUser));
    }
    private List<GrantedAuthority> mapRoles(AppUser appUser){
        return List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name()));
    }
}
