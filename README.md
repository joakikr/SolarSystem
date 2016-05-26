<b><h1>SolarsystemFX</h1></b><br>
View the planets movement from the past, the present and in the future with this little Java FX application.

<b>Calculations:</b><br>
Based on formulas presented in http://aa.quae.nl/en/reken/hemelpositie.html and http://stjarnhimlen.se/comp/tutorial.html.

<b>Credit:</b><br>
Planet Icons: CitizenJustin - http://citizenjustin.deviantart.com/

<b>Run:</b><br>
* Import into Eclipse as JavaFX project.
* Might need to add .jar files to build path (all jars in /libs folder) because external library joda is used. <br>
  -> right click project -> build path -> configure build path -> libraries -> add JAR's -> libs/*
* Run as Java Application

or<br>

* Run the file build/dist/SolarSystemFX.jar

<b>SolarSystem:</b><br>
The displayed system shows orbits as circles instead of ellipses and projectiles 3D coordinates to 2D normalized coordinates.

<b>TSP (Traveling Salesman Path):</b><br>
An additional feature is to show the shortest traveling route between all planets. To toggle this option, click the button in the upper left corner.

<b>Other:</b><br>
If you want to change the starting orbital elements values, create a file that follows the format of /data/orbital_elements.txt and change ORBITAL_ELEMENTS_PATH in src/application/Commons.java
to your new file. 

<b>Snapshot</b><br>
![image](https://s3.amazonaws.com/f.cl.ly/items/2t3g1n0A2U1S2u3A0H0o/Screen%20Shot%202016-05-26%20at%2015.14.32.png?v=e923d180)
