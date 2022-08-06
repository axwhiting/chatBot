<template>
  <div class="home">
    <chat-bot/>
  </div>
</template>

<script>
import ChatBot from '../components/ChatBot.vue';
export default {
  components: { ChatBot },
  name: "home"
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Quicksand:wght@300&display=swap');

div {
  font-family: 'Quicksand', sans-serif;
}
</style>