import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({

  state: {
    messages: [
      {
        id: 1, 
        body: "Hello!", 
        sender: "bot",
        type: "text",
        link:""
      }, 
      {
       id: 2, 
       body: "What can I call you?",
       sender: "bot",
       type: "text",
       link:""

      }
    ],
  

  },
  mutations: {
    ADD_MESSAGE(state, message) {
      state.messages.push(message);
 
    },
    INCREMENT_ID(state) {
      return state.messages.length + 1
    }
  }

    

});