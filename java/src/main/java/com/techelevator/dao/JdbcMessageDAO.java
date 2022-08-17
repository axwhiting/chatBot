package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import com.techelevator.model.Message;

import com.techelevator.services.MemeService;
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
    MemeService memeService = new MemeService();

    JdbcMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> messages(StudentMessage studentMessage) {
        List<Message> outputMessages = new ArrayList<Message>();
        List<BotMessage> botMessages = new ArrayList<>();

        int userId = studentMessage.getUserId();
        String studentMessageBody = studentMessage.getBody();

        String userNameInDatabase = getUserNameById(userId);
        int lastLogQuestionId = getLastLogQuestionIdByUserId(userId);

        // Handles if user clicks a nav bar button before entering a name
            // Does not send the studentMessage back to the frontend to display if student sends a message via a click
        if(userNameInDatabase.equals("Default1234User4321") && studentMessage.getSender().equals("click")){
            updateUserName(studentMessage.getUserId(), "Clicked First, No Name Given");
        }
        // Sends the StudentMessage back to the frontend to display, as long as student did not send a message via a click
        if(!studentMessage.getSender().equals("click")){
            outputMessages.add(logStudentMessage(studentMessage, studentMessage.getUserId()));
            // Sends Hello BotMessage with username and initial navigation message
            if(userNameInDatabase.equals("Default1234User4321")) {
                botMessages.addAll(buildBotResponsesAfterUsernameEntered(userId, studentMessageBody));
            }
        }
        // Handles if the student thanks the bot
        if (studentMessageBody.toLowerCase().contains("thank") && botMessages.size() == 0) {
           botMessages.add(mapCustomMessageToBotMessage("You're welcome!", "happy"));
       // Handles if user is currently answering an interview question
       } else if (lastLogQuestionId != 0) {
           botMessages.add(determineIfStudentAnsweredQuestionCorrectly(lastLogQuestionId, studentMessageBody));
       // Handles if user wants the bot to send them a motivational quote
       } else if (studentMessageBody.toLowerCase().contains("motivat") && botMessages.size() == 0) {
           botMessages.add(quoteService.getQuote());
       // Handles if user wants the bot to ask them an interview question
       } else if (studentMessageBody.toLowerCase().contains("interview question") && botMessages.size() == 0) {
            botMessages.add(getRandomInterviewQuestion());
        // Handles if user wants the bot to give them a meme
        } else if (studentMessageBody.toLowerCase().contains("meme") && botMessages.size() == 0) {
            botMessages.add(memeService.getMeme());
       // Pulls responses from database for Pathway, Help, About
       } else {
            botMessages.addAll(messageLogic(studentMessage));
       }
       // Handles if bot didn't understand the user's question/message
       if(botMessages.size() == 0 ){
           botMessages.add(mapCustomMessageToBotMessage("I'm sorry. I didn't quite understand. Try using a term like resume or elevator pitch in your message.", "sad"));
       }
       // Logs botMessages in message_log database table
       outputMessages.addAll(logBotMessages(botMessages, studentMessage.getUserId()));

       return outputMessages;
    }

    private List<BotMessage> messageLogic(StudentMessage studentMessage) {
        int userId = studentMessage.getUserId();
        String receivedMessageLowerCase = studentMessage.getBody().toLowerCase();

        // Gets what keyword, topic, and/or category that user and bot are currently discussing
        List<String> currentDiscussionPosition = getUserCurrentDiscussionPosition(userId);
        String currentKeyword = currentDiscussionPosition.get(2);
        String currentTopic = currentDiscussionPosition.get(1);
        String currentCategory = currentDiscussionPosition.get(0);

        List<BotMessage> botMessages = new ArrayList<>();

        if(!currentKeyword.equals("None")){
            List<String> subkeywordlist = listOfSubkeywords(currentCategory, currentTopic, currentKeyword);
            for(String subkeyword : subkeywordlist){
                if(receivedMessageLowerCase.contains(subkeyword.toLowerCase())){
                    botMessages.addAll(getSubkeywordMessages(currentCategory, currentTopic, currentKeyword, subkeyword));
                    break;
                }
            }

        } if(!currentTopic.equals("None") && botMessages.size() == 0){
            List<String> keywordList = listOfKeywords(currentCategory, currentTopic);
            for(String keyword : keywordList){
                if(receivedMessageLowerCase.contains(keyword.toLowerCase())){
                    botMessages.addAll(getKeywordMessages(currentCategory, currentTopic, keyword));
                } if(botMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, currentTopic, keyword);
                    botMessages.add(buildSubkeywordWouldYouLikeToKnowMoreMessage(currentTopic, keyword));
                    break;
                }
            }

        } if(!currentCategory.equals("None") && botMessages.size() == 0){
            List<String> topicList = listOfTopics(currentCategory);
            for(String topic : topicList){
                if(receivedMessageLowerCase.contains(topic.toLowerCase())){
                    botMessages.addAll(getTopicMessages(currentCategory, topic));
                } if(botMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, currentCategory, topic, "None");
                    break;
                }
            }

        } if(botMessages.size() == 0) {
            List<String> allTopicsList = listOfAllTopics();
            for (String topic : allTopicsList) {
                if (receivedMessageLowerCase.contains(topic.toLowerCase())) {
                    List<String> keywordList = listOfKeywordsByTopic(topic);
                    for (String keyword : keywordList) {
                        if (receivedMessageLowerCase.contains(keyword.toLowerCase())) {
                            botMessages.addAll(getKeywordMessagesByTopic(topic, keyword));
                            updateUserCurrentDiscussionPosition(userId, "Pathway", topic, keyword);
                            break;
                        }
                    }
                    if (botMessages.size() == 0) {
                        List<String> subkeywordlist = listOfSubkeywordsByTopic(topic);
                        for (String subkeyword : subkeywordlist) {
                            if (receivedMessageLowerCase.contains(subkeyword.toLowerCase())) {
                                botMessages.addAll(getSubkeywordMessagesByTopic(topic, subkeyword));
                                updateUserCurrentDiscussionPosition(userId, "Pathway", topic, "None");
                                break;
                            }
                        }
                    }
                    if (botMessages.size() == 0){
                        botMessages.addAll(getTopicMessagesByTopic(topic));
                        updateUserCurrentDiscussionPosition(userId, "Pathway", topic, "None");
                        break;
                    }
                }
            }
        }
        if(botMessages.size() == 0) {
            List<String> categories = listOfCategories();
            for(String category : categories){
                if(receivedMessageLowerCase.contains(category.toLowerCase())){
                    botMessages.add(getListOfTopics());
                } if(botMessages.size() > 0){
                    updateUserCurrentDiscussionPosition(userId, category, "None", "None");
                    break;
                }
            }
        }
        return botMessages;
    }

    public List<BotMessage> buildBotResponsesAfterUsernameEntered(int userId, String usernameEntered){
        List<BotMessage> botMessages = new ArrayList<BotMessage>();
        if(usernameEntered.equalsIgnoreCase("Codee")){
            botMessages.add(mapCustomMessageToBotMessage("That's my name too!","happy"));
        } else {
            botMessages.add(mapCustomMessageToBotMessage("Nice to meet you, " + usernameEntered + "!", "happy"));
        }
        updateUserName(userId, usernameEntered);
        botMessages.add(getListOfCategories());
        return botMessages;
    }

    private BotMessage determineIfStudentAnsweredQuestionCorrectly(int lastLogQuestionId, String studentAnswer){
        BotMessage botMessage = null;
        String interviewQuestionAnswer = getInterviewQuestionAnswerByQuestionId(lastLogQuestionId);
        if(!interviewQuestionAnswer.equals("n/a")){
            if(studentAnswer.equalsIgnoreCase(interviewQuestionAnswer)){
                botMessage = mapCustomMessageToBotMessage("You got it right!", "happy");
            } else {
                botMessage = mapCustomMessageToBotMessage("Sorry the answer was " + interviewQuestionAnswer + ".", "sad");
            }
        }
        return botMessage;
    }

    private BotMessage getListOfCategories() {
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

    private List<String> listOfCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category_name FROM categories";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            categories.add(results.getString("category_name"));
        }
        return categories;
    }

    private BotMessage getListOfTopics(){
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

    // Log Messages to message_log database table
    private StudentMessage logStudentMessage(StudentMessage studentMessage, int userId) {
        StudentMessage updatedStudentMessage = studentMessage;
        String sql = "INSERT INTO messagelog (user_id,sender,display) " + " VALUES (?,?,?) RETURNING message_id";
        updatedStudentMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, userId, studentMessage.getSender(), studentMessage.getBody()));
        updatedStudentMessage.setUserId(userId);
        return updatedStudentMessage;
    }

    private List<BotMessage> logBotMessages(List<BotMessage> botMessageList, int userId) {
        List<BotMessage> updatedBotMessageList = new ArrayList<BotMessage>();
        String sql = "INSERT INTO messagelog (user_id,sender,display,response_id,question_id) " + " VALUES (?,?,?,?,?) RETURNING message_id";
        for (BotMessage botMessage : botMessageList) {
            botMessage.setUserId(userId);
            botMessage.setMessageId(jdbcTemplate.queryForObject(sql, int.class, userId, botMessage.getSender(), botMessage.getBody(), botMessage.getResponseId(), botMessage.getQuestionId()));
            updatedBotMessageList.add(botMessage);
        }
        return updatedBotMessageList;
    }

    // DISTINCT Database Entry Lists Methods
    private List<String> listOfTopics(){
        List<String> topicsList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicsList.add(results.getString("topic"));
        }
        return topicsList;
    }

    private List<String> listOfCategories2(){
        List<String> topicList = new ArrayList<String>();
        String sql = "SELECT DISTINCT category FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicList.add(results.getString("category"));
        }
        return topicList;
    }

    private List<String> listOfTopics(String category){
        List<String> topicList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses WHERE category = ? AND topic != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category);
        while( results.next() ) {
            topicList.add(results.getString("topic"));
        }
        return topicList;
    }

    private List<String> listOfAllTopics(){
        List<String> topicsList = new ArrayList<String>();
        String sql = "SELECT DISTINCT topic FROM responses";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while( results.next() ) {
            topicsList.add(results.getString("topic"));
        }
        return topicsList;
    }

    private List<String> listOfKeywords(String category, String topic){
        List<String> keywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT keyword FROM responses WHERE category = ? AND topic = ? AND keyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic);
        while( results.next() ) {
            keywordList.add(results.getString("keyword"));
        }
        return keywordList;
    }

    private List<String> listOfKeywordsByTopic(String topic){
        List<String> keywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT keyword FROM responses WHERE topic = ? AND keyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while( results.next() ) {
            keywordList.add(results.getString("keyword"));
        }
        return keywordList;
    }

    private List<String> listOfSubkeywords(String category, String topic, String keyword){
        List<String> subkeywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT subkeyword FROM responses WHERE category = ? AND topic = ? AND keyword = ? AND subkeyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword);
        while( results.next() ) {
            subkeywordList.add(results.getString("subkeyword"));
        }
        return subkeywordList;
    }

    private List<String> listOfSubkeywordsByTopic(String topic){
        List<String> subkeywordList = new ArrayList<String>();
        String sql = "SELECT DISTINCT subkeyword FROM responses WHERE topic = ? AND subkeyword != 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while( results.next() ) {
            subkeywordList.add(results.getString("subkeyword"));
        }
        return subkeywordList;
    }

    // Messages Based on Discussion Position Methods
    private List<BotMessage> getTopicMessages(String category, String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessageResponse(results));
        } return topicMessages;
    }

    private List<BotMessage> getTopicMessagesByTopic(String topic) {
        List<BotMessage> topicMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE topic ILIKE ? AND keyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic);
        while (results.next()) {
            topicMessages.add(mapRowToBotMessageResponse(results));
        } return topicMessages;
    }

    private List<BotMessage> getKeywordMessages(String category, String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        // todo: look into whether subkeyword should always be marked General when keyword is marked General
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessageResponse(results));
        } return keywordMessages;
    }

    private List<BotMessage> getKeywordMessagesByTopic(String topic, String keyword) {
        List<BotMessage> keywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE topic ILIKE ? AND keyword ILIKE ? AND subkeyword = 'General'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, keyword);
        while (results.next()) {
            keywordMessages.add(mapRowToBotMessageResponse(results));
        } return keywordMessages;
    }

    private List<BotMessage> getSubkeywordMessages(String category, String topic, String keyword, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE category ILIKE ? AND topic ILIKE ? AND keyword ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, category, topic, keyword, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessageResponse(results));
        } return subkeywordMessages;
    }

    private List<BotMessage> getSubkeywordMessagesByTopic(String topic, String subkeyword) {
        List<BotMessage> subkeywordMessages = new ArrayList<>();
        String sql = "SELECT response_id, display, display_type, link, codee_style FROM responses WHERE  topic ILIKE ? AND subkeyword ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, topic, subkeyword);
        while (results.next()) {
            subkeywordMessages.add(mapRowToBotMessageResponse(results));
        } return subkeywordMessages;
    }

    // Follow-up messages - Asks if student wants to go further into a topic
    private BotMessage buildSubkeywordWouldYouLikeToKnowMoreMessage(String topic, String keyword) {
        BotMessage subkeywordMessage = null;
        List<String> listOfSubkeywords = listOfSubkeywords("Pathway", topic, keyword);
        if(listOfSubkeywords.size() > 0) {
            String subkeywordMessageBody = "Would you like to know more about ";
            if (listOfSubkeywords.size() == 1) {
                subkeywordMessageBody = subkeywordMessageBody + listOfSubkeywords.get(0) + "?";
            } else {
                for (int i = 0; i < listOfSubkeywords.size(); i++) {
                    if (i != listOfSubkeywords.size() - 1) {
                        subkeywordMessageBody = subkeywordMessageBody + listOfSubkeywords.get(i) + ", ";
                    } else {
                        subkeywordMessageBody = subkeywordMessageBody + "or " + listOfSubkeywords.get(i) + "?";
                    }

                }
            }
            subkeywordMessage = mapCustomMessageToBotMessage(subkeywordMessageBody, "happy");
        }
        return subkeywordMessage;
    }

    // User Table Methods
    private int getUserId() {
        String sql = "INSERT INTO users (username, current_category, current_topic, current_keyword) VALUES ('Default1234User4321', 'None', 'None', 'None') RETURNING user_id";
        return jdbcTemplate.queryForObject(sql, int.class);
    }

    private String getUserNameById(int userId){
        String sql = "SELECT username FROM users WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        String username = "";
        if (result.next()) {
            username = result.getString("username");
        }
        return username;
    }

    private void updateUserName(int userId, String userName){
        String sql = "UPDATE users SET username = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, userName, userId);
    }

    private List<String> getUserCurrentDiscussionPosition(int userId){
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

    private void updateUserCurrentDiscussionPosition(int userId, String category, String topic, String keyword){
        String sql = "UPDATE users SET current_category = ?, current_topic = ?, current_keyword = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, category, topic, keyword, userId);
    }

    private int getLastLogQuestionIdByUserId(int userId) {
        int lastLogQuestionId = 0;
        String sql = "SELECT question_id FROM messagelog WHERE user_id = ? ORDER BY message_id DESC LIMIT 1;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if (result.next()) {
            lastLogQuestionId = result.getInt("question_id");
        }
        return lastLogQuestionId;
    }


    // Interview Table Methods
    private BotMessage getRandomInterviewQuestion(){
        BotMessage question = null;
        String sql = "SELECT question_id, display, link, display_type, codee_style FROM interview_questions ORDER BY RANDOM () LIMIT 1";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            question = mapRowToBotMessageInterviewQuestion(results);
        }
        return question;
    }

    private String getInterviewQuestionAnswerByQuestionId(int questionId){
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
