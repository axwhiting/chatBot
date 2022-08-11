import axios from 'axios'


const https = axios.create({
    baseURL: "https://zenquotes.io/api/random"
  });

export default {
    getQuote(){
        return https.get();
    }

}
