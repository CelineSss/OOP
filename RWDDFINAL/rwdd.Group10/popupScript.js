let userType = ""; 

function openPopup(user) {
    document.getElementById("popup").style.display = "block";
    userType = user; 

    fetchProfileData(user);
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}

// Fetch Profile Data Based on User Type
async function fetchProfileData(userType) {
    let endpoint = "";
    
    if (userType === "student") {
        endpoint = "ProcessGetProfile.S.php";
    } else if (userType === "parent") {
        endpoint = "ProcessGetProfile.P.php";
    } else if (userType === "teacher") {
        endpoint = "ProcessGetProfile.T.php";
    } else {
        console.error("Invalid user type");
        return;
    }

    try {
        let response = await fetch(endpoint);
        let text = await response.text();
        console.log("Raw Response:", text);

        try {
            let data = JSON.parse(text.trim());
            if (data.error) {
                console.error("Error:", data.error);
            } else {
                document.getElementById("fullname").value = data[`${userType}Name`];
                document.getElementById("email").value = data[`${userType}Email`];

                
                let idField = document.getElementById("userID");
                if (idField) {
                    idField.value = data[`${userType}ID`];
                    idField.readOnly = true; 
                }
            }
        } catch (jsonError) {
            console.error("Invalid JSON response:", text);
        }
    } catch (error) {
        console.error("Error fetching data:", error);
    }
}

// Validation name and email
function validateInput(name, email) {
    let nameError = "";
    let emailError = "";

    if (name.trim() === "") {
        nameError = "Name is required.";
    }

    if (!email.match(/^\S+@\S+\.\S+$/)) {
        emailError = "Invalid email format.";
    }

    document.getElementById("nameError").textContent = nameError;
    document.getElementById("emailError").textContent = emailError;

    return nameError === "" && emailError === "";
}

// Save Profile Data Based on User Type
async function saveProfile() {
    console.log("saveProfile() triggered");

    let name = document.getElementById("fullname").value;
    let email = document.getElementById("email").value;

    if (!validateInput(name, email)) {
        console.log("Validation failed");
        return;
    }

    console.log("Validation passed. Sending data...");

    let updatedData = { name, email };
    let endpoint = "";

    if (userType === "student") {
        endpoint = "updateStudentData.php";
    } else if (userType === "parent") {
        endpoint = "updateParentData.php";
    } else if (userType === "teacher") {
        endpoint = "updateTeacherData.php";
    } else {
        console.error("Unknown user type.");
        return;
    }

    try {
        let response = await fetch(endpoint, {  
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedData),
        });

        let text = await response.text();
        console.log("Raw Response:", text);

        let result = JSON.parse(text);
        console.log("Parsed JSON Response:", result);

        if (result.success) {
            alert("Profile updated successfully!");
            closePopup();
        } else {
            alert("Error updating profile.");
        }
    } catch (error) {
        console.error("Error updating profile:", error);
    }
}


document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("saveProfileBtn").addEventListener("click", saveProfile);
});
