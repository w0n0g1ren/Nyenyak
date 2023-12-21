var admin = require('firebase-admin');
const express = require('express');
const { auth } = require('../config');
const { signInWithEmailAndPassword, createUserWithEmailAndPassword} = require('firebase/auth')

const router = express.Router();
router.use(express.json());

const { differenceInYears, parse, isValid } = require('date-fns');
function calculateAge(dateOfBirth) {
  const dob = parse(dateOfBirth, 'dd-MM-yyyy', new Date());
  const age = differenceInYears(new Date(), dob);
  return age;
}

function isValidDateFormat(dateString) {
  const parsedDate = parse(dateString, 'dd-MM-yyyy', new Date());
  return isValid(parsedDate);
}

router.post('/register', async (req, res) => {
  const { email, password, name, gender, birthDate } = req.body;

  if (!isValidDateFormat(birthDate)) {
    return res.status(400).json({
      status: 'failed',
      message: 'Invalid date format',
      error: 'Date of birth must be in dd-MM-yyyy format',
    });
  }

  try {
    const userRecord = await createUserWithEmailAndPassword(auth, email, password);
    const age = calculateAge(birthDate)
    await admin.database().ref(`/users/${userRecord.user.uid}`).set({
      name,
      email,
      gender,
      birthDate,
      age
    });

    res.status(201).json({ 
      status: 'success',
      message: 'User registered successfully', 
      userId: userRecord.user.uid,
      name,
      gender,
      birthDate,
      age
    });
  } catch (error) {
    if (error.code === 'auth/invalid-email') {
      res.status(400).json({ 
        status: 'failed',
        message: 'Email must valid',
        error: 'Email is not valid'
      });
    } else if (error.code === 'auth/email-already-in-use') {
      res.status(400).json({ 
        status: 'failed',
        message: 'User is existed, login or use another email',
        error: 'Email is used'
      });
    } else if (error.code === 'auth/weak-password') {
      res.status(400).json({ 
        status: 'failed',
        message: 'Weak Password, use valid password (at least 6 characters)',
        error: 'Weak Password'
      });
    } else {
      res.status(500).json({ 
        status: 'failed',
        message: 'Error registering user',
        error: 'Server Error'
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
    const expirateTime = new Date(tokenExp).toLocaleString('en-US', { timeZone: 'Asia/Bangkok' });
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
        error: 'Invalid Credential'
      });
    } else {
      res.status(500).json({
        status: 'failed',
        message: 'Cannot login, try it again',
        error: 'Server Error'
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
        message: 'Logout success' });
    })
    .catch((error) => {
      res.status(500).json({ 
        status: 'failed',
        message: 'Error during logout', 
        error: 'Server Error'
      });
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
        message: 'Google login failed'
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
        error: "Server Error"
      });
    }
  });

module.exports = router;
