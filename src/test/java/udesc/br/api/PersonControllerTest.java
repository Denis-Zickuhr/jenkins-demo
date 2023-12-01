package udesc.br.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import udesc.br.api.controller.PersonController;
import udesc.br.api.entity.Person;
import udesc.br.api.repository.PersonRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonController personController;

    @Test
    void getAllPersons() throws Exception {
        when(personRepository.findAll()).thenReturn(Arrays.asList(new Person(), new Person()));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/persons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    void getPersonById() throws Exception {
        Long id = 1L;
        Person person = new Person();
        person.setId(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/persons/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }

    @Test
    void createPerson() throws Exception {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAge(30);

        when(personRepository.save(any(Person.class))).thenReturn(person);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"age\":30}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

//     working test
    @Test
    void updatePerson() throws Exception {
        Long id = 1L;
        Person existingPerson = new Person();
        existingPerson.setId(id);
        existingPerson.setFirstName("OldFirstName");

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("NewFirstName");

        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        mockMvc.perform(MockMvcRequestBuilders.put("/persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"NewFirstName\",\"lastName\":\"Doe\",\"age\":30}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("NewFirstName"));
    }

    // broken test
//    @Test
//    void updatePerson() throws Exception {
//        Long id = 1L;
//        Person existingPerson = new Person();
//        existingPerson.setId(id);
//        existingPerson.setFirstName("OldFirstName");
//
//        Person updatedPerson = new Person();
//        updatedPerson.setFirstName("NewFirstName");
//
//        // Simulate an error when saving the updated person
//        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
//        when(personRepository.save(any(Person.class))).thenThrow(new RuntimeException("Error saving person"));
//
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/persons/{id}", id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"firstName\":\"NewFirstName\",\"lastName\":\"Doe\",\"age\":30}"))
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
//    }

    @Test
    void deletePerson() throws Exception {
        Long id = 1L;

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/persons/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(personRepository, times(1)).deleteById(id);
    }
}
