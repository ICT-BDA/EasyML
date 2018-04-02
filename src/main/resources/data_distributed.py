# example python gen_run.py "10.60.0.54,10.60.0.53,10.60.0.55" "tf_start.sh" "/home/hyx/DL" "hyx" "run.sh" "python dist.py" "10.60.0.53"

import sys
import os
import subprocess

p = subprocess.Popen('hostname', shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
for line in p.stdout.readlines():
    host_ip = line.strip()

ip_list = sys.argv[1].split(',')
ip_list.remove(host_ip)
ip_list.insert(0, host_ip)

worker_ip = [i for i in ip_list if i != host_ip]

start_sh = sys.argv[2]

comms = sys.argv[6].split(" ")
print(comms)

python_path = sys.argv[3] + "/" + comms[1]
user_name = sys.argv[4]
python_dir = sys.argv[3] 

run_sh = sys.argv[5]
command = " ".join(comms[2:])

master_ip = sys.argv[7]

worker_python_dir = "/var/bda_tensorflow/" + python_dir.split('/')[-2]

with open(run_sh, "w") as f:
    f.write("cd " + worker_python_dir + "\nnohup python " + comms[1] + " " + command + " $1 $2 > log 2>&1 & echo $!")

with open(start_sh, "w") as f:
    f.write("exit_code=0\n")
    for ip in worker_ip:
        f.write("scp -r " + python_dir + " " + user_name + "@" + ip + ":" + worker_python_dir + "\n")

    f.write("\n# scp over\n\n")

    f.write("port=`ssh " + user_name + "@" + master_ip + ' "flock /tmp/bda_port.lock -c \\"sh check.sh ' + sys.argv[1] +'\\""`\n')
    f.write("ADDR=($port)\n")
    for i in range(len(ip_list)):
        f.write("ip" + str(i) + "=${ADDR[" + str(i) + "]}\n")
        f.write('if [ "$' + "ip" + str(i) + '" = \'\' ];then\n')
        f.write("exit 1\nfi\n") 

    f.write("\n# check port over\n\n")

    cluster_info = ",".join([ip_list[i] + ":$ip" + str(i) for i in range(len(ip_list))])

    for i in range(len(worker_ip)):
        f.write("pid" + str(i) + "=`ssh " + user_name + "@" + worker_ip[i] + ' "sh ' + worker_python_dir + "/" + run_sh + " " + cluster_info + " " + str(i+1) + '"`\n')
        f.write("echo $pid" + str(i) + "\n")

    f.write("ssh " + user_name + "@" + host_ip + ' "cd ' + sys.argv[3] + '; python ' + comms[1] + " " + command + " " + cluster_info + ' 0"\n')
    f.write("((exit_code=exit_code|$?))\n")

    f.write("\n# run over\n\n")

    for i in range(len(worker_ip)):
        f.write("ssh " + user_name + "@" + worker_ip[i] + " 'rm -rf " + worker_python_dir + "'\n")
        f.write("ssh " + user_name + "@" + worker_ip[i] + ' "kill $pid' + str(i) + ';ls" 2> /dev/null 1> /dev/null\n')
        f.write("((exit_code=exit_code|$?))\n")

    for i in range(len(ip_list)):
        f.write("ssh " + user_name + "@" + ip_list[i] + ' "sudo rm /usr/bda/port/$ip' + str(i) + '"\n')
        f.write("((exit_code=exit_code|$?))\n")

    f.write("exit $exit_code\n")
