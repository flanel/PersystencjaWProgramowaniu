package pl.edu.agh.ki.mwo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

import javax.servlet.http.HttpSession;

@Controller
public class StudentsControler {

    @RequestMapping(value="/Students")
    public String listStudents(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());

        return "studentsList";
    }

	@RequestMapping(value="/AddStudent")
	public String displayAddStudentForm(Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());

		return "studentForm";
	}

    @RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=false) String name,
								@RequestParam(value="studentSurname", required=false) String surname,
								@RequestParam(value="studentPesel", required=false) String pesel,
								@RequestParam(value="schoolClassId", required=false) String classId,
    		Model model, HttpSession session) {
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	Student student = new Student();
    	student.setName(name);
		student.setSurname(surname);
		student.setPesel(pesel);

    	DatabaseConnector.getInstance().addStudent(student, classId);
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Nowy student został dodany");

    	return "studentsList";
    }

	@RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
	public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
									Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		DatabaseConnector.getInstance().deleteStudent(studentId);
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("message", "Student został usunięty");

		return "studentsList";
	}

	@RequestMapping(value="/EditStudent", method=RequestMethod.POST)
	public String displayStudentForm(@RequestParam(value="studentId", required=false) String studentId,
										Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		model.addAttribute("students", DatabaseConnector.getInstance().getStudent(studentId));
		return "studentEditForm";
	}

	@RequestMapping(value="/ApplyStudentEdit", method=RequestMethod.POST)
	public String editStudent(@RequestParam(value="name", required=false) String name,
							  @RequestParam(value="surname", required=false) String surname,
							  @RequestParam(value="studentId", required=false) String studentId,
							  @RequestParam(value="pesel", required=false) String pesel,
							  Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";

		Student student = new Student();
		student.setName(name);
		student.setSurname(surname);
		student.setId(Integer.valueOf(studentId));
		student.setPesel(pesel);


		DatabaseConnector.getInstance().updateStudent(student);

		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("message", "Student został edytowany");

		return "studentsList";
	}

}