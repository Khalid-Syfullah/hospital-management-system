package com.hospital;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AllEndpointsIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void allEndpointsWorkThroughHttpAndSecurity() throws Exception {
        TokenPair admin = register("admin-all@example.com", "ADMIN");
        TokenPair pharmacist = register("pharmacist-all@example.com", "PHARMACIST");

        assertStatus(get("/api/v1/patients"), null, 403);
        TokenPair loggedInAdmin = login("admin-all@example.com");
        TokenPair refreshed = refresh(loggedInAdmin.refreshToken());
        ok(post("/api/v1/auth/logout"), null, Map.of("refreshToken", refreshed.refreshToken()), null);

        JsonNode users = ok(get("/api/v1/users"), admin.accessToken(), null, null);
        assertThat(users.get("success").asBoolean()).isTrue();
        UUID adminUserId = UUID.fromString(admin.userId());
        ok(get("/api/v1/users/{id}", adminUserId), admin.accessToken(), null, null);
        ok(put("/api/v1/users/{id}", adminUserId), admin.accessToken(), Map.of("fullName", "Admin Updated", "phone", "+1000"), null);
        ok(patch("/api/v1/users/{id}/enabled?enabled=true", adminUserId), admin.accessToken(), null, null);

        JsonNode department = ok(post("/api/v1/departments"), admin.accessToken(), Map.of("name", "Cardiology", "description", "Heart care"), null).get("data");
        UUID departmentId = id(department);
        ok(get("/api/v1/departments"), admin.accessToken(), null, null);
        ok(get("/api/v1/departments/{id}", departmentId), admin.accessToken(), null, null);
        ok(put("/api/v1/departments/{id}", departmentId), admin.accessToken(), Map.of("name", "Cardiology Updated", "description", "Heart care"), null);

        JsonNode doctor = ok(post("/api/v1/doctors"), admin.accessToken(), Map.of(
                "fullName", "Dr. Endpoint",
                "licenseNumber", "LIC-" + UUID.randomUUID(),
                "credentials", "MD",
                "specializations", List.of("Cardiology"),
                "availability", "Mon-Fri 09:00-17:00",
                "departmentId", departmentId.toString()), null).get("data");
        UUID doctorId = id(doctor);
        ok(get("/api/v1/doctors"), admin.accessToken(), null, null);
        ok(get("/api/v1/doctors/{id}", doctorId), admin.accessToken(), null, null);
        ok(get("/api/v1/doctors/{id}/availability", doctorId), admin.accessToken(), null, null);
        ok(put("/api/v1/doctors/{id}", doctorId), admin.accessToken(), Map.of(
                "fullName", "Dr. Endpoint Updated",
                "licenseNumber", doctor.get("licenseNumber").asText(),
                "credentials", "MD",
                "specializations", List.of("Cardiology", "Internal Medicine"),
                "availability", "Mon-Thu 09:00-15:00",
                "departmentId", departmentId.toString()), null);

        JsonNode patient = ok(post("/api/v1/patients"), admin.accessToken(), Map.of(
                "fullName", "Endpoint Patient",
                "gender", "FEMALE",
                "phone", "+155501",
                "email", "patient@example.com",
                "allergies", "None"), null).get("data");
        UUID patientId = id(patient);
        ok(get("/api/v1/patients"), admin.accessToken(), null, null);
        ok(get("/api/v1/patients/{id}", patientId), admin.accessToken(), null, null);
        ok(put("/api/v1/patients/{id}", patientId), admin.accessToken(), Map.of(
                "fullName", "Endpoint Patient Updated",
                "gender", "FEMALE",
                "phone", "+155502",
                "email", "patient2@example.com"), null);

        OffsetDateTime start = OffsetDateTime.now().plusDays(7).withNano(0);
        JsonNode appointment = ok(post("/api/v1/appointments"), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "startTime", start.toString(),
                "endTime", start.plusMinutes(30).toString(),
                "reason", "Endpoint checkup"), Map.of("Idempotency-Key", "booking-" + UUID.randomUUID())).get("data");
        UUID appointmentId = id(appointment);
        ok(get("/api/v1/appointments"), admin.accessToken(), null, null);
        ok(get("/api/v1/appointments/{id}", appointmentId), admin.accessToken(), null, null);
        ok(put("/api/v1/appointments/{id}", appointmentId), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "startTime", start.plusHours(1).toString(),
                "endTime", start.plusHours(1).plusMinutes(30).toString(),
                "reason", "Rescheduled"), null);
        ok(delete("/api/v1/appointments/{id}", appointmentId), admin.accessToken(), null, null);

        JsonNode record = ok(post("/api/v1/medical-records"), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "icd10Code", "A00",
                "diagnoses", "Diagnosis",
                "symptoms", "Symptoms",
                "visitNotes", "Notes"), null).get("data");
        UUID recordId = id(record);
        ok(get("/api/v1/medical-records"), admin.accessToken(), null, null);
        ok(get("/api/v1/medical-records/{id}", recordId), admin.accessToken(), null, null);
        ok(put("/api/v1/medical-records/{id}", recordId), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "icd10Code", "B00",
                "diagnoses", "Updated",
                "symptoms", "Updated",
                "visitNotes", "Updated"), null);
        ok(delete("/api/v1/medical-records/{id}", recordId), admin.accessToken(), null, null);

        JsonNode prescription = ok(post("/api/v1/prescriptions"), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "medicineName", "Amoxicillin",
                "dosage", "500mg",
                "frequency", "BID",
                "duration", "5 days",
                "instructions", "After food"), null).get("data");
        UUID prescriptionId = id(prescription);
        ok(get("/api/v1/prescriptions"), admin.accessToken(), null, null);
        ok(get("/api/v1/prescriptions/{id}", prescriptionId), admin.accessToken(), null, null);
        ok(put("/api/v1/prescriptions/{id}", prescriptionId), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "doctorId", doctorId.toString(),
                "medicineName", "Amoxicillin",
                "dosage", "250mg",
                "frequency", "TID",
                "duration", "7 days"), null);
        ok(delete("/api/v1/prescriptions/{id}", prescriptionId), admin.accessToken(), null, null);

        JsonNode lab = ok(post("/api/v1/lab-orders"), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "testName", "CBC",
                "status", "REQUESTED"), null).get("data");
        UUID labId = id(lab);
        ok(get("/api/v1/lab-orders"), admin.accessToken(), null, null);
        ok(get("/api/v1/lab-orders/{id}", labId), admin.accessToken(), null, null);
        ok(put("/api/v1/lab-orders/{id}", labId), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "testName", "CBC",
                "status", "COMPLETED",
                "result", "Normal"), null);
        ok(delete("/api/v1/lab-orders/{id}", labId), admin.accessToken(), null, null);

        JsonNode invoice = ok(post("/api/v1/billing/invoices"), admin.accessToken(), Map.of(
                "patientId", patientId.toString(),
                "insuranceClaimNumber", "CLAIM-1",
                "items", List.of(Map.of("description", "Consultation", "unitPrice", new BigDecimal("100.00"), "quantity", 1))), null).get("data");
        UUID invoiceId = id(invoice);
        ok(get("/api/v1/billing/invoices"), admin.accessToken(), null, null);
        ok(get("/api/v1/billing/invoices/{id}", invoiceId), admin.accessToken(), null, null);
        ok(post("/api/v1/billing/invoices/{id}/payments", invoiceId), admin.accessToken(), null, Map.of("Idempotency-Key", "pay-" + UUID.randomUUID()));
        ok(delete("/api/v1/billing/invoices/{id}", invoiceId), admin.accessToken(), null, null);

        JsonNode medication = ok(post("/api/v1/pharmacy/medications"), admin.accessToken(), Map.of(
                "name", "Endpoint Med",
                "strength", "10mg",
                "form", "Tablet",
                "stockQuantity", 25,
                "lowStockThreshold", 5), null).get("data");
        UUID medicationId = id(medication);
        ok(get("/api/v1/pharmacy/medications"), admin.accessToken(), null, null);
        ok(get("/api/v1/pharmacy/medications/{id}", medicationId), admin.accessToken(), null, null);
        ok(put("/api/v1/pharmacy/medications/{id}", medicationId), admin.accessToken(), Map.of(
                "name", "Endpoint Med Updated",
                "strength", "20mg",
                "form", "Tablet",
                "stockQuantity", 25,
                "lowStockThreshold", 5), null);
        ok(post("/api/v1/pharmacy/medications/{id}/dispense", medicationId), pharmacist.accessToken(), Map.of("quantity", 2), null);
        ok(delete("/api/v1/pharmacy/medications/{id}", medicationId), admin.accessToken(), null, null);

        JsonNode ward = ok(post("/api/v1/wards"), admin.accessToken(), Map.of("name", "Ward A", "floor", "1"), null).get("data");
        UUID wardId = id(ward);
        ok(get("/api/v1/wards"), admin.accessToken(), null, null);
        ok(get("/api/v1/wards/{id}", wardId), admin.accessToken(), null, null);
        ok(put("/api/v1/wards/{id}", wardId), admin.accessToken(), Map.of("name", "Ward A Updated", "floor", "2"), null);
        JsonNode bed = ok(post("/api/v1/wards/beds"), admin.accessToken(), Map.of(
                "wardId", wardId.toString(),
                "bedNumber", "A-1",
                "status", "AVAILABLE"), null).get("data");
        UUID bedId = id(bed);
        ok(get("/api/v1/wards/beds"), admin.accessToken(), null, null);
        ok(get("/api/v1/wards/beds/{id}", bedId), admin.accessToken(), null, null);
        ok(put("/api/v1/wards/beds/{id}", bedId), admin.accessToken(), Map.of(
                "wardId", wardId.toString(),
                "bedNumber", "A-1",
                "status", "OCCUPIED",
                "patientId", patientId.toString()), null);
        ok(delete("/api/v1/wards/beds/{id}", bedId), admin.accessToken(), null, null);

        ok(get("/api/v1/notifications"), admin.accessToken(), null, null);
        ok(get("/api/v1/audit-logs"), admin.accessToken(), null, null);
        ok(delete("/api/v1/patients/{id}", patientId), admin.accessToken(), null, null);
    }

    private TokenPair register(String email, String role) throws Exception {
        JsonNode data = ok(post("/api/v1/auth/register"), null, Map.of(
                "email", email,
                "password", "StrongPass123",
                "fullName", role + " User",
                "roles", List.of(role)), null).get("data");
        return new TokenPair(data.get("userId").asText(), data.get("accessToken").asText(), data.get("refreshToken").asText());
    }

    private TokenPair login(String email) throws Exception {
        JsonNode data = ok(post("/api/v1/auth/login"), null, Map.of("email", email, "password", "StrongPass123"), null).get("data");
        return new TokenPair(data.get("userId").asText(), data.get("accessToken").asText(), data.get("refreshToken").asText());
    }

    private TokenPair refresh(String refreshToken) throws Exception {
        JsonNode data = ok(post("/api/v1/auth/refresh"), null, Map.of("refreshToken", refreshToken), null).get("data");
        return new TokenPair(data.get("userId").asText(), data.get("accessToken").asText(), data.get("refreshToken").asText());
    }

    private JsonNode ok(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request,
                        String token, Object body, Map<String, String> headers) throws Exception {
        MvcResult result = perform(request, token, body, headers).andExpect(status().isOk()).andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private void assertStatus(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request,
                              String token, int status) throws Exception {
        perform(request, token, null, null).andExpect(status().is(status));
    }

    private org.springframework.test.web.servlet.ResultActions perform(
            org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request,
            String token,
            Object body,
            Map<String, String> headers) throws Exception {
        request.contentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }
        if (headers != null) {
            headers.forEach(request::header);
        }
        if (body != null) {
            request.content(objectMapper.writeValueAsString(body));
        }
        return mockMvc.perform(request);
    }

    private UUID id(JsonNode node) {
        return UUID.fromString(node.get("id").asText());
    }

    private record TokenPair(String userId, String accessToken, String refreshToken) {}
}
