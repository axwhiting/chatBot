<template>
  <div>
    <div v-for="message in this.$store.state.messages" 
    v-bind:key="message.id">
        
    <p  v-bind:class="{bot:message.sender === 'bot', student:message.sender === 'student'}"> {{message.body}}
    </p>

    </div>
  </div>
</template>

<script>


export default {


}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Quicksand:wght@300&display=swap');

.user-greeting {
  display: flex;
  justify-content: center;
  font-family: 'Quicksand', sans-serif;
    
}

</style>