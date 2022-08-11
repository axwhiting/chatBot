<template>
  <div class="home">
    <input-field class="input-field"/>
    <chat-bot class ="chat-bot"/>
  </div>
</template>

<script>
import ChatBot from '../components/ChatBot.vue';
import InputField from '../components/InputField.vue';
export default {
  components: 
  { 
    ChatBot,
    InputField,
    
   },
  name: "home"
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Quicksand:wght@300&display=swap');

div {
  font-family: 'Quicksand', sans-serif;
}
.home{
  display: flex;
  height: 100vh;
  flex-direction: column-reverse;
  justify-content: flex-start;
}
.input-field{
  position: fixed;
  bottom: 0;
  z-index: 500;
  height: 10%;
  background-color:transparent;
  width: 100%;
}
html{
background: #eee8e8;
background: #FFFFFF;
background: linear-gradient(to bottom, #FFFFFF, #D4D6D6);
}
.chat-bot{
  margin-bottom: 10%;
  height: 90%;
}

/* #home{
   display: grid;
  grid-template-columns: 1fr, 2fr, 1fr;
  grid-template-areas:  "... chatbot ...";
  align-self: center; */

/* #the-big-div {
  grid-area: chatbot;

} */

</style>