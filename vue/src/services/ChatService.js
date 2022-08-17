import axios from 'axios'


const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {

  sendMessage(msg) {
    return http.post('/messages', msg);
  },
  getInitialMessages(){
    return http.get('/messages/welcome')
  },
  getTopics(){
    return http.get('/topics')
  }

}