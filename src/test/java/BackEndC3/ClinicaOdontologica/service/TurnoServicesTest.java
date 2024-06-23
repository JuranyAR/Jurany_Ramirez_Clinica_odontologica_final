package BackEndC3.ClinicaOdontologica.service;

import BackEndC3.ClinicaOdontologica.entity.Domicilio;
import BackEndC3.ClinicaOdontologica.entity.Odontologo;
import BackEndC3.ClinicaOdontologica.entity.Paciente;
import BackEndC3.ClinicaOdontologica.entity.Turno;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TurnoServicesTest {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    public void guardarTurno(){
        Odontologo odontologo= new Odontologo("MP10","Gina","Arias");
        odontologoService.guardarOdontologo(odontologo);
        Paciente paciente= new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge1.pereyra@digitalhouse.com");
        pacienteService.guardarPaciente(paciente);
        Turno turno= new Turno(paciente,odontologo,LocalDate.of(2024,6,20));

        Turno turnoGuardado= turnoService.guardarTurno(turno);
        assertEquals(1L,turnoGuardado.getId());
    }

    @Test
    @Order(2)
    public void buscarTurnoPorId(){
        Long id= 1L;
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        assertNotNull(turnoBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarTurno(){
        Long id= 1L;
        Odontologo odontologo= new Odontologo(id,"MP10","Gina","Arias");
        odontologoService.guardarOdontologo(odontologo);
        Paciente paciente= new Paciente(id,"Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge1.pereyra4@digitalhouse.com");
        pacienteService.guardarPaciente(paciente);
        Turno turno= new Turno(id,paciente,odontologo,LocalDate.of(2024,8,30));

        turnoService.actualizarTurno(turno);
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        assertEquals(LocalDate.of(2024, 8, 30), turnoBuscado.get().getFecha());
    }

    @Test
    @Order(4)
    public void ListarTodos(){
        List<Turno> listaTurnos= turnoService.listarTodos();
        assertEquals(1,listaTurnos.size());
    }

    @Test
    @Order(5)
    public void eliminaTurno(){
        turnoService.eliminarTurno(1L);
        Optional<Turno> turnoEliminado= turnoService.buscarPorId(1L);
        assertFalse(turnoEliminado.isPresent());
    }
}
