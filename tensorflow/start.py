# -*- coding: utf-8 -*-
import tensorflow as tf
import sys
import numpy as np

ps = [i.strip() for i in sys.argv[1].split(',')]
worker = [i.strip() for i in sys.argv[2].split(',')]
name = sys.argv[3]
index = int(sys.argv[4])

cluster = tf.train.ClusterSpec({"ps": ps, "worker": worker})
server = tf.train.Server(cluster, job_name=name, task_index=index)

server.join()
