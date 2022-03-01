package com.rafaguido.desafioeteg.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;

public interface LocacaoService {

	Locacao salvar(Locacao locacao);
	
	Locacao atualizar(Locacao locacao);
	
	void deletar(Locacao locacao);
	
	List<Locacao> buscar( Locacao locacaoFiltro );
	
	void atualizarStatus(Locacao locacao, StatusLocacao status);
	
	void validar(Locacao locacao);
	
	Optional<Locacao> obterPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario(Long id);
	
}
