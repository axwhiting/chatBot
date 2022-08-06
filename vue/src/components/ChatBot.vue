<template>
  <div class="the-big-div">
    <div v-for="message in this.$store.state.messages" 
    v-bind:key="message.id" 
    v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}">
      <p> 
        {{message.body}}
      </p>
    </div>
    
    <form v-on:submit.prevent="addMessage()">
      <input type="text" v-model="msg.body" />
      <button type="submit" value="Submit">Send</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      msg: {
        id: this.$store.state.messages.length + 1,
        body: "",
        sender: "student"
      }
    }
  },
  methods: {
    addMessage() {
      const message = this.msg
      this.$store.commit("ADD_MESSAGE", message);
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
@import url('https://fonts.googleapis.com/css2?family=Quicksand:wght@300&display=swap');

/* todo: move "template" formatting to the appropriate view */
template {
  display: grid;
  grid-template-columns: 1fr, 2fr, 1fr;
}

.the-big-div{
  display: flex;
  flex-direction: column;
  width: 70vw;
  font-family: 'Quicksand', sans-serif;
}

div.bot {
  display: flex;
  justify-content: flex-start;
  width: 100%;
}

div.student{
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

p {
  max-width: 60%;
  padding: .5%;
}

div.bot p {
  background-color: #71D96F;
}

div.student p{
  background-color: #34AAE1;
}


</style>