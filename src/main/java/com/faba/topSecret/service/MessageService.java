package com.faba.topSecret.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MessageService {

    public String getMessage(ArrayList<ArrayList<String>> msgList) throws RuntimeException {

        //obtain words that make up the message
        List<String> msgWords = getMsgWords(msgList);
        if (!validateMessagesSize(msgList, msgWords.size()))
            throw new RuntimeException("Tamaño del mensaje incorrecto");

        //sort msgList by size, ascending order
        msgList.sort(Comparator.comparing(List::size));

        //remove message lag based on the shortest msg recieved
        removeLag(msgList, msgList.get(0).size());

        return buildMessage(msgList);
    }

    private String buildMessage(ArrayList<ArrayList<String>> msgList) {
        //At this point we should have lists of the same size inside the nested list.
        //So each word we find is part of the original message.
        String message = "";

        for (int elem = 0; elem < msgList.get(0).size(); elem++) {
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

        List<String> listWords = new ArrayList<String>();
        for (List<String> msg : msgList) {
            listWords = Stream.concat(listWords.stream(), msg.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        listWords.remove("");
        return listWords;
    }

    private ArrayList<ArrayList<String>> removeLag(ArrayList<ArrayList<String>> msgList, int msgSize) {
        int s = 0;
        for (int i = 0; i < msgList.size(); i++) {
            s = msgList.get(i).size();
            msgList.set(i, new ArrayList<>(msgList.get(i).subList(s - msgSize, s)));
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
