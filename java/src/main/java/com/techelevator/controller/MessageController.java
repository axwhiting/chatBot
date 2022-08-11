package com.techelevator.controller;

import com.techelevator.dao.MessageDAO;
import com.techelevator.model.BotMessage;
import com.techelevator.model.Message;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.MotivationalQuote;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class MessageController {

    private MessageDAO messageDAO;

    public MessageController(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public List<Message> sendResponse(@RequestBody StudentMessage studentMessage){
        return messageDAO.messages(studentMessage);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public BotMessage categoryList() {
        return messageDAO.getListOfCommands();
    }

//    @RequestMapping(value = "/topics", method = RequestMethod.GET)
//    public BotMessage topicsList(){
//        return messageDAO.getListOfTopics();
//    }

    @RequestMapping(value = "/messages/welcome", method = RequestMethod.GET)
    public List<BotMessage> getInitialMessages() { return messageDAO.getInitialMessages();
    }

}
