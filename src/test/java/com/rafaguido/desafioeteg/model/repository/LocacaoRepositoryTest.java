package com.rafaguido.desafioeteg.model.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;
import com.rafaguido.desafioeteg.model.enums.TipoLocacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LocacaoRepositoryTest {

	@Autowired
	LocacaoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmaLocacao() {
		Locacao locacao = criarLocacao();
		
		locacao = repository.save(locacao);
		
		assertThat(locacao.getId()).isNotNull();
	}

	@Test
	public void deveDeletarUmaLocacao() {
		Locacao locacao = criarEPersistirUmaLocacao();
		
		locacao = entityManager.find(Locacao.class, locacao.getId());
		
		repository.delete(locacao);
		
		Locacao locacaoInexistente = entityManager.find(Locacao.class, locacao.getId());
		assertThat(locacaoInexistente).isNull();
	}

	
	@Test
	public void deveAtualizarUmaLocacao() {
		Locacao locacao = criarEPersistirUmaLocacao();
		
		locacao.setAno(2018);
		locacao.setFilme("Teste Atualizar");
		locacao.setStatus(StatusLocacao.CANCELADO);
		
		repository.save(locacao);
		
		Locacao locacaoAtualizado = entityManager.find(Locacao.class, locacao.getId());
		
		assertThat(locacaoAtualizado.getAno()).isEqualTo(2018);
		assertThat(locacaoAtualizado.getFilme()).isEqualTo("Teste Atualizar");
		assertThat(locacaoAtualizado.getStatus()).isEqualTo(StatusLocacao.CANCELADO);
	}
	
	@Test
	public void deveBuscarUmaLocacaoPorId() {
		Locacao locacao = criarEPersistirUmaLocacao();
		
		Optional<Locacao> locacaoEncontrado = repository.findById(locacao.getId());
		
		assertThat(locacaoEncontrado.isPresent()).isTrue();
	}

	private Locacao criarEPersistirUmaLocacao() {
		Locacao locacao = criarLocacao();
		entityManager.persist(locacao);
		return locacao;
	}
	
	public static Locacao criarLocacao() {
		return Locacao.builder()
									.ano(2019)
									.mes(1)
									.filme("filme qualquer")
									.valor(BigDecimal.valueOf(10))
									.tipo(TipoLocacao.ALUGUEL)
									.status(StatusLocacao.PENDENTE)
									.dataCadastro(LocalDate.now())
									.build();
	}
	
	
	
	
	
}