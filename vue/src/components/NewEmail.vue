<template>
  <div class="emailContainer">
    <button @click="toggleForm()" id="showEmailForm" class="showEmailForm">Email</button>
    <div id="emailForm" class="hidden" ref="emailForm">
      <form ref="form" class="emailForm" @submit.prevent="sendEmail">
        <label>Your Name:</label>
        <input type="text" name="name">
        <label>Recipient Email:</label>
        <input type="email" name="email">
        <div class="email-options-container">
          <label class="email-options"><input type="radio" name="email" v-on:change.prevent="lastMessages()">Send Codee's last messages.</label>
          <label class="email-options"><input type="radio" name="email" v-on:change.prevent="fullChatLog()">Send full chat log.</label>
        </div>
        <input class="emailSubmitButton" type="submit" value="Send">
        <textarea name="message" class="text-area" v-model="emailBody"></textarea>
      </form>
    </div>
  </div>
</template>

<script>
import emailjs from '@emailjs/browser';

export default {
  data() {
    return {
      emailBody: ""
    }
  },
  methods: {
    sendEmail() {
      emailjs.sendForm('service_codeebot', 'template_codeeBot', this.$refs.form, '6JvaykMUbw9sach9s')
        .then((result) => {
            console.log('SUCCESS!', result.text);
        }, (error) => {
            console.log('FAILED...', error.text);
        });
        this.$refs.form.reset();
    },

    toggleForm() {
      let emailForm = this.$refs.emailForm;
      if (emailForm.classList.contains('hidden')) {
        emailForm.classList.add('visible');
        emailForm.classList.remove('hidden');
      } else {
        emailForm.classList.add('hidden');
        emailForm.classList.remove('visible');
      }
      this.lastMessages();
    },
    lastMessages() {
      let botMessages = [];
      let currentMessages = this.$store.state.messages;
      for (let i = currentMessages.length - 1; i >= 0; i--) {
        if (currentMessages[i].sender === 'bot') {
          botMessages.unshift(currentMessages[i]);
        } else {
          break;
        }
      }
      let messagesToSend = "";
      botMessages.forEach(message => {
        if (message.type === 'link') {
          messagesToSend = messagesToSend + "Codee: " + message.body + ": " + message.link + '\n';
        } else if (message.type === 'embed') {
          messagesToSend = messagesToSend + "Codee: " + message.body + ": " + message.link + '\n';
        } else {
          messagesToSend = messagesToSend + "Codee: " + message.body + '\n';
        }
        }
      );
      this.emailBody = messagesToSend;
    },
    fullChatLog() {
      let chatHistory = this.$store.state.messages;
      let messagesToSend = "";
      chatHistory.forEach(message => {
        if (message.sender === 'student') {
          messagesToSend = messagesToSend + 'Student: ' + message.body + '<br>';
        } else if (message.type === 'link') {
          messagesToSend = messagesToSend + 'Codee: ' + message.body + ': ' + message.link + '<br>';
        } else if (message.type === 'embed') {
          messagesToSend = messagesToSend + 'Codee: ' + message.body + ': ' + message.link + '<br>';
        } else {
          messagesToSend = messagesToSend + 'Codee: ' + message.body + '<br>';
        }
        }
      );
      this.emailBody = messagesToSend;
    }
  }
}
</script>
 
<style scoped>
* {
  box-sizing: border-box;
}

#showEmailForm{
  margin-left: 20%;
  margin: 10px;
}

.emailContainer{
  display: flex;
  flex-direction: column;
  align-items:center;
}

.emailForm {
  max-width: 20vw;
  font-size: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-weight: bold;
}

label {
  float: left;
  margin-left: 4%;
}

input[type=text], [type=email], textarea {
  width: 90%;
  padding: 5px;
  border: 2px solid #1790BF;
  border-radius: 25px;
  box-sizing: border-box;
  margin-top: 10px;
  margin-bottom: 10px;
  resize: vertical;
  background-color: #EBF2F0;
  margin-left: 4%;
}

input[type=submit] {
  background-color: #61F1C1;
  display: inline-block;
  cursor: pointer;
  font-size: 12px;
  line-height: 1;
  border-radius: 500px;
  transition-property: background-color,border-color,color,box-shadow,filter;
  transition-duration: .3s;
  border: 1px solid transparent;
  letter-spacing: 2px;
  text-transform: uppercase;
  font-weight: 700;
  text-align: center;
  padding: 6px 15px;
  color: black;
  width: 12vw;
  margin-left: 10px;
  margin-right: 10px;
  margin-top: 10px;
  box-shadow: 2px 2px 4px #2c8366;
}

input[type=submit]:hover {
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

textarea {
  height: 100px;
}

.hidden {
  display: none;
}

.visible {
  display: block;
}

.showEmailForm {
  background-color: #61F1C1;
  display: inline-block;
  cursor: pointer;
  font-size: 12px;
  line-height: 1;
  border-radius: 500px;
  transition-property: background-color,border-color,color,box-shadow,filter;
  transition-duration: .3s;
  border: 1px solid transparent;
  letter-spacing: 2px;
  text-transform: uppercase;
  font-weight: 700;
  text-align: center;
  padding: 6px 15px;
  color: black;
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  width: 12vw;
  box-shadow: 2px 2px 4px #2c8366;
}

button:hover{
  transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

.email-options-container {
  display: flex;
  flex-direction: column;
  width: 12vw;
  align-content: space-around;
}

.email-options {
  display: flex;
  margin-bottom: 12px;
}

.text-area {
  z-index: -1000;
  height: 1px;
}

</style>