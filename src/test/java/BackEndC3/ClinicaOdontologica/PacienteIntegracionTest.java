package BackEndC3.ClinicaOdontologica;

import BackEndC3.ClinicaOdontologica.entity.Domicilio;
import BackEndC3.ClinicaOdontologica.entity.Paciente;
import BackEndC3.ClinicaOdontologica.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PacienteIntegracionTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PacienteService pacienteService;

    @Test
    public void ListarTodosLosPacientesTest() throws Exception{
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra4@digitalhouse.com"));

        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/pacientes").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void testGuardarPaciente() throws Exception {
        Paciente paciente= new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra2@digitalhouse.com");

        MvcResult respuesta= mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String jsonResponse = respuesta.getResponse().getContentAsString();
        Paciente pacienteRespuesta = objectMapper.readValue(jsonResponse, Paciente.class);

        assertThat(pacienteRespuesta.getNombre()).isEqualTo("Jorgito");
        assertThat(pacienteRespuesta.getApellido()).isEqualTo("pereyra");
        assertThat(pacienteRespuesta.getCedula()).isEqualTo("11111");
        assertThat(pacienteRespuesta.getFechaIngreso()).isEqualTo("2024-06-20");
        assertThat(pacienteRespuesta.getDomicilio().getCalle()).isEqualTo("calle falsa");
        assertThat(pacienteRespuesta.getDomicilio().getNumero()).isEqualTo(123);
        assertThat(pacienteRespuesta.getDomicilio().getProvincia()).isEqualTo("Argentina");
        assertThat(pacienteRespuesta.getDomicilio().getLocalidad()).isEqualTo("La Rioja");
        assertThat(pacienteRespuesta.getEmail()).isEqualTo("jorge.pereyra2@digitalhouse.com");
    }

    @Test
    public void testObtenerPacientePorId() throws Exception {
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra3@digitalhouse.com"));

        MvcResult respuestaId = mockMvc.perform(get("/pacientes/buscar/id/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Paciente pacienteRespuestaId = objectMapper.readValue(jsonResponseId, Paciente.class);

        assertThat(pacienteRespuestaId.getNombre()).isEqualTo("Jorgito");
        assertThat(pacienteRespuestaId.getApellido()).isEqualTo("pereyra");
        assertThat(pacienteRespuestaId.getCedula()).isEqualTo("11111");
        assertThat(pacienteRespuestaId.getFechaIngreso()).isEqualTo("2024-06-20");
        assertThat(pacienteRespuestaId.getDomicilio().getCalle()).isEqualTo("calle falsa");
        assertThat(pacienteRespuestaId.getDomicilio().getNumero()).isEqualTo(123);
        assertThat(pacienteRespuestaId.getDomicilio().getProvincia()).isEqualTo("Argentina");
        assertThat(pacienteRespuestaId.getDomicilio().getLocalidad()).isEqualTo("La Rioja");
        assertThat(pacienteRespuestaId.getEmail()).isEqualTo("jorge.pereyra3@digitalhouse.com");
    }

    @Test
    public void testActualizarPaciente() throws Exception {
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra5@digitalhouse.com"));

        // Actualizar el paciente
        paciente.setNombre("Carlos");
        paciente.setApellido("Garcia");

        mockMvc.perform(put("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("paciente actualizado con exito"));

        // Verificar la actualización
        MvcResult respuestaId = mockMvc.perform(get("/pacientes/buscar/id/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Paciente pacienteRespuestaId = objectMapper.readValue(jsonResponseId, Paciente.class);

        assertThat(pacienteRespuestaId.getNombre()).isEqualTo("Carlos");
        assertThat(pacienteRespuestaId.getApellido()).isEqualTo("Garcia");
        assertThat(pacienteRespuestaId.getCedula()).isEqualTo("11111");
        assertThat(pacienteRespuestaId.getFechaIngreso()).isEqualTo("2024-06-20");
        assertThat(pacienteRespuestaId.getDomicilio().getCalle()).isEqualTo("calle falsa");
        assertThat(pacienteRespuestaId.getDomicilio().getNumero()).isEqualTo(123);
        assertThat(pacienteRespuestaId.getDomicilio().getProvincia()).isEqualTo("Argentina");
        assertThat(pacienteRespuestaId.getDomicilio().getLocalidad()).isEqualTo("La Rioja");
        assertThat(pacienteRespuestaId.getEmail()).isEqualTo("jorge.pereyra5@digitalhouse.com");
    }

    @Test
    public void testEliminarPaciente() throws Exception {
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra6@digitalhouse.com"));

        // Eliminar el paciente
        mockMvc.perform(delete("/pacientes/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("paciente eliminado con exito"));

        // Verificar que el paciente ya no existe
        mockMvc.perform(get("/pacientes/buscar/id/" + paciente.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
