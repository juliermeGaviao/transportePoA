package com.dinamica.transportePoA.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dinamica.transportePoA.converter.ItinerarioConverter;
import com.dinamica.transportePoA.converter.LinhaConverter;
import com.dinamica.transportePoA.dto.ItinerarioDto;
import com.dinamica.transportePoA.dto.LinhaDto;
import com.dinamica.transportePoA.model.Itinerario;
import com.dinamica.transportePoA.model.Linha;
import com.dinamica.transportePoA.repository.ItinerarioRepository;
import com.dinamica.transportePoA.repository.LinhaRepository;

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

	@RequestMapping("/onibus/buscarLinhaPeloNome/{nome}")
    public ResponseEntity<?> findLinhaByName(@PathVariable String nome) {
		List<LinhaDto> linhasDto = new ArrayList<>();

    	if (nome != null && nome.trim().length() > 0) {
    		List<Linha> linhas = this.linhaRepository.findByName(nome.trim().toUpperCase());

    		for (Linha linha : linhas) {
    			LinhaDto linhaDto = this.linhaConverter.toDto(linha);
    			List<Itinerario> itinerarios = this.itinerarioRepository.findByLine(linha.getId());
    			List<ItinerarioDto> itinerariosDto = new ArrayList<>();

    			for (Itinerario itinerario : itinerarios) {
    				itinerariosDto.add(this.itinerarioConverter.toDto(itinerario));
    			}

    			linhaDto.setItinerarios(itinerariosDto);
    			linhasDto.add(linhaDto);
    		}
    	}

    	ResponseEntity<?> result;

    	if (linhasDto.isEmpty()) {
    		result = new ResponseEntity<String>("Nenhuma linha encontrada!", HttpStatus.NO_CONTENT);
    	} else {
    		result = new ResponseEntity<List<LinhaDto>>(linhasDto, HttpStatus.OK);
    	}

    	return result;
    }
}
