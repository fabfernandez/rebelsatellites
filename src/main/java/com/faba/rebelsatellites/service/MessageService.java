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

        ArrayList<ArrayList<String>> msgList =
                satellites.stream().map(Satellite::getMessage)
                        .collect(toCollection(ArrayList::new));

        if (messagesAreEmpty(msgList)) {
            throw new EmptyMessagesException("All messages recieved from the satellites are empty.");
            //TODO TEST THIS
        }
        //obtain words that make up the message
        List<String> msgWords = getMsgWords(msgList);

        //sort msgList by size, ascending order
        msgList.sort(Comparator.comparing(List::size));
        //remove message lag based on the shortest msg recieved
        //int msgSize = msgList.get(0).size();
        int msgSize = getShortestMessageSizeWithoutLag(msgList);
        removeLag(msgList, msgSize);

        validateMessageCanBeDeciphered(msgList, msgWords.size());

        String decipheredMessage = buildMessage(msgList, msgSize);

        String[] words = decipheredMessage.split("\\s+"); //split in every space
        if (words.length != msgSize) throw new NotEnoughWordsToDecipherMessageException
                ("There isn't enough information in the satellites to dechiper the message");

        return decipheredMessage;
    }

    private int getShortestMessageSizeWithoutLag(ArrayList<ArrayList<String>> msgList) {
        for (ArrayList<String> message : msgList) {
            for (int i = 0, messageSize = message.size(); i < messageSize; i++) {
                String word = message.get(i);
                if (!word.equals("")) {
                    return message.size();
                }
            }
        }
        return 0;
    }


    private String buildMessage(ArrayList<ArrayList<String>> msgList, int msgSize) {
        //At this point we should have lists of the same size inside the nested list.
        //So each word we find is part of the original message.
        String message = "";

        for (int elem = 0; elem < msgSize; elem++) {
            //elem is the position on the string list.

            for (int list = 0; list < msgList.size(); list++) {
                //list is the index of the list inside the nested list.

                String word = msgList.get(list).get(elem);

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

    private List<String> getMsgWords(ArrayList<ArrayList<String>> msgList) {

        List<String> listWords = new ArrayList<>();
        for (List<String> msg : msgList) {
            listWords = Stream.concat(listWords.stream(), msg.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        listWords.remove("");
        return listWords;
    }

    private void removeLag(ArrayList<ArrayList<String>> msgList, int msgSize) {
        for (int i = 0; i < msgList.size(); i++) {
            int size = msgList.get(i).size();
            msgList.set(i, new ArrayList<>(msgList.get(i).subList(size - msgSize, size)));
        }
    }

    private void validateMessageCanBeDeciphered(ArrayList<ArrayList<String>> messages, int quantityOfWords) {
        for (List<String> message : messages) {
            if (message.size() < quantityOfWords) {
                throw new NotEnoughWordsToDecipherMessageException
                        ("There isn't enough information in the satellites to dechiper the message");
                //TODO TEST THIS
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
