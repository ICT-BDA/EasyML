# QuickStart

Before you can use *EasyML Studio*, you must configure the environment which include runtime servers environment and your development environment.

## Development Environment
### Import project to IDEA or Eclipse 

- Get code from Git repository <https://github.com/ICT-BDA/EasyML>
- Import the code to your IDE via maven project 

<div align=center>
<img src="./img/import_eml.png" height ="500px" margin-top = "10px" alt="import project to IDEA"/>
</div>
- Only support **java version 1.7**

<div align=center>
<img src="./img/import_jdk.png" height ="400px"  alt="configure JDK path"/>
</div>

  
### Configure GWT Lib Path
* Make sure your IDE have install ***Maven***
* Use Maven to down load all ralated dependency package
<div align=center>
<img src="./img/import_maven.png" height ="400px" width = "550" alt="configure maven"/>
</div>
* Make sure you can see the whole **External Libraries** on the right
<div align=center>
<img src="./img/import_maven_suc.png"  alt="External libraries"/>
</div>
* Install GWT plugin in your IDE (IDEA user can skip this step)
### Create and run our EML web application
  After you have get all dependencies, you can start building your *EasyML Studio* web app for the following steps:

* Edit run configurations

<div align=center>
<img src="./img/import_config.png" alt="Edit configuration"/>
</div>
* Add GWT web app via `+` at upper left corner, if you do not see `GWT Configuration` in the list, you might have something wrong in the step of *Configure GWT Lib Path*. Go back to the last step, resolve the problem.

<div align=center>
<img src="./img/import_rundetail.png" width = "90%" alt="Edit Easy ML"/>
</div>
* `Use Super Dev Mode` can allow you debugging your web app at the browser side. It is make debugging more effient, for which remember choosing it. 
* When you have finished all the steps above, you can click the green run button to make and debug the **EasyML**. After a while, you can blowse EasyML in your *Chrome* to accesss it.
<div align=center>
<img src="./img/home_page.png" width = "90%" alt="Easy ML home page"/>
</div>

## Properation for virtual server cluster
Our server cluster is based on docker, thus you can build it on your own computer. It is convenient for you to develop project without any remote connections. Further more, you can also contribute to the server envrionments. The docker version server cluster is not stable and effient, for which we can do a mount of things on it. However, you first step to access it is installing Docker.
### Install Docker 
* Just follow the [official guide](https://www.docker.com/) to intall Docker.
* Make sure your docker service runs correctly via `Docker info` and `Docker version`
* No mater which system your computer is, stop the **Firewall** of your system
* If you are using *centos 7*, you also should stop the **selinux**, in order to avoiding run Docker container error. 
* Run ```sudo docker run helloworld``` to see if we have install docker successfully.
### Down load install dependent package
Every single server in our cluster is created by one *docker image*, and this *image* can be built via a **Dockerfile** which has defined by us and includes all utilities we need such as hadoop. Thus we need to download the **Dockerfile** and all dependent files and configration files from our [google drive disk](https://drive.google.com/open?id=0B5Lj6qkCMBbFWW5uYlJwb2drb1k).


### Pull mysql server images from docker hub
* Pull our mysql server images from our [docker hub](https://hub.docker.com/u/nkxujun/):

    `docker pull nkxujun/emlsql`
### Pull ubuntu_eml images from docker hub
* Our Eml server images is based on ubuntu, so pull it first:   
    `docker pull nkxujun/ubuntu_eml`
### Build Eml server images  

* Use build.sh to build our image, this process will last for a few minute  

     ```sh build.sh``` 
* You can use `docker images` to see if you have built successed:

## Run docker virtual server cluster

* You can use `sh run_containers` to run all servers
* If your have run all these four server successed: **mysql, hadoop-master, hadoop-slave1,hadoop-slave2**, you can use `docker ps` to check:

### Visit three websites to confirm correctness of whole process


### Check the hadoop namenode 

### Configure local hosts 
  * Add your Localhost(Linux) or Docker IP(Windows) as `hadoop-master` and `mysql` to your hosts file, for example:
 
    <img src="./img/hosts.png" alt="Hosts"/>




