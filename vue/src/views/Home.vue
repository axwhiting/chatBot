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
