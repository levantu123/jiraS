package training.mini.maxji.services

 

import javax.annotation.PostConstruct

 

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

 

import training.mini.maxji.model.Employee
import training.mini.maxji.model.EmployeeRepository
import training.mini.maxji.model.Issue
import training.mini.maxji.model.IssueRepository
import training.mini.maxji.model.Project
import training.mini.maxji.model.ProjectRepository

 

@Service
public class EmployeeGraphQLService {

 

    @Autowired
    EmployeeRepository employeeRepository

 


    @PostConstruct
    void pullDataEmployee(){

 

        employeeRepository.deleteAll()

 

        URL url = new URL("http://localhost:8080/rest/api/latest/group/member?groupname=jira-core-users&startAt=0")
        String encoding = Base64.getEncoder().encodeToString("levantu.13139:levantu13139".getBytes("utf-8"))
        HttpURLConnection conn = (HttpURLConnection) url.openConnection()
        conn.setRequestMethod("GET")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty  ("Authorization", "Basic " + encoding)
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
            + conn.getResponseCode())
        }

 

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))
        StringBuilder sb = new StringBuilder()
        String line
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n")
        }

 

        JSONObject obj = new JSONObject(sb.toString())
        JSONArray issues = obj.getJSONArray("values")
        for(int i = 0; i<issues.length(); i++) {
            Employee e =new Employee()
            e.setId(issues.getJSONObject(i).getString("key"))
            e.setName(issues.getJSONObject(i).getString("name"))
            e.setEmailAddress(issues.getJSONObject(i).getString("emailAddress"))
            e.setProjectKeys([])
            employeeRepository.save(e)
        }
    }
}

 


@Service
class IssueGraphQLService {

 

    @Autowired
    IssueRepository issueRepository
    
    @Autowired
    EmployeeRepository employeeRepository

 

    @PostConstruct
    void pullDataIssue(){
        issueRepository.deleteAll()
        URL url = new URL("http://localhost:8080/rest/api/latest/search?")
        String encoding = Base64.getEncoder().encodeToString("levantu.13139:levantu13139".getBytes("utf-8"))
        HttpURLConnection conn = (HttpURLConnection) url.openConnection()
        conn.setRequestMethod("GET")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty  ("Authorization", "Basic " + encoding)
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
            + conn.getResponseCode())
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))
        StringBuilder sb = new StringBuilder()
        String line
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n")
        }
        JSONObject obj = new JSONObject(sb.toString())
        JSONArray issues = obj.getJSONArray("issues")
        for(int i = 0; i < issues.length(); i++) {
			
            Issue issue = new Issue()
			
			
			
            issue.setId(issues.getJSONObject(i).getString("id"))
            if(!issues.getJSONObject(i).getJSONObject("fields").isNull("description")) {
                issue.setDescription(issues.getJSONObject(i).getJSONObject("fields").getString("description"))
            }
			
			issue.setIssueKey(issues.getJSONObject(i).getString("key"))
			
            issue.setCreatorName(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("creator").getString("name"))
            issue.setPriority(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("priority").getString("name"))
            issue.setSummary(issues.getJSONObject(i).getJSONObject("fields").getString("summary"))
            issue.setType(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("issuetype").getString("name"))
            issue.setStatus(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("status").getString("name"))
            issue.setProjectKey(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("project").getString("key"))
            
            if(!issues.getJSONObject(i).getJSONObject("fields").isNull("assignee")) {
                issue.setAssigneeKey(issues.getJSONObject(i).getJSONObject("fields").getJSONObject("assignee").getString("key"))
                updateUserByProjectKey(issue.getAssigneeKey(), issue.getProjectKey())    
            }
            issueRepository.save(issue)
        }
    }
	
    def updateUserByProjectKey(id, key) {
        
        Employee employee = employeeRepository.findById(id).get()

 

        if(!employee.projectKeys.contains(key)) {
            employee.projectKeys.add(key)
            employeeRepository.save(employee)
        }
    }
}

 

@Service
class ProjectGraphQLService{

 

    @Autowired
    ProjectRepository projectRepository

 

    @PostConstruct
    void pullDataProject(){

 

        projectRepository.deleteAll()

 

        URL url = new URL("http://localhost:8080/rest/api/latest/project")
        String encoding = Base64.getEncoder().encodeToString("levantu.13139:levantu13139".getBytes("utf-8"))
        HttpURLConnection conn = (HttpURLConnection) url.openConnection()
        conn.setRequestMethod("GET")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty  ("Authorization", "Basic " + encoding)
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
            + conn.getResponseCode())
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))
        StringBuilder sb = new StringBuilder()
        String line
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n")
        }
        JSONArray projects = new JSONArray(sb.toString())
        for(int i = 0; i < projects.length(); i++) {
            def project = new Project();
            project.setName(projects.getJSONObject(i).getString('name'))
            project.setId(projects.getJSONObject(i).getString('key'))
            project.setProjectTypeKey(projects.getJSONObject(i).getString('projectTypeKey'))
            projectRepository.save(project)
        }
    }

 

}

 