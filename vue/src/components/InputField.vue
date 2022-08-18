<template>
  <div>
    <form class="form" v-on:submit.prevent="addMessage()">
      <input contenteditable spellcheck="true" class="input" type="text" id="spokenText" v-model="msg.body" />
      <button class="button" type="submit" value="Submit" role="button">Send</button>
      <button class="button speakyButton" :class="isListening ? 'listening' : 'notListening'" v-on:click.prevent="micButtonClicked()">
        <img class="micOffBlack" id="speakyButton" v-if="!isListening" src="..\assets\mic_off.png">
        <!-- <img class="micOffWhite" id="speakyButton" v-if="!isListening" src="..\assets\mic_off_white.png"> -->
        <img id="speakyButton" v-if="isListening" src="..\assets\mic_on.png" >
      </button>
    </form>
  </div>
</template>

<script>
import chatService from '@/services/ChatService'

let SpeechRecognition = null;
let textField = null;
let speakyButton = null;

document.addEventListener('DOMContentLoaded', () => {
    textField = document.getElementById('spokenText');
    speakyButton = document.getElementById('speakyButton');
    console.log("Dom content loaded");

    // Once the DOM is loaded, initialize the Speech to Text engine
    initSpeech();
});

function initSpeech() {  
  // Get Microphone access
  navigator.mediaDevices.getUserMedia({ audio: true} )
    .then( () => {      
      // Get either the SpeechRecognition API (Chrome/Edge) or webkitSpeechRecognition (Firefox)
      const SpeechRec = window.SpeechRecognition || window.webkitSpeechRecognition;

      // Get an instance of the SpeechRecognition API and initialize it
      SpeechRecognition = new SpeechRec();   
      SpeechRecognition.lang = "en-US";
      SpeechRecognition.continuous = false;
      SpeechRecognition.interimResults = false;

      // Set an event listen for when the SpeechRecognition API encounters and error
      SpeechRecognition.onerror = (error) => { console.error(error); }

      speakyButton.disabled = false;
  })
  .catch( error => {
      console.error(error);
      alert("Please enable access to the microphone");
  });
}

export default { 
  data()
   {
    return {
      micCounter: 0,
      isListening: false,
      msg: {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
        type: "text",
        link:""
      }
    }
  },
   methods:{
     addMessage() {
      this.msg.userId = this.$store.state.userId;
      chatService.sendMessage(this.msg).then(response => {
         response.data.forEach(message => {
           this.$store.commit("ADD_MESSAGE", message)
           this.scrollToBottom();
         })
       });
      this.msg = {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
        type: "text",
        link:""
      }
     },
     scrollToBottom() {
       setTimeout( () => {
          // Get the last child of the document
          const divToScroll = document.querySelector('div.chatbot').lastChild;
          divToScroll.scrollIntoView({
            behavior: 'smooth'
          });
       }, 
       100);
     },
      micButtonClicked() {
        if(this.isListening) {
          this.isListening = false;
        } else {
          this.isListening = true;
        }
        if(this.micCounter % 2 === 0) {
          SpeechRecognition.start();
          SpeechRecognition.onresult = (event) => {
                  // Get what was said as text
                  let whatWasSaid = event.results[0][0].transcript.toLowerCase();
                  // Set the value to the text box
                  textField.value = whatWasSaid;
              }
        } else {
          SpeechRecognition.stop();
          this.msg.body = textField.value;
          this.addMessage();
        }
        this.micCounter = this.micCounter + 1;
    },
    getBigram(word) {
      let result = [];
      for (let i = 0; i <word.length-1; i++) {
        result.push(word[i] + word[i + 1]);
      }
      return result;
    },
    getSimilarity(word1,word2) {
      word1 = word1.toLowerCase();
      word2 = word2.toLowerCase();
      const bigram1 = this.getBigram(word1), bigram2 = this.getBigram(word2);
      let similar = [];
      for(let i = 0; i < bigram1.length; i++) {
        if (bigram2.indexOf(bigram1[i])> -1) {
          similar.push(bigram1[i]);
        }
      }
    return similar.length/Math.max(bigram1.length,bigram2.length);
  }, 
  AutoCorrect(word,knownWords=this.ListOfTopics(), similarityThreshold=0.5){
    let maxSimilarity = 0;
    let mostSimilar = word;
     for (let i = 0; i < knownWords.length; i++){
      let similarity = this.getSimilarity(knownWords.length, word)
      if (similarity > maxSimilarity) {
        maxSimilarity = similarity;
        mostSimilar = knownWords[i];
      }
      return mostSimilar > similarityThreshold ? mostSimilar : word; 
    } 
  }
  
 },
  created() {
    chatService.getInitialMessages().then(response => {
      response.data.forEach(botMessage => {
        this.$store.commit("ADD_MESSAGE", botMessage)
        this.$store.commit("UPDATE_ID", botMessage.userId)
      });
    })
  }
}

</script>

<style scoped>

div {
  display: flex;
}
#searching{
  background-color: transparent;
flex-direction: row;}

.input {
  display: flex;
  flex-shrink: 1;
  border-radius: 25px;
  border: 2px solid #1790BF;
  padding: 5px;
  background-color:#ebf2f0;
  margin-left: 2%;
  width: 85%;
  height: 2.9vh;
}

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
  box-shadow: 2px 2px 4px #2c8366;
}

.button:hover{
  display: flex;
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

.form {
  display: flex;
  width:100%;
  justify-content: center;
}

.speakyButton {
  padding-bottom: 5px;
  padding-top: 5px;
  display: flex;
  transform: scale(1.02);
  color: white;
}

#speakyButton {
  height: 20px;
  display: flex;
}

.listening {
  background-color: #2B57F1;
  transform: scale(1.02);
}

.speakyButton:hover .micOffBlack {
  filter: invert(1);
}

</style>