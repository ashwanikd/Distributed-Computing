public class Process implements Runnable{
  Thread t;
  Message m;
  Process(){
    t = new Thread(this,"Process Thread");
  }
  @Override
  public void run(){

  }
}
