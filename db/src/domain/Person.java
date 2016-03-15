package domain;

public class Person {
	private Integer personid;
	private String name;
	private String phone;
	
	public Person(Integer personid, String name, String phone) {
		this.personid = personid;
		this.name = name;
		this.phone = phone;
	}
	public Integer getPersonid() {
		return personid;
	}
	public void setPersonid(Integer personid) {
		this.personid = personid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
