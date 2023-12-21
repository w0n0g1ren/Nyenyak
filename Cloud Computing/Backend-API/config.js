var admin = require('firebase-admin');
const { getAuth }  = require("firebase/auth");
const { initializeApp } = require("firebase/app");

const serviceAccount = require('./serviceAccountKey.json');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://nyenyak-project-dev-default-rtdb.asia-southeast1.firebasedatabase.app/',
});

// Initialize Realtime Database and get a reference to the service
const db = admin.database(); 
const authorization = admin.auth(); 

// Middleware for Firebase Authentication
const verifyFirebaseToken = async (req, res, next) => {
    const authorizationHeader = req.headers.authorization;

    try {
        if (!authorizationHeader || !authorizationHeader.startsWith('Bearer ')) {
            throw new Error('No ID token provided');
        }

        const idToken = authorizationHeader.split('Bearer ')[1];
        const decodedToken = await authorization.verifyIdToken(idToken);
        req.user = decodedToken;
        next();
    } catch (error) {
        console.error('Firebase Authentication Error:', error.message);
        return res.status(401).json({ error: 'Unauthorized: Invalid or missing token' });
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

const firebaseApp = initializeApp(firebaseConfig);
const auth = getAuth(firebaseApp)

module.exports = { db, auth, verifyFirebaseToken, authorization};