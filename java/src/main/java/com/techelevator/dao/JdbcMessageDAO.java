package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;

import org.springframework.expression.spel.support.BooleanTypedValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class JdbcMessageDAO implements MessageDAO{

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> messages(StudentMessage studentMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(studentMessage);
        String userName = getUserNameById(studentMessage.getUserId());
       if(userName.equals("Default1234User4321")){
           BotMessage greetingMessage = mapCustomMessageToBotMessage("Hi " + studentMessage.getBody());
           updateUserName(studentMessage.getUserId(), studentMessage.getBody());
           messages.add(greetingMessage);
           messages.add(getListOfTopics());
       } else {
           List<String> topicsList = listOfTopics();
           List<Message> topicMessages = new ArrayList<>();
           for(String topic : topicsList){
               String request = studentMessage.getBody().toLowerCase();
               if(request.contains(topic.toLowerCase())){
                   topicMessages.addAll(getResources(topic));
                   break;
               }
           }
           if(topicMessages.size() > 0){
               messages.addAll(topicMessages);
           } else {
               BotMessage didntUnderstandMessage = mapCustomMessageToBotMessage("I'm sorry. I didn't quite understand. Try using a term like resume or elevator pitch in your message.");
                messages.add(didntUnderstandMessage);
           }
       }
        return messages;
    }

    public void updateUserName(int userId, String userName){
        String sql = "UPDATE users SET username = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, userName, userId);
    }

    public String getUserNameById(int userId){
        String sql = "SELECT username FROM users WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        String username = "";
        if (result.next()) {
            username = result.getString("username");
        }
      return username;
    }

    public List<BotMessage> getResources(String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE category = 'Pathway' AND topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessage(results));
        } return topicMessages;

    }

    public BotMessage getListOfTopics(){
        String topicsList = "";
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        boolean isfirstResult = true;
        while( results.next() ) {
            if(isfirstResult){
                topicsList = results.getString("topic");
                isfirstResult = false;
            } else {
                topicsList = topicsList + ", " + results.getString("topic");
            }
        }
        String customMessage = "I'm happy discuss to following topics with you: " + topicsList + ". Which topic would you like to discuss?";
        BotMessage botMessage = mapCustomMessageToBotMessage(customMessage);
        return botMessage;
    }

    // Should be combined with getListofTopics() at some point
    public List<String> listOfTopics(){
        List<String> topicsList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicsList.add(results.getString("topic"));
        }
        return topicsList;
    }

    @Override
    public List<BotMessage> getInitialMessages() {
       BotMessage firstMessage = mapCustomMessageToBotMessage("Hi!");
       BotMessage secondMessage = mapCustomMessageToBotMessage("What can I call you?");
       int userId = getUserId();
       firstMessage.setUserId(userId);
       secondMessage.setUserId(userId);
       String sql = "INSERT INTO messagelog (user_id,body,sender,type,link) " + " VALUES (?,?,?,?,?) RETURNING message_id";
       firstMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, firstMessage.getUserId(), firstMessage.getBody(),
               firstMessage.getSender(), firstMessage.getType(), firstMessage.getLink()));
       secondMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, secondMessage.getUserId(), secondMessage.getBody(),
               secondMessage.getSender(), secondMessage.getType(), secondMessage.getLink()));
       List<BotMessage> initialMessages = new ArrayList<BotMessage>();
       initialMessages.add(firstMessage);
       initialMessages.add(secondMessage);
       return initialMessages;
    }

    public int getUserId() {
        String sql = "INSERT INTO users (username) VALUES ('Default1234User4321') RETURNING user_id";
        return jdbcTemplate.queryForObject(sql, int.class);
    }

    private BotMessage mapRowToBotMessage(SqlRowSet row) {
        BotMessage message = new BotMessage();
        message.setBody(row.getString("display"));
        message.setLink(row.getString("link"));
        message.setType(row.getString("display_type"));
        message.setSender("bot");
        return message;
    }

    private BotMessage mapCustomMessageToBotMessage(String customMessage) {
        BotMessage message = new BotMessage();
        message.setBody(customMessage);
        message.setLink("n/a");
        message.setType("text");
        message.setSender("bot");

        return message;
    }
}
