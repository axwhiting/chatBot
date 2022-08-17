<template>
    <div class="container">
        <div class="header">
            <codee-header />
        </div>
        <div class="body">
            <nav-bar class="navbar"/>
            <chat-bot class="chatbot"/>
            <speech />
            <input-field id="input-field"/>
        </div>
    </div>
</template>

<script>
import CodeeHeader from '../components/CodeeHeader.vue';
import ChatBot from '../components/ChatBot.vue';
import NavBar from '../components/NavBar.vue';
import InputField from '../components/InputField.vue';
import Speech from '../components/Speech.vue'

export default {
    name: "homev2",
    components: {
        CodeeHeader,
        ChatBot,
        NavBar,
        InputField,
        Speech
    }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Roboto+Mono&display=swap');

div {
    font-family: 'Roboto Mono', monospace;
}

.container {
    display: grid;
    grid-template-rows: auto 4fr;
    grid-template-areas: 
        "header"
        "chatbot";
    height: 100vh;
}

.body {
    grid-area: chatbot;
    display: grid;
    grid-template-columns: auto 8fr;
    grid-template-areas: 
        "navbar chatbot"
        "navbar input";
}

.header {
    grid-area: header;
}

.chatbot {
    grid-area: chatbot;
    height: 80vh;
    overflow-y: scroll;
}

.navbar {
    grid-area: navbar;
}

</style>