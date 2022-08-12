package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;

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

    // this method has been changed to return a list of categories (pathway, curriculum, motivational quote) after getting name
    // functionality to drill down from selecting the category and returning appropriate action needs to be finished
    // additions have been commented and original functionality restored in order to not push broken code
    @Override
    public List<Message> messages(StudentMessage studentMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(studentMessage);
        String userName = getUserNameById(studentMessage.getUserId());
        // First Response Back To Student with Their Name
       if(userName.equals("Default1234User4321")){
           BotMessage greetingMessage = mapCustomMessageToBotMessage("Nice to meet you, " + studentMessage.getBody() + "!");
           updateUserName(studentMessage.getUserId(), studentMessage.getBody());
           messages.add(greetingMessage);
           messages.add(getListOfCategories());
       // Process for Every Message After First Response
       } else {
           String request = studentMessage.getBody().toLowerCase();
//           List<String> categories = listOfCategories();
            List<String> topicsList = listOfTopics();
            List<Message> topicMessages = new ArrayList<>();
           List<Message> messagesFromCategory = new ArrayList<>();
//           for (String category : categories) {
//               if (request.contains(category.toLowerCase())) {
//                   // write a new method that returns list of topics when given a category
//                   messagesFromCategory.addAll(getListOfTopics(category));
//                   break;
//               }
//           }
           for(String topic : topicsList){
               if(request.contains(topic.toLowerCase())){
                   topicMessages.addAll(getPathwayResources(topic));
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

    public List<BotMessage> messageLogic(StudentMessage studentMessage) {
        int userId = studentMessage.getUserId();
        String recievedMessageLowerCase = studentMessage.getBody().toLowerCase();
        List<String> currentDiscussionPosition = getUserCurrentDiscussionPosition(userId);
        String currentKeyword = currentDiscussionPosition.get(2);
        String currentTopic = currentDiscussionPosition.get(1);
        String currentCategory = currentDiscussionPosition.get(0);
        List<BotMessage> topicMessages = new ArrayList<>();

        if(!currentKeyword.equals("None")){
            List<String> subkeywordlist = listOfSubkeywords(currentCategory, currentTopic, currentKeyword);
            for(String subkeyword : subkeywordlist){
                if(recievedMessageLowerCase.contains(subkeyword.toLowerCase())){
                    topicMessages.addAll(getSubkeywordMessages(currentCategory, currentTopic, currentKeyword, subkeyword));
                    break;
                }
            }

        } if(!currentTopic.equals("None") || topicMessages.size() == 0){
            List<String> keywordList = listOfKeywords(currentCategory, currentTopic);
            for(String keyword : keywordList){
                if(recievedMessageLowerCase.contains(keyword.toLowerCase())){
                    topicMessages.addAll(getKeywordMessages(currentCategory, currentTopic, keyword));
                } if(topicMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, currentTopic, keyword);
                }
                break;
            }

        } if(!currentCategory.equals("None") || topicMessages.size() == 0){
            List<String> topicList = listOfTopics(currentCategory);
            for(String topic : topicList){
                if(recievedMessageLowerCase.contains(topic.toLowerCase())){
                    topicMessages.addAll(getTopicMessages(currentCategory, topic));
                } if(topicMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, topic, "None");
                }
                break;
            }

        } if(topicMessages.size() == 0) {
            List<String> allTopicsList = listOfAllTopics();
            for (String topic : allTopicsList) {
                if (recievedMessageLowerCase.contains(topic.toLowerCase())) {
//                    List<String> keywordList = listOfKeywordsByTopic(topic);
//                    for (String keyword : keywordList) {
//                        if (recievedMessageLowerCase.contains(keyword.toLowerCase())) {
//                            topicMessages.addAll(getKeywordMessagesByTopic(topic, keyword));
//                            break;
//                        }
//                    }
//                    if (topicMessages.size() == 0) {
//                        List<String> subkeywordlist = listOfSubkeywordsByTopic(topic);
//                        for (String subkeyword : subkeywordlist) {
//                            if (recievedMessageLowerCase.contains(subkeyword.toLowerCase())) {
//                                topicMessages.addAll(getSubkeywordMessagesByTopic(topic, subkeyword));
//                                break;
//                            }
//                        }
//                    } else {
                        topicMessages.addAll(getTopicMessagesByTopic(topic));
                        break;
//                    }
                }
            }
        }
        return topicMessages;
    }



    public List<BotMessage> getPathwayResources(String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE category = 'Pathway' AND topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessage(results));
        } return topicMessages;
    }

    @Override
    public BotMessage getListOfCategories() {
        String categories = "";
        String sql = "SELECT DISTINCT category_name FROM categories";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        boolean isFirstResult = true;
        while ( results.next() ) {
            if (isFirstResult) {
                categories = results.getString("category_name");
                isFirstResult = false;
            } else {
                categories = categories + ", " + results.getString("category_name");
            }
        }
        String customMessage = "Enter one of the categories below to get started: " + categories + ".";
        BotMessage botMessage = mapCustomMessageToBotMessage(customMessage);
        return botMessage;
    }

    @Override
    public List<String> listOfCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category_name FROM categories";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            categories.add(results.getString("category_name"));
        }
        return categories;
    }

    // method needs to take in String category as argument
    // sql should be updated to "SELECT DISTINCT topic FROM responses WHERE category ILIKE ?"
    // category should be passed to jdbctemplate call
    @Override
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



    // NEW: DISTINCT Database Entry Lists Methods
    // Should be combined with getListofTopics() at some point
    // look into common misspellings
    public List<String> listOfTopics(){
        List<String> topicsList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicsList.add(results.getString("topic"));
        }
        return topicsList;
    }

    public List<String> listOfCategories2(){
        List<String> topicList = new ArrayList<String>();
        String sql = "SELECT DISTINCT category FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicList.add(results.getString("category"));
        }
        return topicList;
    }

    public List<String> listOfTopics(String category){
        List<String> topicList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses WHERE category = ? AND topic != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category);
        while( results.next() ) {
            topicList.add(results.getString("topic"));
        }
        return topicList;
    }

    public List<String> listOfAllTopics(){
        List<String> topicsList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicsList.add(results.getString("topic"));
        }
        return topicsList;
    }

    public List<String> listOfKeywords(String category, String topic){
        List<String> keywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT keyword FROM responses WHERE category = ? AND topic = ? AND keyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic);
        while( results.next() ) {
            keywordList.add(results.getString("keyword"));
        }
        return keywordList;
    }

    public List<String> listOfKeywordsByTopic(String topic){
        List<String> keywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT keyword FROM responses WHERE topic = ? AND keyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while( results.next() ) {
            keywordList.add(results.getString("keyword"));
        }
        return keywordList;
    }

    public List<String> listOfSubkeywords(String category, String topic, String keyword){
        List<String> subkeywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT subkeyword FROM responses WHERE category = ? AND topic = ? AND keyword = ? AND subkeyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword);
        while( results.next() ) {
            subkeywordList.add(results.getString("subkeyword"));
        }
        return subkeywordList;
    }

    public List<String> listOfSubkeywordsByTopic(String topic){
        List<String> subkeywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT subkeyword FROM responses WHERE topic = ? AND subkeyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while( results.next() ) {
            subkeywordList.add(results.getString("subkeyword"));
        }
        return subkeywordList;
    }


    // NEW: Messages Based on Discussion Position Methods
    public List<BotMessage> getTopicMessages(String category, String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword = 'General' AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessage(results));
        } return topicMessages;
    }

    public List<BotMessage> getTopicMessagesByTopic(String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessage(results));
        } return topicMessages;
    }

    public List<BotMessage> getKeywordMessages(String category, String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        // todo: look into whether subkeyword should always be marked General when keyword is marked General
        String sql = "SELECT display, display_type, link FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessage(results));
        } return keywordMessages;
    }

    public List<BotMessage> getKeywordMessagesByTopic(String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessage(results));
        } return keywordMessages;
    }

    public List<BotMessage> getSubkeywordMessages(String category, String topic, String keyword, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessage(results));
        } return subkeywordMessages;
    }

    public List<BotMessage> getSubkeywordMessagesByTopic(String topic, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE  topic ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessage(results));
        } return subkeywordMessages;
    }


    // User Table Methods
    public int getUserId() {
        String sql = "INSERT INTO users (username, current_category, current_topic, current_keyword) VALUES ('Default1234User4321', 'None', 'None', 'None') RETURNING user_id";
        return jdbcTemplate.queryForObject(sql, int.class);
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

    public void updateUserName(int userId, String userName){
        String sql = "UPDATE users SET username = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, userName, userId);
    }

    public List<String> getUserCurrentDiscussionPosition(int userId){
        List<String> currentDiscussionPosition = new ArrayList<String>();
        String sql = "SELECT current_category, current_topic, current_keyword FROM users WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            currentDiscussionPosition.add(result.getString("current_category"));
            currentDiscussionPosition.add(result.getString("current_topic"));
            currentDiscussionPosition.add(result.getString("current_keyword"));
        }
        return currentDiscussionPosition;
    }

    public void updateUserCurrentDiscussionPosition(int userId, String category, String topic, String keyword){
        String sql = "UPDATE users SET current_category = ?, current_topic = ?, current_keyword = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, category, topic, keyword, userId);
    }



    // Map To Message Methods
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
