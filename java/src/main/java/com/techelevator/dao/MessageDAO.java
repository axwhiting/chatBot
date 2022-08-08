package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;

import java.util.List;

public interface MessageDAO {

    List<BotMessage> messages(String studentMessage);
}
