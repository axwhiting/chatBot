<template>
  <div class="wrapper">
   <div class="header">Team Alpha ChatBot</div>
    <div class="body">
      <input-field class="input-field"/>
      <chat-bot class ="chat-bot"/>
      <div class="left-side"></div>
      
    </div>
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
@import url('https://fonts.googleapis.com/css2?family=Roboto+Mono&display=swap');

div {
font-family: 'Roboto Mono', monospace;
}

.wrapper {
  display: flex;
  flex-direction: column;
}

.body {
  /* background: #eee8e8;
background: #FFFFFF;
background: linear-gradient(to bottom, #FFFFFF, #D4D6D6); 
 background-color: #dcdcdc; */
  display: grid;
  height: 100vh;
  grid-template-areas: 
  /* "header header" */
  "left chat"
  "left input"
  "left footer";

  grid-template-columns: 1fr 8fr;
  grid-template-rows:  6fr 2fr .5fr;
  grid-column-gap: 5px;
  grid-row-gap: 0px;
}

.input-field {
  display: flex;
  position: fixed;
  z-index: 500;
  bottom: 0; 
  height: 10%;
  background-color:transparent;
  width: 100%;
  grid-area: input;
  align-items: flex-start;
  padding-top: 15%;
  padding-left: 11%;
}

.chat-bot {
  margin-bottom: 10%;
  height: 90%;
  grid-area: chat;
}

input {
  border-radius: 25px;
  border: 2px solid #1790BF;
  padding: 5px;
  background-color:#ebf2f0;
  margin-left: 2%;
}

form {
  width:100%;
}

button {
  background-color: #04BF68;
  border-radius: 100px;
  box-shadow: rgba(28, 148, 74, 0.2) 0 -25px 18px -14px inset,rgba(11, 90, 41, 0.15) 0 1px 2px,rgba(44, 187, 99, .15) 0 2px 4px,rgba(44, 187, 99, .15) 0 4px 8px,rgba(44, 187, 99, .15) 0 8px 16px,rgba(44, 187, 99, .15) 0 16px 32px;
  color: black;
  cursor: pointer;
  display: inline-block;
  padding: 5px 20px ;
  text-align: center;
  text-decoration: none;
  transition: all 250ms;
  border: 0;
  font-size: 16px;
  touch-action: manipulation;
  margin-left: 2%;
  font-family: 'Roboto Mono', monospace;
  font-weight: bolder;
}

button {
  box-shadow: rgba(21, 105, 54, 0.35) 0 -25px 18px -14px inset,rgba(44,187,99,.25) 0 1px 2px,rgba(44,187,99,.25) 0 2px 4px,rgba(44,187,99,.25) 0 4px 8px,rgba(44,187,99,.25) 0 8px 16px,rgba(44,187,99,.25) 0 16px 32px;
  transform: scale(1.05) rotate(-1deg);
}

.left-side {
  grid-area: left;
  background-color: #BFC0C0;
}

.right-side {
  grid-area: right;
  background-color: #BFC0C0;
}

.header {
  padding: 20px;
  background-color: #909595;
  position:fixed;
  z-index: 1000;
  width: 100%;
  text-align: center;
}

.footer {
 background-color: #BFC0C0;
}

</style>