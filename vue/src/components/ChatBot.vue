<template>
  <div class="the-big-div"> 
    <div v-for="message in this.$store.state.messages" v-bind:key="message.id" 
      v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}">
      <p class = "bot-text" v-show="message.type === 'text'">{{message.body}}</p>
      <a v-show="message.type === 'link'" v-bind:href="message.link" target="_blank">{{message.body}}</a>
      <div class="embed" v-show="message.type === 'embed'"><iframe  v-bind:src="message.link" width="425" height="240" allow="autoplay" /></div>
      <p v-show="message.type === 'question'">{{message.body}}</p>
    <div class = "mc-question" v-show="message.type === 'question'"> 
      <p>{{message.body.split('/')[0]}}</p> 
      <form class = "mc-form" v-on:submit.prevent="mcaMessage()"> 
       <label> <input class = "mc-input" type="radio"  v-model="mca.body" name="choice" value="A"> {{message.body.split('/')[1]}} </label>
        <label><input class = "mc-input" type="radio"  v-model="mca.body" name="choice" value="B"> {{message.body.split('/')[2]}} </label>
       <label> <input class = "mc-input" type="radio"  v-model="mca.body" name="choice" value="C"> {{message.body.split('/')[3]}}</label>
        <button class = "mc-button" type="submit" value="Submit" role="button" :disabled="isMcButtonDisabled">Submit</button>
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
        link:""
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
     }
  },
  computed: {
    isMcButtonDisabled(){
      let isQuestion = true;
      if(this.$store.state.messages[this.$store.state.messages.length - 1].type =='question'){
        isQuestion = false;
      }
    return isQuestion;



    }
  }
  
  }
  

</script>

<style scoped>

.the-big-div{
  display: flex;
  flex-direction: column;
  justify-content: flex-end;  
}

div.bot {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  font-weight: bold;
}

div.student{
  display: flex;
  justify-content: flex-end;
  width: 100%;
  font-weight: bold;
}

p.bot-text, a, .embed {
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
  margin-right: 2%;
  margin-left:2% ;
  margin-top: 1%;
  margin-bottom: 1%
}
  
div.bot p.bot-text, div.bot a, .embed {
  background-color:  #C6E3F0;
  /* animation: 1s ease-in-out 0s  slideInLeft; */
  flex-direction:  row;
  opacity: 75%;
}

div.student p.bot-text, div.student a{
  background-color: #61F1C1;
  opacity: 75%;
}
.mc-question {
   background-color: #61F1C1;
   opacity: 75%;
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
  margin-right: 2%;
  margin-left:2% ;
  margin-top: 1%;
  margin-bottom: 1%
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
   background-color: #a4a8c2;
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
  color: black;
  height: 36px;
  width: 20%;

}

</style>