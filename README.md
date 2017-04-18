# Easy Machine Learning

## What is Easy Machine Learning
Machine learning algorithms have become the key components in many big data applications. However, the full potential of machine learning is still far from been realized because using machine learning algorithms is hard,
especially on distributed platforms such as Hadoop and Spark. The key barriers come from not only the implementation of the algorithms themselves, but also the processing for applying them to real applications which often involve multiple steps 
and different algorithms. In this demo we present a general-purpose dataflow-based system for easing the process of applying machine learning algorithms to real world tasks. In the system a learning task is formulated as a directed acyclic graph (DAG) in which each node represents an operation 
(e.g., a machine learning algorithm), and each edge represents the flow of the data from one node to its descendants. The task can be defined manually or be cloned from existing tasks/templates. After submitting a task to the cloud, each node will be automatically scheduled to execute according to the DAG. 
Graphical user interface is implemented for making users to create, configure, submit, and monitor a task in a drag-and-drop manner. Advantages of the system include 
* 1) Lowing the barriers of defining and executing machine learning tasks;
* 2) Sharing and re-using the implementations of the algorithms, the job DAGs, and the experimental results;
* 3) Seamlessly integrating the stand-alone algorithms as well as the distributed algorithms in one task.




The system consists of three major components: 1) A distributed machine learning library which implements not only popular used machine learning algorithms, but also the algorithms for data pre/post-processing, data format transformation, feature generation, performance evaluation etc. These algorithms are mainly implemented based on Spark.  2) A GUI-based machine learning studio system which enable users to create, configure, submit, monitor, and sharing their machine learning process in a drag-and-drop manner. All of the algorithms in the machine learning library can be accessed and configured in the studio system. They are the key building blocks for constructing machine learning tasks. 3) A cloud service for executing the tasks. We build the service based on the open source big data platform of Hadoop and Spark. After receiving a task DAG from the GUI, each node will be automatically scheduled to run when all of its dependent data sources are ready. The algorithm corresponds to the node will scheduled to run on Linux, Spark, or Map-Reduce\cite, according to their implementation.

<div align=center>
<img src="./img/LR_DAG.png" width="400" height="300" alt="An example dataflow DAG"/>
</div>

## How to involve in our project

Pull all project and prepare some necessary environments and a kind of development utilities. Follows the step in Quick-start, and you can create our webapp. You can use our offical account to login.


## How to use Easy Machine Learning Studio 

