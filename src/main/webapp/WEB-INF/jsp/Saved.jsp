<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Application</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #000000;
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
      overflow: hidden;
      position: relative;
    }

    /* Style for blinking and size-changing text */
    .submitted-word {
      position: absolute;
      font-size: 16px;
      color: white;
      opacity: 0.2;
      animation: blinkSize 3s infinite ease-in-out;
      white-space: nowrap; /* Prevent text from wrapping */
    }

    /* Keyframes for changing size and blinking */
    @keyframes blinkSize {
      0% { font-size: 10px; opacity: 0.2; }
      50% { font-size: 50px; opacity: 1; }
      100% { font-size: 10px; opacity: 0.2; }
    }

    .container {
      position: relative;
      z-index: 2;
      background-color: #000000;
      padding: 20px;
      border-radius: 8px;
      color: #ffffff;
      box-shadow: 0 0 15px rgba(255, 247, 0, 0.1);
      width: 100%;
      max-width: 400px;
    }

    h1 {
      animation: rgbLightings 3s infinite;
      text-align: center;
      color: #ffffff;
    }

    @keyframes rgbLightings {
      0% {color:#ff0000}
      25% {color: aqua;}
      50% {color:#7300ff}
      75% {color:#00eaff}
      100% {color:#22ff00}
    }

    input[type="submit"] {
      padding: 10px;
      border: none;
      background-color: #ffffff;
      color: rgb(0, 0, 0);
      border-radius: 4px;
      cursor: pointer;
      font-size: 16px;
      animation: rgbLightingBackground 3s infinite;
      font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
    }

    input[type="submit"]:hover {
      background-color: #218838;
    }
  </style>
</head>
<body>
  <!-- Generate multiple "Submitted" words with additional text -->
  <script>
    const phrases = [
      "Submitted Successfully!",
      "Submission Complete!",
      "Your Submission is Recorded!",
      "Thanks for Submitting!",
      "Form Submitted!",
      "Your Entry is Saved!",
      "Submission Received!",
      "Data Submitted!"
    ];

    for (let i = 0; i < 50; i++) {
      let word = document.createElement('div');
      word.classList.add('submitted-word');
      word.innerText = `${phrases[Math.floor(Math.random() * phrases.length)]}`;
      word.style.top = Math.random() * 100 + 'vh';
      word.style.left = Math.random() * 100 + 'vw';
      word.style.animationDelay = Math.random() * 3 + 's';  /* Random delay for animations */
      document.body.appendChild(word);
    }
  </script>

  <div class="container">
    <h1>Saved</h1>
    <a href="home"><input type="submit" value="Go Back"></a>
  </div>
</body>
</html>
