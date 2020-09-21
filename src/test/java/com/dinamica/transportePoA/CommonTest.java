package com.dinamica.transportePoA;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonTest {

    protected String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
