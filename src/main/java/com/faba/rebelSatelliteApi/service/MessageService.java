package com.faba.rebelSatelliteApi.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {

    public String getMessage(ArrayList<ArrayList<String>> msgList) throws RuntimeException {

        //TODO: VALIDAR QUE VENGA POR LO MENOS UN MENSAJE en msgList

        //obtain words that make up the message
        List<String> msgWords = getMsgWords(msgList);
        if (!validateMessagesSize(msgList, msgWords.size()))
            throw new RuntimeException("Tamaño del mensaje incorrecto");
        //TODO: hacer una excepcion personalizada

        //sort msgList by size, ascending order
        msgList.sort(Comparator.comparing(List::size));

        int msgSize = msgList.get(0).size();

        //remove message lag based on the shortest msg recieved
        removeLag(msgList, msgSize);

        return buildMessage(msgList, msgSize);
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

    private ArrayList<ArrayList<String>> removeLag(ArrayList<ArrayList<String>> msgList, int msgSize) {
        for (int i = 0; i < msgList.size(); i++) {
            int size = msgList.get(i).size();
            msgList.set(i, new ArrayList<>(msgList.get(i).subList(size - msgSize, size)));
        }
        return msgList;
    }

    private boolean validateMessagesSize(ArrayList<ArrayList<String>> messages, int size) {
        for (List<String> m : messages) {
            if (m.size() < size) {
                return false;
            }
        }
        return true;
    }
}
