let SpeechRecognition = null;
let textField = null;
let speakyButton = null;
let notices = null;

document.addEventListener('DOMContentLoaded', () => {
    textField = document.getElementById('spokenText');
    speakyButton = document.getElementById('speakyButton');
    notices = document.getElementById('notices');

    // Once the DOM is loaded, initialize the Speech to Text engine
    initSpeech();

    speakyButton.addEventListener('click', () => {
        // When the button is clicked start listening for speech
        startSpeech();
    })
});


function initSpeech() {
  
    // Get Microphone access
    navigator.mediaDevices.getUserMedia({ audio: true} )
        .then( (stream) => {

            // Get either the SpeechRecognition API (Chrome/Edge) or webkitSpeechRecognition (Firefox)
            const SpeechRec = window.SpeechRecognition || window.webkitSpeechRecognition;

            // Get an instance of the SpeechRecognition API and initialize it
            SpeechRecognition = new SpeechRec();   
            SpeechRecognition.lang = "en-US";
            SpeechRecognition.continuous = false;
            SpeechRecognition.interimResults = false;

            // set an event listen for when Speech has been detected
            SpeechRecognition.onresult = (event) => {
                // Get what was said as text
                let whatWasSaid = event.results[0][0].transcript.toLowerCase();
                // Set the value to the text box
                textField.value = whatWasSaid;
                // Stop listening for speech (until the next time the button is clicked)
                SpeechRecognition.stop();
                notices.innerText = "";
            }

            // Set an event listen for when the SpeechRecognition API encounters and error
            SpeechRecognition.onerror = (error) => { console.error(error); }

            speakyButton.disabled = false;
        })
        .catch( error => {
            console.error(error);
            alert("Please enable access to the microphone");
        });

}

function startSpeech() {
    // Start listening for speech
    SpeechRecognition.start();
    notices.innerText = "Speak Now";
}




