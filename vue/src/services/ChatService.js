import axios from 'axios'


const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {
  sendMessage(msg) {
    return http.post('/messages', msg);
  },
  getAllKeywords(){
    return http.get('/keywords')
  },
  getSenderKeyword(keyword){
    return http.get(`/keywords/${keyword}`)
  }
 

}