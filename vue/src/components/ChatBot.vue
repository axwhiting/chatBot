<template>
  <div class="the-big-div">
    <div v-for="message in this.$store.state.messages" 
    v-bind:key="message.id" 
    v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}">
      <p v-show="message.type === 'text'"> 
        {{message.body}}
      </p>
   <a v-show="message.type === 'link'" v-bind:href="message.link" target="_blank">{{message.body}}</a>
    </div>
 
  </div>
</template>

<script>
export default {

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
  overflow-y: scroll;
}

p,a {
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
}

.home{
  overflow-y: scroll;
}

div.bot p, div.bot a {
  background-color: #63e675;
  animation: 1s ease-in-out 0s  slideInLeft;
}

div.student p, div.student a{
  background-color: #67c3df;
  /* animation: 1s ease-in 0s  slideInRight; */
}

</style>