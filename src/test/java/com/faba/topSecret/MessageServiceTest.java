package com.faba.topSecret;

import com.faba.topSecret.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Test
    void getMessage() {
        ArrayList<String> satellite1 = new ArrayList<>(
                List.of("", "this", "is", "a", "secret", "message")
        );
        ArrayList<String> satellite2 = new ArrayList<>(
                List.of("this", "", "a", "secret", "message")
        );
        ArrayList<String> satellite3 = new ArrayList<>(
                List.of("", "", "is", "a", "", "message")
        );

        String solution = "this is a secret message";

        String message = messageService.getMessage(satellite1, satellite2, satellite3);

        Assertions.assertEquals(message, solution);

    }
}
