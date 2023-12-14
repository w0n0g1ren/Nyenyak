# Nyenyak-model-API

### Prerequisites

- Python 3.x
- pip (package installer for Python)

### 1. Create a virtual environment (optional)
```
python -m venv venv
venv\Scripts\activate
```
### 2. Install dependencies
```
pip install -r requirements.txt
```
### 3. Run the Flask app
```
python app.py
```
Use the `/prediction` endpoint for making predictions. Send a POST request with JSON data to get predictions.
```
{
  "Gender": 0,
  "Age": 35,
  "Sleep_Duration": 6.5,
  "Sleep_Quality": 7,
  "Physical_Activity_Level": 45,
  "Stress_Level": 9,
  "BMI_Category": 1,
  "Heart_Rate": 97,
  "Daily_Steps": 1200,
  "BP_Category": 1
}
```
