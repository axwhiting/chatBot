<template>
    <div class="navbar">
        <div class="avatars">
          <avatars />  
        </div>
        <div class="menu">
          <button @click="sendClick('About Codee')" id="about" class="about button">About</button>
          <button @click="sendClick('Help for Codee')" id="help" class="help button">Help</button>
          <button @click="sendClick('Get meme')" id="meme" class="meme button">Meme</button>
          <button @click="sendClick('Interview Question')" id="interview" class="interview button">Interview</button>
          <button @click="saySomething()" id="speech" class="speech button">Talk to me</button>
          <new-email />
        </div>
    </div>
</template>

<script>
import avatars from './avatars.vue';
import NewEmail from './NewEmail.vue';
import chatService from '@/services/ChatService';

export default {
  components: { 
    avatars,
    NewEmail
  },
  data()
   {
    return {
      counter: 0,
      msg: {
        messageId: "",
        userId: "",
        body: "",
        sender: "click",
        type: "text",
        link:""
      },
      voices: []
    }
  },
  methods: {
    sendClick(buttonClicked) {
      this.msg.body = buttonClicked;
      this.msg.userId = this.$store.state.userId;
       chatService.sendMessage(this.msg).then(response => {
         response.data.forEach(message => {
           this.$store.commit("ADD_MESSAGE", message);
           this.scrollToBottom();
         })
       });
      this.msg = {
        messageId: "",
        userId: "",
        body: "",
        sender: "click",
        type: "text",
        link:""
      }
    },
    scrollToBottom() {
      setTimeout( () => {
          // Get the last child of the document
          const divToScroll = document.querySelector('div.chatbot').lastChild;
          divToScroll.scrollIntoView({
            behavior: 'smooth'
          });
       }, 
       100);
     },
    saySomething() {
      let msgToRead = this.getSpeechReady();
      let botMessages = [];
      let currentMessages = this.$store.state.messages;
      for (let i = currentMessages.length - 1; i >= 0; i--) {
        if (currentMessages[i].sender === 'bot' && currentMessages[i].type === 'text') {
          botMessages.unshift(currentMessages[i]);
        } else {
          break;
        }
      }
        botMessages.forEach(message => {
          msgToRead.text = message.body;
          window.speechSynthesis.speak(msgToRead);
        })   
    },
    getSpeechReady() {
      let msgToRead = new SpeechSynthesisUtterance();
      var voices = speechSynthesis.getVoices();
      msgToRead.voice = voices[4];
      msgToRead.lang = "en-US";
      msgToRead.pitch = .65;
      msgToRead.volume = 1;
      msgToRead.rate = 1;
      return msgToRead;
    } 
  },
  created() {
    this.getSpeechReady;
    this.voices = speechSynthesis.getVoices();
  }

}
</script>

<style scoped>
.navbar {
  display: grid;
  background-color: #e3e3e3;
  background-image: linear-gradient(0deg, #82828b 0%, #e3e3e3 100%);  grid-template-rows:  auto 1fr;
  grid-template-areas: 
    "avatar"
    "menu";
}

.avatars {
  grid-area: avatar;
  display: flex;
  justify-content: center;
}

.menu {
  grid-area: menu;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.input {
  border-radius: 25px;
  border: 2px solid #1790BF;
  padding: 5px;
  background-color:#ebf2f0;
  margin-left: 2%;
  width: 60%;
  height: 2.5vh;
}

.button{
  background-color: #61F1C1;
  display: inline-block;
  cursor: pointer;
  font-size: 12px;
  line-height: 1;
  border-radius: 500px;
  transition-property: background-color,border-color,color,box-shadow,filter;
  transition-duration: .3s;
  border: 1px solid transparent;
  letter-spacing: 2px;
  text-transform: uppercase;
  font-weight: 700;
  text-align: center;
  padding: 6px 15px;
  color: black;
  width: 12vw;
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 10px;
  box-shadow: 2px 2px 4px #2c8366;
}

.button:hover{
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

.form {
  width:100%;
}

</style>