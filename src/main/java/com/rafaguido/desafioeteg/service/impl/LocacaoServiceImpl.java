package com.rafaguido.desafioeteg.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rafaguido.desafioeteg.exception.RegraNegocioException;
import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;
import com.rafaguido.desafioeteg.model.enums.TipoLocacao;
import com.rafaguido.desafioeteg.model.repository.LocacaoRepository;
import com.rafaguido.desafioeteg.service.LocacaoService;

@Service
public class LocacaoServiceImpl implements LocacaoService {
	
	private LocacaoRepository repository;
	
	public LocacaoServiceImpl(LocacaoRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Locacao salvar(Locacao locacao) {
		validar(locacao);
		locacao.setStatus(StatusLocacao.PENDENTE);
		return repository.save(locacao);
	}

	@Override
	@Transactional
	public Locacao atualizar(Locacao locacao) {
		Objects.requireNonNull(locacao.getId());
		validar(locacao);
		return repository.save(locacao);
	}

	@Override
	@Transactional
	public void deletar(Locacao locacao) {
		Objects.requireNonNull(locacao.getId());
		repository.delete(locacao);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Locacao> buscar(Locacao locacaoFiltro) {
		Example example = Example.of( locacaoFiltro, 
				ExampleMatcher.matching()
					.withIgnoreCase()
					.withStringMatcher(StringMatcher.CONTAINING) );
		
		return repository.findAll(example);
	}
	
	@Override
	public void atualizarStatus(Locacao locacao, StatusLocacao status) {
		locacao.setStatus(status);
		atualizar(locacao);
	}

	@Override
	public void validar(Locacao locacao) {
		
		if(locacao.getFilme() == null || locacao.getFilme().trim().equals("")) {
			throw new RegraNegocioException("Informe um Filme válido.");
		}
		
		if(locacao.getMes() == null || locacao.getMes() < 1 || locacao.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido.");
		}
		
		if(locacao.getAno() == null || locacao.getAno().toString().length() != 4 ) {
			throw new RegraNegocioException("Informe um Ano válido.");
		}
		
		if(locacao.getUsuario() == null || locacao.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um Usuário.");
		}
		
		if(locacao.getValor() == null || locacao.getValor().compareTo(BigDecimal.ZERO) < 1 ) {
			throw new RegraNegocioException("Informe um Valor válido.");
		}
		
		if(locacao.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Locação.");
		}
	}

	@Override
	public Optional<Locacao> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterSaldoPorUsuario(Long id) {
		
		BigDecimal devolucoes = repository.obterSaldoPorTipoLocacaoEUsuarioEStatus(id, TipoLocacao.DEVOLUÇÃO, StatusLocacao.EFETIVADO);
		BigDecimal alugueis = repository.obterSaldoPorTipoLocacaoEUsuarioEStatus(id, TipoLocacao.ALUGUEL, StatusLocacao.EFETIVADO);
		
		if(devolucoes == null) {
			devolucoes = BigDecimal.ZERO;
		}

		if(alugueis == null) {
			alugueis = BigDecimal.ZERO;
		}
		
		return devolucoes.subtract(alugueis);
	}

}