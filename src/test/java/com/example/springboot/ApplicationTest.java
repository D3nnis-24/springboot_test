package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    //A user posts a string and then deletes it. After accessing the history the string cannot be found
    @Test
    void testPostDeleteHistorySequence() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=Hello")).andReturn(); //post string "Hello"
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=Hello")).andReturn(); //delete string "Hello"

        //access history, "Hello" should not be a part of history
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(not(containsString("Hello"))));
    }

    //Check if the delete functionality is case-sensitive
    @Test
    void testDeleteCaseSensitive() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=abc")).andReturn(); //post "abc"
        mockMvc.perform(MockMvcRequestBuilders.post("/api?post_input_text=Abc")).andReturn(); //post "Abc"
        mockMvc.perform(MockMvcRequestBuilders.post("/delete?post_text=abc")).andReturn(); //delete "abc"

        //access history, "Abc" should still be a part of the history if delete is case-sensitive
        mockMvc.perform(MockMvcRequestBuilders.get("/history").contentType(MediaType.ALL))
                .andExpect(content().string(containsString("Abc")));
    }

}