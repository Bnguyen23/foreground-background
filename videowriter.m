vidObj1 = VideoWriter("test_output\video_out","MPEG-4");
vidObj1.FrameRate=6;
%vidObj1.Height=1036;
%vidObj1.Width=1384;

open(vidObj1);

for ii=1:307
    %filename=sprintf('test_output\\test%d_out.bmp',ii);
    %image = open(filename);
    image = imread(sprintf('test_output\\test%d_out.bmp',ii));
    writeVideo(vidObj1,image);
end
close(vidObj1);
%open("test_output\test1_out.bmp");
