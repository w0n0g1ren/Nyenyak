const express = require('express');
const app = express();
const diagnosisRouter = require('./routes/diagnosis');
const authRouter = require('./routes/auth');
const { verifyFirebaseToken } = require('./config'); // Import the middleware

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Apply Firebase Auth middleware to routes that require authentication
app.use('/diagnosis', verifyFirebaseToken, diagnosisRouter); // Apply middleware to the routes
app.use('/auth', authRouter); // Use the authentication routes

const port = process.env.PORT || 8080;

app.get('/', (req, res) => {
  res.send(`
  <p>Hello World!</p>
  <form action="http://localhost:8080/auth/google-login" method="post">
    <button type="submit">Google Login Test</button>
  </form>
`);
});

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
