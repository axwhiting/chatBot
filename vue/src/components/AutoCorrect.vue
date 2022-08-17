<template>
    <div></div>
</template>

<script>
import chatService from '@/services/ChatService'
export default {
  data()
   {
    return {
      msg: {
        messageId: "",
        userId: "",
        body: "",
        sender: "student",
        type: "text",
        link:""
      }
    }
  },
   methods:{
getBigram(word) {
      let result = [];
      for (let i = 0; i <word.length-1; i++) {
        result.push(word[i] + word[i + 1]);
      }
      return result;
    },
    getSimilarity(word1,word2) {
      word1 = word1.toLowerCase();
      word2 = word2.toLowerCase();
     const bigram1 = this.getBigram(word1), bigram2 = this.getBigram(word2);
      let similar = [];
      for(let i = 0; i < bigram1.length; i++) {
        if (bigram2.indexOf(bigram1[i])> -1) {
          similar.push(bigram1[i]);
        }
      }
    
  },
    AUtocorrect(word){
    let maxSimilarity = 0;
    let mostSimilar = word;

    for (let i = 0; i < chatService.type.length; i++){
      let similarity = this.getSimilarity(chatService.type[i], word)
      if (similarity > maxSimilarity) {
        maxSimilarity = similarity;
        mostSimilar = chatService.type[i];
      }
      return mostSimilar;
    }
  }
   }  
}
</script>
