<template>
    <div class="navbar">
        <div class="avatars">
          <avatars />  
        </div>
        <div class="menu">
          <button @click="sendClick('About Codee Chatbot')" id="about" class="about button">About</button>
          <button @click="sendClick('Help for Codee Chatbot')" id="help" class="help button">Help</button>
          <button @click="sendClick('Interview Question')" id="interview" class="interview button">Interview</button>
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
      msg: {
        messageId: "",
        userId: "",
        body: "",
        sender: "click",
        type: "text",
        link:""
      }
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
     } 
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