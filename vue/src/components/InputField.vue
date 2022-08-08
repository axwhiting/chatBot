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
        sender: "student"
      }
    }
  },
  methods: 
  {
    addMessage() {
      const message = this.msg
      this.$store.commit("ADD_MESSAGE", message);
      chatService.sendMessage(this.msg).then(response =>{
          const botMessage = {
                id: this.$store.state.messages.length + 1,
                body: response.data,
                sender: "bot"

          }
         this.$store.commit("ADD_MESSAGE", botMessage);
      }
      )
      this.msg = {
        id: this.$store.state.messages.length + 1,
        body: "",
        sender: "student"
      }
    }
  }
    }


</script>

<style>

</style>