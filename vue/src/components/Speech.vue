<template>
  <div>
    <input v-show="false" type="text" id="spokenText" />
    <button class="button speakyButton"><img id="speakyButton" src="..\assets\mic.png"></button>
    <p id="notices"></p>
    </div>
</template>

<script src="..\services\speechToText.js">

export default {

}
</script>

<style scoped>
.button{
  display: flex;
  flex-basis: auto;
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
  margin-right: 10px;
}

.button:hover{
  display: flex;
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

.speakyButton {
  padding-bottom: 5px;
  padding-top: 5px;
}

#speakyButton {
  height: 20px;
  display: flex;
}

</style>