package com.rafaguido.desafioeteg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaguido.desafioeteg.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
