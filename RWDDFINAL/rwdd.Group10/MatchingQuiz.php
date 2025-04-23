<!--For student to do Matching Quiz-->
<?php
session_start();
require('config.php'); 

// Validate quizID
if (!isset($_GET['quizID']) || !is_numeric($_GET['quizID'])) {
    die("Error: Invalid quizID.");
}

$quizID = intval($_GET['quizID']);

// Check if student is logged in
if (!isset($_SESSION['studentID'])) {
    die("Error: Student not logged in.");
}
$studentID = $_SESSION['studentID'];

// Fetch matching pairs
$query = "SELECT matchingID, questionID, subject, correctAnswer 
          FROM matchingpair 
          WHERE questionID IN (SELECT questionID FROM question WHERE quizID = ?)";
$stmt = $con->prepare($query);
$stmt->bind_param("i", $quizID);
$stmt->execute();
$result = $stmt->get_result();

$matchingPairs = [];
while ($row = $result->fetch_assoc()) {
    $matchingPairs[] = $row;
}

$stmt->close();
$con->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matching Quiz</title>
    <link rel="stylesheet" href="MatchingQuiz.css">

</head>
<body>
    <div class="container">
        <div class="question">
            <div class="matching-title">Matching Quiz</div>
            <br>
            <div class="matching-hint">Instructions: Select a prompt and match it with its correct answer.</div>
            <form action="ProcessSaveAttempt.php" method="POST" id="quizForm">
                <input type="hidden" name="quizID" value="<?php echo $quizID; ?>">
                <input type="hidden" name="studentID" value="<?php echo $studentID; ?>">
                <input type="hidden" name="durationTaken" id="durationTaken">
                <input type="hidden" name="score" id="score">
                <input type="hidden" name="answers" id="answers"> <!-- ✅ Keep only one -->

                <div id="quizQuestions">
                    <?php foreach ($matchingPairs as $row): ?>
                        <div class="matching-boxes">
                            <div class="prompt-box" data-answer="<?php echo htmlspecialchars($row['correctAnswer']); ?>" 
                                data-matching-id="<?php echo $row['matchingID']; ?>">
                                <?php echo htmlspecialchars($row['subject']); ?>
                            </div>
                            <div class="answer-box" data-answer="<?php echo htmlspecialchars($row['correctAnswer']); ?>" 
                                data-matching-id="<?php echo $row['matchingID']; ?>">
                                <?php echo htmlspecialchars($row['correctAnswer']); ?>
                            </div>
                        </div>
                    <?php endforeach; ?>
                </div>
                <button type="submit">Submit Quiz</button>
            </form>
            <div id="totalScore"></div>
        </div>
    </div>

    <script>
        let selectedPrompt = null;
        let correctAnswers = 0;
        let totalQuestions = <?php echo count($matchingPairs); ?>;
        let startTime = Date.now();
        let studentAnswers = []; 
        let answeredQuestions = new Set(); 

        function saveAnswer(matchingID, userAnswer) {
            studentAnswers = studentAnswers.filter(a => !a.startsWith(matchingID + ":"));
            studentAnswers.push(matchingID + ":" + userAnswer);
            answeredQuestions.add(matchingID); // Mark question as answered
            console.log("✅ Updated studentAnswers:", studentAnswers);
        }

        document.querySelectorAll(".prompt-box").forEach(prompt => {
            prompt.addEventListener("click", function () {
                if (this.classList.contains("disabled")) return;
                if (selectedPrompt) selectedPrompt.classList.remove("selected");
                selectedPrompt = this;
                selectedPrompt.classList.add("selected");
            });
        });

        document.querySelectorAll(".answer-box").forEach(answer => {
            answer.addEventListener("click", function () {
                if (!selectedPrompt || this.classList.contains("disabled")) return;

                let matchingID = selectedPrompt.dataset.matchingId;
                let correctAnswer = selectedPrompt.dataset.answer;
                let selectedAnswer = this.textContent.trim();
                let isCorrect = selectedAnswer === correctAnswer;

                // Save answer and mark question as answered
                saveAnswer(matchingID, selectedAnswer);

                selectedPrompt.classList.add(isCorrect ? "matched" : "incorrect");
                this.classList.add(isCorrect ? "matched" : "incorrect");

                if (isCorrect) correctAnswers++;

                selectedPrompt.classList.add("disabled");
                this.classList.add("disabled");

                selectedPrompt = null; // Reset selection
            });
        });

        // Ensure all questions are answered before submitting
        document.getElementById("quizForm").addEventListener("submit", function (event) {
            if (answeredQuestions.size < totalQuestions) {
                event.preventDefault();
                alert("⚠️ Please answer all the questions before submitting!");
                return;
            }

            let formattedAnswers = studentAnswers.join(",");
            document.getElementById("answers").value = formattedAnswers;
            document.getElementById("score").value = correctAnswers;
            document.getElementById("durationTaken").value = Math.floor((Date.now() - startTime) / 1000);

            console.log("📤 Submitting Data:", formattedAnswers);
        });
    </script>

</body>
</html>
