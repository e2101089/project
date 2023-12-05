public class Student {
	private int student_id;
	private String Name;
	private String email;

	public Student() {
	}

	public Student(int id, String Name, String email) {
		super();
		this.student_id = id;
		this.Name = Name;
		this.email = email;
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}