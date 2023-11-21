const express = require('express');
const crypto = require('crypto');
const { db } = require('../config'); // Import your Firebase database reference

const router = express.Router();
router.use(express.json());

// Function to generate a unique ID using crypto with a specified size
function generateUniqueId(size) {
  return crypto.randomBytes(size).toString('hex');
}

// Number to String Month
const month = {
  1:"Jan",
  2:"Feb",
  3:"Mar",
  4:"Apr",
  5:"May",
  6:"Jun",
  7:"Jul",
  8:"Aug",
  9:"Sep",
  10:"Oct",
  11:"Nov",
  12:"Dec"
}

// Route to get all diagnoses from Realtime Database
router.get('/', (req, res) => {
  db.ref('diagnosis').once('value', (snapshot) => {
    const data = snapshot.val();
    res.json(data);
  }, (error) => {
    res.status(500).json({ message: 'Error fetching diagnoses', error: error.message });
  });
});

// Route to get a specific diagnosis by ID from Realtime Database
router.get('/:id', (req, res) => {
  const diagnosisId = req.params.id;
  db.ref(`diagnosis/${diagnosisId}`).once('value', (snapshot) => {
    const diagnosis = snapshot.val();
    if (!diagnosis) {
      return res.status(404).json({ message: 'Diagnosis not found' });
    }
    res.json(diagnosis);
  }, (error) => {
    res.status(500).json({ message: 'Error fetching diagnosis', error: error.message });
  });
});

// Route to create a new diagnosis in Realtime Database
router.post('/', (req, res) => {
  const { username, age, result } = req.body;
  if (!result) {
    return res.status(400).json({ message: 'ERROR: Try it again' });
  }
  // const newId = nanoid(8);
  const newId = generateUniqueId(8); // Generate a unique ID using crypto
  
  // Get Date
  const current = Date.now()
  const dateTime = new Date(current);
  const date = dateTime.getDate() + " " + month[dateTime.getMonth().toString()] + " " + dateTime.getFullYear();

  const newDiagnosis = {
    id: newId,
    username,
    age,
    date:date,
    result
  };
  db.ref(`diagnosis/${newId}`).set(newDiagnosis)
    .then(() => {
      res.status(201).json(newDiagnosis);
    })
    .catch((error) => {
      res.status(500).json({ message: 'Error creating diagnosis', error: error.message });
    });
});

// Route to update a diagnosis by ID in Realtime Database
router.put('/:id', (req, res) => {
  const diagnosisId = req.params.id;
  const { username, age, result } = req.body;

  const updateData = {
    id: diagnosisId,
    username,
    age,
    result
  };

  db.ref(`diagnosis/${diagnosisId}`).update(updateData)
    .then(() => {
      res.json({ message: 'Data is updated', updatedDiagnosis: updateData });
    })
    .catch((error) => {
      res.status(500).json({ message: 'Error updating diagnosis', error: error.message });
    });
});

// Route to delete a diagnosis by ID from Realtime Database
router.delete('/:id', (req, res) => {
  const diagnosisId = req.params.id;
  db.ref(`diagnosis/${diagnosisId}`).remove()
    .then(() => {
      res.json({ message: 'Data is deleted' });
    })
    .catch((error) => {
      res.status(500).json({ message: 'Error deleting diagnosis', error: error.message });
    });
});

module.exports = router;
