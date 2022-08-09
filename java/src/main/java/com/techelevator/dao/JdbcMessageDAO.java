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

@Component
public class JdbcMessageDAO implements MessageDAO{

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override

    public List<Message> messages(StudentMessage studentMessage) {
        List<Message> messages = new ArrayList<>();
//        String sql = "SELECT display, display_type, link FROM responses WHERE category = 'Pathway' AND topic ILIKE ?";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, studentMessage.getBody());
//        while (results.next()) {
//            messages.add(mapRowToBotMessage(results));
//        }
        messages.add(studentMessage);
       String userName = getUserNameById(studentMessage.getUserId());
       if(!(userName.equals("Default1234User4321"))){
           BotMessage greetingMessage = mapCustomMessageToBotMessage("Hi" + studentMessage.getBody());
           updateUserName(studentMessage.getUserId(), studentMessage.getBody());
           messages.add(greetingMessage);
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

      return String.valueOf(result);
    }

    public BotMessage getListOfTopics(){
        String topicsList = "";
        String sql = "SELECT DISTINCT topic from responses";
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
        return message;
    }

    private BotMessage mapCustomMessageToBotMessage(String customMessage) {
        BotMessage message = new BotMessage();
        message.setBody(customMessage);
        message.setLink("n/a");
        message.setType("text");
        return message;
    }


}
