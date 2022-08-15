<template>
  <div>
      <form class="form" v-on:submit.prevent="addMessage()">
      <input class="input" type="text" v-model="msg.body" />
      <button class="button" type="submit" value="Submit" role="button">Send</button>
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
  /* height: 3.3vh; */
  margin-right: 5%;
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