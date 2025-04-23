<!--Usage: Link our system with database-->

<?php
$con = new mysqli("localhost", "root", "", "quizzing_you")
                                            //above is database name
                                            //should be same with xampp
or die ("Failed to connect to database".mysqli_connect_error());
?>