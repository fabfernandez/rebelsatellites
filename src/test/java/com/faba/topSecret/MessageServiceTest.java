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
    void decipherSimpleMessage() {
        String solution = "this is a secret message";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("",  //lag
                        "this", "is", "a", "secret", "message")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("this", "", "a", "secret", "message")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", //lag
                        "", "is", "a", "", "message")
        );

        ArrayList<ArrayList<String>> msgList = new ArrayList<>(List.of(msg1, msg2, msg3));

        String message = messageService.getMessage(msgList);

        Assertions.assertEquals(message, solution);
    }

    @Test
    void decipherLongMessage() {
        String solution = "this is a much longer message hail the empire and the dark lord send more troopers";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", //lag
                        "", "is", "", "much", "", "message", "", "the", "", "and", "", "dark", "", "send", "", "troopers")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("this", "", "", "much", "", "", "hail", "", "", "and", "", "", "lord", "", "", "troopers")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", //lag
                        "", "", "a", "", "longer", "message", "", "the", "empire", "", "the", "dark", "lord", "", "more", "troopers")
        );

        ArrayList<ArrayList<String>> msgList = new ArrayList<>(List.of(msg1, msg2, msg3));

        String message = messageService.getMessage(msgList);

        Assertions.assertEquals(message, solution);
    }

    @Test
    void decipherOneFullMsgAndTwoEmpty() {
        String solution = "we need to go deeper";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", //lag
                        "", "", "", "", "")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("",  //lag
                        "we", "need", "to", "go", "deeper")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", //lag
                        "", "", "", "", "")
        );

        ArrayList<ArrayList<String>> msgList = new ArrayList<>(List.of(msg1, msg2, msg3));

        String message = messageService.getMessage(msgList);

        Assertions.assertEquals(message, solution);
    }

    @Test
    void decipherOneWordOnEachMsg() {
        String solution = "send more troopers";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "", "", "troopers")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", //lag
                        "", "more", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", //lag
                        "send", "", "")
        );

        ArrayList<ArrayList<String>> msgList = new ArrayList<>(List.of(msg1, msg2, msg3));

        String message = messageService.getMessage(msgList);

        Assertions.assertEquals(message, solution);
    }

}
