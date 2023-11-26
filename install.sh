NPDIR=/usr/share/netpaint
SERVER_URL="24.210.155.111"

mkdir -v $NPDIR
cp -v dist/NetPaint.jar $NPDIR
chmod +rx $NPDIR/NetPaint.jar
echo $SERVER_URL > $NPDIR/serverURL.txt
#cp -v runClient.sh runServer.sh $NPDIR

#ln -s $NPDIR/runClient.sh /usr/bin/netpaint
#ln -s $NPDIR/runServer.sh /usr/bin/netpaintserver

echo "java -cp $NPDIR/NetPaint.jar client.PaintClient 1234 \`cat $NPDIR/serverURL.txt\`" >  /usr/bin/netpaint
chmod +x /usr/bin/netpaint

echo "java -cp $NPDIR/NetPaint.jar server.PaintServer 1234" >  /usr/bin/netpaintserver
chmod +x /usr/bin/netpaintserver

ln -s /usr/bin/netpaint $HOME/Desktop/NetPaint

