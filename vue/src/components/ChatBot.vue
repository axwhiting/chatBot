<template>
  <div class="the-big-div"> 
    <div v-for="message in this.$store.state.messages" v-bind:key="message.id" 
      v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}">
      <p v-show="message.type === 'text'">{{message.body}}</p>
      <a v-show="message.type === 'link'" v-bind:href="message.link" target="_blank">{{message.body}}</a>
    </div>
  </div>
</template>

<script>
export default {

}

</script>

<style>

@import url('https://fonts.googleapis.com/css2?family=Cinzel:wght@800&display=swap');

/* todo: move "template" formatting to the appropriate view */
/* template {
 
  
}  */

.the-big-div{
  display: flex;
  flex-direction: column;
  font-family: 'Cinzel', serif;
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


  p,a {
  max-width: 60%;
  padding: 1.5%;
  border-radius: 7.5px;
  margin-right: 2%;
  margin-left:2% ;
  margin-top: 1%;
  margin-bottom: 1%
}
  



div.bot p, div.bot a {
  background-color: #04BF68;
  animation: 1s ease-in-out 0s  slideInLeft;
  flex-direction:  row;
  opacity: 75%;
}

div.student p, div.student a{
  background-color: #F294AD;
  opacity: 75%;

  /* animation: 1s ease-in 0s  slideInRight; */

}

</style>