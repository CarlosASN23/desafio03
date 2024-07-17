package br.com.compassuol.Desafio._3.model;


import br.com.compassuol.Desafio._3.model.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Usuario implements UserDetails, Serializable {

    private static final long serialVersionUID = 5620606054205035360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @JsonIgnore
    @Column(nullable = false)
    private UserRoles role;


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Venda> vendas = new ArrayList<>();

    public Usuario(Long id, String email, UserRoles role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UserRoles getUserRoles() {
        return UserRoles.valueOf(UserRoles.toEnum(role.getRole()));
    }
    public void setUserRoles(UserRoles userRoles){
        this.role = userRoles;
    }

    public Usuario(String email, String encryptedPass, UserRoles role) {
        this.email = email;
        this.senha = encryptedPass;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "ID = " + id +
                "Email = " + email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRoles.ADMIN)return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return senha;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
