<template>
  <div class="home">
    <user-greeting/>
  </div>
</template>

<script>
import UserGreeting from '@/components/UserGreeting.vue'
export default {
  components: { UserGreeting },
  name: "home"
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Quicksand:wght@300&display=swap');

div {
  font-family: 'Quicksand', sans-serif;
}
</style>