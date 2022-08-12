package com.techelevator.services;

import com.techelevator.model.BotMessage;
import com.techelevator.model.MotivationalQuote;
import org.springframework.web.client.RestTemplate;

public class QuoteService {

    RestTemplate restTemplate = new RestTemplate();

    public BotMessage getQuote() {
        String url = "https://zenquotes.io/api/random";
        MotivationalQuote[] quoteArray = restTemplate.getForObject(url, MotivationalQuote[].class);
        BotMessage botMessage = new BotMessage();
        botMessage.setBody(quoteArray[0].getQ() + " ~" + quoteArray[0].getA());
        botMessage.setLink("n/a");
        botMessage.setType("text");
        botMessage.setSender("bot");
        return botMessage;
    }

}
