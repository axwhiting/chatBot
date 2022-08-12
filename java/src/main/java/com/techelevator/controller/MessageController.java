package com.techelevator.controller;

import com.techelevator.dao.MessageDAO;
import com.techelevator.model.BotMessage;
import com.techelevator.model.Message;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.MotivationalQuote;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.List;

@RestController
@CrossOrigin
public class MessageController {

    private MessageDAO messageDAO;
    RestTemplate restTemplate = new RestTemplate();

    public MessageController(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public List<Message> sendResponse(@RequestBody StudentMessage studentMessage){
        return messageDAO.messages(studentMessage);
    }

//    @RequestMapping(value = "/topics", method = RequestMethod.GET)
//    public BotMessage topicsList(){
//        return messageDAO.getListOfTopics();
//    }

    @RequestMapping(value = "/messages/welcome", method = RequestMethod.GET)
    public List<BotMessage> getInitialMessages() { return messageDAO.getInitialMessages();
    }

    @RequestMapping(value = "/quote")
    public BotMessage getQuote() {
        String url = "https://zenquotes.io/api/random";
        MotivationalQuote[] quoteArray = restTemplate.getForObject(url, MotivationalQuote[].class);
        BotMessage botMessage = new BotMessage();
        botMessage.setBody(quoteArray[0].getQ() + " ~" + quoteArray[0].getA());
        botMessage.setLink("n/a");
        botMessage.setType("quote");
        botMessage.setSender("bot");
        return botMessage;
    }

}
