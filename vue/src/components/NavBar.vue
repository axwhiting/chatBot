<template>
    <div class="navbar">
        <div class="avatars">
          <avatars />  
        </div>
        <div class="menu">
          <button @click="toggleForm()" id="about" class="about button">About</button>
          <button @click="toggleForm()" id="showEmailForm" class="showEmailForm button">Email</button>
          <button @click="toggleForm()" id="help" class="help button">Help</button>
          <button id="interview" class="interview button">Interview</button>
        </div>
    </div>
</template>

<script>
import avatars from './avatars.vue'
export default {
  components: { avatars },

}
</script>

<style scoped>
.navbar {
  display: grid;
  background-color: #BFC0C0;
  grid-template-rows:  auto 1fr;
  grid-template-areas: 
      "avatar"
      "menu";
}

.avatars {
  grid-area: avatar;
  display: flex;
  justify-content: center;
}

.menu {
  grid-area: menu;
  display: flex;
  flex-direction: column;
  align-items: center;
}

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
  margin-top: 2px;
  margin-bottom: 2px;
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