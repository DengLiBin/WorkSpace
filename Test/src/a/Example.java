package a;

public class Example{
	/*
	static String str=new String("good");
	static char[] ch={'a','b','c'};
	*/
	public static void main(String[] args){
		String str=new String("good");
		char[] ch={'a','b','c'};
		Example ex=new Example();
		ex.change(str, ch);
		System.out.println(str+"and"+ch[0]+ch[1]+ch[2]);
	}
	public void change(String str1,char[] ch){
		str1="test ok";
		System.out.println(str1);
		ch[0]='g';
	}
}
	
