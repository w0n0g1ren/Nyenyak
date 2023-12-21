const express = require('express');
const crypto = require('crypto');
const axios = require('axios');
const { db, verifyFirebaseToken } = require('../config');

const router = express.Router();
router.use(express.json());

router.use(verifyFirebaseToken);

function generateUniqueId(size) {
  return crypto.randomBytes(size).toString('hex');
}

function getCurrentTimestamp() {
  const date = new Date();
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();

  return `${day}-${month}-${year}`;
}

// Route to get all diagnoses by logged user
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

// Route to create a new diagnosis
router.post('/', async (req, res) => {
  try {
  const uid = req.user.uid;

  const userSnapshot = await db.ref(`/users/${uid}`).once('value');
  const userData = userSnapshot.val();

  if (!userData) {
    return res.status(400).json({ message: 'ERROR: User data not found' });
  }

  const {
    gender: userGender,
    age: userAge,
    name: userName,
  } = userData;

  const {
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

  const newId = generateUniqueId(8);
  const createdAt = getCurrentTimestamp();

  const toMinute = physicalActivityLevel*60;

  // Data in the format accepted by the Flask model API
  const modelApiInput = {
    "Gender": userGender === 'Male' ? 1 : 0,
    "Age": userAge,
    "Sleep_Duration": sleepDuration,
    "Sleep_Quality": (() => {
      if (qualityOfSleep >= 4 && qualityOfSleep <= 9) {
        return qualityOfSleep - 4;
      } else if (qualityOfSleep < 4) {
        return 0;
      } else {
        return 5;
      }
    })(),
    "Physical_Activity_Level": physicalActivityLevel,
    "Stress_Level": (() => {
      if (stressLevel >= 3 && stressLevel <= 8) {
        return stressLevel - 3;
      } else if (stressLevel < 3) {
        return 0;
      } else {
        return 5;
      }
    })(),
    "BMI_Category": BMIcategory === 'Obese' ? 1 : (BMIcategory === 'Overweight' ? 2 : 0),
    "Heart_Rate": heartRate,
    "Daily_Steps": dailySteps,
    "BP_Category": (() => {
      if (bloodPressure === 'Normal') {
        return 1;
      } else if (bloodPressure === 'Stage 1') {
        return 2;
      } else if (bloodPressure === 'Stage 2') {
        return 3;
      } else {
        return 0;
      }
    })()
  };
  
  // Send data to Flask model API
  const modelApi = 'https://nyenyak-model-api-z2dhcxitca-et.a.run.app/prediction'; // PUT DEPLOYED MODEL API ENDPOINT HERE, DONT FORGET TO ADD /prediction to ENDPOINT
  const modelApiResponse = await axios.post(modelApi, modelApiInput);

  const sleepDisorderPrediction = modelApiResponse.data.sleep_disorder;

  const solution = await db.ref(`solution/${sleepDisorderPrediction}`).once('value').then(snapshot => snapshot.val());

  const newDiagnosis = {
    id: newId,
    uid: uid,
    date: createdAt,
    gender: userGender,
    age: userAge,
    name: userName,
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

// Route to delete a diagnosis by ID
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