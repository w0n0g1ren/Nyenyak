const express = require('express');
const { db } = require('../config');

const router = express.Router();
router.use(express.json());

// Route to get all articles
router.get('/', async (req, res) => {
  try {
    const articlesSnapshot = await db.ref('articles').once('value');
    const articlesData = articlesSnapshot.val();

    if (!articlesData) {
      return res.status(404).json({ status: "failed", message: 'No articles found' });
    }

    const articles = Object.values(articlesData);
    res.json(articles);
  } catch (error) {
    console.error('Error fetching articles:', error.message);
    res.status(500).json({ status: "failed", message: 'Error fetching articles', error: error.message });
  }
});

// Route to post a new article
router.post('/', async (req, res) => {
  try {
    const { title, type, image, content } = req.body;

    if (!title || !type || !image || !content) {
      return res.status(400).json({ status: "failed", message: 'Please provide all required fields for the article' });
    }

    const newArticleRef = db.ref('articles').push();
    const newArticleId = newArticleRef.key;

    const newArticle = {
      id: newArticleId,
      title,
      type,
      image,
      content
    };

    await newArticleRef.set(newArticle);

    res.status(201).json({ status: "success", message: 'Article successfully added', newArticle });
  } catch (error) {
    console.error('Error creating article:', error.message);
    res.status(500).json({ status: "failed", message: 'Error creating article', error: error.message });
  }
});

// Route to get articles based on sleep disorder type
router.get('/:type', async (req, res) => {
  try {
    const sleepDisorderType = req.params.type;
    const articlesSnapshot = await db.ref('articles').orderByChild('type').equalTo(sleepDisorderType).once('value');
    const articlesData = articlesSnapshot.val();

    if (!articlesData) {
      return res.status(404).json({ status: "failed", message: 'No articles found for the specified sleep disorder type' });
    }

    const articles = Object.values(articlesData);
    res.json(articles);
  } catch (error) {
    console.error('Error fetching articles:', error.message);
    res.status(500).json({ status: "failed", message: 'Error fetching articles', error: error.message });
  }
});

module.exports = router;
