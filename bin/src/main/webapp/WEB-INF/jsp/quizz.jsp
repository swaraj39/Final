<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Timer</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
        }
        .question-card {
            transition: opacity 0.5s ease, transform 0.5s ease;
        }
        .hidden {
            opacity: 0;
            transform: scale(0.9);
        }
        .block {
            opacity: 1;
            transform: scale(1);
        }
    </style>
</head>
<body class="bg-gradient-to-r from-black to-red-800 min-h-screen flex items-center justify-center">

    <div class="container mx-auto p-4">
        <div class="bg-black shadow-xl rounded-lg p-8 max-w-5xl mx-auto space-y-8">
            <h1 class="text-3xl font-semibold text-center text-white">Quiz Timer</h1>

            <div id="timer" class="text-center text-5xl font-bold text-red-600 mb-6">
                01:00:00
            </div>

            <form id="quizForm" action="submitQuiz" method="post">
                <div id="questionsContainer">
                    <c:forEach items="${result}" var="question" varStatus="status">
                        <div class="question-card ${status.index == 0 ? 'block' : 'hidden'}" id="q${question.id}">
                            <p class="text-lg font-medium text-white mb-4"><strong>Question ${question.id}:</strong> ${question.question}</p>
                            <div class="space-y-4 mb-6">
                                <label class="flex items-center space-x-2 text-white">
                                    <input type="radio" id="q${question.id}_1" name="q${question.id}" value="${question.option1}" class="form-radio text-red-600">
                                    <span>${question.option1}</span>
                                </label>
                                <label class="flex items-center space-x-2 text-white">
                                    <input type="radio" id="q${question.id}_2" name="q${question.id}" value="${question.option2}" class="form-radio text-red-600">
                                    <span>${question.option2}</span>
                                </label>
                                <label class="flex items-center space-x-2 text-white">
                                    <input type="radio" id="q${question.id}_3" name="q${question.id}" value="${question.option3}" class="form-radio text-red-600">
                                    <span>${question.option3}</span>
                                </label>
                                <label class="flex items-center space-x-2 text-white">
                                    <input type="radio" id="q${question.id}_4" name="q${question.id}" value="${question.option4}" class="form-radio text-red-600">
                                    <span>${question.option4}</span>
                                </label>
                            </div>
                            <div class="flex justify-end mt-6">
                                <button type="button" class="btn btn-primary bg-red-600 text-white px-6 py-2 rounded-lg transition duration-300 transform hover:bg-red-700 hover:scale-105 nextBtn">
                                    Next <i class="fas fa-arrow-right"></i>
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="flex justify-end mt-6">
                    <button type="submit" class="btn btn-primary bg-red-700 text-white px-6 py-2 rounded-lg hidden transition duration-300 transform hover:bg-red-800 hover:scale-105" id="submitBtn">
                        Submit Quiz <i class="fas fa-check-circle"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Timer settings: 1 hour in seconds
        let timer = 60 * 60;

        // Function to format time as HH:MM:SS
        function formatTime(seconds) {
            const h = Math.floor(seconds / 3600);
            const m = Math.floor((seconds % 3600) / 60);
            const s = seconds % 60;
            return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
        }

        // Function to update the timer every second
        function updateTimer() {
            const timerElement = document.getElementById('timer');
            if (!timerElement) {
                console.error("Timer element not found!");
                return;
            }

            if (timer <= 0) {
                alert("Time is up!");
                document.getElementById('quizForm').submit(); // Submit the quiz when time is up
            } else {
                timerElement.textContent = formatTime(timer);
                timer--; // Decrement the timer by 1 second
            }
        }

        // Start the timer
        setInterval(updateTimer, 1000); // Call updateTimer every second

        // Navigation functionality
        const questionCards = document.querySelectorAll('.question-card');
        const nextBtns = document.querySelectorAll('.nextBtn');
        const submitBtn = document.getElementById('submitBtn');
        let currentQuestionIndex = 0;

        function showQuestion(index) {
            questionCards.forEach((card, i) => {
                card.classList.toggle('block', i === index);
                card.classList.toggle('hidden', i !== index);
            });

            submitBtn.classList.toggle('hidden', index !== questionCards.length - 1);
        }

        nextBtns.forEach((btn, index) => {
            btn.addEventListener('click', () => {
                if (index < questionCards.length - 1) {
                    currentQuestionIndex++;
                    showQuestion(currentQuestionIndex);
                }
            });
        });

        // Display the first question by default
        showQuestion(currentQuestionIndex);
    </script>
</body>
</html>
