const express = require('express');
const crypto = require('crypto');
const axios = require('axios');
const { db, verifyFirebaseToken } = require('../config'); // Import your Firebase database reference

const router = express.Router();
router.use(express.json());

// Apply Firebase Auth middleware to protect routes that require authentication
router.use(verifyFirebaseToken);

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

// Route to get all diagnoses from Realtime Database, filtered by user UID
router.get('/', (req, res) => {
  const uid = req.user.uid;

  db.ref(`diagnosis/${uid}`).once('value')
    .then((snapshot) => {
      const data = snapshot.val();
      const diagnoses = data ? Object.values(data) : [];
      res.json(diagnoses);
    })
    .catch((error) => {
      console.error('Error fetching diagnoses:', error.message);
      res.status(500).json({ status: 'failed', message: 'Error fetching diagnoses' });
    });
});

// Route to get a specific diagnosis by ID for the logged-in user
router.get('/:id', (req, res) => {
  try {
    const diagnosisId = req.params.id;
    const uid = req.user.uid;

    db.ref(`diagnosis/${uid}/${diagnosisId}`).once('value', (snapshot) => {
      const diagnosis = snapshot.val();

      if (!diagnosis) {
        return res.status(404).json({ status: "failed", message: 'Diagnosis not found for the logged-in user' });
      }

      res.json(diagnosis);
    }, (error) => {
      console.error('Error fetching diagnosis:', error.message);
      res.status(500).json({ status: "failed", message: 'Error fetching diagnosis' });
    });
  } catch (error) {
      console.error('Exception in fetching diagnosis:', error.message);
      res.status(500).json({ status: "failed", message: 'Exception in fetching diagnosis' });
  }
});

// Route to create a new diagnosis in Realtime Database
router.post('/', async (req, res) => {
  try {
  const {
    gender,
    age,
    weight,
    height,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel,
    stressLevel,
    bloodPressure,
    heartRate,
    dailySteps
  } = req.body;

  if (!sleepDuration) {
    return res.status(400).json({ message: 'ERROR: Please provide all required fields' });
  }
  // Count BMI
  const heightInMeters = height / 100;
  const BMI = weight / (heightInMeters ** 2);

  let BMIcategory;
  if (BMI < 25) {
    BMIcategory = 'Normal';
  } else if (BMI < 30) {
    BMIcategory = 'Overweight';
  } else {
    BMIcategory = 'Obese';
  }

  const uid = req.user.uid;

  const newId = generateUniqueId(8);
  const createdAt = getCurrentTimestamp();

  const toMinute = physicalActivityLevel*60;

  // Data in the format accepted by the Flask model API
  const modelApiInput = {
    "Age": age,
    "Sleep_Duration": sleepDuration,
    "Sleep_Quality": qualityOfSleep,
    "Physical_Activity_Level": physicalActivityLevel,
    "Stress_Level": stressLevel,
    "Heart_Rate": heartRate,
    "Daily_Steps": dailySteps,
    "Gender_Male": gender === 'Male' ? 1 : 0,
    "BMI_Category_Obese": BMIcategory === 'Obese' ? 1 : 0,
    "BMI_Category_Overweight": BMIcategory === 'Overweight' ? 1 : 0,
    "BP_Category_Normal": bloodPressure === 'Normal' ? 1 : 0,
    "BP_Category_Stage 1": bloodPressure === 'Stage 1' ? 1 : 0,
    "BP_Category_Stage 2": bloodPressure === 'Stage 2' ? 1 : 0
  };

  // Send data to Flask model API
  const modelApiResponse = await axios.post('https://nyenyak-model-api-z2dhcxitca-et.a.run.app/prediction', modelApiInput);

  // Extract the sleep disorder prediction from the Flask model API response
  const sleepDisorderPrediction = modelApiResponse.data.sleep_disorder;

  // Get the corresponding solution based on sleep disorder condition
  const solution = await db.ref(`solution/${sleepDisorderPrediction}`).once('value').then(snapshot => snapshot.val());



  const newDiagnosis = {
    id: newId,
    uid: uid,
    date: createdAt,
    gender,
    age,
    BMIcategory,
    sleepDuration,
    qualityOfSleep,
    physicalActivityLevel: toMinute,
    stressLevel,
    bloodPressure,
    heartRate,
    dailySteps,
    sleepDisorder: sleepDisorderPrediction,
    solution: solution
  };

  // Perform data insertion
  db.ref(`diagnosis/${uid}/${newId}`).set(newDiagnosis)
  .then(() => {
      res.status(201).json({ status: "success", message: "Data successfully added", newDiagnosis });
  })
  .catch((error) => {
      console.error('Error creating diagnosis:', error.message);
      res.status(500).json({ status: "failed", message: 'Error creating diagnosis' });
  });
  } catch (error) {
    console.error('Exception in creating diagnosis:', error.message);
    res.status(500).json({ status: "failed", message: 'Exception in creating diagnosis' });
  }
});

// Route to delete a diagnosis by ID from Realtime Database
router.delete('/:id', (req, res) => {
  try {
    const diagnosisId = req.params.id;
    const uid = req.user.uid;

    db.ref(`diagnosis/${uid}/${diagnosisId}`).remove()
      .then(() => {
        res.status(200).json({ status: "success", message: 'Data is deleted' });
      })
      .catch((error) => {
        console.error('Error deleting diagnosis:', error.message);
        res.status(500).json({ status: "failed", message: 'Error deleting diagnosis' });
      });
  } catch (error) {
    console.error('Exception in deleting diagnosis:', error.message);
    res.status(500).json({ status: "failed", message: 'Exception in deleting diagnosis' });
  }
});

module.exports = router;