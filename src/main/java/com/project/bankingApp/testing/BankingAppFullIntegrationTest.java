/** package com.project.bankingApp.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bankingApp.dto.*;
import com.project.bankingApp.entity.EnumforRoles;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankingAppFullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static MockCookie userAccessToken;
    static MockCookie userRefreshToken;
    static MockCookie adminAccessToken;
    static MockCookie adminRefreshToken;

    static long userId;
    static long adminId;

    @Test
    @Order(1)
    void userSignup() throws Exception {
        AccountDto user = new AccountDto();
        user.setAccHolderName("Test User");
        user.setRole(EnumforRoles.USER);
        user.setPassword("password");

        String response = mockMvc.perform(MockMvcRequestBuilders
                .post("/account/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andReturn().getResponse().getContentAsString();

        userId = objectMapper.readValue(response, AccountDto.class).getAccNumber();
    }

    @Test
    @Order(2)
    void adminSignup() throws Exception {
        AccountDto admin = new AccountDto();
        admin.setAccHolderName("Test Admin");
        admin.setRole(EnumforRoles.ADMIN);
        admin.setPassword("adminpass");

        String response = mockMvc.perform(MockMvcRequestBuilders
                .post("/account/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andReturn().getResponse().getContentAsString();

        adminId = objectMapper.readValue(response, AccountDto.class).getAccNumber();
    }

    @Test
    @Order(3)
    void userLogin() throws Exception {
        LoginDto loginDto = new LoginDto(userId, "password");

        var result = mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andReturn();

        userAccessToken = (MockCookie) result.getResponse().getCookie("accessToken");
        userRefreshToken = (MockCookie) result.getResponse().getCookie("refreshToken");

        Assertions.assertNotNull(userAccessToken);
        Assertions.assertNotNull(userRefreshToken);
    }

    @Test
    @Order(4)
    void adminLogin() throws Exception {
        LoginDto loginDto = new LoginDto(adminId, "adminpass");

        var result = mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andReturn();

        adminAccessToken = (MockCookie) result.getResponse().getCookie("accessToken");
        adminRefreshToken = (MockCookie) result.getResponse().getCookie("refreshToken");

        Assertions.assertNotNull(adminAccessToken);
        Assertions.assertNotNull(adminRefreshToken);
    }

    @Test
    @Order(5)
    void testUserGetAccountDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/account/" + userId)
                .cookie(userAccessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @Order(6)
    void testAddMoneyAsUser() throws Exception {
        DWDto dto = new DWDto();
        dto.setAmount(5000);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/transaction/addMoney/" + userId)
                .cookie(userAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(5000));
    }

    @Test
    @Order(7)
    void testRoleEditAsAdmin() throws Exception {
        EditRoledto editDto = new EditRoledto();
        editDto.setId(userId);
        editDto.setRole(EnumforRoles.ADMIN);
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/editRoles")
                .cookie(adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @Order(8)
    void testDeactivateAsAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/deactivateAccount")
                .cookie(adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    @Order(9)
    void testAccessWithInvalidToken() throws Exception {
        MockCookie fakeToken = new MockCookie("accessToken", "invalid.token.string");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/account/" + adminId)
                .cookie(fakeToken))
            .andExpect(status().isForbidden()); // or Unauthorized depending on your filter setup
    }

    @Test
    @Order(10)
    void testValidationError() throws Exception {
        AccountDto invalid = new AccountDto();
        invalid.setAccHolderName(""); // should fail @Valid
        invalid.setRole(EnumforRoles.USER);
        invalid.setPassword("");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/account/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }
}
**/
