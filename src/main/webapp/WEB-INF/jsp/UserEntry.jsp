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
      position: relative;
      overflow: hidden;
    }

    .background-dots {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 1;
      background: transparent;
      overflow: hidden;
    }

    .dot {
      position: absolute;
      width: 5px;
      height: 5px;
      border-radius: 50%;
      background-color: white;
      animation: flicker 2s infinite alternate ease-in-out;
    }

    @keyframes flicker {
      0% { opacity: 0.2; }
      50% { opacity: 1; }
      100% { opacity: 0.2; }
    }

    .container {
      position: relative;
      z-index: 2;
      background-color: #000000;
      padding: 20px;
      border-radius: 8px;
      color: #ffffff;
      max-width: 400px;
      width: 100%;
      animation: rgbLightingBoxShadow 6s cubic-bezier(0.90, 0, 0.5, 5) infinite;
      box-shadow: 0px 21px 34px 21px rgba(179, 2, 255, 0.1);
      text-align: center;

    }

    @keyframes rgbLightingBoxShadow {
      0% { box-shadow: 0 0 15px rgba(255, 0, 0, 0.5); }
      25% { box-shadow: 0 0 15px rgba(0, 255, 255, 0.5); }
      50% { box-shadow: 0 0 15px rgba(115, 0, 255, 0.5); }
      75% { box-shadow: 0 0 15px rgba(0, 234, 255, 0.5); }
      100% { box-shadow: 0 0 15px rgba(34, 255, 0, 0.5); }
    }

    h1 {
      /* animation: rgbLightings 3s infinite; */
      text-align: center;
      color: #ffffff;
    }

    form {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
    }

    .form-group {
      display: flex;
      flex-direction: column;
      width: 45%; /* Adjust width as needed */
    }

    label {
      margin-bottom: 5px;
      font-size: 14px;
    }

    input[type="text"] {
      padding: 10px;
      border: 1px solid #000000;
      animation: rgbLightingBoxShadow 6s cubic-bezier(0.90, 0, 0.90, 4) infinite;
      border-radius: 4px;
      font-size: 14px;
    }

    input[type="submit"] {
      padding: 10px;
      border: 1px solid #fefefe;
      background-color: #000000;
      color: rgb(255, 255, 255);
      border-radius: 4px;
      border-color: #ffffff;
      cursor: pointer;
      font-size: 16px;
      width: 100%;
      /* animation: rgbLightings 3s infinite; */
      font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
    }

    input[type="submit"]:hover {
      color: #000000;
      animation: rgbLightings 3s infinite;
    }

    @keyframes rgbLightings {
      0% { color: #ff0000; }
      25% { color: aqua; }
      50% { color: #7300ff; }
      75% { color: #00eaff; }
      100% { color: #22ff00; }
    }
  </style>
</head>
<body>
  <div class="background-dots">
    <!-- Add 50 stable, blinking white dots -->
    <script>
      for (let i = 0; i < 50; i++) {
        let dot = document.createElement('div');
        dot.classList.add('dot');
        dot.style.top = Math.random() * 100 + 'vh';
        dot.style.left = Math.random() * 100 + 'vw';
        dot.style.width = Math.random() * 10 + 3 + 'px';
        dot.style.height = dot.style.width;
        dot.style.animationDelay = Math.random() * 2 + 's';
        document.querySelector('.background-dots').appendChild(dot);
      }
    </script>
  </div>

  <div class="container">
    <h1>User Adding</h1>
    <form action="signups">
      <div class="form-group">
        <label>Enter the Name:</label>
        <input type="text" name="name">
      </div>
      <div class="form-group">
        <label>Enter the Id:</label>
        <input type="text" name="id">
      </div>
      <div class="form-group">
        <label>Enter the Password:</label>
        <input type="password" name="password">
      </div>
      <div class="form-group">
        <label>Enter the Email:</label>
        <input type="text" name="email">
      </div>
      <div class="form-group">
        <label>Enter the Phone Number</label>
        <input type="text" name="phoneno">
      </div>
      
      <input type="submit" value="Save">
    </form>
  </div>
</body>
</html>
