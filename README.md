APU Hostel Management Fees Payment System
Quick Start
1. Install Java JDK 8 or higher
   Download here： https://www.oracle.com/java/technologies/downloads/?er=221886

2. Download or clone this project.

3. Open the project in your Java IDE (like NetBeans, IntelliJ IDEA, Eclipse) OR use the terminal.

4. Go to the /main folder and run HostelManagementSystem.java.

Terminal example:
javac main/HostelManagementSystem.java
java main.HostelManagementSystem
** No external libraries required — uses only standard Java!

Description
The APU Hostel Management Fees Payment System manages hostel residents' fees, user accounts, and hostel information for APU.
The system includes:
- Login and Registration for users
- Manager Functions:
Approval of pending user registrations
Manage users and rates
Manage hostel rules and information
Modify manager profile
- Staff Functions: Make Payment & Generate Receipt
- Resident Functions: View Payment Records

** All user data and transaction details are stored in text files.

How to Run
This is a Java project.
Option 1: Using Terminal (Command Prompt)
javac main/HostelManagementSystem.java
java main.HostelManagementSystem
** Make sure you are inside the project directory when running the above commands.

Option 2: Using IDE (Recommended)
Open the project in your favorite Java IDE (Eclipse, IntelliJ IDEA, NetBeans).
Navigate to /main/HostelManagementSystem.java.
Right-click and choose Run.

Requirements
- Java Development Kit (JDK) 8 or higher
- No external libraries or Maven dependencies — 100% built with standard Java.

** Important Notes
- Make sure the /data folder and /gui/images folder are present in the correct location.
- All user credentials, hostel information, and fee records are stored in text files under /data.
- If running outside of an IDE, ensure the working directory includes the full project path so that file reading works correctly.
- Exception handling included: Invalid emails and exceeded login attempts.
