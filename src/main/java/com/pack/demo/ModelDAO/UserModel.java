package com.pack.demo.ModelDAO;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
// import java.util.HashSet;
import java.util.List;
// import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data

@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor // ✅ Add this
@ToString(exclude = "dashboards")
@Getter
@Setter
public class UserModel implements UserDetails {
    private String name;
    @Id
    private String id;
    private String password;
    private String email;
    private String phoneno;
    private LocalDate joinDate;
    private boolean isVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate dailyquestion;
    private String avatar;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Streak streak;

    @Transient
    private int noquiz;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dashboard> dashboards;

    // ✅ Add this to support JOIN from QuizAttempt
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizAttempt> attempts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getRole().name()));
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
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
        return this.isVerified;
    }
}
