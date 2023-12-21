# Nyenyak-backend
Nyenyak's backend is built using Express.js. It serves as an API and integrates with Firebase for authentication and uses axios for making HTTP requests. 

## Prerequisites

Before setting up Nyenyak-backend, ensure that you have the following prerequisites installed on your system:

- Node.js (https://nodejs.org/)
- npm (Node Package Manager, included with Node.js installation)

## 1. Install Dependencies

Before getting started, make sure to install the required dependencies using npm:

```
npm install express
npm install firebase
npm install firebase-admin@latest
npm install axios
npm install date-fns
```

## 2. Set Up Firebase Service Account

To set up Firebase service account, follow these steps:

1. Log in to your Firebase console.
2. Navigate to Project Settings > Service accounts.
3. Generate a new private key and save it as `serviceAccountKey.json` in the root of your project.

## 3. Start the Nyenyak-backend

After installing the dependencies and setting up the Firebase service account, you can run the backend:
```
npm start
```
This command will start the Express.js server, and your API will be accessible at http://localhost:8080 by default.
