package com.faba.rebelsatellites.service;

import com.faba.rebelsatellites.exceptions.EmptyMessagesException;
import com.faba.rebelsatellites.exceptions.NotEnoughWordsToDecipherMessageException;
import com.faba.rebelsatellites.model.Satellite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MessageServiceTest {

    private final MessageService messageService;

    @Autowired
    public MessageServiceTest(MessageService messageService) {
        this.messageService = messageService;
    }

    @Test
    void decipherSimpleMessage() {
        String expectedMessage = "this is a secret message";

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

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherLongMessage() {
        String expectedMessage = "this is a much longer message hail the empire and the dark lord send more troopers";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", //lag
                        "", "is", "", "much", "", "message", "", "the", "", "and", "", "dark", "", "send", "",
                        "troopers")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("this", "", "", "much", "", "", "hail", "", "", "and", "", "", "lord", "", "", "troopers")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", //lag
                        "", "", "a", "", "longer", "message", "", "the", "empire", "", "the", "dark", "lord", "",
                        "more", "troopers")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherOneFullMsgAndTwoEmpty() {
        String expectedMessage = "we need to go deeper";

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

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherOneWordOnEachMsg() {
        String expectedMessage = "send more troopers";

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

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherWithoutAllTheWordsFails() {
        String expectedMessage = "send more troopers";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "", "", "troopers")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", //lag
                        "", "more", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", //lag
                        "", "", "")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertNotEquals(expectedMessage, message);
    }

    @Test
    void decipherOneEmptyMsgAndTwoIncomplete() {
        String expectedMessage = "we are under attack";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "we", "", "", "attack")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", //lag
                        "", "", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", //lag
                        "", "are", "under", "")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherFromFourSources() {
        String expectedMessage = "we are under attack help";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "we", "", "", "attack", "")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", //lag
                        "", "", "", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", //lag
                        "", "are", "under", "", "")
        );
        ArrayList<String> msg4 = new ArrayList<>(
                List.of( //lag
                        "", "", "", "", "help")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build(),
                        Satellite.builder().message(msg4).build()));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void decipherFromFiveSources() {
        String expectedMessage = "we are under attack help";

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",  //lag
                        "", "are", "", "attack", "")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", //lag
                        "", "", "under", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", //lag
                        "", "are", "", "attack", "help")
        );
        ArrayList<String> msg4 = new ArrayList<>(
                List.of("", "", //lag
                        "", "", "", "", "help")
        );
        ArrayList<String> msg5 = new ArrayList<>(
                List.of( //lag
                        "we", "", "", "", "help")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build(),
                        Satellite.builder().message(msg4).build(),
                        Satellite.builder().message(msg5).build()
                ));

        String message = messageService.getMessage(satellites);

        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    void whenAllMessagesAreEmptyExceptionIsThrown() {

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("", "", "", "", "", "",
                        "", "", "", "")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "",
                        "", "", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "",
                        "", "", "", "")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        assertThrows(EmptyMessagesException.class, () -> messageService.getMessage(satellites));
    }

    @Test
    void whenOneMessageIsShorterThanTheAmountOfWordsFoundExceptionIsThrown() {

        ArrayList<String> msg1 = new ArrayList<>(
                List.of("this", "are", "many", "words", "more", "than", "expected", "yeah")
        );
        ArrayList<String> msg2 = new ArrayList<>(
                List.of("", "", "", "")
        );
        ArrayList<String> msg3 = new ArrayList<>(
                List.of("", "", "", "", "", "", "", "", "", "", "", "", "", "", "yeah")
        );

        ArrayList<Satellite> satellites = new ArrayList<>(
                List.of(Satellite.builder().message(msg1).build(),
                        Satellite.builder().message(msg2).build(),
                        Satellite.builder().message(msg3).build()));

        assertThrows(NotEnoughWordsToDecipherMessageException.class, () -> messageService.getMessage(satellites));
    }

}
