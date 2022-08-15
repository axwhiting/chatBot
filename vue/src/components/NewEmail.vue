<template>
  <div class="emailContainer">
    <button @click="toggleForm()" id="showEmailForm" class="showEmailForm">Email</button>
    <div id="emailForm" class="hidden" ref="emailForm">
      <form ref="form" @submit.prevent="sendEmail">
        <label>Name</label>
        <input type="text" name="name">
        <label>Email</label>
        <input type="email" name="email">
        <label>Message</label>
        <textarea name="message"></textarea>
        <input type="submit" value="Send">
      </form>
    </div>
  </div>
</template>

<script>
import emailjs from '@emailjs/browser';

export default {
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
    // showForm() {
    //   var x = document.getElementById("emailForm");
    //   if (x.style.display === "none") {
    //     x.style.display = "block";
    //   } 
    // },
    toggleForm() {
      let emailForm = this.$refs.emailForm;
      if (emailForm.classList.contains('hidden')) {
        emailForm.classList.add('visible');
        emailForm.classList.remove('hidden');
      } else {
        emailForm.classList.add('hidden');
        emailForm.classList.remove('visible');
      }
    }
  }
}
</script>
 
<style scoped>
* {box-sizing: border-box;}
#showEmailForm{
    margin-left: 20%;
    margin: 8%;
}

.emailContainer{
    display: flex;
    flex-direction: column;
    align-items:center;
}

label {
  float: left;
  margin-left: 4%;
}

input[type=text], [type=email], textarea {
  width: 80%;
  padding: 10px;
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
  margin: 8%;
  margin-left: 20%;
}

input[type=submit]:hover {
 transform: scale(1.02);
  background-color: #2B57F1;
  color: white;
}

.hidden {
  display: none;
}

.visible {
  display: block;
}

</style>