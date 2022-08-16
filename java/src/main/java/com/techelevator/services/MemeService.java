package com.techelevator.services;

import com.techelevator.model.BotMessage;
import com.techelevator.model.Meme;
import com.techelevator.model.MotivationalQuote;
import org.springframework.web.client.RestTemplate;

public class MemeService {

    RestTemplate restTemplate = new RestTemplate();

    public BotMessage getMeme() {
        String url = "https://meme-api.herokuapp.com/gimme/programmerhumor";
        Meme meme = restTemplate.getForObject(url, Meme.class);

        while (meme.isNsfw()) {
            meme = restTemplate.getForObject(url, Meme.class);
        }

        BotMessage botMessage = new BotMessage();
        botMessage.setLink(meme.getUrl());
        botMessage.setSender("bot");
        botMessage.setType("meme");
        botMessage.setBody(meme.getTitle());
        return botMessage;
    }

}
