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
      }, 
      {
       id: 2, 
       body: "SCOPE IT ALL",
       sender: "bot",

      }
    ],
    message: {
      body: "",
      sender: "",
    }


  }

});