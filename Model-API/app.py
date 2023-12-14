from flask import Flask, request, jsonify
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
import joblib

app = Flask(__name__)

# Load the normalization model
scaler = joblib.load('models/scaler_model.save')

# Load the saved model
loaded_model = tf.keras.models.load_model('models/model_ann.h5')

@app.route('/')
def index():
    return '<h1>FLASK APP IS RUNNING!</h1>'

@app.route('/prediction', methods=['POST'])
def predict_sleep_disorder():
    # RECEIVE THE REQUEST
    content = request.json

    # PRINT THE DATA IN THE REQUEST
    print("[INFO] Request: ", content)

    # Create a DataFrame from the received JSON data
    input_data = pd.DataFrame({
        'Gender': [content['Gender']],
        'Age': [content['Age']],
        'Sleep_Duration': [content['Sleep_Duration']],
        'Sleep_Quality': [content['Sleep_Quality']],
        'Physical_Activity_Level': [content['Physical_Activity_Level']],
        'Stress_Level': [content['Stress_Level']],
        'BMI_Category': [content['BMI_Category']],
        'Heart_Rate': [content['Heart_Rate']],
        'Daily_Steps': [content['Daily_Steps']],
        'BP_Category': [content['BP_Category']]
    })

    # Normalize the input data using the pre-trained scaler
    #input_data[['Age', 'Sleep_Duration', 'Physical_Activity_Level', 'Heart_Rate', 'Daily_Steps']] = scaler.transform(input_data[['Age', 'Sleep_Duration', 'Physical_Activity_Level', 'Heart_Rate', 'Daily_Steps']])
    input_data[['Age', 'Sleep_Duration', 'Physical_Activity_Level', 'Heart_Rate', 'Daily_Steps']] = 0.0
    print(input_data)
    # PREDICT THE CLASS USING THE LOADED MODEL
    prediction = loaded_model.predict(input_data)

    # Get the predicted class (using argmax)
    predicted_class = int(tf.argmax(prediction, axis=1))

    # Define the mapping of predicted classes
    sleep_disorder_mapping = {0: 'Insomnia', 1: 'None', 2: 'Sleep Apnea'}

    # Get the predicted sleep disorder category
    predicted_sleep_disorder = sleep_disorder_mapping[predicted_class]

    # ADD THE PREDICTION RESULT TO THE INPUT DATA
    content['sleep_disorder'] = predicted_sleep_disorder

    # PRINT THE RESULT
    print("[INFO] Response: ", predicted_sleep_disorder)

    # SEND THE RESULT AS JSON OBJECT
    return jsonify(content)

if __name__ == '__main__':
    app.run("0.0.0.0", port=5000)
