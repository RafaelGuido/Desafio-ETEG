package com.rafaguido.desafioeteg.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;
import com.rafaguido.desafioeteg.model.enums.TipoLocacao;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

	@Query( value = 
			  " select sum(l.valor) from Locacao l join l.usuario u "
			+ " where u.id = :idUsuario and l.tipo =:tipo and l.status = :status group by u " )
	BigDecimal obterSaldoPorTipoLocacaoEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLocacao tipo,
			@Param("status") StatusLocacao status);
	
}