package com.dinamica.transportePoA;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.dinamica.transportePoA.dto.LinhaDto;
import com.dinamica.transportePoA.dto.PontoReferenciaDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OnibusControllerTest extends CommonTest {

    private final MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findLinhaByNameTest() {
		try {
			this.mockMvc.perform(get(new URI("/onibus/buscarLinhaPeloNome/xxxx")).contentType(this.jsonContentType)).andExpect(status().isBadRequest());

			this.mockMvc.perform(get(new URI("/onibus/buscarLinhaPeloNome/maio")).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType))
				.andExpect(jsonPath("$.length()", is(4)));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/buscarLinhaPeloNome calling");
		} catch (Exception e) {
			fail("Error on /onibus/buscarLinhaPeloNome calling");
		}
    }

    @Test
    public void findLinhaByCodeTest() {
		try {
			this.mockMvc.perform(get(new URI("/onibus/buscarLinhaPeloCodigo/250-1")).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType))
				.andExpect(jsonPath("$.length()", is(1)));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/buscarLinhaPeloCodigo calling");
		} catch (Exception e) {
			fail("Error on /onibus/buscarLinhaPeloCodigo calling");
		}
    }

    @Test
    public void insertLineTest() {
		try {
			LinhaDto linha = new LinhaDto(null, "250-3", "Zona Sul");
			this.mockMvc.perform(post(new URI("/onibus/inserirLinha")).content(asJsonString(linha)).contentType(this.jsonContentType)).andExpect(status().isCreated())
				.andExpect(content().contentType(this.jsonContentType));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/inserirLinha calling");
		} catch (Exception e) {
			fail("Error on /onibus/inserirLinha calling");
		}
	}

    @Test
    public void updateLineTest() {
		try {
			LinhaDto linha = new LinhaDto(5542, "1493-1", "ICARAI/MENINO DEUS");
			this.mockMvc.perform(post(new URI("/onibus/editarLinha")).content(asJsonString(linha)).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/editarLinha calling");
		} catch (Exception e) {
			fail("Error on /onibus/editarLinha calling");
		}
    }

    @Test
    public void deleteLineTest() {
		try {
			this.mockMvc.perform(delete(new URI("/onibus/removerLinha/6031")).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/removerLinha calling");
		} catch (Exception e) {
			fail("Error on /onibus/removerLinha calling");
		}
    }

    @Test
    public void findLinesWithinRadiusTest() {
		try {
			PontoReferenciaDto ponto = new PontoReferenciaDto(new Float(-30.052785), new Float(-51.144045), new Float(0.5));

			this.mockMvc.perform(post(new URI("/onibus/linhasDentroRaio")).content(asJsonString(ponto)).contentType(this.jsonContentType)).andExpect(status().isOk())
				.andExpect(content().contentType(this.jsonContentType)).andExpect(jsonPath("$.length()", is(5)));
		} catch (URISyntaxException e) {
			fail("Invalid URI on /onibus/linhasDentroRaio calling");
		} catch (Exception e) {
			fail("Error on /onibus/linhasDentroRaio calling");
		}
    }
}
