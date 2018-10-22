- How to compile source code and run program in Netbeans:
	* NOTE, we have included the latest compilation of the "target" folder with the assignment submission, so it
may be able to be ran with the green arrow straight away. But if it doesn't, below is the instructions on how to
compile the project.
	- In Netbeans: click File > Open Project > navigate to and select "PlagiarismDetection" folder > Open Project.
	- You should now be able to see our source code under the "Projects" window on the left hand side of the screen,
in Source Packages > PlagiarismDetection > FXMLController.java, etc.
	- Before compiling, you may need to download the project's dependencies: in the "Projects" window, right click the
"Dependencies" folder and click "Download Declared Dependencies". This may take some time. You will know its finished
when the blue bar and status updates at the bottom right of the screen are gone. (It's to the left of the cursor
coordinates).
	- After all dependencies have been downloaded, click the "Clean and Build Project" button (Hammer and Broom icon).
This will re-create the "target" folder from scratch, which has the compiled classes for our source code.
It will also create the PlagiarismDetection-x.x.jar. Note if you need to compile/build the project again, to save
time you can just press the "Build" button instead of clean and build. As long as no new project dependencies were added
then this will re-compile just our source code and not delete anything that wasn't changed.
	- The green arrow or debug button can now be used to run the program. If you want to, you can add breakpoints to
the source code and debug it, it will work just fine.


- How to view and edit the scene.fxml file (This file is where the GUI layout is stored):
	- Download: https://gluonhq.com/products/scene-builder/
	- Once downloaded, you should now be able to open and view the file in PlagiarismDetection\src\main\resources\fxml\Scene.fxml


- How to run .jar file without netbeans:
	- Double click the PlagiarismDetection-x.x.jar to open the program WITHOUT console remaining on the screen.
	- Open "RunDebug.bat" to open the program WITH console remaining on the screen.
*NOTE: 
	- MAKE SURE TO HAVE THE serviceAccountKey.json in the same folder as the jar and the .bat file.
	- Make sure that if you're using RunDebug.bat, that the name of the jar is correct. Just edit it if it's not.
So: java -jar whateverName.jar matches the jar you're trying to open.


 - EXTRA NOTES:
	- We have also included all .jar files that were made at different points of the program's development, this 
can be found in the folder "Program Executables". 
	- Also included are screenshots of the Firebase firestore(the database) and users authentication screens,
which is in the "Firebase console pictures" folder.
	- There is an admin user that can be used for marking, email: test@test.com, password: test123. Admin users are given access
to the debug screen (top right corner of the screen, there will be the debug/test button).
	- If you wish to see the Firebase console, please send an email to Daniel at djo307@uowmail.edu.au (with a gmail account)
and he will give you viewing and editing rights to it. On the Firebase console you can see all stored documents and users in realtime
so it may be of interest for marking.
	- Here is the link to our git repository if you wish to see it: https://github.com/danieloakman/CSIT214-814-Group-Program/tree/master

