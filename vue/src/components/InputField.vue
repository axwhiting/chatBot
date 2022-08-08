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
      }
    }
  },
  
  methods: 
  {
    addMessage() {
      const message = this.msg
    
      this.$store.commit("ADD_MESSAGE", message);
      chatService.sendMessage(this.msg).then(response =>{
          response.data.forEach(msgBody => {
            const botMessage = {
                id: this.$store.state.messages.length + 1,
                body: msgBody.body,
                sender: "bot",
                type: msgBody.type,
                link:msgBody.link
            }
            this.$store.commit("ADD_MESSAGE", botMessage)
          });
        }
      )
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