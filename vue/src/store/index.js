import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({

  state: {
    userId : null,
    messages: [],
  },
  mutations: {
    ADD_MESSAGE(state, message) {
      state.messages.push(message);
    },
    UPDATE_ID(state, userId) {
      state.userId = userId
    }
  },
});