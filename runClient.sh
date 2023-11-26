ERVER_URL=`cat serverURL.txt `

java -classpath "./build/classes" -Dsun.java2d.pmoffscreen=false client.PaintClient 1234 $SERVER_URL
