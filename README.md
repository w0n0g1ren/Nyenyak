# Machine Learning Documentation (Nyenyak)

## Sleep Disorder Prediction Model
The sleep disorder prediction model is a system that uses machine learning techniques to analyze and predict an individual’s sleep patterns based on available data. Its primary purpose is to assist individuals and health professionals in identifying and managing sleep disorders.

### Analysis Methods
#### Feature-Based Classification
This method uses classification algorithms to predict whether an individual has a sleep disorder or not based on certain features, such as sleep duration, sleep quality, sleep habits, and other factors.

#### Pattern-Based Classification
This method uses techniques such as clustering or neural networks to identify abnormal sleep patterns and classify them into different categories of sleep disorders.

### Artificial Neural Network (ANN)
The model employs an ANN for training. ANNs are computing systems inspired by the biological neural networks that constitute animal brains. They are capable of learning complex patterns and relationships from data, making them suitable for tasks such as sleep disorder prediction. The ANN is trained using a suitable optimization algorithm, such as stochastic gradient descent or Adam optimizer, and a suitable loss function, such as binary cross-entropy for binary classification tasks.

### Datasets

The dataset used in this model is the [sleep health and lifestyle dataset.](https://www.kaggle.com/datasets/uom190346a/sleep-health-and-lifestyle-dataset/) This dataset contains data about sleep patterns, lifestyle, and health conditions of a number of individuals.

Dataset Preview<br>
| Person ID | Gender | Age | Occupation | Sleep Duration | Quality of Sleep | Physical Activity Level | Stress Level | BMI Category | Blood Pressure | Heart Rate | Daily Steps | Sleep Disorder |
|-----------|--------|-----|------------|----------------|------------------|-------------------------|--------------|--------------|----------------|------------|-------------|----------------|
| 1 | Male | 27 | Software Engineer | 6.1 | 6 | 42 | 6 | Overweight | 126/83 | 77 | 4200 | None |
| 2 | Male | 28 | Doctor | 6.2 | 6 | 60 | 8 | Normal | 125/80 | 75 | 10000 | None |
| 3 | Male | 28 | Doctor | 6.2 | 6 | 60 | 8 | Normal | 125/80 | 75 | 10000 | None |
| 4 | Male | 28 | Sales Representative | 5.9 | 4 | 30 | 8 | Obese | 140/90 | 85 | 3000 | Sleep Apnea |
| 5 | Male | 28 | Sales Representative | 5.9 | 4 | 30 | 8 | Obese | 140/90 | 85 | 3000 | Sleep Apnea |
| 6 | Male | 28 | Software Engineer | 5.9 | 4 | 30 | 8 | Obese | 140/90 | 85 | 3000 | Insomnia |
| 7 | Male | 29 | Teacher | 6.3 | 6 | 40 | 7 | Obese | 140/90 | 82 | 3500 | Insomnia |
| 8 | Male | 29 | Doctor | 7.8 | 7 | 75 | 6 | Normal | 120/80 | 70 | 8000 | None |
| 9 | Male | 29 | Doctor | 7.8 | 7 | 75 | 6 | Normal | 120/80 | 70 | 8000 | None |
| 10 | Male | 29 | Doctor | 7.8 | 7 | 75 | 6 | Normal | 120/80 | 70 | 8000 | None |

Data is extracted directly from the database for processing. This data undergoes a cleaning process to ensure its quality and is then prepared for the training phase. The Artificial Neural Network (ANN) model is trained using this prepared data. After the training phase is complete, the model is capable of predicting sleep disorders in individuals.

### Deployment
The model’s structure is deployed to the backend service. Following deployment, the model accesses the data, performs the necessary computations, and transmits the prediction to the application.

### Components
* Python: The programming language used for the project.
* Keras: A high-level neural networks API, running on top of TensorFlow.
* Pandas: For data manipulation and analysis.
* NumPy: For numerical computation.
* Scikit-learn: For machine learning and data mining.
* TensorFlow: An open-source platform for machine learning.
* seaborn: For data visualization.
* matplotlib.pyplot: For creating static, animated, and interactive visualizations in Python.
* plotly.express: For creating interactive data visualizations.
* warnings: For suppressing warnings.
* joblib: For saving and loading scikit-learn models.

### Requirements
* [Python](https://www.python.org/downloads/) version 3.6 or above.
* Latest [TensorFlow](https://www.tensorflow.org/) version.
* [Jupyter Notebook](https://jupyter.org/)/[Google Colab](https://colab.research.google.com/)

### Model Evaluation
The model is evaluated based on metrics such as accuracy, precision, recall, and F1-score. This evaluation is crucial to ensure that the model can accurately predict sleep disorders.
