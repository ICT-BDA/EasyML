# Easy Machine Learning

## What is Easy Machine Learning
Scalable machine learning algorithms have become the key components in many big data applications. However, the full potential of machine learning is still far from been realized because using machine learning algorithms is hard,
especially on distributed platforms such as Hadoop and Spark. The key barriers come from not only the implementation of the algorithms themselves, but also the processing for applying them to real applications which often involve multiple steps and different algorithms. 

The easy machine learning (EasyML) project presents a general-purpose dataflow-based system for easing the process of applying machine learning algorithms to real world tasks. In EasyML, a machine learning task is formulated as a directed acyclic graph (DAG) in which each node represents an operation (e.g., a machine learning algorithm or a data processing module), and each edge represents the flow of the data from one node to its descendants. The task can be defined manually or be cloned from existing tasks/templates. After submitting a task to the cloud, each node will be automatically scheduled to execute according to the DAG. Graphical user interface is implemented for making users to create, configure, submit, and monitor a task in a drag-and-drop manner. EasyML provides the following advantages: 
* Lowing the barriers of constructing and executing machine learning tasks;
* Sharing and re-using the implementations of the algorithms, the job DAGs, and the results;
* Seamlessly integrating the stand-alone algorithms as well as the distributed algorithms in one task.

The EasyML consists of two major components: 
* A GUI-based machine learning studio system which enable users to create, configure, submit, monitor, and sharing their machine learning process in a drag-and-drop manner. 
 
* A cloud service for executing the tasks. We build the service based on the open source big data platform of Hadoop and Spark. After receiving a task DAG from the GUI, each node will be automatically scheduled to run when all of its dependent data sources are ready. The algorithms correspond to the nodes will automatically scheduled to run on Linux, Spark, or Map-Reduce, according to their implementations and configurations.

<div align=center>
<img src="./img/LR_DAG.png" width="400" height="300" alt="An example dataflow DAG"/>
</div>

The EasyML project is originated from the Big Data Analysis (BDA) system developed at Institute of Computing Technolgy, Chinese Academy of Sciences. Detailed introduction can be found in [Guo et al., CIKM 2016]. The online service is available at http://159.226.40.104:18080/. Details of the system and the guest account can be found in the following paper. (PDF: http://www.bigdatalab.ac.cn/~junxu/publications/CIKM2016_BDADemo.pdf) 

[Guo et al., CIKM 2016] Tianyou Guo, Jun Xu, Xiaohui Yan, Jianpeng Hou, Ping Li, Zhaohui Li, Jiafeng Guo, and Xueqi Cheng. Ease the Process of Machine Learning with Dataflow. Proceedings of the 25th ACM International Conference on Information and Knowledge Management (CIKM '16), Indianapolis, USA, pp. 2437-2440, 2016. 



## How to involve in our project

Pull all project and prepare some necessary environments and a kind of development utilities. Follows the step in Quick-start, and you can create our webapp.


## How to use Easy Machine Learning Studio 
After you have ran Easy ML，You can login via `http://localhost:8888/EMLStudio,html`with our official account `bdaict@hotmail.com` and password `bdaict`. For the best user experience, it is recommended to use Chrome.
[登录图]

## Acknowledgement


