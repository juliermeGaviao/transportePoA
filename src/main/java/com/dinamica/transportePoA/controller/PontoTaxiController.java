package com.dinamica.transportePoA.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dinamica.transportePoA.dto.PontoReferenciaDto;
import com.dinamica.transportePoA.dto.PontoTaxiDto;
import com.dinamica.transportePoA.service.GeodeticService;

@RestController
public class PontoTaxiController {

	@Autowired
	private GeodeticService geodeticService;

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	private List<PontoTaxiDto> pontoTaxiDtoList;

	public List<PontoTaxiDto> getPontoTaxiDtoList() {
		// Pontos lidos do arquivo apenas uma vez
		if (this.pontoTaxiDtoList == null) {
			this.pontoTaxiDtoList = new ArrayList<>();

			try {
				// Arquivo de pontos de táxi disponível no diretório de execução da aplicação
				BufferedReader reader = new BufferedReader(new FileReader("ponto_taxi.csv"));
				String line = reader.readLine();

				while (line != null) {
					StringTokenizer tokenizer = new StringTokenizer(line, "#");
					PontoTaxiDto pontoTaxiDto = new PontoTaxiDto();

					pontoTaxiDto.setNome(tokenizer.nextToken());
					pontoTaxiDto.setLatitude(Float.parseFloat(tokenizer.nextToken()));
					pontoTaxiDto.setLongitude(Float.parseFloat(tokenizer.nextToken()));
					pontoTaxiDto.setDataCadastro(tokenizer.nextToken());

					this.pontoTaxiDtoList.add(pontoTaxiDto);

					line = reader.readLine();
				}

				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this.pontoTaxiDtoList;
	}

	@RequestMapping(value="/taxi/inserirPontoTaxi", method=RequestMethod.POST)
	public ResponseEntity<?> insertTaxiPoint(@RequestBody PontoTaxiDto pontoTaxiDto) {
		ResponseEntity<?> result;

		if (pontoTaxiDto.getNome() == null || pontoTaxiDto.getNome().trim().length() == 0) {
			result = new ResponseEntity<String>("Nome é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoTaxiDto.getLatitude() == null) {
			result = new ResponseEntity<String>("Latitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoTaxiDto.getLongitude() == null) {
			result = new ResponseEntity<String>("Longitude é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else {
			try {
				// Arquivo de pontos de táxi está no diretório de execução da aplicação
				FileWriter writer = new FileWriter("ponto_taxi.csv", true);
				String dataCadastro = this.dateFormatter.format(new Date());

				writer.append(pontoTaxiDto.getNome()).append('#');
				writer.append(pontoTaxiDto.getLatitude().toString()).append('#');
				writer.append(pontoTaxiDto.getLongitude().toString()).append('#');
				writer.append(dataCadastro).append('\n');

				writer.close();

				pontoTaxiDto.setDataCadastro(dataCadastro);
				this.getPontoTaxiDtoList().add(pontoTaxiDto);

				result = new ResponseEntity<PontoTaxiDto>(pontoTaxiDto, HttpStatus.OK);
			} catch (IOException e) {
				result = new ResponseEntity<String>("Erro ao gravar arquivo de pontos de táxi!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return result;
	}

	@RequestMapping("/taxi/buscarPontoTaxi/{nome}")
	public ResponseEntity<?> findByName(@PathVariable String nome) {
		ResponseEntity<?> result;

		if (nome == null || nome.trim().length() == 0) {
			result = new ResponseEntity<String>("Parâmetro \"nome\" é necessário para se fazer a busca!", HttpStatus.BAD_REQUEST);
		} else {
			List<PontoTaxiDto> pontosTaxiEncontrados = new ArrayList<>();
			String buscar = nome.trim().toUpperCase();

			// Seleciona qualquer ponto de táxi cujo nome contenha o parâmetro do método como substring
			for (PontoTaxiDto pontoTaxiDto : this.getPontoTaxiDtoList()) {
				if (pontoTaxiDto.getNome().contains(buscar)) {
					pontosTaxiEncontrados.add(pontoTaxiDto);
				}
			}

			result = new ResponseEntity<List<PontoTaxiDto>>(pontosTaxiEncontrados, HttpStatus.OK);
		}

		return result;
	}

	@RequestMapping(value="/taxi/pontosTaxiDentroRaio", method=RequestMethod.POST)
	public ResponseEntity<?> findTaxiPointsWithinRadius(@RequestBody PontoReferenciaDto pontoReferenciaDto) {
		ResponseEntity<?> result;

		if (pontoReferenciaDto.getLatitude() == null) {
			result = new ResponseEntity<String>("A latitude do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoReferenciaDto.getLongitude() == null) {
			result = new ResponseEntity<String>("A longitude do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else if (pontoReferenciaDto.getRaio() == null) {
			result = new ResponseEntity<String>("O raio a partir do ponto de referência é de preenchimento obrigatório!", HttpStatus.BAD_REQUEST);
		} else {
			// Busca todas as linhas de ônibus da base uma única vez porque a partir de então as mesmas ficam no cache do Hibernate
			List<PontoTaxiDto> pontosResultantes = new ArrayList<>();
			double latitude = pontoReferenciaDto.getLatitude().doubleValue();
			double longitude = pontoReferenciaDto.getLongitude().doubleValue();
			double raio = pontoReferenciaDto.getRaio().doubleValue();

			// Para cada linha de ônibus, verifica-se a distância do ponto informado como
			// parâmetro até o ponto de táxi
			for (PontoTaxiDto pontoTaxiDto : this.getPontoTaxiDtoList()) {
				double distancia = this.geodeticService.getDistanceBetweenCoordinates(latitude, longitude, pontoTaxiDto.getLatitude().doubleValue(), pontoTaxiDto.getLongitude().doubleValue());

				if (distancia <= raio) {
					pontosResultantes.add(pontoTaxiDto);
				}
			}

			result = new ResponseEntity<List<PontoTaxiDto>>(pontosResultantes, HttpStatus.OK);
		}

		return result;
	}
}
