const express = require('express');
const crypto = require('crypto');
const { db } = require('../config'); // Import your Firebase database reference

const router = express.Router();
router.use(express.json());

// Function to generate a unique ID using crypto with a specified size
function generateUniqueId(size) {
  return crypto.randomBytes(size).toString('hex');
}

// Function to get the current timestamp in dd-mm-yyyy format
function getCurrentTimestamp() {
  const date = new Date();
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();

  return `${day}-${month}-${year}`;
}

// Route to get all diagnoses from Realtime Database, filtered by username if provided
router.get('/', (req, res) => {
  const requestedUsername = req.query.username;

  db.ref('diagnosis').once('value', (snapshot) => {
    const data = snapshot.val();

    // Filter data based on the requested username, example: http://localhost:8080/diagnosis?username=john
    const filteredDiagnosis = requestedUsername
      ? Object.values(data).filter(diagnosis => diagnosis.username === requestedUsername)
      : Object.values(data);

    res.json(filteredDiagnosis);
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
  const {
    date,
    gender,
    username,
    age,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel,
    stressLevel,
    BMIcategory,
    bloodPressure,
    heartRate,
    dailySteps,
    sleepDisorder
  } = req.body;

  if (!sleepDisorder) {
    return res.status(400).json({ message: 'ERROR: Please provide all required fields' });
  }

  const newId = generateUniqueId(8); // Generate a unique ID using crypto
  const createdAt = getCurrentTimestamp(); // Get current timestamp

  const newDiagnosis = {
    id: newId,
    gender,
    username,
    age,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel,
    stressLevel,
    BMIcategory,
    bloodPressure,
    heartRate,
    dailySteps,
    sleepDisorder,
    date: createdAt
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
  const {
    gender,
    username,
    age,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel,
    stressLevel,
    BMIcategory,
    bloodPressure,
    heartRate,
    dailySteps,
    sleepDisorder
  } = req.body;

  const updateData = {
    id: diagnosisId,
    gender,
    username,
    age,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel,
    stressLevel,
    BMIcategory,
    bloodPressure,
    heartRate,
    dailySteps,
    sleepDisorder
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
