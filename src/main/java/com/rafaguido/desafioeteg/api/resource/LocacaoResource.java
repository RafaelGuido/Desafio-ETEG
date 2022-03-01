package com.rafaguido.desafioeteg.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafaguido.desafioeteg.api.dto.AtualizaStatusDTO;
import com.rafaguido.desafioeteg.api.dto.LocacaoDTO;
import com.rafaguido.desafioeteg.exception.RegraNegocioException;
import com.rafaguido.desafioeteg.model.entity.Locacao;
import com.rafaguido.desafioeteg.model.entity.Usuario;
import com.rafaguido.desafioeteg.model.enums.StatusLocacao;
import com.rafaguido.desafioeteg.model.enums.TipoLocacao;
import com.rafaguido.desafioeteg.service.LocacaoService;
import com.rafaguido.desafioeteg.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/locacoes")
@RequiredArgsConstructor
public class LocacaoResource {

	private final LocacaoService service;
	private final UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value ="filme" , required = false) String filme,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario
			) {
		
		Locacao locacaoFiltro = new Locacao();
		locacaoFiltro.setFilme(filme);
		locacaoFiltro.setMes(mes);
		locacaoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado.");
		}else {
			locacaoFiltro.setUsuario(usuario.get());
		}
		
		List<Locacao> locacaos = service.buscar(locacaoFiltro);
		return ResponseEntity.ok(locacaos);
	}
	
	@GetMapping("{id}")
	public ResponseEntity obterLocacao( @PathVariable("id") Long id ) {
		return service.obterPorId(id)
					.map( locacao -> new ResponseEntity(converter(locacao), HttpStatus.OK) )
					.orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND) );
	}

	@PostMapping
	public ResponseEntity salvar( @RequestBody LocacaoDTO dto ) {
		try {
			Locacao entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LocacaoDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			try {
				Locacao locacao = converter(dto);
				locacao.setId(entity.getId());
				service.atualizar(locacao);
				return ResponseEntity.ok(locacao);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () ->
			new ResponseEntity("Locação não encontrada na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus( @PathVariable("id") Long id , @RequestBody AtualizaStatusDTO dto ) {
		return service.obterPorId(id).map( entity -> {
			StatusLocacao statusSelecionado = StatusLocacao.valueOf(dto.getStatus());
			
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("Não foi possível atualizar o status da locação, envie um status válido.");
			}
			
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			}catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		
		}).orElseGet( () ->
		new ResponseEntity("Locação não encontrada na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar( @PathVariable("id") Long id ) {
		return service.obterPorId(id).map( entidade -> {
			service.deletar(entidade);
			return new ResponseEntity( HttpStatus.NO_CONTENT );
		}).orElseGet( () -> 
			new ResponseEntity("Locação não encontrada na base de Dados.", HttpStatus.BAD_REQUEST) );
	}
	
	private LocacaoDTO converter(Locacao locacao) {
		return LocacaoDTO.builder()
					.id(locacao.getId())
					.filme(locacao.getFilme())
					.valor(locacao.getValor())
					.mes(locacao.getMes())
					.ano(locacao.getAno())
					.status(locacao.getStatus().name())
					.tipo(locacao.getTipo().name())
					.usuario(locacao.getUsuario().getId())
					.build();
					
	}
	
	private Locacao converter(LocacaoDTO dto) {
		Locacao locacao = new Locacao();
		locacao.setId(dto.getId());
		locacao.setFilme(dto.getFilme());
		locacao.setAno(dto.getAno());
		locacao.setMes(dto.getMes());
		locacao.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
			.obterPorId(dto.getUsuario())
			.orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o Id informado.") );
		
		locacao.setUsuario(usuario);

		if(dto.getTipo() != null) {
			locacao.setTipo(TipoLocacao.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus() != null) {
			locacao.setStatus(StatusLocacao.valueOf(dto.getStatus()));
		}
		
		return locacao;
	}
}