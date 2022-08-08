import axios from 'axios'


const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {
  sendMessage(msg) {
    return http.post('/messages', msg);
  },
  getAllTopics(){
    return http.get('/topics')
  },
  getSenderKeyword(keyword){
    return http.get(`/topics/${keyword}`)
  }
 

}