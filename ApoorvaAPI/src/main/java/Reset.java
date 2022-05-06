

import java.util.ArrayList;
import java.util.List;


public class Reset implements Runnable {
	
  private int count = 0;
  private List<Integer> Ids = new ArrayList<Integer>();

 
  public void addId(int id) 
  {
	  if(!this.Ids.contains(id)) {
			this.Ids.add(id);
			this.count++;
		}
  }
  
  public int countValue() {
	  return count;
  }

public void run() {
	// TODO Auto-generated method stub
	try {
		Log.log(count);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	count = 0;
	Ids.clear();
	
}
    
}
