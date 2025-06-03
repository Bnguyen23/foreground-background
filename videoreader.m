vidObj1 = VideoReader('AN2957 (+ 30 min).MOV');
vidObj2 = VideoReader('D-PZQ(test1) 10a_t001.MOV');

n=1;
while(hasFrame(vidObj1))
%while(true)
    %frame = readFrame(vidObj1);
    frame = read(vidObj1,n);
    imwrite(frame,sprintf('aldult\\test%d.bmp',n));
    %imshow(sprintf('adult\\test%d.bmp',n));
    n = n +1;
        %info=imfinfo(sprintf('adult\test%d.bmp',n));
        %info.ColorType;
        %[info.Height,info.Width,info.BitDepth]
        
        %imshow(frame);
        %title(sprintf('Current Time = %.3fsec', vidObj1.CurrentTime));
        %pause(2/vidObj1.FrameRate);
end

%while(hasFrame(vidObj2))
%    frame = readFrame(vidObj2);
%    imshow(frame);
%    title(sprintf('Current Time = %.3fsec', vidObj2.CurrentTime));
%    pause(2/vidObj2.FrameRate);
%end
