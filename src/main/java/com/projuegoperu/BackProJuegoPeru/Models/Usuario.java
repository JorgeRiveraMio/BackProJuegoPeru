//package com.projuegoperu.BackProJuegoPeru.Models;
//
//import com.projuegoperu.BackProJuegoPeru.Models.Persona;
//import jakarta.persistence.*;
//    import lombok.*;
//
//import org.springframework.security.core.GrantedAuthority;
//    import org.springframework.security.core.authority.SimpleGrantedAuthority;
//    import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.LocalDateTime;
//import java.util.Collection;
//    import java.util.HashSet;
//    import java.util.Set;
//
//    @Entity
//    @Data
//
//    @Builder
//
//    @Table(name = "usuario")
//    @PrimaryKeyJoinColumn(name = "idPersona")  // Correcto para herencia JOINED
//    public class Usuario extends Persona implements UserDetails {
//
//        @Column(name = "username")
//        private String username;
//
//        @Column(name = "password")
//        private String password;
//
//        @Column(name = "creationDate")
//        private LocalDateTime creationDate;
//
//        @Column(name = "tipo_usuario")
//        private String tipoUsuario;
//
//
//        @ManyToMany(fetch = FetchType.EAGER)
//        @JoinTable(
//                name = "usuario_rol",
//                joinColumns = @JoinColumn(name = "usuario_id"),
//                inverseJoinColumns = @JoinColumn(name = "rol_id")
//        )
//        private Set<Rol> roles;
//
//        public Usuario() {
//
//        }
//
//        @PrePersist
//        protected void onCreate() {
//            this.creationDate = LocalDateTime.now();
//        }
//
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            Set<GrantedAuthority> authorities = new HashSet<>();
//            for (Rol rol : roles) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getName()));
//            }
//            return authorities;
//        }
//        public Set<Rol> getRoles() {
//            return roles;
//        }
//        @Override
//        public String getPassword() {
//            return this.password;
//        }
//
//        @Override
//        public String getUsername() {
//            return this.username;
//        }
//
//        @Override
//        public boolean isAccountNonExpired() {
//            return UserDetails.super.isAccountNonExpired();
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//            return UserDetails.super.isAccountNonLocked();
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return UserDetails.super.isCredentialsNonExpired();
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return UserDetails.super.isEnabled();
//        }
//
//        public Usuario(int idPersona, String name, String lastname, String dni, String username, String password, LocalDateTime creationDate, String tipoUsuario, Set<Rol> roles) {
//            super(idPersona, name, lastname, dni);
//            this.username = username;
//            this.password = password;
//            this.creationDate = creationDate;
//            this.tipoUsuario = tipoUsuario;
//            this.roles = roles;
//        }
//
//
//    }
