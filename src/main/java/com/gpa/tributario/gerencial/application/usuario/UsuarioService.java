package com.gpa.tributario.gerencial.application.usuario;

import com.gpa.tributario.gerencial.application.usuario.request.LoginUsuarioRequest;
import com.gpa.tributario.gerencial.application.usuario.request.UsuarioRequest;
import com.gpa.tributario.gerencial.application.usuario.request.UsuarioSenhaRequest;
import com.gpa.tributario.gerencial.application.usuario.response.UsuarioResponse;
import com.gpa.tributario.gerencial.entity.Usuario;
import com.gpa.tributario.gerencial.enuns.TipoAcaoUsuarioEnum;
import com.gpa.tributario.gerencial.infrastructure.exception.BusinessException;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.infrastructure.security.JwtTokenService;
import com.gpa.tributario.gerencial.infrastructure.security.SecurityUtil;
import com.gpa.tributario.gerencial.infrastructure.security.UserDetailsImpl;
import com.gpa.tributario.gerencial.infrastructure.security.config.SecurityConfiguration;
import com.gpa.tributario.gerencial.repository.UsuarioRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public List<UsuarioResponse> buscarTodos(){

        List<Usuario> lst = usuarioRepositorio.findByAtivoIsTrue();
        return lst.stream().map(this::getResponse).toList();
    }

    public UsuarioResponse buscaPorId(String id){

        Usuario usuario = usuarioRepositorio.findByUserNameAndAtivoIsTrue(id.toUpperCase()).orElseThrow(NotFoundException::new);

        return getResponse(usuario);
    }

    public void remover(String userName){

        Usuario usuario = usuarioRepositorio.findById(userName.toUpperCase()).orElseThrow(NotFoundException::new);
        usuario.setAtivo(false);
        usuario.addHistorico(SecurityUtil.getIdentification(), TipoAcaoUsuarioEnum.DELETE);
        usuarioRepositorio.save(usuario);
    }

    public void alterarSenha(UsuarioSenhaRequest request){

        String userName = SecurityUtil.getIdentification();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userName, request.getPasswordOld());

        try {
            // Autentica o usuário com as credenciais fornecidas
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // Obtém o objeto UserDetails do usuário autenticado
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Usuario usuario = userDetails.getUser();
            usuario.setPassword(securityConfiguration.passwordEncoder().encode(request.getPasswordNew()));
            usuario.addHistorico(userName, TipoAcaoUsuarioEnum.ALTER_PASSWORD);

            usuarioRepositorio.save(usuario);
        }catch (BadCredentialsException e){
            throw new BusinessException("Senha antiga não corresponse à cadastrada");
        }

    }

    public void alterar(String id, UsuarioRequest request){
        Usuario usuario = usuarioRepositorio.findById(id).orElseThrow(NotFoundException::new);

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(securityConfiguration.passwordEncoder().encode(request.getPassword()));
        usuario.setRoles(request.getGrupos());
        usuario.setAtivo(true);

        usuario.addHistorico(SecurityUtil.getIdentification(), TipoAcaoUsuarioEnum.ALTER);

        // Salva o novo usuário no banco de dados
        usuarioRepositorio.save(usuario);
    }

    // Método responsável por autenticar um usuário e retornar um token JWT
    public UsuarioResponse authenticateUser(LoginUsuarioRequest loginRequest) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName().toUpperCase(), loginRequest.getPassword());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UsuarioResponse response = getResponse(userDetails.getUser());
        response.setToken(jwtTokenService.generateToken(userDetails));
        // Gera um token JWT para o usuário autenticado
        return response;
    }

    // Método responsável por criar um usuário
    public void createUser(UsuarioRequest request) {

        Usuario newUser = usuarioRepositorio.findById(request.getUserName().toUpperCase()).orElse(new Usuario());

        newUser.setUserName(request.getUserName().toUpperCase());
        newUser.setNome(request.getNome());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(securityConfiguration.passwordEncoder().encode(request.getPassword()));
        newUser.setRoles(request.getGrupos());
        newUser.setAtivo(true);

        newUser.addHistorico(SecurityUtil.getIdentification(), TipoAcaoUsuarioEnum.CREATE);

        // Salva o novo usuário no banco de dados
        usuarioRepositorio.save(newUser);
    }

    private UsuarioResponse getResponse(Usuario usuario){
        UsuarioResponse response = new UsuarioResponse();
        BeanUtils.copyProperties(usuario, response);
        response.setGrupos(usuario.getRoles());
        return response;
    }

}
