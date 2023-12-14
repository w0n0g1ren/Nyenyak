from flask import Flask, request, jsonify
from joblib import load
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf

app = Flask(__name__)

# Load the normalization scaler
scaler = load("models/normalization_model.joblib")

# Load the updated TensorFlow model
loaded_model = tf.keras.models.load_model('models/model_ann.h5')

# Define the columns to be normalized
columns_to_normalize = ['Age', 'Sleep_Duration', 'Physical_Activity_Level', 'Heart_Rate', 'Daily_Steps']

# Provide feature names to the scaler
scaler.feature_names_in_ = columns_to_normalize

@app.route('/')
def index():
    return '<h1>FLASK APP IS RUNNING!</h1>'

@app.route('/prediction', methods=['POST'])
def predict_sleep_disorder():
    # RECEIVE THE REQUEST
    content = request.json

    # PRINT THE DATA IN THE REQUEST
    print("[INFO] Request: ", content)

    # Create a Pandas DataFrame from the input data
    input_df = pd.DataFrame(content, index=[0])

    # NORMALIZE THE SPECIFIED COLUMNS
    input_df[columns_to_normalize] = scaler.transform(input_df[columns_to_normalize])

    # CONVERT INPUT TO DICTIONARY
    content_normalized = input_df.to_dict(orient='records')[0]

    # CONVERT INPUT TO NUMPY ARRAY
    input_data = np.array(list(content_normalized.values())).reshape(1, -1)

    # PREDICT THE CLASS USING THE LOADED MODEL
    prediction = loaded_model.predict(input_data)

    # GET THE INDEX OF THE PREDICTED CLASS
    predicted_class_index = np.argmax(prediction, axis=1)

    # MAP THE INDEX TO THE CLASS LABEL
    labels = ["Insomnia", "None", "Sleep Apnea"]
    predicted_class = labels[predicted_class_index[0]]

    # ADD THE PREDICTION RESULT TO THE INPUT DATA
    content_normalized['sleep_disorder'] = predicted_class

    # PRINT THE RESULT
    print("[INFO] Response: ", predicted_class)

    # SEND THE RESULT AS JSON OBJECT
    return jsonify(content_normalized)

if __name__ == '__main__':
    app.run("0.0.0.0")
