# QuickStart

Before you can use *EasyML Studio*, you must configure the environment which include runtime servers environment and your development environment.

## Development Environment
### Import project to IDEA or Eclipse 

- Get code from Git repository (*https://github.com/ICT-BDA/EasyML*)
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
<img src="./img/import_rundetail.png" alt="Edit configuration"/>
</div>
* `Use Super Dev Mode` can allow you debugging your web app at the browser side. It is make debugging more effient, for which remember choosing it. 

## Properation for virtual servers cluster

### Install Docker 
### Down load install dependent package

### Build Eml server images 
 use build.sh 

### Pull mysql serever images from docker hub


## Run docker virtual server cluster

## Configure local hosts 

### Visit three websites to confirm correctness of whole process



