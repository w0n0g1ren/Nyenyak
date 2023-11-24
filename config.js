// config.js
const admin = require('firebase-admin');

// Initialize Firebase Admin SDK
const serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: 'https://nyenyak-project-dev-default-rtdb.asia-southeast1.firebasedatabase.app/'
});

// Initialize Realtime Database and get a reference to the service
const db = admin.database(); // Using Realtime Database

// Middleware for Firebase Authentication
const verifyFirebaseToken = async (req, res, next) => {
    const authorizationHeader = req.headers.authorization;

    try {
        if (!authorizationHeader || !authorizationHeader.startsWith('Bearer ')) {
            throw new Error('No ID token provided');
        }

        const idToken = authorizationHeader.split('Bearer ')[1]; // Extract the token part
        const decodedToken = await auth.verifyIdToken(idToken);
        req.user = decodedToken;
        next();
    } catch (error) {
        console.error('Firebase Authentication Error:', error.message);
        return res.status(401).json({ error: 'Unauthorized: Invalid or missing token', authorizationHeader });
    }
};

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

module.exports = { db, auth, verifyFirebaseToken};