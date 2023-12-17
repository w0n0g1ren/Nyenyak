# Nyenyak Cloud Computing Documentation
## Project Summary
Nyenyak uses Google Cloud Platform (GCP) and Firebase to develop a system capable of classifying sleep disorders, such as insomnia and sleep apnea, based on user data. Additionally, the system will provide actionable suggestions to enhance the sleep quality of users experiencing sleep disturbances. The project involves a collaboration between machine learning, mobile development, and cloud computing teams

## Cloud Architecture
![Architecture Illustration](https://github.com/canggihwr/cc-doc/blob/main/infrastructure.jpg)

## 1. Setup Google Cloud Platform

- Create Project & Configure Identity and Access Management.
- Enable the following APIs:
  - App Engine API
  - Cloud Run Admin API
  - Google Container Registry API
  - Firebase API (Management, Realtime Database, etc.)
  - Cloud Monitoring API
  - Cloud Logging API

## 2. Setup Firebase

- Open [Firebase](https://firebase.google.com/), go to the console, and connect it to your Google Cloud Project.
- Activate Firebase Auth & Firebase Realtime Database.
- Create a **Service Account** and download the corresponding `seviceAccountKey.json` file.

## 3. Clone Project and Set Google Cloud Account

- Open cloud shell or your preferred code editor (Visual Studio Code).
- Clone the Nyenyak project from [Nyenyak-Backend-Repo](https://github.com/w0n0g1ren/Nyenyak/tree/BackEnd) using the command `git clone -b BackEnd https://github.com/w0n0g1ren/Nyenyak.git`.
- Initialize a Git repository with `git init` and connect it to your Google Cloud account.

## 4. Set Project and Deploy Application

- In the terminal, set your project by executing `gcloud config set project nyenyak-project-dev`.
- Deploy both nodeJS and model API to App Engine and Cloud Run.

## 6. API Documentation

- For API Documentation, refer to the following link: [API Documentation](https://docs.google.com/document/d/1qCDyOA_lNiGtEkrO0boCFtzEbLcFK6LTec5WuPWMv50/edit?usp=sharing)

**Additional Backend Details:**
- Backend API is built using Node Express.js to handle user authentication, diagnosis, articles, and user details.
- We deployed the backend API to App Engine for easier scalability and reliability.
- A separate API for TensorFlow model, built using Flask and deployed to Cloud Run.
- Utilize Cloud Monitoring & Logging for comprehensive resource monitoring and alerting.

## Conclusion

The Nyenyak project integrates Google Cloud Platform and Firebase Realtime Database to create a robust and scalable solution. The backend architecture ensures efficient communication between the mobile app, backend API, and machine learning model, providing users with accurate sleep disorder diagnoses and solution for improvement.


===============================================

# Nyenyak Cloud Computing Documentation


[![Status: Active](https://img.shields.io/badge/Status-Active-green.svg)](https://github.com/w0n0g1ren/Nyenyak/tree/BackEnd)
[![Version: 1.0.0](https://img.shields.io/badge/Version-1.0.0-blue.svg)](https://github.com/w0n0g1ren/Nyenyak/tree/BackEnd)
[![Contributors: 2](https://img.shields.io/badge/Contributors-2-orange.svg)](https://github.com/w0n0g1ren/Nyenyak/graphs/contributors)

# Project Summary

Nyenyak is a capstone project aim to develop a system capable of classifying sleep disorders, such as insomnia and sleep apnea, based on user data. Additionally, the system will provide actionable suggestions to enhance the sleep quality of users experiencing sleep disturbances. It uses Google Cloud Platform and Firebase services to create a scalable and reliable solution that connects users, mobile apps, backend APIs, and machine learning models.

## Table of Contents

- [Nyenyak Cloud Computing Documentation](#nyenyak-cloud-computing-documentation)
  - [Project Summary](#project-summary)
  - [Table of Contents](#table-of-contents)
  - [Cloud Architecture](#cloud-architecture)
  - [Setup Google Cloud Platform](#setup-google-cloud-platform)
  - [Setup Firebase](#setup-firebase)
  - [Clone Project and Set Google Cloud Account](#clone-project-and-set-google-cloud-account)
  - [Set Project and Deploy Application to App Engine and Cloud Run](#set-project-and-deploy-application-to-app-engine-and-cloud-run)
  - [API Documentation](#api-documentation)

## Cloud Architecture

![Architecture Illustration](https://github.com/canggihwr/cc-doc/blob/main/infrastructure.jpg)

## Setup Google Cloud Platform

- Create Project & Configure Identity and Access Management.
- Enable the following APIs:
  - App Engine API
  - Cloud Run Admin API
  - Google Container Registry API
  - Firebase API (Management, Realtime Database, etc.)
  - Cloud Monitoring API
  - Cloud Logging API


## Setup Firebase

- Open [Firebase](https://firebase.google.com/), go to the console, and connect it to your Google Cloud Project.
- Activate Firebase Auth & Firebase Realtime Database.
- Create a **Service Account** and download the corresponding `seviceAccountKey.json` file.

## Clone Project and Set Google Cloud Account

- Open cloud shell or your preferred code editor (Visual Studio Code).
- Clone the Nyenyak project from [Nyenyak-Backend-Repo](https://github.com/w0n0g1ren/Nyenyak/tree/BackEnd) using the command `git clone -b BackEnd https://github.com/w0n0g1ren/Nyenyak.git`.
- Initialize a Git repository with `git init` and connect it to your Google Cloud account.

## Set Project and Deploy Application to App Engine and Cloud Run

- In the terminal, set your project by executing `gcloud config set project your-project`.
- Deploy both nodeJS and model API to App Engine and Cloud Run.
  - Navigate to the directory of your Node.js API and execute the following command `gcloud app deploy`.
  - Navigate to the Model API directory, build and push your docker image, then deploy the model API to Cloud Run by running this command `gcloud run deploy --image gcr.io/nyenyak-project-dev/nyenyak-model-api`

## API Documentation

- For API Documentation, refer to the following link: [API Documentation](https://docs.google.com/document/d/1qCDyOA_lNiGtEkrO0boCFtzEbLcFK6LTec5WuPWMv50/edit?usp=sharing)

**Additional Backend Details:**
- Backend API is built using Node Express.js to handle user authentication, diagnosis, articles, and user details.
- We deployed the backend API to App Engine for easier scalability and reliability.
- A separate API for TensorFlow model, built using Flask and deployed to Cloud Run.
- Utilize Cloud Monitoring & Logging for comprehensive resource monitoring and alerting.

## Conclusion

The Nyenyak project integrates Google Cloud Platform and Firebase Realtime Database to create a robust and scalable solution. The backend architecture ensures efficient communication between the mobile app, backend API, and machine learning model, providing users with accurate sleep disorder diagnoses and solution for improvement.
