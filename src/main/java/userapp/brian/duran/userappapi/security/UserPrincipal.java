package userapp.brian.duran.userappapi.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import userapp.brian.duran.userappapi.entity.User;

import java.util.Collection;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    @JsonIgnore
    private final User user;

    public List<String> getRolesAsList() {
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return user.getSecurity().isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getSecurity().isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getSecurity().isAccountNonLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getSecurity().isAccountNonExpired();
    }

    @Override
    public String toString() {
        return "UserPrincipal{username='" + user.getUsername() + "'}";
    }
}
