package com.faba.rebelsatellites.service;

import com.faba.rebelsatellites.exceptions.EmptyMessagesException;
import com.faba.rebelsatellites.exceptions.NotEnoughWordsToDecipherMessageException;
import com.faba.rebelsatellites.model.Satellite;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

@Service
public class MessageService {

    public String getMessage(ArrayList<Satellite> satellites) {

        ArrayList<ArrayList<String>> messages =
                satellites.stream().map(Satellite::getMessage)
                        .collect(toCollection(ArrayList::new));

        if (messagesAreEmpty(messages)) {
            throw new EmptyMessagesException("All messages received from the satellites are empty.");
        }
        //obtain words that make up the message
        List<String> msgWords = getMsgWords(messages);
        validateMessagesSize(messages, msgWords.size());
        //sort messages by size, ascending order
        messages.sort(Comparator.comparing(List::size));

        int shortestMsgSize = messages.get(0).size();
        //remove message lag based on the shortest msg received
        removeLag(messages, shortestMsgSize);

        return buildMessage(messages, shortestMsgSize);
    }


    private String buildMessage(ArrayList<ArrayList<String>> messages, int msgSize) {
        //At this point we should have lists of the same size inside the nested list.
        //So each word we find is part of the original message.
        String message = "";

        for (int elem = 0; elem < msgSize; elem++) {
            //elem is the string list index.

            for (int list = 0; list < messages.size(); list++) {
                //list is the nested list index.

                String word = messages.get(list).get(elem);

                if (!word.equals("")) {
                    if (message.equals("")) {
                        message = word;
                    } else {
                        message = message.concat(" " + word);
                    }
                    break;
                }
            }
        }
        return message;
    }

    private List<String> getMsgWords(ArrayList<ArrayList<String>> messages) {

        List<String> wordList = new ArrayList<>();
        for (List<String> msg : messages) {
            wordList = Stream.concat(wordList.stream(), msg.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        wordList.remove("");
        return wordList;
    }

    private void removeLag(ArrayList<ArrayList<String>> messages, int shortestMsgSize) {
        for (int msgIndex = 0; msgIndex < messages.size(); msgIndex++) {
            int size = messages.get(msgIndex).size();
            messages.set(
                    msgIndex, new ArrayList<>(messages.get(msgIndex).subList(size - shortestMsgSize, size))
            );
        }
    }

    private void validateMessagesSize(ArrayList<ArrayList<String>> messages, int wordCount) {
        for (List<String> message : messages) {
            if (message.size() < wordCount) {
                throw new NotEnoughWordsToDecipherMessageException
                        ("There isn't enough information in the satellites to decipher the message");
            }
        }
    }

    private boolean messagesAreEmpty(ArrayList<ArrayList<String>> messages) {
        for (List<String> message : messages) {
            for (String word : message) {
                if (!word.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
