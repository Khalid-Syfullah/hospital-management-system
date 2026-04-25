package com.hospital;

import com.hospital.auth.LoginRequest;
import com.hospital.auth.RegisterRequest;
import com.hospital.department.DepartmentCreateRequest;
import com.hospital.doctor.DoctorCreateRequest;
import com.hospital.patient.PatientCreateRequest;
import com.hospital.user.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HospitalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void auth_Register() throws Exception {
        long unique = System.currentTimeMillis();
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test" + unique + "@hospital.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPhone(String.valueOf(unique));
        request.setRole(Role.ADMIN.name());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    void auth_Login() throws Exception {
        long unique = System.currentTimeMillis();
        String email = "user" + unique + "@hospital.com";
        
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setPhone(String.valueOf(unique + 1));
        registerRequest.setRole(Role.PATIENT.name());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    void department_Create() throws Exception {
        String token = getAdminToken();

        DepartmentCreateRequest request = new DepartmentCreateRequest();
        request.setName("Cardiology" + System.currentTimeMillis());
        request.setDescription("Heart care");
        request.setFloorNumber(2);

        mockMvc.perform(post("/api/v1/departments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void department_GetById() throws Exception {
        String token = getAdminToken();

        DepartmentCreateRequest createRequest = new DepartmentCreateRequest();
        createRequest.setName("Neurology" + System.currentTimeMillis());
        createRequest.setDescription("Brain care");

        String createResponse = mockMvc.perform(post("/api/v1/departments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getContentAsString();

        String deptId = objectMapper.readTree(createResponse).get("data").get("id").asText();

        mockMvc.perform(get("/api/v1/departments/" + deptId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").exists());
    }

    @Test
    void department_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/departments")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void ward_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/wards")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void doctor_Create() throws Exception {
        String token = getAdminToken();
        String deptId = createDepartment(token, "Surgery");

        DoctorCreateRequest request = new DoctorCreateRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setEmail("dr" + System.currentTimeMillis() + "@hospital.com");
        request.setPhone(String.valueOf(System.currentTimeMillis()));
        request.setSpecialization("Surgery");
        request.setLicenseNumber("DOC" + System.currentTimeMillis());
        request.setLicenseExpiryDate(LocalDate.now().plusYears(1));
        request.setDepartmentId(java.util.UUID.fromString(deptId));

        mockMvc.perform(post("/api/v1/doctors")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void doctor_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/doctors")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void patient_Create() throws Exception {
        String token = getAdminToken();

        PatientCreateRequest request = new PatientCreateRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("patient" + System.currentTimeMillis() + "@example.com");
        request.setPhone(String.valueOf(System.currentTimeMillis()));
        request.setDateOfBirth(LocalDate.of(1990, 1, 1));
        request.setGender("MALE");
        request.setBloodType("A_POSITIVE");
        request.setAddress("123 Main St");
        request.setEmergencyContactPhone(String.valueOf(System.currentTimeMillis() + 1));

        mockMvc.perform(post("/api/v1/patients")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mrn").exists());
    }

    @Test
    void patient_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/patients")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void prescription_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/prescriptions")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void labOrder_List() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/v1/lab-orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    private String createDepartment(String token, String name) throws Exception {
        DepartmentCreateRequest request = new DepartmentCreateRequest();
        request.setName(name + System.currentTimeMillis());
        request.setDescription("Test department");

        String response = mockMvc.perform(post("/api/v1/departments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("data").get("id").asText();
    }

    private String getAdminToken() throws Exception {
        long unique = System.currentTimeMillis();
        String email = "admin" + unique + "@hospital.com";
        
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword("password123");
        request.setFirstName("Admin");
        request.setLastName("User");
        request.setPhone(String.valueOf(unique));
        request.setRole(Role.ADMIN.name());

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password123");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("data").get("accessToken").asText();
    }
}