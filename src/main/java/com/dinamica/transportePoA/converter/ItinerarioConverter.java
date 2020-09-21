package com.dinamica.transportePoA.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinamica.transportePoA.dto.ItinerarioDto;
import com.dinamica.transportePoA.model.Itinerario;
import com.dinamica.transportePoA.repository.LinhaRepository;

@Service
public class ItinerarioConverter {

	@Autowired
	private LinhaRepository linhaRepository;

	public Itinerario fromDto(ItinerarioDto itinerario) {
		if (itinerario == null) {
			return null;
		}

		Itinerario result = new Itinerario(itinerario.getId(), itinerario.getOrdem(), itinerario.getLatitude(), itinerario.getLongitude());

		if (itinerario.getIdLinha() != null) {
			result.setLinha(this.linhaRepository.findById(itinerario.getIdLinha()).get());
		}

		return result;
	}

	public ItinerarioDto toDto(Itinerario itinerario) {
		if (itinerario == null) {
			return null;
		}

		ItinerarioDto result = new ItinerarioDto(itinerario.getId(), itinerario.getOrdem(), itinerario.getLatitude(), itinerario.getLongitude());

		if (itinerario.getLinha() != null) {
			result.setIdLinha(itinerario.getLinha().getId());
		}

		return result;
	}

	public List<ItinerarioDto> toDtoList(List<Itinerario> itinerarios) {
		List<ItinerarioDto> result = new ArrayList<>();

		if (itinerarios != null && !itinerarios.isEmpty()) {
			for (Itinerario itinerario : itinerarios) {
				result.add(this.toDto(itinerario));
			}
		}

		return result;
	}

}
