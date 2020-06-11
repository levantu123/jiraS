package training.mini.maxji.resolver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver

import training.mini.maxji.model.Employee
import training.mini.maxji.model.EmployeeRepository
import training.mini.maxji.model.Issue
import training.mini.maxji.model.IssueRepository
import training.mini.maxji.model.Project
import training.mini.maxji.model.ProjectRepository

@Component
class Query implements GraphQLQueryResolver{
	
	@Autowired
	EmployeeRepository employeeRepository
	
	@Autowired
	IssueRepository issueRepository
	
	@Autowired
	ProjectRepository projectRepository
	
	public List<Employee> getAllEmployees() {
		employeeRepository.findAll()
	}
	public Optional<Employee> getEmployee(id) {
		employeeRepository.findById(id)
	}
	
	public List<Issue> getAllIssues() {
		issueRepository.findAll()
	}
	
	public Optional<Issue> getIssue(id) {
		issueRepository.findById(id)
	}
	
	public List<Project> getAllProjects() {
		projectRepository.findAll()
	}
	
	def Optional<Project>  getProject(id){
		projectRepository.findById(id)
	}
	
	def List<Employee> getEmployeesWithFilter(List<String> names, List<String> projects){
		if(names.isEmpty() && projects.isEmpty()) {
			return employeeRepository.findAll()
		}
		if(names.isEmpty()){
			return employeeRepository.findByProjectKeysIn(projects)
		}
		if(projects.isEmpty()){
			return employeeRepository.findByNameIn(names)
		}
		employeeRepository.findByNameInAndProjectKeysIn(names, projects)
	}
}
@Component
class ProjectResolver implements GraphQLResolver<Project>{
	
}
@Component
class IssueResolver implements GraphQLResolver<Issue>{
	@Autowired
	ProjectRepository projectRepository
	
	@Autowired
	EmployeeRepository employeeRepository
	
	def Project getProject(Issue issue) {
		projectRepository.findById(issue.getProjectKey()).get()
	}
	
	def Employee getAssignee(Issue issue) {
		employeeRepository.findById(issue.assigneeKey).get()
	}
}
@Component
class EmployeetResolver implements GraphQLResolver<Employee>{
	
	@Autowired
	ProjectRepository projectRepository
	@Autowired
	IssueRepository issueRepository
		
	def List<Project> getProjects(Employee employee) {
		List<Project> list = []
		
		for(int i = 0; i< employee.getProjectKeys().size(); i++) {
			list.add(projectRepository.findById(employee.getProjectKeys().get(i)))
		}
		list
	}
	
	def List<Issue> getTasks(Employee employee){
		issueRepository.findByAssigneeKey(employee.getId())
	}

}



