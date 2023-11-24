const express = require('express');
const app = express();
const diagnosisRouter = require('./routes/diagnosis');
const authRouter = require('./routes/auth'); // Import the authentication routes

// Middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Routes
app.use('/diagnosis', diagnosisRouter);
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

// Start the server
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
