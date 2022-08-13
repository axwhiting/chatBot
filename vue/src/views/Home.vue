<template>
  <div class="wrapper">
    <codee-header />
    <div class="body">
      <input-field class="input-field"/>
      <chat-bot class ="chat-bot"/>
      <div class="left-side">
        <avatars />
        <new-email/>
      </div>
    </div>
  </div>
</template>

<script>
import ChatBot from '../components/ChatBot.vue';
import InputField from '../components/InputField.vue';
import CodeeHeader from '../components/CodeeHeader.vue'
import Avatars from '../components/avatars.vue';
import NewEmail from '../components/NewEmail.vue';

export default {
  components: 
  { 
    ChatBot,
    InputField,
    CodeeHeader,
    Avatars,
    NewEmail
   },
  name: "home",
 
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Roboto+Mono&display=swap');

div {
font-family: 'Roboto Mono', monospace;
}

.wrapper {
   background: #eee8e8;
  background: #FFFFFF;
  background: linear-gradient(to bottom, #FFFFFF, #D4D6D6); 
  background-color: #dcdcdc; 
  display: flex;
  flex-direction: column;
  max-height: 100%; 
}

.body {
  display: grid;
  height: 100%;
  grid-template-areas: 
  /* "header header" */
  "left chat"
  "left input"
  "left footer";

  grid-template-columns: 1fr 8fr;
  grid-template-rows:  12fr 1fr .5fr;
  grid-column-gap: 5px;
  grid-row-gap: 0px;
}

.input-field {
  position: fixed;
  z-index: 500;
  bottom: 0; 
  height: 10%;
  background-color:transparent;
  width: 100vw;
  grid-area: input;
  align-items: center;
  padding-left: 11%;
}

.chat-bot {
  height: 90vh;
  grid-area: chat;
  overflow-y: auto;
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
  background-color: #61F1C1;
  display: inline-block;
  cursor: pointer;
  font-size: 14px;
  line-height: 1;
  border-radius: 500px;
  transition-property: background-color,border-color,color,box-shadow,filter;
  transition-duration: .3s;
  border: 1px solid transparent;
  letter-spacing: 2px;
  text-transform: uppercase;
  font-weight: 700;
  text-align: center;
  padding: 10px 20px;
  color: black;
  height: 36px;
  margin-left: 4px;
}

button:hover{
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
   }
              
.left-side {
  grid-area: left;
  background-color: #BFC0C0;
  display:grid;
  grid-template-rows:  1fr 1fr 1fr 1fr;

}

.right-side {
  grid-area: right;
  background-color: #BFC0C0;
}

.footer {
 background-color: #BFC0C0;
}

#happy{
    background-color: #002A42;
    vertical-align: middle;
    border-radius: 50%;justify-content: center;
    padding:10%;
    margin-left: 22%;
}

</style>