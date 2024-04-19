package com.gpa.tributario.gerencial.infrastructure.security;

import com.gpa.tributario.gerencial.entity.Usuario;
import com.gpa.tributario.gerencial.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUserNameAndAtivoIsTrue(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        return new UserDetailsImpl(user);
    }

}