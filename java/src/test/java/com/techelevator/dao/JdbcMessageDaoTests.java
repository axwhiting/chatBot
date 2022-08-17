package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.Message;
import com.techelevator.model.StudentMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcMessageDaoTests extends BaseDaoTests {

    private JdbcMessageDAO sut;
    private StudentMessage studentMessage;
    int userId = 1;
    String username = "Alpha";


    @Before
    public void setup () {
        DataSource dataSource = this.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcMessageDAO(jdbcTemplate);
        studentMessage = new StudentMessage();
        studentMessage.setBody("Alpha");
        studentMessage.setSender("student");
        studentMessage.setType("text");
        studentMessage.setLink("n/a");
    }

    @Test
    public void trueForClassConnection() {
        boolean hasConnection = false;
        List<BotMessage> messages = sut.getInitialMessages();
        hasConnection = messages.size() == 2;

        Assert.assertTrue(hasConnection);
    }

    @Test
    public void givenBotNameAsUserNameReturnsExpectedResult() {
        BotMessage expectedResult = new BotMessage();
        String codeeUsername = "Codee";
        expectedResult.setBody("That's my name too!");

        List<BotMessage> returnedMessages = sut.buildBotResponsesAfterUsernameEntered(userId, codeeUsername);
        BotMessage actualResult = returnedMessages.get(0);
        Assert.assertEquals(expectedResult.getBody(), actualResult.getBody());
    }


}
