<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration - Quizzing You</title>
    <link href="https://fonts.googleapis.com/css2?family=Neucha&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Neucha', cursive;
            background: linear-gradient(135deg, #E6FFE6, #F0E6FF);
            margin: 0;
            padding: 0;
            min-height: 100vh;
        }

        .header {
            display: flex;
            padding: 20px 40px;
            background-color: transparent;
        }

        .logo {
            width: 380px;
            height: 130px;
        }

        .registration-container {
            max-width: 600px;
            margin: 5px auto;
            padding: 30px;
            background: white;
            border-radius: 20px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }

        h1 {
            font-size: 50px;
            color: #6B4E8B;
            margin-top: 5px;
            margin-bottom: 30px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            font-size: 24px;
            color: #6B4E8B;
            margin-bottom: 4px;
        }

        .form-group input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 17px; 
            font-family: 'Neucha', cursive;
            height: 50px; 
            box-sizing: border-box;
        }

        .password-requirements {
            font-size: 17px;
            color: #666;
            margin-top: 3px;
        }

        .show-password {
            display: flex;
            align-items: center;
            gap: 5px;
            margin-top: 4px; 
            color: #6B4E8B;
        }

        .gender-select {
            width: 100%;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            font-family: 'Neucha', cursive;
            height: 50px; 
            box-sizing: border-box;
            background-color: #f8f8f8;
        }

        .register-btn {
            width: 100%;
            padding: 18px;
            background: #542d80;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 25px;
            font-family: 'Neucha', cursive;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .register-btn:hover {
            background: #6b4e8beb;
        }

        .show-password input[type="checkbox"] {
            width: 16px;
            height: 16px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="left-section">
            <img src="images/logo2.png" alt="Logo" class="logo">
        </div>
    </header>

    <div class="registration-container">
        <h1>Registration</h1>
        <form method="POST" action="ProcessReg.T.php">
            <div class="form-group">
                <label for="teacherName">NAME</label>
                <input type="text" id="teacherName" name="teacherName" placeholder="Enter Name" required>
            </div>

            <div class="form-group">
                <label for="teacherPW">PASSWORD</label>
                <input type="password" id="teacherPW" name="teacherPW" placeholder="Enter password" 
                       pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d\-_\.@]{8,}$" 
                       title="Password must be at least 8 characters, contain both letters and numbers, and only these symbols: -_.@" required>
                <div class="password-requirements">
                    Password must be at least 8 characters, and contain both letters and numbers. Only these symbols can be used -_.@
                </div>
                <div class="show-password">
                    <input type="checkbox" id="showPass1" onclick="togglePassword('teacherPW', 'showPass1')"> Show password
                </div>
            </div>

            <div class="form-group">
                <label for="confirmPassword">CONFIRM</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Re-enter password" required>
                <div class="password-requirements">
                    Password must match the one above.
                </div>
                <div class="show-password">
                    <input type="checkbox" id="showPass2" onclick="togglePassword('confirmPassword', 'showPass2')"> Show password
                </div>
            </div>

            <div class="form-group">
                <label for="gender">GENDER</label>
                <select id="gender" name="gender" class="gender-select" required>
                    <option value="">-Select your gender-</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>
                </select>
            </div>

            <div class="form-group">
                <label for="teacherEmail">EMAIL</label>
                <input type="email" id="teacherEmail" name="teacherEmail" placeholder="Enter e-mail address" required>
            </div>

            <button type="submit" name="register" value="register" class="register-btn">REGISTER</button>
        </form>
    </div>

    <script>
        function togglePassword(inputId, checkboxId) {
            const passwordField = document.getElementById(inputId);
            const checkbox = document.getElementById(checkboxId);
            if (checkbox.checked) {
                passwordField.type = 'text';
            } else {
                passwordField.type = 'password';
            }
        }

        document.querySelector('form').onsubmit = function(event) {
            const password = document.getElementById('teacherPW').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert("Passwords do not match.");
                event.preventDefault(); 
            }
        };
    </script>
</body>
</html>
