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
  flex-direction: column-reverse;
}
.input-field{
  position: fixed;
  bottom: 0;
  z-index: 500;
  height: 10vh;
  background-color: white;
}
.chat-bot{
  margin-bottom: 10vh;
}

</style>