const express = require('express')
const app = express()
const diagnosisRouter = require("./routes/diagnosis")

// Midleware
app.use(express.json())
app.use(express.urlencoded({extended: true}))
app.use('/diagnosis', diagnosisRouter)

const port = process.env.PORT || 8080

app.get('/', (req, res) => {
    res.send('Hello World!')
})

// Start the server
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
  });