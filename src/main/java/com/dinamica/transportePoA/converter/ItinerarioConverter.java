package com.dinamica.transportePoA.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinamica.transportePoA.dto.ItinerarioDto;
import com.dinamica.transportePoA.model.Itinerario;

@Service
public class ItinerarioConverter {

	@Autowired
	private LinhaConverter linhaConverter;

	public Itinerario fromDto(ItinerarioDto itinerario) {
		if (itinerario == null) {
			return null;
		}

		Itinerario result = new Itinerario(itinerario.getId(), itinerario.getOrdem(), itinerario.getLatitude(), itinerario.getLongitude());

		if (itinerario.getLinha() != null) {
			result.setLinha(this.linhaConverter.fromDto(itinerario.getLinha()));
		}

		return result;
	}

	public ItinerarioDto toDto(Itinerario itinerario) {
		if (itinerario == null) {
			return null;
		}

		ItinerarioDto result = new ItinerarioDto(itinerario.getId(), itinerario.getOrdem(), itinerario.getLatitude(), itinerario.getLongitude());

		if (itinerario.getLinha() != null) {
			result.setLinha(this.linhaConverter.toDto(itinerario.getLinha()));
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
