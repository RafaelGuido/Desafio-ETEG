package com.rafaguido.desafioeteg.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaguido.desafioeteg.model.entity.Locacao;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

}
