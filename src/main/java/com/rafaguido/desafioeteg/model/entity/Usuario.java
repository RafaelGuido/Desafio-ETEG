package com.rafaguido.desafioeteg.model.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.rafaguido.desafioeteg.model.enums.Sexo;

import lombok.Builder;
import lombok.Data;

@Entity
@Table( name = "usuario" , schema = "desafio")
@Builder
@Data
public class Usuario {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "sexo")
	@Enumerated(value = EnumType.STRING)
	private Sexo sexo;
	
	@Column(name = "senha")
	private String senha;
	
	@Column(name = "data_nascimento")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private String dataNascimento;
	
	@Column(name = "data_cadastro")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private String dataCadastro;

}
