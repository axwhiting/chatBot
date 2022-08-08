<template>
  <div>
      <form v-on:submit.prevent="addMessage()">
      <input type="text" v-model="msg.body" />
      <button type="submit" value="Submit">Send</button>
    </form>
  </div>
</template>

<script>
import chatService from '@/services/ChatService'
export default {
data()
 
   {
    return {
      msg: {
        id: this.$store.state.messages.length + 1,
        body: "",
        sender: "student",
        type: "text",
        link:""
      },
      msgWithUsername: {
        id: this.$store.state.messages.length + 1,
        body: "",
        sender: "bot",
        type: "text",
        link:""
      },
    }
  },
  methods: 
  {
    addMessage() {
      const message = this.msg
      this.$store.commit("ADD_MESSAGE", message);
      if(this.$store.state.messages.length === 3){
        this.msgWithUsername.body = "Hi, " + this.msg.body + "!";
        this.$store.commit("ADD_MESSAGE", this.msgWithUsername);
        chatService.getAllTopics().then(response =>{
          const botMessage = {
                  id: this.$store.state.messages.length + 1,
                  body: response.data.body,
                  sender: "bot",
                  type: response.data.type,
                  link: response.data.link
          }
        this.$store.commit("ADD_MESSAGE", botMessage);
        });
      } else {
      chatService.sendMessage(this.msg).then(response =>{
          response.data.forEach(msgBody => {
            const botMessage = {
                id: this.$store.state.messages.length + 1,
                body: msgBody.body,
                sender: "bot",
                type: msgBody.type,
                link: msgBody.link
            }
            this.$store.commit("ADD_MESSAGE", botMessage)
          });
        });
      }
      this.msg = {
        id: this.$store.state.messages.length + 1,
        body: "",
        sender: "student",
        type: "text",
        link:""
      }
     
    },
    //need an "if the message index is 3 , or messages.length = 3 , send list of topics
    //Or if the message.body != topic, return list of topics
    
    ///this doesn't work
    createListOfTopics(){
      chatService.getAllTopics().then( response => {
            this.topicsList = response.data;
    })
  
  }
  }
  
}
</script>

<style>

</style>