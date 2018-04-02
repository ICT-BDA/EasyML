IFS=',' read -ra ADDR <<< "$1"
for ip in "${ADDR[@]}"; do
        port=`ssh root@$ip 'sudo sh check_port.sh'`
        echo $port
done
