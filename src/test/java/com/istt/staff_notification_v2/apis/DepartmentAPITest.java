package com.istt.staff_notification_v2.apis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;


import org.hamcrest.CoreMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.istt.staff_notification_v2.dto.DepartmentDTO;
import com.istt.staff_notification_v2.entity.Department;
import com.istt.staff_notification_v2.entity.User;
import com.istt.staff_notification_v2.repository.UserRepo;
import com.istt.staff_notification_v2.service.AuthService;
import com.istt.staff_notification_v2.service.DepartmentService;
import com.istt.staff_notification_v2.service.UserService;
//@WebMvcTest(DepartmentAPI.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ContextConfiguration(locations = "/staff-notificationv2/src/main/resources/test-context.xml")
@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = DepartmentAPI.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DepartmentAPITest {
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext applicationContext;

    @MockBean
    private DepartmentService departmentService;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private AuthService authService;
    
    @Mock
    private UserRepo userRepo;
    

    @Autowired
    private ObjectMapper objectMapper;
    private Department department;
    private DepartmentDTO departmentDTO;
    private User user;
    
    @Autowired
	AuthenticationManager authenticationManager;
    
    @BeforeAll
    public void init(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        setupUser();
        
    }

    private void setupUser(){
        user = new User();
        user.setStatus("ACTIVE");
		user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
		user.setPassword(new BCryptPasswordEncoder().encode("abcd456789"));
		user.setUsername("admin");
        userRepo.save(user);
        
    }
    
    
    @BeforeEach
    void setup() {
    	department = Department.builder()
				.departmentId("string")
				.departmentName("string").build();
		departmentDTO = DepartmentDTO.builder()
				.departmentId("string")
				.departmentName("string").build();
		
    }
    
    
    @Test
//    @WithMockUser(username ="admin")
    @WithUserDetails("admin")
    public void DepartmentAPI_CreatePokemon_ReturnResponseSTO() throws Exception {
        System.err.println(departmentDTO.toString());
    	given(departmentService.create(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));
        ResultActions response = mockMvc.perform(post("/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(departmentDTO)));
        response
        .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$data.departmentId", 
                		CoreMatchers.is(departmentDTO.getDepartmentId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$data.departmentName", 
                		CoreMatchers.is(departmentDTO.getDepartmentName())));
    }

}
