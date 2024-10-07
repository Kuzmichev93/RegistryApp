package app.controller.filter;


import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CustomFilterTest {
    private final String url = "http://localhost:8080/api/user/search";

    @Autowired
    private MockMvc mockMvc;


    @Test
    void searchGet() throws Exception {
        //Проверка сработает, если отправлен любой метод кроме POST
        JSONObject data = new JSONObject();
        data.put("Error","Метод обрабатывает только POST запрос");
        var request = get(url);
        mockMvc.perform(request).andExpectAll(status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(String.valueOf(data)));

    }
    @Test
    void searchPostAndContentLength() throws Exception {
        //Проверка сработает,если отравлен метод POST и тело запроса 0 байт
        JSONObject data = new JSONObject();
        data.put("Error","Метод POST должен содержать тело запроса");
        var request = post(url).header("Content-Length","0");
        mockMvc.perform(request).andExpectAll(status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void searchPostAndContentType() throws Exception {
        //Проверка сработает,если отравлен метод POST и тело запроса > 0 байт и content- type != application-json
        JSONObject data = new JSONObject();
        data.put("Error","Метод принимает данные только в формате json");
        var request = post(url).contentType(MediaType.APPLICATION_ATOM_XML).header("Content-Length","1");
        mockMvc.perform(request).andExpectAll(status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(String.valueOf(data)));

    }


}
