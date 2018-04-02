import sys

start = "tf_start.sh"
end = "tf_end.sh"

ip_list = sys.argv[1].split(',')
user_name = sys.argv[2]
master_ip = sys.argv[3]

def last(s):
    return s[s.rfind(".")+1:].replace("-","_")

with open(start, "w") as f:
    # init port
    f.write("port=`ssh " + user_name + "@" + master_ip + ' "flock /tmp/bda_port.lock -c \\"sh check.sh ' + sys.argv[1] +'\\""`\n')
    f.write("ADDR=($port)\n")
    for i in range(len(ip_list)):
        f.write("p" + last(ip_list[i]) + "=${ADDR[" + str(i) + "]}\n")
        f.write("echo $p" + last(ip_list[i]) + " >> tf_port\n")
    
    f.write("echo $p" + last(ip_list[0]) + " > tf_commit\n")
    command = "sh start.sh " + ip_list[0] + ":$p" + last(ip_list[0]) + " "
    for ip in ip_list[1:]:
        command += (ip + ":$p" + last(ip) + ",")
    command = command[:-1] + " "

    # init pid
    i = 0
    f.write("ssh  " + user_name + "@" + ip_list[0] + ' "' + command + "ps 0" + '"\n')
    for ip in ip_list[1:]:
        f.write("ssh  " + user_name + "@" + ip + ' "' + command + "worker " + str(i) + '"\n')
        i+=1

with open(end, 'w') as f:
    i = 0
    for ip in ip_list:
        f.write("pid" + last(ip) + "=`head -n " + str(i+1) + " tf_pid | tail -n 1`\n")
        f.write("port" + last(ip) + "=`head -n " + str(i+1) + " tf_port | tail -n 1`\n")
        f.write('if [ "$' + "port" + last(ip) + '" = \'\' ];then\n')
        f.write("exit 1\nfi\n")
        i += 1

    for ip in ip_list:
        f.write("ssh  "+ user_name +"@"+ ip + ' "kill $pid' + last(ip) + '"\n')
        f.write("ssh  "+ user_name +"@"+ ip + ' "sudo rm -rf /usr/bda/port/$port' + last(ip) + '"\n')

    f.write("echo 'kill finish'\n")