var admin = require('firebase-admin');
const express = require('express');
const { auth } = require('../config');
const { signInWithEmailAndPassword, createUserWithEmailAndPassword} = require('firebase/auth')

const router = express.Router();
router.use(express.json());

router.post('/register', async (req, res) => {
  const { email, password } = req.body;
  try {
    // Create a new user
    const userRecord = await createUserWithEmailAndPassword(auth, email, password);
    res.status(201).json({ 
      status: "success",
      message: 'User registered successfully', 
      userId: userRecord.user.uid 
    });
  } catch (error) {
    if (error.code === 'auth/invalid-email') {
      res.status(500).json({ 
        status: "failed",
        message: 'Email must valid', 
        error: error.message 
      });
    } else if (error.code === 'auth/email-already-in-use') {
      res.status(500).json({ 
        status: "failed",
        message: 'User is existed', 
        error: error.message 
      });
    } else {
      res.status(500).json({ 
        status: "failed",
        message: 'Error registering user', 
        error: error.message 
      });
    }
    
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

    res.status(200).json({
      status: 'success',
      message: 'Login successful',
      token,
      expirateTime,
    });
  } catch (error) {
    if (error.code === 'auth/invalid-credential' ) {
      res.status(401).json({
        status: 'failed',
        message: 'Email or password is incorrect',
        error: error.message
      });
    } else {
      res.status(401).json({
        status: 'failed',
        message: 'Invalid credential',
        error: error.message
      });
    }
  }
});


// Route to log out
router.post('/logout', (req, res) => {
  // logout logic
  auth.signOut()
    .then(() => {
      res.status(200).json({ 
        status: 'success',
        message: 'Logout successful' });
    })
    .catch((error) => {
      res.status(500).json({ 
        status: 'failed',
        message: 'Error during logout', 
        error: error.message });
    });
});

// Route to log in with Google
router.post('/google-login', async (req, res) => {
    const { idToken } = req.body;
  
    try {
      const credential = admin.auth.GoogleAuthProvider.credential(idToken);
      const userRecord = await admin.auth().signInWithCredential(credential);
  
      const token = await userRecord.getIdToken();
  
      res.status(200).json({ 
        status: 'success',
        message: 'Google login successful', 
        token 
      });
    } catch (error) {
      res.status(401).json({ 
        status: 'failed',
        message: 'Google login failed', 
        error: error.message 
      });
    }
  });

// Route to get all users
router.get('/', async (req, res) => {
    try {
      const userRecords = await admin.auth().listUsers();
      const users = userRecords.users.map(userRecord => ({
        uid: userRecord.uid,
        email: userRecord.email,
      }));
  
      res.status(200).json({ 
        status: 'success', 
        users 
      });
    } catch (error) {
      res.status(500).json({ 
        status: 'failed', 
        message: 'Error fetching users', 
        error: error.message 
      });
    }
  });

module.exports = router;
