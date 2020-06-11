package training.mini.maxji.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
class Project {
	@Id
	String id
	String name
	String projectTypeKey
}

interface ProjectRepository extends MongoRepository<Project, String> {
}

@Document
class Issue {
	@Id
	String id
	String issueKey
	String description
	String summary
	String creatorName
	String type
	String priority
	String status
	String projectKey
	String assigneeKey
}
interface IssueRepository extends MongoRepository<Issue, String> {
	List<Issue> findByAssigneeKey(String assigneeKey) 
}

@Document
class Employee {
	@Id
	String id
	String name
	String emailAddress
	List<String> projectKeys
}
interface EmployeeRepository extends MongoRepository<Employee, String> {
	List<Employee>findByName(String name)
	List<Employee>findByNameIn(String names)
	List<Employee>findByProjectKeysIn(List<String> projectKeys)
	List<Employee>findByNameInAndProjectKeysIn(List<String> names, List<String> projectKeys )

}

@Document
class EmployeeLogwork{
	@Id
	String id
	String idEmployee
	int logworkHours
	Date dateOfTask
}

interface EmployeeLogworkRepository extends MongoRepository<EmployeeLogwork, String> {
}




