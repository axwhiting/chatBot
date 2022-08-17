import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({

  state: {
    userId : null,
    messages: [],
    spokenMessage: {
      messageId: "",
      userId: "",
      body: "",
      sender: "student",
      type: "text",
      link:""
    }
  },
  mutations: {
    ADD_MESSAGE(state, message) {
      state.messages.push(message);
    },
    UPDATE_ID(state, userId) {
      state.userId = userId
    },
    GET_SPOKEN_MESSAGE(state, spokenMessageBody) {
      state.spokenMessage.body = spokenMessageBody;
      this.ADD_MESSAGE(state.spokenMessage);
      state.spokenMessage.body = "";
    }
  },
});