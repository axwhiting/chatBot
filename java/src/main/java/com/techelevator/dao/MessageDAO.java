package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;
import com.techelevator.model.MotivationalQuote;


import java.util.List;

public interface MessageDAO {

    List<Message> messages(StudentMessage studentMessage);
    BotMessage getListOfTopics();

    List<BotMessage> getInitialMessages();

}


