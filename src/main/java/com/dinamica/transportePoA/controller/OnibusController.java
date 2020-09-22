package com.dinamica.transportePoA.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dinamica.transportePoA.converter.ItinerarioConverter;
import com.dinamica.transportePoA.converter.LinhaConverter;
import com.dinamica.transportePoA.dto.ItinerarioDto;
import com.dinamica.transportePoA.dto.LinhaDto;
import com.dinamica.transportePoA.dto.PontoReferenciaDto;
import com.dinamica.transportePoA.model.Itinerario;
import com.dinamica.transportePoA.model.Linha;
import com.dinamica.transportePoA.repository.ItinerarioRepository;
import com.dinamica.transportePoA.repository.LinhaRepository;
import com.dinamica.transportePoA.service.GeodeticService;

@RestController
public class OnibusController {

	@Autowired
	private LinhaRepository linhaRepository;

	@Autowired
	private ItinerarioRepository itinerarioRepository;

	@Autowired
	private LinhaConverter linhaConverter;

	@Autowired
	private ItinerarioConverter itinerarioConverter;

	@Autowired
	private GeodeticService geodeticService;

	@RequestMapping("/onibus/buscarLinhaPeloNome/{nome}")
	public ResponseEntity<?> findLinhaByName(@PathVariable String nome) {
		List<LinhaDto> linhasDto = new ArrayList<>();

		if (nome != null && nome.trim().length() > 0) {
			// Utiliza a busca através de LIKE, do SQL
			List<Linha> linhas = this.linhaRepository.containsName(nome.trim().toUpperCase());

			linhasDto.addAll(this.getLineDtos(linhas));
		}

		ResponseEntity<?> result;

		if (linhasDto.isEmpty()) {
			result = new ResponseEntity<String>("Nenhuma linha encontrada!", HttpStatus.NO_CONTENT);
		} else {
			result = new ResponseEntity<List<LinhaDto>>(linhasDto, HttpStatus.OK);
		}

		return result;
	}

	@RequestMapping("/onibus/buscarLinhaPeloCodigo/{codigo}")
	public ResponseEntity<?> findLinhaByCode(@PathVariable String codigo) {
		List<LinhaDto> linhasDto = new ArrayList<>();

		if (codigo != null && codigo.trim().length() > 0) {
			List<Linha> linhas = this.linhaRepository.findByCode(codigo.trim());

			linhasDto.addAll(this.getLineDtos(linhas));
		}

		ResponseEntity<?> result;

		if (linhasDto.isEmpty()) {
			result = new ResponseEntity<String>("Nenhuma linha encontrada!", HttpStatus.NO_CONTENT);
		} else {
			result = new ResponseEntity<List<LinhaDto>>(linhasDto, HttpStatus.OK);
		}

		return result;
	}

	@RequestMapping(value="/onibus/inserirLinha", method=RequestMethod.POST)
	public ResponseEntity<?> insertLine(@RequestBody LinhaDto linhaDto) {
		ResponseEntity<?> result;

		if (linhaDto.getNome() == null || linhaDto.getNome().trim().length() == 0) {
			result = new ResponseEntity<String>("Nome é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (linhaDto.getCodigo() == null || linhaDto.getCodigo().trim().length() == 0) {
			result = new ResponseEntity<String>("Código é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else {
			// Impede gravação de múltiplas linhas com o mesmo código
			List<Linha> linesSameCode = this.linhaRepository.findByCode(linhaDto.getCodigo().trim());

			if (linesSameCode != null && !linesSameCode.isEmpty()) {
				result = new ResponseEntity<String>("Já existe linha com o mesmo código!", HttpStatus.CONFLICT);
			} else {
				Linha linha = this.linhaConverter.fromDto(linhaDto);

				linha = this.linhaRepository.save(linha);

				result = new ResponseEntity<Integer>(linha.getId(), HttpStatus.CREATED);
			}
		}

		return result;
	}

	@RequestMapping(value="/onibus/editarLinha", method=RequestMethod.POST)
	public ResponseEntity<?> updateLine(@RequestBody LinhaDto linhaDto) {
		ResponseEntity<?> result;

		if (linhaDto.getNome() == null || linhaDto.getNome().trim().length() == 0) {
			result = new ResponseEntity<String>("Nome é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (linhaDto.getCodigo() == null || linhaDto.getCodigo().trim().length() == 0) {
			result = new ResponseEntity<String>("Código é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (linhaDto.getId() == null || linhaDto.getId() <= 0) {
			result = new ResponseEntity<String>("Identificação de linha inválido!", HttpStatus.BAD_REQUEST);
		} else if (!this.linhaRepository.existsById(linhaDto.getId())) {
			result = new ResponseEntity<String>("Linha com a identifiação [" + linhaDto.getId().toString() + "] não encontrada!", HttpStatus.BAD_REQUEST);
		} else {
			// Não permite mudar o código da linha caso exista outra com o mesmo código
			int sameCode = this.otherLineExists(linhaDto.getId(), this.linhaRepository.findByCode(linhaDto.getCodigo().trim().toUpperCase()));

			if (sameCode > 0) {
				result = new ResponseEntity<String>("Já existe outra linha com o mesmo código, de identifiação [" + sameCode + "]!", HttpStatus.CONFLICT);
			} else {
				Linha linha = this.linhaRepository.findById(linhaDto.getId()).get();

				linha.setCodigo(linhaDto.getCodigo());
				linha.setNome(linhaDto.getNome());
				this.linhaRepository.save(linha);
				result = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
			}
		}

		return result;
	}

	@RequestMapping(value="/onibus/removerLinha/{idLinha}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteLine(@PathVariable Integer idLinha) {
		ResponseEntity<?> result;

		if (idLinha == null || idLinha.intValue() <= 0) {
			result = new ResponseEntity<String>("A identificação da linha é inválida!", HttpStatus.BAD_REQUEST);
		} else {
			// Optional<?> é uma opção ao existsById do CRUDRepository
			Optional<Linha> linha = this.linhaRepository.findById(idLinha);

			if (linha.isPresent()) {
				this.linhaRepository.delete(linha.get());
				result = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
			} else {
				result = new ResponseEntity<String>("Linha com a identifiação [" + idLinha.toString() + "] não encontrada!", HttpStatus.NO_CONTENT);
			}
		}

		return result;
	}

	@RequestMapping(value="/onibus/buscarItinerario/{idLinha}")
	public ResponseEntity<?> findItineraryByIdLinha(@PathVariable Integer idLinha) {
		ResponseEntity<?> result;

		if (idLinha == null || idLinha.intValue() <= 0) {
			result = new ResponseEntity<String>("A identificação da linha é inválida!", HttpStatus.BAD_REQUEST);
		} else {
			List<Itinerario> itinerarios = this.itinerarioRepository.findByLine(idLinha);

			if (itinerarios != null && !itinerarios.isEmpty()) {
				// Usuário da API não tem visibilidade de Entities, apenas de seus correspondentes DTOs
				// Isso forma camadas de software, favorecendo mudanças futuras com menor custo possível
				List<ItinerarioDto> itinerariosDto = this.itinerarioConverter.toDtoList(itinerarios);

				result = new ResponseEntity<List<ItinerarioDto>>(itinerariosDto, HttpStatus.OK);
			} else {
				result = new ResponseEntity<String>("Itinerário da linha com a identifiação [" + idLinha.toString() + "] não encontrado!", HttpStatus.NO_CONTENT);
			}
		}

		return result;
	}

	@RequestMapping(value="/onibus/inserirItinerario/{idLinha}", method=RequestMethod.POST)
	public ResponseEntity<?> insertItinerary(@PathVariable Integer idLinha, @RequestBody ItinerarioDto itinerarioDto) {
		ResponseEntity<?> result;

		if (idLinha == null || idLinha.intValue() <= 0) {
			result = new ResponseEntity<String>("Identificação da linha é obrigatória!", HttpStatus.BAD_REQUEST);
		} else if (itinerarioDto.getLatitude() == null) {
			result = new ResponseEntity<String>("Latitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (itinerarioDto.getLongitude() == null) {
			result = new ResponseEntity<String>("Longitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (!this.linhaRepository.existsById(idLinha)) {
			result = new ResponseEntity<String>("Não existe linha com identificação [" + idLinha.toString() + "]!", HttpStatus.NO_CONTENT);
		} else {
			// Ao inserir um ponto de itinerário, a maior ordem é utilizada, independentemente se um ponto no meio tenha sido removido
			// Ainda assim, a ordem entre os pontos é preservada
			Integer ordem = this.itinerarioRepository.getMaxItineraryOrder(idLinha);
			// Não causa NullPointerException porque a existência do ponto já foi testada acima
			Linha linha = this.linhaRepository.findById(idLinha).get();
			Itinerario itinerario = this.itinerarioConverter.fromDto(itinerarioDto);

			ordem = ordem != null && ordem.intValue() > 0 ? new Integer(ordem.intValue() + 1) : new Integer(1);
			itinerario.setOrdem(ordem);
			itinerario.setLinha(linha);
			itinerario = this.itinerarioRepository.save(itinerario);

			result = new ResponseEntity<Integer>(itinerario.getId(), HttpStatus.CREATED);
		}

		return result;
	}

	@RequestMapping(value="/onibus/editarItinerario", method=RequestMethod.POST)
	public ResponseEntity<?> updateItinerary(@RequestBody ItinerarioDto itinerarioDto) {
		ResponseEntity<?> result;

		if (itinerarioDto.getLatitude() == null) {
			result = new ResponseEntity<String>("Latitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (itinerarioDto.getLongitude() == null) {
			result = new ResponseEntity<String>("Longitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (itinerarioDto.getId() == null || itinerarioDto.getId() <= 0) {
			result = new ResponseEntity<String>("Identificação de itinerário inválido!", HttpStatus.BAD_REQUEST);
		} else if (!this.itinerarioRepository.existsById(itinerarioDto.getId())) {
			result = new ResponseEntity<String>("Itinerário com a identifiação [" + itinerarioDto.getId().toString() + "] não encontrado!", HttpStatus.NO_CONTENT);
		} else {
			// Não causa NullPointerException porque a existência do ponto já foi testada acima
			Itinerario itinerario = this.itinerarioRepository.findById(itinerarioDto.getId()).get();

			// Propositalmente, a ordem do ponto não é modificado. Opção momentânea de regra de negócio
			itinerario.setLatitude(itinerarioDto.getLatitude());
			itinerario.setLongitude(itinerarioDto.getLongitude());
			this.itinerarioRepository.save(itinerario);
			result = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		}

		return result;
	}

	@RequestMapping(value="/onibus/removerItinerario/{idItinerario}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteItinerario(@PathVariable Integer idItinerario) {
		ResponseEntity<?> result;

		if (idItinerario == null || idItinerario.intValue() <= 0) {
			result = new ResponseEntity<String>("A identificação do itinerário é inválida!", HttpStatus.BAD_REQUEST);
		} else {
			// Optional<?> é uma opção ao existsById do CRUDRepository
			Optional<Itinerario> itinerario = this.itinerarioRepository.findById(idItinerario);

			if (itinerario.isPresent()) {
				this.itinerarioRepository.delete(itinerario.get());
				result = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
			} else {
				result = new ResponseEntity<String>("Itinerário com a identifiação [" + idItinerario.toString() + "] não encontrado!", HttpStatus.NO_CONTENT);
			}
		}

		return result;
	}

	@RequestMapping(value="/onibus/linhasDentroRaio", method=RequestMethod.POST)
	public ResponseEntity<?> findLinesWithinRadius(@RequestBody PontoReferenciaDto pontoReferenciaDto) {
		ResponseEntity<?> result;

		if (pontoReferenciaDto.getLatitude() == null) {
			result = new ResponseEntity<String>("A latitude do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoReferenciaDto.getLongitude() == null) {
			result = new ResponseEntity<String>("A longitude do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoReferenciaDto.getRaio() == null) {
			result = new ResponseEntity<String>("O raio a partir do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else {
			// Busca todas as linhas de ônibus da base uma única vez porque a partir de então as mesmas ficam no cache do Hibernate
			Iterable<Linha> linhas = this.linhaRepository.findAll();
			List<Linha> linhasResultantes = new ArrayList<>();
			double latitude = pontoReferenciaDto.getLatitude().doubleValue();
			double longitude = pontoReferenciaDto.getLongitude().doubleValue();
			double raio = pontoReferenciaDto.getRaio().doubleValue();

			// Para cada linha de ônibus, verifica-se a distância do ponto informado como
			// parâmetro até cada um dos pontos de seu itinerário
			// A solução não cobre a situação em que a circunferência tangencia ou
			// intersecta a linha formada por dois pontos do itinerário,
			// mas sem englobar nenhum destes pontos
			// Trata-se de uma simplificação. Solução ideal é utilizar módulos GEO presente
			// na maioria dos bancos de dados atuais. São eficientes e precisos
			for (Linha linha : linhas) {
				if (this.isLineInRadius(latitude, longitude, raio, linha.getItinerarios())) {
					linhasResultantes.add(linha);
				}
			}

			List<LinhaDto> linhasDto = this.linhaConverter.toDtoList(linhasResultantes);

			result = new ResponseEntity<List<LinhaDto>>(linhasDto, HttpStatus.OK);
		}

		return result;
	}

	/**
	 * Carrega o itinerário da linha. Isso é realizado para alguns rotinas da API
	 * pois nem sempre o consumidor da API necessita do itinerário, isso evite
	 * exceço de processamento e carga de dados na rede.
	 * 
	 * @param linhas A lista de entities correspondente ao itinerário e que é
	 *               convertida em uma lista de DTOs
	 * @return O itinerário no formato DTO
	 */
	private List<LinhaDto> getLineDtos(List<Linha> linhas) {
		List<LinhaDto> result = new ArrayList<>();

		if (linhas != null && !linhas.isEmpty()) {
			result.addAll(this.linhaConverter.toDtoList(linhas));

			List<ItinerarioDto> itinerarios;
			for (int i = 0; i < linhas.size(); i++) {
				itinerarios = this.itinerarioConverter.toDtoList(linhas.get(i).getItinerarios());
				result.get(i).setItinerarios(itinerarios);
			}
		}

		return result;
	}

	/**
	 * Rotina utilizada para determinar se há outra linha com o mesmo código,
	 * utilizando-se seu id
	 * 
	 * @param id     A identificação da linha candidata a ter seu código alterado
	 * @param linhas Listagem de linhas encontradas no banco de dados com o código
	 * @return -1 se não há outra linha com o mesmo código, ou número maior ou igual
	 *         a zero indicando que há outra linha com o mesmo código
	 */
	private int otherLineExists(Integer id, List<Linha> linhas) {
		int result = -1;

		for (int i = 0; i < linhas.size() && result <= 0; i++) {
			result = linhas.get(i).getId().intValue() != id.intValue() ? linhas.get(i).getId() : -1;
		}

		return result;
	}

	/**
	 * Sinaliza se um itinerário está dentro do raio ao redor de um ponto qualquer.
	 * O itinerário é uma lista de coordenadas geodésicas.
	 * 
	 * @param latitudePonto  Latitude do ponto.
	 * @param longitudePonto Longitude do ponto.
	 * @param raio           O raio em Km a partir do ponto em que o itinerário será
	 *                       testado.
	 * @param itinerarios    Lista de coordenadas geodésicas.
	 * @return true, se há algum segmento do itinerário com distância menor ou igual
	 *         ao raio informado. false, no caso contrário.
	 */
	private boolean isLineInRadius(double latitudePonto, double longitudePonto, double raio, List<Itinerario> itinerarios) {
		boolean result = false;

		if (itinerarios.size() > 1) {
			Itinerario ponto;
			double distancia;

			for (int i = 0; i < itinerarios.size() - 1 && !result; i++) {
				ponto = itinerarios.get(i);

				// A solução não cobre a situação em que a circunferência tangencia ou
				// intersecta a linha formada por dois pontos do itinerário,
				// mas sem englobar nenhum destes pontos
				// Trata-se de uma simplificação. Solução ideal é utilizar módulos GEO presente
				// na maioria dos bancos de dados atuais. São eficientes e precisos
				distancia = this.geodeticService.getDistanceBetweenCoordinates(latitudePonto, longitudePonto,
						ponto.getLatitude().doubleValue(), ponto.getLongitude().doubleValue());

				result = distancia <= raio;
			}
		} else if (itinerarios.size() == 1) {
			double distancia = this.geodeticService.getDistanceBetweenCoordinates(latitudePonto, latitudePonto, itinerarios.get(0).getLatitude().doubleValue(), itinerarios.get(0).getLongitude().doubleValue());
			result = distancia <= raio;
		}

		return result;
	}

}
