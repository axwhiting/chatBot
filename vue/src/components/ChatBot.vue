<template>
  <div class="the-big-div"> 
    <div v-for="message in this.$store.state.messages" v-bind:key="message.id" 
      v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}">
        <img v-if="message.sender === 'bot' && message.codeeStyle === 'happy'" src="@/assets/cropHappy.png" id="happy" alt="Happy robot with arms reached out">
        <img v-if="message.sender === 'bot' && message.codeStyle === 'sad'" src="" alt="sad robot">
        <img v-if="message.sender === 'bot' && message.codeeStyle === 'magnifier'" src="" alt="curious robot with magnifiying glass over one eye">
        <img v-if="message.sender === 'bot' && message.codeeStyle === 'lightbulb'" src="" alt="excited robot with a lightbulb displayed as his face">
      <p class = "bot-text" v-if="message.type === 'text'">{{message.body}}</p>
      <a v-if="message.type === 'link'" v-bind:href="message.link" target="_blank">{{message.body}}</a>
      <div class="embed" v-if="message.type === 'embed'"><iframe  v-bind:src="message.link" width="425" height="240" allow="autoplay" /></div>
      <div v-if="message.type === 'meme'" class="meme"><p>{{message.body}}</p><img v-bind:src="message.link" width="425" /></div>
      <!-- <div class="embed" v-if="message.type === 'meme'"><iframe v-bind:src="message.link" width="425"/></div> -->
      <div class = "mc-question" v-if="message.type === 'question'"> 
        <p>{{message.body.split('/')[0]}}</p> 
        <form class = "mc-form" v-on:submit.prevent="mcaMessage()"> 
          <label> <input class="mc-input" type="radio"  v-model="mca.body" v-bind:name="message.questionId" value="A" :disabled="isMcButtonDisabled(message.questionId)">  {{message.body.split('/')[1]}} </label><br>
          <label> <input class="mc-input" type="radio"  v-model="mca.body" v-bind:name="message.questionId" value="B" :disabled="isMcButtonDisabled(message.questionId)">  {{message.body.split('/')[2]}} </label><br>
          <label> <input class="mc-input" type="radio"  v-model="mca.body" v-bind:name="message.questionId" value="C" :disabled="isMcButtonDisabled(message.questionId)">  {{message.body.split('/')[3]}}</label><br>
          <button class="mc-button" :class="isMcButtonDisabled(message.questionId) ?  'disabled-mc-button' : 'enabled-mc-button'" type="submit" value="Submit" role="button" :disabled="isMcButtonDisabled(message.questionId)">Submit</button>
        </form>  
      </div>
    </div>
  </div>
</template>

<script>
import chatService from '@/services/ChatService'
export default {
data()
   {
    return {
      mca: {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
        type: "text",
        link:"",
      }, 
      quote: {
        body: ""
      }
    }
  },
   methods: {
     mcaMessage() {
      this.mca.userId = this.$store.state.userId;
       chatService.sendMessage(this.mca).then(response => {
         response.data.forEach(message => {
          this.$store.commit("ADD_MESSAGE", message)
          this.scrollToBottom();
         })
       });
      this.mca = {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
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
    isMcButtonDisabled(questionId){
      let isQuestion = true;
      if(this.$store.state.messages[this.$store.state.messages.length - 1].questionId ===questionId){
        isQuestion = false;
      }
    return isQuestion;
    }
   }
    
}
   
</script>

<style scoped>

div.bot {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  font-weight: bold;
  overflow: hidden;
}

div.student{
  display: flex;
  justify-content: flex-end;
  width: 100%;
  font-weight: bold;
}

p.bot-text, a, .embed, .meme {
  background-color:  #C6E3F0;
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
  margin-right: 2%;
  margin-left:10px ;
  margin-top: 1%;
  margin-bottom: 1%;
  flex-direction: row;
  opacity: 75%;
  box-shadow: 4px 4px 4px #202e46;
}

.meme {
  display: flex;
  flex-direction: column;
}

.meme p, .meme img {
  display: flex;
  align-self: center;
}
  
div.student p.bot-text, div.student a{
  background-color: #61F1C1;
  opacity: 75%;
  box-shadow: 4px 4px 4px #2c8366;
}

.mc-question {
  background-color: #C6E3F0;
  opacity: 75%;
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
  margin-right: 2%;
  margin-left:10px ;
  margin-top: 1%;
  margin-bottom: 1%;
  box-shadow: 4px 4px 4px #202e46;
}

.mc-form {
  display: flex;
  justify-content: space-evenly;
  flex-direction: column;
}

label {
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  text-align: left;
}

#choice {
  width: 2vw;
}

.mc-button {
  background-color: #2B57F1;;
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
  padding: 10px 20px;
  text-align: center;
  color: white;
  height: 36px;
}

.mc-button:hover{
  transform: scale(1.02);
  background-color: #61F1C1;
  color: black;
  text-align: center;
}

.disabled-mc-button, .disabled-mc-button:hover {
  transform: scale(1);
  background-color: gray;
  color: white;
}

.mc-input {
  margin: 10px;
}

/* div.student{
  animation: .2s ease-in-out 0s  slideInRight;
} */

/* div.bot {
	animation: slide-in-elliptic-right-fwd 0.7s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;

}
@keyframes slide-in-elliptic-right-fwd {
  0% {
    transform: translateX(800px) rotateY(-30deg) scale(0);
    transform-origin: -100% 50%;
    opacity: 0;
  }
  100% {
    transform: translateX(0) rotateY(0) scale(1);
    transform-origin: -1800px 50%;
    opacity: 1;
  }} */

#happy {
  background-color: #002A42;
  border-radius: 50%;
  padding: 4px;
  margin-left: 10px;
  margin-top:15px ;
  height: 10vw;
  max-width: 45px;
  max-height: 40px
}

</style>