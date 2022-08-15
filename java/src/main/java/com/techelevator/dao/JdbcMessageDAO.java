package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;

import com.techelevator.services.QuoteService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class JdbcMessageDAO implements MessageDAO{

    private final JdbcTemplate jdbcTemplate;
    QuoteService quoteService = new QuoteService();

    public JdbcMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> messages(StudentMessage studentMessage) {
        List<BotMessage> botMessages = new ArrayList<>();
        List<Message> outputMessages = new ArrayList<Message>();
        String userName = getUserNameById(studentMessage.getUserId());
        int lastLogQuestionId = getLastLogQuestionIdByUserId(studentMessage.getUserId());

        // First Response Back To Student with Their Name
        if(userName.equals("Default1234User4321") && studentMessage.getSender().equals("click")){
            updateUserName(studentMessage.getUserId(), "Clicked First, No Name Given");
        }
        if(!studentMessage.getSender().equals("click")){
            outputMessages.add(logStudentMessage(studentMessage, studentMessage.getUserId()));
            if(userName.equals("Default1234User4321")) {
                if(studentMessage.getBody().equalsIgnoreCase("Codee")){
                    botMessages.add(mapCustomMessageToBotMessage("That's my name too!","happy"));
                } else {
                    botMessages.add(mapCustomMessageToBotMessage("Nice to meet you, " + studentMessage.getBody() + "!", "happy"));
                }
                updateUserName(studentMessage.getUserId(), studentMessage.getBody());
                botMessages.add(getListOfCategories());
                // Process for Every Message After First Response
            }
        } else if (studentMessage.getBody().toLowerCase().contains("thank")) {
           botMessages.add(mapCustomMessageToBotMessage("You're welcome!", "happy"));
       } else if (lastLogQuestionId != 0) {
           String interviewQuestionAnswer = getInterviewQuestionAnswerByQuestionId(lastLogQuestionId);
           if(!interviewQuestionAnswer.equals("n/a")){
               if(studentMessage.getBody().equalsIgnoreCase(interviewQuestionAnswer)){
                   botMessages.add(mapCustomMessageToBotMessage("You got it right!", "happy"));
               } else {
                   botMessages.add(mapCustomMessageToBotMessage("Sorry the answer was " + interviewQuestionAnswer + ".", "sad"));
               }
           }
       } else if (studentMessage.getBody().toLowerCase().contains("motivat")) {
           BotMessage quote = quoteService.getQuote();
           botMessages.add(quote);
       } else if (studentMessage.getBody().toLowerCase().contains("interview question")){
           botMessages.add(getRandomInterviewQuestion());
       } else {
           List<BotMessage> topicMessages = messageLogic(studentMessage);
           if(topicMessages.size() > 0){
               botMessages.addAll(topicMessages);
           } else {
               BotMessage didntUnderstandMessage = mapCustomMessageToBotMessage("I'm sorry. I didn't quite understand. Try using a term like resume or elevator pitch in your message.", "sad");
                botMessages.add(didntUnderstandMessage);
           }
       }

       outputMessages.add(logStudentMessage(studentMessage, studentMessage.getUserId()));
       outputMessages.addAll(logBotMessages(botMessages, studentMessage.getUserId()));

       return outputMessages;
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

        } if(!currentTopic.equals("None") && topicMessages.size() == 0){
            List<String> keywordList = listOfKeywords(currentCategory, currentTopic);
            for(String keyword : keywordList){
                if(recievedMessageLowerCase.contains(keyword.toLowerCase())){
                    topicMessages.addAll(getKeywordMessages(currentCategory, currentTopic, keyword));
                } if(topicMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, currentTopic, keyword);
                    break;
                }
            }

        } if(!currentCategory.equals("None") && topicMessages.size() == 0){
            List<String> topicList = listOfTopics(currentCategory);
            for(String topic : topicList){
                if(recievedMessageLowerCase.contains(topic.toLowerCase())){
                    topicMessages.addAll(getTopicMessages(currentCategory, topic));
                } if(topicMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, topic, "None");
                    break;
                }
            }

        } if(topicMessages.size() == 0) {
            List<String> allTopicsList = listOfAllTopics();
            for (String topic : allTopicsList) {
                if (recievedMessageLowerCase.contains(topic.toLowerCase())) {
                    List<String> keywordList = listOfKeywordsByTopic(topic);
                    for (String keyword : keywordList) {
                        if (recievedMessageLowerCase.contains(keyword.toLowerCase())) {
                            topicMessages.addAll(getKeywordMessagesByTopic(topic, keyword));
                            updateUserCurrentDiscussionPosition(userId, "Pathway", topic, keyword);
                            break;
                        }
                    }
                    if (topicMessages.size() == 0) {
                        List<String> subkeywordlist = listOfSubkeywordsByTopic(topic);
                        for (String subkeyword : subkeywordlist) {
                            if (recievedMessageLowerCase.contains(subkeyword.toLowerCase())) {
                                topicMessages.addAll(getSubkeywordMessagesByTopic(topic, subkeyword));
                                updateUserCurrentDiscussionPosition(userId, "Pathway", topic, "None");
                                break;
                            }
                        }
                    }
                    if (topicMessages.size() == 0){
                        topicMessages.addAll(getTopicMessagesByTopic(topic));
                        updateUserCurrentDiscussionPosition(userId, "Pathway", topic, "None");
                        break;
                    }
                }
            }
        }
        if(topicMessages.size() == 0) {
            List<String> categories = listOfCategories();
            for(String category : categories){
                if(recievedMessageLowerCase.contains(category.toLowerCase())){
                    topicMessages.add(getListOfTopics());
                } if(topicMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, category, "None", "None");
                    break;
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
            topicMessages.add(mapRowToBotMessageResponse(results));
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
        BotMessage botMessage = mapCustomMessageToBotMessage(customMessage, "magnifier");
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

    @Override
    public BotMessage getListOfTopics(){
        String topicsList = "";
        String sql = "SELECT DISTINCT topic FROM responses WHERE category ILIKE 'Pathway'";
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
        BotMessage botMessage = mapCustomMessageToBotMessage(customMessage, "magnifier");
        return botMessage;
    }

    @Override
    public List<BotMessage> getInitialMessages() {
       BotMessage firstMessage = mapCustomMessageToBotMessage("Hi, I'm Codee!", "hello");
       BotMessage secondMessage = mapCustomMessageToBotMessage("What can I call you?", "happy");
       int userId = getUserId();
       firstMessage.setUserId(userId);
       secondMessage.setUserId(userId);
       String sql = "INSERT INTO messagelog (user_id,sender,display) " + " VALUES (?,?,?) RETURNING message_id";
       firstMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, firstMessage.getUserId(), firstMessage.getSender(), firstMessage.getBody()));
       secondMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, secondMessage.getUserId(), secondMessage.getSender(), secondMessage.getBody()));
       List<BotMessage> initialMessages = new ArrayList<BotMessage>();
       initialMessages.add(firstMessage);
       initialMessages.add(secondMessage);
       return initialMessages;
    }
    public StudentMessage logStudentMessage(StudentMessage studentMessage, int userId) {
        StudentMessage updatedStudentMessage = studentMessage;
        String sql = "INSERT INTO messagelog (user_id,sender,display) " + " VALUES (?,?,?) RETURNING message_id";
        updatedStudentMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, userId, studentMessage.getSender(), studentMessage.getBody()));
        updatedStudentMessage.setUserId(userId);
        return updatedStudentMessage;
    }

    public List<BotMessage> logBotMessages(List<BotMessage> botMessageList, int userId) {
        List<BotMessage> updatedBotMessageList = new ArrayList<BotMessage>();
        String sql = "INSERT INTO messagelog (user_id,sender,display,response_id,question_id) " + " VALUES (?,?,?,?,?) RETURNING message_id";
        for (BotMessage botMessage : botMessageList) {
            botMessage.setUserId(userId);
            botMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, userId, botMessage.getSender(), botMessage.getBody(), botMessage.getResponseId(), botMessage.getQuestionId()));
            updatedBotMessageList.add(botMessage);
        }
        return updatedBotMessageList;
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
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessageResponse(results));
        } return topicMessages;
    }

    public List<BotMessage> getTopicMessagesByTopic(String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessageResponse(results));
        } return topicMessages;
    }

    public List<BotMessage> getKeywordMessages(String category, String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        // todo: look into whether subkeyword should always be marked General when keyword is marked General
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessageResponse(results));
        } return keywordMessages;
    }

    public List<BotMessage> getKeywordMessagesByTopic(String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessageResponse(results));
        } return keywordMessages;
    }

    public List<BotMessage> getSubkeywordMessages(String category, String topic, String keyword, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessageResponse(results));
        } return subkeywordMessages;
    }

    public List<BotMessage> getSubkeywordMessagesByTopic(String topic, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE  topic ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessageResponse(results));
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

    public int getLastLogQuestionIdByUserId(int userId) {
        int lastLogQuestionId = 0;
        String sql = "SELECT question_id FROM messagelog WHERE user_id = ? ORDER BY message_id DESC LIMIT 1;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            lastLogQuestionId = result.getInt("question_id");
        }
        return lastLogQuestionId;
    }



    // Interview Table Methods
    public BotMessage getRandomInterviewQuestion(){
        BotMessage question = null;
        String sql = "SELECT question_id, display, link, display_type, codee_style FROM interview_questions ORDER BY RANDOM () LIMIT 1";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            question = mapRowToBotMessageInterviewQuestion(results);
        }
        return question;
    }

    public String getInterviewQuestionAnswerByQuestionId(int questionId){
        String answer = "n/a";
        String sql = "SELECT answer FROM interview_questions WHERE question_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, questionId);
        if (result.next()) {
            answer = result.getString("answer");
        }
        return answer;
    }



    // Map To Message Methods
    private BotMessage mapRowToBotMessageResponse(SqlRowSet row) {
        BotMessage message = new BotMessage();
        message.setBody(row.getString("display"));
        message.setLink(row.getString("link"));
        message.setType(row.getString("display_type"));
        message.setSender("bot");
        message.setCodeeStyle(row.getString("codee_style"));
        message.setResponseId(row.getInt("response_id"));
        return message;
    }

    private BotMessage mapRowToBotMessageInterviewQuestion(SqlRowSet row) {
        BotMessage message = new BotMessage();
        message.setBody(row.getString("display"));
        message.setLink(row.getString("link"));
        message.setType(row.getString("display_type"));
        message.setSender("bot");
        message.setCodeeStyle(row.getString("codee_style"));
        message.setQuestionId(row.getInt("question_id"));
        return message;
    }

    private BotMessage mapCustomMessageToBotMessage(String customMessage, String codeeStyle) {
        BotMessage message = new BotMessage();
        message.setBody(customMessage);
        message.setLink("n/a");
        message.setType("text");
        message.setSender("bot");
        message.setCodeeStyle(codeeStyle);
        return message;
    }
}
