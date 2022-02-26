package com.rafaguido.desafioeteg.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "locacao", schema = "desafio")
@Data
@Builder
public class Locacao {
	
	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "filme")
	private String filme;
	
	@Column(name = "genero")
	private String genero;
	
	@ManyToOne
	@Column(name = "id_usuario")
	private Usuario usuario;
	
	@Column(name = "data_devolucao")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private String dataDevolucao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilme() {
		return filme;
	}

	public void setFilme(String filme) {
		this.filme = filme;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataDevolucao, filme, genero, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Locacao other = (Locacao) obj;
		return Objects.equals(dataDevolucao, other.dataDevolucao) && Objects.equals(filme, other.filme)
				&& Objects.equals(genero, other.genero) && Objects.equals(id, other.id)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "Locacao [id=" + id + ", filme=" + filme + ", genero=" + genero + ", usuario=" + usuario
				+ ", dataDevolucao=" + dataDevolucao + "]";
	}

}
