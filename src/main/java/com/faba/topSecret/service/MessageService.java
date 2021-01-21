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
        msgList = removeLag(msgList, msgList.get(0).size());
        //String message = completeMessage(msgList);
        //String message = buildMessage(msgList);
        String message = buildMessage(msgList);

        return message;
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
                        break;
                    } else {
                        message = message.concat(" " + word);
                        break;
                    }
                }
            }
        }
        return message;
    }

    private String buildMessage1(ArrayList<ArrayList<String>> msgList) {

        List<String> messageAsList = msgList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        String message = "";

        for (String element : messageAsList) {
            message.concat(element);
        }
        return message;
    }

    public List<String> getMsgWords(ArrayList<ArrayList<String>> msgList) {

        List<String> listWords = new ArrayList<String>();
        for (List<String> msg : msgList) {
            listWords = Stream.concat(listWords.stream(), msg.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        listWords.remove("");
        return listWords;
    }

    public ArrayList<ArrayList<String>> removeLag(ArrayList<ArrayList<String>> msgList, int msgSize) {
        int s = 0;
        for (int i = 0; i < msgList.size(); i++) {
            s = msgList.get(i).size();
            msgList.set(i, new ArrayList<>(msgList.get(i).subList(s - msgSize, s)));
        }
        return msgList;

        //msgList.stream().forEach(s -> s.subList(s.size()-lagSize, s.size()));
    }

    public String completeMessage(ArrayList<ArrayList<String>> msgList) {

        String phrase = "";
        for (List<String> message : msgList) {

            if (!message.isEmpty() && !message.get(0).equals("")) {
                phrase = (message.size() == 1) ? message.get(0) : message.get(0) + " ";
                msgList.stream().forEach(s -> s.remove(0));
                return phrase + completeMessage(msgList);
            }
        }
        //if(messages.get(0).size()>0)
        return "";
    }


    public boolean validateMessagesSize(ArrayList<ArrayList<String>> messages, int size) {
        for (List<String> m : messages) {
            if (m.size() < size) {
                return false;
            }
        }
        return true;
    }

    public void validateMessagePhrases(List<String> words, String message) {
        for (String word : words) {
            Assert.hasText(word, message);
        }
    }
}
