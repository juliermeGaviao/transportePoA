package com.dinamica.transportePoA;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.dinamica.transportePoA.dto.PontoReferenciaDto;
import com.dinamica.transportePoA.dto.PontoTaxiDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PontoTaxiControllerTest extends CommonTest {

    private final MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void insertAndDeleteTaxiPointTest() {
		try {
			PontoTaxiDto pontoTaxiDto = new PontoTaxiDto("PONTO-ZONA-LESTE-3", new Float(-30.052785), new Float(-51.144045));

			this.mockMvc.perform(post(new URI("/taxi/inserirPontoTaxi")).content(asJsonString(pontoTaxiDto)).contentType(this.jsonContentType))
				.andExpect(content().contentType(this.jsonContentType))
				.andExpect(status().isCreated());

			this.mockMvc.perform(delete(new URI("/taxi/removerPontoTaxi/PONTO-ZONA-LESTE-3")).content(asJsonString(pontoTaxiDto)).contentType(this.jsonContentType))
				.andExpect(content().contentType(this.jsonContentType))
				.andExpect(status().isOk());
		} catch (Exception e) {
			fail("Error on /taxi/inserirPontoTaxi calling");
		}
    }

    @Test
    public void findByNameTest() {
		try {
			this.mockMvc.perform(get(new URI("/taxi/buscarPontoTaxi/sul")).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType))
				.andExpect(jsonPath("$.length()", is(1)));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /taxi/buscarPontoTaxi calling");
		} catch (Exception e) {
			fail("Error on /taxi/buscarPontoTaxi calling");
		}
    }

    @Test
    public void findTaxiPointsWithinRadiusTest() {
		try {
			PontoReferenciaDto ponto = new PontoReferenciaDto(new Float(-30.052785), new Float(-51.144045), new Float(8));

			this.mockMvc.perform(post(new URI("/taxi/pontosTaxiDentroRaio")).content(asJsonString(ponto)).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType)).andExpect(jsonPath("$.length()", is(1)));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /taxi/pontosTaxiDentroRaio calling");
		} catch (Exception e) {
			fail("Error on /taxi/pontosTaxiDentroRaio calling");
		}
    }
}
