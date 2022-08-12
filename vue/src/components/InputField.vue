<template>
  <div>
      <form v-on:submit.prevent="addMessage()">
      <input type="text" v-model="msg.body" />
      <button type="submit" value="Submit" role="button">Send</button>
    </form>
  </div>
</template>

<script>
//import quoteService from '@services/QuoteService'
import chatService from '@/services/ChatService'
export default {
data()
   {
    return {
      msg: {
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
   methods:{
     addMessage() {
      this.msg.userId = this.$store.state.userId;
       chatService.sendMessage(this.msg).then(response => {
         response.data.forEach(message => {
           this.$store.commit("ADD_MESSAGE", message)
         })
       });
      this.msg = {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
        type: "text",
        link:""
      }
     }
  },
  created() {
    chatService.getInitialMessages().then(response => {
      response.data.forEach(botMessage => {
        this.$store.commit("ADD_MESSAGE", botMessage)
        this.$store.commit("UPDATE_ID", botMessage.userId)
      });
    })
  }
}
</script>

<style>

input {
    width: 60%;
    height: 2.5vh;
}

button{
  height: 3.3vh;
  margin-right: 5%;
}

</style>