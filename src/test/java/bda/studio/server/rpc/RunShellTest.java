package bda.studio.server.rpc;

import bda.studio.client.ui.widget.command.CommandParseException;
import bda.studio.server.util.DistributedRunShellGenerator;
import bda.studio.server.util.RunShellGenerator;

public class RunShellTest {

  public static void main(String []args) throws CommandParseException{
    RunShellGenerator gen = new DistributedRunShellGenerator();
    String str = "";
    System.out.println(gen.generate("spark-submit --class bda.spark.runnable.logisticRegression.Predict spark.jar --model_pt {in:General:\"模型文件\"} --test_pt {in:General:\"测试集文件\"} --predict_pt {out:General,HFile:\"预测结果文件\"}"));
//    System.out.println(  gen.generate( "java -cp local.jar bda.local.runnable.tree.randomForest.Train --train_pt {in:LabeledPoint:训练集} --valid_pt {in:LabeledPoint:测试集} --model_pt {out:RandomForestModel:模型文件} --impurity [纯度计算方法:String:default,Variance] --loss [损失计算方法:String:default,SquaredError] --max_depth [最大树深:Int:default,10] --max_bins [最大分箱数:Int:default,32] --bin_samples [分箱采样数:Int:default,10000] --min_node_size [最小叶子尺寸:Int:default,15] --min_info_gain [最少信息增益:Double:default,1e-6] --row_rate [样本采样比例:Double:default,0.6] --col_rate [特征采样比例:Double:default,0.6] --num_trees [树的数目:Int:default,20]") );
  }
}
