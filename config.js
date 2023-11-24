var admin = require('firebase-admin');
const firebase = require('firebase');

const serviceAccount = require('./serviceAccountKey.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://nyenyak-project-dev-default-rtdb.asia-southeast1.firebasedatabase.app/',
});

// Initialize Realtime Database and get a reference to the service
const db = admin.database(); // Using Realtime Database

// Initialize Firebase Auth
const firebaseConfig = {
    apiKey: "AIzaSyC4h4Cmlq6RFegh37KvoDfbnLx1dPK8mTw",
    authDomain: "nyenyak-project-dev.firebaseapp.com",
    databaseURL: "https://nyenyak-project-dev-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "nyenyak-project-dev",
    storageBucket: "nyenyak-project-dev.appspot.com",
    messagingSenderId: "49869973263",
    appId: "1:49869973263:web:fccfa7c9331c589846007a",
    measurementId: "G-7CKP2X9LNN"
  };

const firebaseApp = firebase.initializeApp(firebaseConfig);
const auth = firebaseApp.auth(); // Initialize Firebase Auth


module.exports = { db, auth };
