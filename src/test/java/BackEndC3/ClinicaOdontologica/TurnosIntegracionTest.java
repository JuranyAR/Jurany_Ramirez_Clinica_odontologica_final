package BackEndC3.ClinicaOdontologica;

import BackEndC3.ClinicaOdontologica.entity.Domicilio;
import BackEndC3.ClinicaOdontologica.entity.Odontologo;
import BackEndC3.ClinicaOdontologica.entity.Paciente;
import BackEndC3.ClinicaOdontologica.entity.Turno;
import BackEndC3.ClinicaOdontologica.service.OdontologoService;
import BackEndC3.ClinicaOdontologica.service.PacienteService;
import BackEndC3.ClinicaOdontologica.service.TurnoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TurnosIntegracionTest {
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void ListarTodosLosTurnosTest() throws Exception{
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge2.pereyra@digitalhouse.com"));
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Turno turno= turnoService.guardarTurno(new Turno(paciente,odontologo,LocalDate.of(2024,6,20)));

        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/turnos").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }


    @Test
    public void testGuardarTurno() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge1.pereyra@digitalhouse.com"));
        Turno turno= new Turno(paciente,odontologo,LocalDate.of(2024,6,20));


        MvcResult result = mockMvc.perform(post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turno)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Turno turnoRespuesta = objectMapper.readValue(jsonResponse, Turno.class);

        assertThat(turnoRespuesta.getPaciente().getId()).isEqualTo(paciente.getId());
        assertThat(turnoRespuesta.getOdontologo().getId()).isEqualTo(odontologo.getId());
        assertThat(turnoRespuesta.getFecha()).isEqualTo(LocalDate.of(2024, 6, 20));

    }

    @Test
    public void testObtenerTurnoPorId() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra@digitalhouse.com"));
        Turno turno= turnoService.guardarTurno(new Turno(paciente,odontologo,LocalDate.of(2024,6,20)));

        MvcResult respuestaId = mockMvc.perform(get("/turnos/buscar/id/" + turno.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Turno TurnoRespuestaId = objectMapper.readValue(jsonResponseId, Turno.class);

        assertThat(TurnoRespuestaId.getOdontologo().getId()).isEqualTo(odontologo.getId());
        assertThat(TurnoRespuestaId.getPaciente().getId()).isEqualTo(paciente.getId());
        assertThat(TurnoRespuestaId.getFecha()).isEqualTo("2024-06-20");
    }

    @Test
    public void testActualizarTurno() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra3@digitalhouse.com"));
        Turno turno= turnoService.guardarTurno(new Turno(paciente,odontologo,LocalDate.of(2024,6,20)));


        turno.setFecha(LocalDate.of(2024, 7, 21));

        mockMvc.perform(put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turno)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Turno actualizado correctamente"));

        MvcResult respuestaId =  mockMvc.perform(get("/turnos/buscar/id/" + turno.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Turno TurnoRespuestaId = objectMapper.readValue(jsonResponseId, Turno.class);

        assertThat(TurnoRespuestaId.getOdontologo().getId()).isEqualTo(odontologo.getId());
        assertThat(TurnoRespuestaId.getPaciente().getId()).isEqualTo(paciente.getId());
        assertThat(TurnoRespuestaId.getFecha()).isEqualTo("2024-07-21");
    }

    @Test
    public void testEliminarTurno() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra4@digitalhouse.com"));
        Turno turno= turnoService.guardarTurno(new Turno(paciente,odontologo,LocalDate.of(2024,6,20)));


        mockMvc.perform(delete("/turnos/" + turno.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Turno eliminado correctamente"));

        // Verificar que el turno ya no existe
        mockMvc.perform(get("/turnos/buscar/id/" + turno.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
