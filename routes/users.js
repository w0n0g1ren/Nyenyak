const express = require('express');
const { db } = require('../config'); // Import your Firebase database reference

const router = express.Router();
router.use(express.json());

// Route to get user data by UID
router.get('/:uid', async (req, res) => {
  try {
    const uid = req.params.uid;

    const userSnapshot = await db.ref(`/users/${uid}`).once('value');
    const userData = userSnapshot.val();

    if (!userData) {
      return res.status(404).json({ status: 'failed', message: 'User not found' });
    }

    res.status(200).json({ status: 'success', user: userData });
  } catch (error) {
    console.error('Error fetching user data:', error.message);
    res.status(500).json({ status: 'failed', message: 'Error fetching user data' });
  }
});

// Route to update user data by UID
router.put('/:uid', async (req, res) => {
  try {
    const uid = req.params.uid;
    const newData = req.body;

    // Update the user data in the database
    await db.ref(`/users/${uid}`).update(newData);

    res.status(200).json({ status: 'success', message: 'User data updated successfully', data: newData });
  } catch (error) {
    console.error('Error updating user data:', error.message);
    res.status(500).json({ status: 'failed', message: 'Error updating user data' });
  }
});

module.exports = router;