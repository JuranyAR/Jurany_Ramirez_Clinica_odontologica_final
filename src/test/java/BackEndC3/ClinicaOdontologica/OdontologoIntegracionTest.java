package BackEndC3.ClinicaOdontologica;

import BackEndC3.ClinicaOdontologica.entity.Odontologo;
import BackEndC3.ClinicaOdontologica.service.OdontologoService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OdontologoIntegracionTest {

    @Autowired
    private OdontologoService odontologoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void ListarTodosLosOdontologosTest() throws Exception{
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));

        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/odontologos").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

    @Test
    public void testGuardarOdontologo() throws Exception {
        Odontologo odontologo= new Odontologo("MP10","Gina","Arias");

        MvcResult respuesta= mockMvc.perform(post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(odontologo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String jsonResponse = respuesta.getResponse().getContentAsString();
        Odontologo odontologoRespuesta = objectMapper.readValue(jsonResponse, Odontologo.class);

        assertThat(odontologoRespuesta.getNombre()).isEqualTo("Gina");
        assertThat(odontologoRespuesta.getApellido()).isEqualTo("Arias");
        assertThat(odontologoRespuesta.getMatricula()).isEqualTo("MP10");

    }

    @Test
    public void testObtenerOdontologoPorId() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Jurany","Ramirez"));

        MvcResult respuestaId = mockMvc.perform(get("/odontologos/buscar/id/" + odontologo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Odontologo odontologoRespuestaId = objectMapper.readValue(jsonResponseId, Odontologo.class);

        assertThat(odontologoRespuestaId.getNombre()).isEqualTo("Jurany");
        assertThat(odontologoRespuestaId.getApellido()).isEqualTo("Ramirez");
        assertThat(odontologoRespuestaId.getMatricula()).isEqualTo("MP10");
    }

    @Test
    public void testActualizarOdontologo() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Jurany","Ramirez"));

        // Actualizar el odontólogo
        odontologo.setNombre("Carlos");
        odontologo.setApellido("Garcia");

        mockMvc.perform(put("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(odontologo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("odontologo actualizado con exito"));

        // Verificar la actualización
        MvcResult respuestaId = mockMvc.perform(get("/odontologos/buscar/id/" + odontologo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponseId = respuestaId.getResponse().getContentAsString();
        Odontologo odontologoRespuestaId = objectMapper.readValue(jsonResponseId, Odontologo.class);

        assertThat(odontologoRespuestaId.getNombre()).isEqualTo("Carlos");
        assertThat(odontologoRespuestaId.getApellido()).isEqualTo("Garcia");
        assertThat(odontologoRespuestaId.getMatricula()).isEqualTo("MP10");
    }

    @Test
    public void testEliminarOdontologo() throws Exception {
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Jurany","Ramirez"));

        // Eliminar el odontólogo
        mockMvc.perform(delete("/odontologos/" + odontologo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("odontologo eliminado con exito"));

        // Verificar que el odontólogo ya no existe
        mockMvc.perform(get("/odontologos/buscar/id/" + odontologo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
