<!--Ready Menu for Matching Quiz-->
<!--Currently use for Matching Quiz-->
<?php
session_start();
require('config.php'); 

if (!isset($_GET['quizID']) || !is_numeric($_GET['quizID'])) {
    die("Error: Invalid quizID.");
}

$quizID = intval($_GET['quizID']);
$gifFiles = [
    "giphy(1).gif",
    "giphy(2).gif",
    "giphy(3).gif",
    "giphy(4).gif",
    "giphy(5).gif",
    "giphy(6).gif",
    "giphy(7).gif",
    "giphy(8).gif",
    "giphy(9).gif",
    "giphy(10).gif"
];
$randomGif = $gifFiles[array_rand($gifFiles)];
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ready Menu</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Comic+Neue:wght@400;700&display=swap');

        body { 
            font-family: 'Comic Neue', cursive;
            text-align: center;
            background: linear-gradient(135deg, #E6FFE6, #F0E6FF);
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }
        .container { 
            background: white;
            border-radius: 25px;
            padding: 40px;
            width: 80%;
            max-width: 800px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.15);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        .gif-container img { 
            max-width: 800px; 
            border-radius: 10px; 
            margin-bottom:20px;
        }
        .button { 
            background: linear-gradient(145deg, #82ec76, #9e76f3);
            border: none;
            border-radius: 25px;
            padding: 15px 40px;
            font-size: 20px;
            color: white;
            cursor: pointer;
            transition: all 0.3s ease;
            font-family: 'Comic Neue', cursive;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .button:hover { 
            transform: scale(1.05);
            box-shadow: 0 6px 12px rgba(0,0,0,0.15);
        }
    </style>
</head>
<body>

<div class="container">
    <h1>👆🏻Start Quiz When You Are Ready👆🏻</h1>
    <div class="gif-container">
        <img src="gifs/<?php echo $randomGif; ?>" alt="Ready GIF">
    </div>
    <button class="button" onclick="startQuiz()">Start Quiz</button>
</div>

<script>
    function startQuiz() {
        window.location.href = "MatchingQuiz.php?quizID=<?php echo $quizID; ?>";
    }
</script>

</body>
</html>
