var admin = require('firebase-admin');
const express = require('express');
const { auth } = require('../config');
const { signInWithEmailAndPassword, createUserWithEmailAndPassword } = require('firebase/auth')

const router = express.Router();
router.use(express.json());

// Route to register a new user with email and password
router.post('/register', async (req, res) => {
    const { email, password } = req.body;
  
    try {
      // const userRecord = await admin.auth().createUser({
      //   email,
      //   password,
      // });
      const userRecord = await createUserWithEmailAndPassword(auth, email, password);
  
      res.status(201).json({ message: 'User registered successfully', userId: userRecord.user.uid });
    } catch (error) {
      res.status(500).json({ message: 'Error registering user', error: error.message });
    }
  });

// Route to log in with email and password
router.post('/login', async (req, res) => {
    const { email, password } = req.body;
  
    try {
      const userRecord = await signInWithEmailAndPassword(auth, email, password);
      const token = await userRecord.user.getIdToken();
      const tokenExp = userRecord.user.stsTokenManager.expirationTime;
      const expirateTime = new Date(tokenExp).toLocaleString();
  
      res.status(200).json({ message: 'Login successful', token, expirateTime });
    } catch (error) {
      res.status(401).json({ message: 'Invalid credentials', error: error.message });
    }
  });

// Route to log out
router.post('/logout', (req, res) => {
  // logout logic
  auth.signOut()
    .then(() => {
      res.status(200).json({ message: 'Logout successful' });
    })
    .catch((error) => {
      res.status(500).json({ message: 'Error during logout', error: error.message });
    });
});

// Route to log in with Google
router.post('/google-login', async (req, res) => {
    const { idToken } = req.body;
  
    try {
      const credential = admin.auth.GoogleAuthProvider.credential(idToken);
      const userRecord = await admin.auth().signInWithCredential(credential);
  
      const token = await userRecord.getIdToken();
  
      res.status(200).json({ message: 'Google login successful', token });
    } catch (error) {
      res.status(401).json({ message: 'Google login failed', error: error.message });
    }
  });

// Route to get all users
router.get('/', async (req, res) => {
    try {
      const userRecords = await admin.auth().listUsers();
      const users = userRecords.users.map(userRecord => ({
        uid: userRecord.uid,
        email: userRecord.email,
        // Include any other user information you want to expose
      }));
  
      res.status(200).json({ users });
    } catch (error) {
      res.status(500).json({ message: 'Error fetching users', error: error.message });
    }
  });

module.exports = router;
