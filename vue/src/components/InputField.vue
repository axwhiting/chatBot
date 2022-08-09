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
        message_id: "",
        user_id: "",
        body: "",
        sender: "student",
        type: "text",
        link:""
      },
      
    }
  },
   methods:{
     addMessage() {
       chatService.sendMessage(this.msg).then(response => {
         response.data.forEach(message => {
           this.$store.commit("ADD_MESSAGE", message)
         })
       });
     }

  
 
  },
  created() {
    chatService.getInitialMessages().then(response => {
       response.data.forEach(botMessage => {
          this.$store.commit("ADD_MESSAGE", botMessage)
          });

    })
  
}
  
}
</script>

<style>
input{
    width: 60%;
    height: 2.5vh;
}
button{
  height: 3.1vh;
  width: 5vh;
  margin-right: 5%;

}

</style>