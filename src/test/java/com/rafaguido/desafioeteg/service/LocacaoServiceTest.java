package com.rafaguido.desafioeteg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.rafaguido.desafioeteg.exception.RegraNegocioException;
import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.entity.Usuario;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;
import com.rafaguido.desafioeteg.model.enums.TipoLocacao;
import com.rafaguido.desafioeteg.model.repository.LocacaoRepository;
import com.rafaguido.desafioeteg.model.repository.LocacaoRepositoryTest;
import com.rafaguido.desafioeteg.service.impl.LocacaoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LocacaoServiceTest {

	@SpyBean
	LocacaoServiceImpl service;
	@MockBean
	LocacaoRepository repository;
	
	@Test
	public void deveSalvarUmaLocacao() {
		
		Locacao locacaoASalvar = LocacaoRepositoryTest.criarLocacao();
		doNothing().when(service).validar(locacaoASalvar);
		
		Locacao locacaoSalvo = LocacaoRepositoryTest.criarLocacao();
		locacaoSalvo.setId(1l);
		locacaoSalvo.setStatus(StatusLocacao.PENDENTE);
		when(repository.save(locacaoASalvar)).thenReturn(locacaoSalvo);
		
		Locacao locacao = service.salvar(locacaoASalvar);
		
		assertThat( locacao.getId() ).isEqualTo(locacaoSalvo.getId());
		assertThat(locacao.getStatus()).isEqualTo(StatusLocacao.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmaLocacaoQuandoHouverErroDeValidacao() {
		
		Locacao locacaoASalvar = LocacaoRepositoryTest.criarLocacao();
		doThrow( RegraNegocioException.class ).when(service).validar(locacaoASalvar);
		
		catchThrowableOfType( () -> service.salvar(locacaoASalvar), RegraNegocioException.class );
		verify(repository, never()).save(locacaoASalvar);
	}
	
	@Test
	public void deveAtualizarUmaLocacao() {
		
		Locacao locacaoSalvo = LocacaoRepositoryTest.criarLocacao();
		locacaoSalvo.setId(1l);
		locacaoSalvo.setStatus(StatusLocacao.PENDENTE);

		doNothing().when(service).validar(locacaoSalvo);
		
		when(repository.save(locacaoSalvo)).thenReturn(locacaoSalvo);
		
		service.atualizar(locacaoSalvo);
		
		verify(repository, times(1)).save(locacaoSalvo);
		
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmaLocacaoQueAindaNaoFoiSalva() {
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		
		catchThrowableOfType( () -> service.atualizar(locacao), NullPointerException.class );
		verify(repository, never()).save(locacao);
	}
	
	@Test
	public void deveDeletarUmaLocacao() {
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		locacao.setId(1l);
		
		service.deletar(locacao);
		
		verify( repository ).delete(locacao);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmaLocacaoQueAindaNaoFoiSalva() {
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		
		catchThrowableOfType( () -> service.deletar(locacao), NullPointerException.class );
		
		verify( repository, never() ).delete(locacao);
	}
	
	
	@Test
	public void deveFiltrarLocacoes() {
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		locacao.setId(1l);
		
		List<Locacao> lista = Arrays.asList(locacao);
		when( repository.findAll(any(Example.class)) ).thenReturn(lista);
		
		List<Locacao> resultado = service.buscar(locacao);
		
		assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(locacao);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmaLocacao() {
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		locacao.setId(1l);
		locacao.setStatus(StatusLocacao.PENDENTE);
		
		StatusLocacao novoStatus = StatusLocacao.EFETIVADO;
		doReturn(locacao).when(service).atualizar(locacao);
		
		service.atualizarStatus(locacao, novoStatus);
		
		assertThat(locacao.getStatus()).isEqualTo(novoStatus);
		verify(service).atualizar(locacao);
		
	}
	
	@Test
	public void deveObterUmaLocacaoPorID() {
		
		Long id = 1l;
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		locacao.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(locacao));
		
		Optional<Locacao> resultado =  service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveREtornarVazioQuandoALocacaoNaoExiste() {
		
		Long id = 1l;
		
		Locacao locacao = LocacaoRepositoryTest.criarLocacao();
		locacao.setId(id);
		
		when( repository.findById(id) ).thenReturn( Optional.empty() );
		
		Optional<Locacao> resultado =  service.obterPorId(id);
		
		assertThat(resultado.isPresent()).isFalse();
	}
	
	@Test
	public void deveLancarErrosAoValidarUmaLocacao() {
		Locacao locacao = new Locacao();
		
		Throwable erro = Assertions.catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Filme válido.");
		
		locacao.setFilme("");
		
		erro = Assertions.catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Filme válido.");
		
		locacao.setFilme("Matrix");
		
		erro = Assertions.catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		locacao.setAno(0);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		locacao.setAno(13);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");
		
		locacao.setMes(1);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		locacao.setAno(202);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");
		
		locacao.setAno(2020);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		locacao.setUsuario(new Usuario());
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		locacao.getUsuario().setId(1l);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		locacao.setValor(BigDecimal.ZERO);
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		locacao.setValor(BigDecimal.valueOf(1));
		
		erro = catchThrowable( () -> service.validar(locacao) );
		assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Locação.");
		
	}
	
	@Test
	public void deveObterSaldoPorUsuario() {
		
		Long idUsuario = 1l;

		when( repository
				.obterSaldoPorTipoLocacaoEUsuarioEStatus(idUsuario, TipoLocacao.DEVOLUÇÃO, StatusLocacao.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(100));
		
		when( repository
				.obterSaldoPorTipoLocacaoEUsuarioEStatus(idUsuario, TipoLocacao.ALUGUEL, StatusLocacao.EFETIVADO)) 
				.thenReturn(BigDecimal.valueOf(50));
		
		BigDecimal saldo = service.obterSaldoPorUsuario(idUsuario);
		
		assertThat(saldo).isEqualTo(BigDecimal.valueOf(50));
		
	}
	
}