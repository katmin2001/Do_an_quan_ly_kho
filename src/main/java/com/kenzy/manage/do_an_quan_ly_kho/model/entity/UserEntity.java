package com.kenzy.manage.do_an_quan_ly_kho.model.entity;

import com.kenzy.manage.do_an_quan_ly_kho.model.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(name = "username", length = 45, nullable = false)
    private String username;
    @Column(name = "password", length = 100, nullable = false)
    private String password;
    @Column(name = "email", length = 45)
    private String email;
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "phone", length = 45)
    private String phone;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
