package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;
import com.techelevator.model.MotivationalMessage;


import java.util.List;

public interface MessageDAO {

    List<Message> messages(StudentMessage studentMessage);
    BotMessage getListOfTopics();

    List<BotMessage> getInitialMessages();

    MotivationalMessage getMotivationalMessage();

}


