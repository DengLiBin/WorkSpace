package my.service;

public class PersonService {
	String sub;
	public void save(String username){
		sub=username.substring(6);
	}
	
	public int add(int a,int b){
		return a+b;
	}
}
