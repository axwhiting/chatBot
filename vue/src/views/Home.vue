// <template>
//   <div>
//   <codee-header />
//     <div class="wrapper">
    
//     <div class="body">
//       <input-field class="input-field"/>
//       <chat-bot class ="chat-bot"/>
//       <nav-bar />
      
//     </div>
//     </div>
//   </div>
// </template>

// <script>
// import ChatBot from '../components/ChatBot.vue';
// import InputField from '../components/InputField.vue';
// import CodeeHeader from '../components/CodeeHeader.vue';
// import NavBar from '../components/NavBar.vue';
// // import Avatars from '../components/avatars.vue';
// // import NewEmail from '../components/NewEmail.vue';

// export default {
//   components: 
//   { 
//     ChatBot,
//     InputField,
//     CodeeHeader,
//     NavBar
//     // Avatars,
//     // NewEmail
//    },
//   name: "home",
 
// };
// </script>

// <style>
// @import url('https://fonts.googleapis.com/css2?family=Roboto+Mono&display=swap');

// div {
// font-family: 'Roboto Mono', monospace;
// }

// .wrapper {
//   background: #eee8e8;
//   background: #FFFFFF;
//   background: linear-gradient(to bottom, #FFFFFF, #D4D6D6); 
//   background-color: #dcdcdc; 
//   display: flex;
//   flex-direction: column;
//   max-height: 100%;
// }

// .body {
//   display: grid;
//   overflow-y: auto;
//   height: 100vh;
//   grid-template-areas: 
//   /* "header header" */
//   "left chat"
//   "left input"
//   "left footer";

//   grid-template-columns: 1fr 8fr;
//   grid-template-rows:  12fr 1fr .5fr;
//   grid-column-gap: 5px;
//   grid-row-gap: 0px;
// }

// .input-field {
//   position:static;
//   z-index: 500;
//   bottom: 0; 
//   height: 10%;
//   background-color:transparent;
//   width: 100%;
//   grid-area: input;
//   align-items: center;
//   padding-left: 11%;
// }

// .chat-bot {
//   height: 90%;
//   grid-area: chat;
// }

              
// .left-side {
//   grid-area: left;
//   background-color: #BFC0C0;
//   display:grid;
//   /* grid-template-rows:  1fr 1fr 1fr 1fr; */
// }

// .right-side {
//   grid-area: right;
//   background-color: #BFC0C0;
// }

// .footer {
//  background-color: #BFC0C0;
// }

// #happy{
//     background-color: #002A42;
//     vertical-align: middle;
//     border-radius: 50%;justify-content: center;
//     padding:10%;
//     margin-left: 22%;
// }

// </style>