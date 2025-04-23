//Validate QuizID that student entered at the HomePage and lead them to the MatchingQuiz.php to do quiz
document.addEventListener("DOMContentLoaded", function () {
    let inputField = document.getElementById("join-game-pin");
    inputField.addEventListener("keypress", handleEnter);
});

function handleEnter(event) {
    if (event.key === "Enter" || event.keyCode === 13 || event.which === 13) {
        event.preventDefault();
        validateQuizID();
    }
}

function checkQuizID(event) {
    // Check if Enter (keyCode 13) is pressed
    if (event.keyCode === 13) {
        event.preventDefault(); 
        document.getElementById('quizForm').submit();
    }
}

async function validateQuizID() {
    let quizID = document.getElementById("join-game-pin").value.trim();
    if (quizID === "") {
        alert("Please enter a Quiz ID.");
        return;
    }
    if (!/^\d+$/.test(quizID)) {
        alert("Quiz ID should contain only numbers.");
        return;
    }

    let inputField = document.getElementById("join-game-pin");
    inputField.disabled = true;

    let loader = document.getElementById("loading-spinner");
    if (loader) loader.style.display = "block"; 

    try {
        let response = await fetch(`validateQuizID.php?join-game-pin=${quizID}`);
        let result = await response.text();

        if (result.trim() === "valid") {
            window.location.href = `ReadyMenu.php?quizID=${quizID}`;
        } else {
            alert("Entered QuizID does not exist. Please try again.");
        }
    } catch (error) {
        alert("An error occurred while validating the Quiz ID.");
        console.error("Error:", error);
    } finally {
        inputField.disabled = false;
        if (loader) loader.style.display = "none";
    }
}

