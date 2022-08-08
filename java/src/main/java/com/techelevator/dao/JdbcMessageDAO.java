package com.techelevator.dao;

import com.techelevator.model.BotMessage;
import com.techelevator.model.StudentMessage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcMessageDAO implements MessageDAO{

    private final JdbcTemplate jdbcTemplate;

    public JdbcMessageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BotMessage> messages(String studentMessage) {
        List<BotMessage> messages = new ArrayList<>();
        String sql = "SELECT display, display_type, link FROM responses WHERE catogory = 'Pathway' AND topic ILIKE ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, studentMessage);
        while (results.next()) {
            messages.add(mapRowToBotMessage(results));
        }
        return messages;
    }
    private BotMessage mapRowToBotMessage(SqlRowSet row) {
        BotMessage message = new BotMessage();
        message.setBody(row.getString("display"));
        return message;
    }

}
