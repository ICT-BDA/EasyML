cd /usr/bda/port
for i in `seq 65535 -1 10000`
do
        num=`netstat -anp | awk -F' ' '{print $4}' | grep $i | wc -l`
        if [ "$num" -eq "0" ] && [ ! -f "$i" ]
        then
                touch "$i"
                break
        fi
done
echo $i
