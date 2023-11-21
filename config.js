//Firebase Firestore and Auth Configuration 
var admin = require('firebase-admin')
var { getAuth} = require('firebase-admin/auth');

const serviceAccount = require("./serviceAccountKey.json"); // Put your firebase service account file here
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: 'https://nyenyak-project-dev-default-rtdb.asia-southeast1.firebasedatabase.app/'
});

// Initialize Realtime Database and get a reference to the service
const db = admin.database(); // Using Realtime Database
const auth= getAuth();

module.exports = {db, auth}