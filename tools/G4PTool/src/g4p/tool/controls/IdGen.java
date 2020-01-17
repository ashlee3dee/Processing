package g4p.tool.controls;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Singleton class to provide unique identity numbers.
 * 
 * @author Peter Lager
 *
 */
public final class IdGen {

	private static IdGen instance;
	private static int low  = 200000;
	private static int high = 999999;
	private static Random rnd;
	
	public static IdGen instance(){
		if(instance == null){
			instance = new IdGen();
		}
		return instance;
	}
	
	private Set<Integer> ids;

	private IdGen(){
		ids = new HashSet<Integer>();
		rnd = new Random();
	}
	
	public boolean used(Integer id){
		return ids.contains(id);
	}

	public void add(Integer id){
		ids.add(id);
	}
	
	public void remove(Integer id){
		ids.remove(id);
	}

	public void reset(){
		ids.clear();
	}
	
	public Integer getNext(){
		Integer id;
		do{
			id = new Integer((int) (rnd.nextFloat() * (high - low) + low));
		} while(ids.contains(id));
		ids.add(id);
		return id;
	}
	
	public String get_size(){
		return "" + ids.size();
	}
}
