package com.techelevator.controller;

import com.techelevator.dao.MessageDAO;
import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
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
    public List<BotMessage> sendResponse(@RequestBody StudentMessage studentMessage){
        return messageDAO.messages(studentMessage);
    }
}
