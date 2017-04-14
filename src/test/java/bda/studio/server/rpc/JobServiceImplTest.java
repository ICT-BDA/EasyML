package bda.studio.server.rpc;

import java.util.List;

import bda.studio.server.util.TimeUtils;
import bda.studio.shared.model.BdaJob;
import bda.studio.shared.oozie.OozieAction;

public class JobServiceImplTest {
  static JobServiceImpl impl = new JobServiceImpl();
  public static void testGetJobs(){
    
    List<BdaJob> jobs = impl.listRecentExampleJobs();
//    for( BdaJob job :jobs ){
//      System.out.println( job.getCurOozJob().getAppName() );
//    }
//    
//    jobs = impl.getRecentJobs();
//    for( BdaJob job :jobs ){
//      System.out.println( job.getCurOozJob().getAppName() );
//    }
//    
    jobs = impl.listRecentUserJobs("fortianyou@qq.com");
    for( BdaJob job :jobs ){
      System.out.println( job.getCurOozJobId() + " " + TimeUtils.format( job.getCurOozJob().getCreatedTime()) );
    }
    
  }
  
  public static void testGetWorkflowJob(){
    BdaJob job = impl.getBdaJob("0000068-160307093203096-oozie-oozi-W");
    if( job != null && job.getCurOozJob() != null ){
      System.out.println( job.getCurOozJob().getAppName() );
      if( job.getCurOozJob().getActions() != null ){
        System.out.println( "action size: " + job.getCurOozJob().getActions().size() );
        for( OozieAction action: job.getCurOozJob().getActions() ){
          System.out.println( action.getName() +" " + action.getStatus());  
        }
        
      }else{
        System.out.println("action is null");
      }
    }
  }
  
  public static void main(String []args){
//   testGetWorkflowJob(); 
    testGetJobs();
  }

}
