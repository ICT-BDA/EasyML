package bda.studio.server.rpc;

import java.util.List;

import bda.studio.shared.model.Program;

public class ProgramServiceImplTest {

  public static void main(String args[]) throws Exception{
    ProgramServiceImpl impl = new ProgramServiceImpl();
    List<Program> progList = impl.load();
    for( Program prog : progList){
      System.out.println( prog.getName() );
    }
  }
}
