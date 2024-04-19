package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUserNameAndAtivoIsTrue(String id);

    List<Usuario> findByAtivoIsTrue();

}
