frontground:
	echo "This is a foreground script"
	echo "run MATHLAB to extract raw data in BMP format for all the frame in the MOV video"
	cd C:\Users\brian\Documents\MATLAB
	C:\Program Files\MATLAB\R2022b\bin\matlab.exe -r C:\Users\brian\Documents\MATLAB\videoreader.m
	echo "transfer BMP frames to Visual studio code directory"	
	move C:\Users\brian\Desktop\adult\* C:\Users\brian\Desktop\664-foreground\test_data\.
	move C:\Users\brian\Desktop\adult\* C:\Users\brian\Desktop\664-foreground\test_data2\.
	echo "run visual studio code to generate the frontground frames with the foreground.java";
	C:\Users\brian\AppData\Local\Programs\Microsoft VS Code\code.exe C:\Users\brian\foreground
	echo "the frontground output frames are in the test_output directory"
	echo "run MATHLAB to convert 307 BMP output frames to compressed MOV format"
	C:\Program Files\MATLAB\R2022b\bin\matlab.exe -r C:\Users\brian\Documents\MATLAB\videowriter.m