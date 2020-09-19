package com.dinamica.transportePoA.converter;

import org.springframework.stereotype.Service;

import com.dinamica.transportePoA.dto.LinhaDto;
import com.dinamica.transportePoA.model.Linha;

@Service
public class LinhaConverter {

	public Linha fromDto(LinhaDto linha) {
		if (linha == null) {
			return null;
		}

		Linha result = new Linha(linha.getId(), linha.getCodigo(), linha.getNome());

		return result;
	}

	public LinhaDto toDto(Linha linha) {
		if (linha == null) {
			return null;
		}

		LinhaDto result = new LinhaDto(linha.getId(), linha.getCodigo(), linha.getNome());

		return result;
	}
}
