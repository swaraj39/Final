<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Quiz Results</title>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Roboto', sans-serif;
      background: linear-gradient(135deg, #1b1b1b, #292929);
      color: #f0f0f0;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 900px;
      margin: 40px auto;
      padding: 30px;
      background-color: #333;
      border-radius: 20px;
      box-shadow: 0 8px 20px rgba(0,0,0,0.5);
      text-align: center;
      animation: fadeIn 1.2s ease-out;
    }

    h1 {
      font-size: 2.5rem;
      margin-bottom: 10px;
      color: #f6a623;
    }

    h3 {
      margin-bottom: 30px;
    }

    .progress-circle {
      position: relative;
      width: 120px;
      height: 120px;
      margin: 0 auto 30px;
    }

    .progress-circle svg {
      transform: rotate(-90deg);
      width: 100%;
      height: 100%;
    }

    .progress-circle circle {
      fill: none;
      stroke-width: 10;
    }

    .bg-circle {
      stroke: #444;
    }

    .progress {
      stroke: #f6a623;
      stroke-dasharray: 283;
      stroke-dashoffset: calc(283 - (283 * var(--percentage)) / 100);
      transition: stroke-dashoffset 1s ease;
    }

    .percentage {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      font-size: 1.5rem;
      font-weight: bold;
      color: #fff;
    }

    .question-container {
      background-color: #222;
      border-radius: 12px;
      padding: 20px;
      margin: 20px 0;
      text-align: left;
      transition: transform 0.4s ease, box-shadow 0.3s ease;
      animation: slideUp 0.8s ease;
    }

    .question-container:hover {
      transform: scale(1.03);
      box-shadow: 0 0 20px rgba(246, 166, 35, 0.3);
    }

    .options p {
      margin: 8px 0;
    }

    .correct-answer {
      color: #4CAF50;
      font-weight: bold;
    }

    .user-answer {
      color: #f6a623;
      font-weight: bold;
    }

    .wrong-answer {
      color: #F44336;
      font-weight: bold;
    }

    a {
      color: #f6a623;
      font-size: 1.1rem;
      font-weight: bold;
      text-decoration: none;
      display: inline-block;
      margin-top: 20px;
      transition: color 0.3s;
    }

    a:hover {
      color: #fff;
    }

    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(-10px); }
      to { opacity: 1; transform: translateY(0); }
    }

    @keyframes slideUp {
      from { opacity: 0; transform: translateY(50px); }
      to { opacity: 1; transform: translateY(0); }
    }
  </style>
</head>

<body>
<div class="container">
  <h1>🎉 Congratulations!</h1>
  <h3>Your Quiz Result: <span th:text="${result}"></span> out of <span th:text="${total}"></span></h3>

  <div class="progress-circle">
    <svg>
      <circle class="bg-circle" cx="60" cy="60" r="45"></circle>
      <circle class="progress" cx="60" cy="60" r="45" 
              style="--percentage: ${percentage};"></circle>
    </svg>
    <div class="percentage" th:text="${percentage + '%'}"></div>
  </div>

  <h2>All Questions and Your Answers:</h2>

  <div th:each="q : ${results}" class="question-container">
    <p><strong>Q:</strong> <span th:text="${q.question}">Question text here</span></p>
    <div class="options">
      <p>1: <span th:text="${q.option1}">Option 1</span></p>
      <p>2: <span th:text="${q.option2}">Option 2</span></p>
      <p>3: <span th:text="${q.option3}">Option 3</span></p>
      <p>4: <span th:text="${q.option4}">Option 4</span></p>
      
      <p th:class="${q.correct} ? 'user-answer' : 'wrong-answer'">
        <span th:if="${!q.correct}">✖ Your Answer: <span th:text="${q.userAnswer}">User answer</span></span>
        <span th:if="${q.correct}">✔ Your Answer: <span th:text="${q.userAnswer}">User answer</span></span>
      </p>
      
      <p class="correct-answer">✅ Correct Answer: <span th:text="${q.correctans}">Correct answer</span></p>
    </div>
  </div>

  <a href="/quiz">🔁 Take the quiz again</a>
</div>
</body>
</html>