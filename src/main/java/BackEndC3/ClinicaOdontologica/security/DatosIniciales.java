package BackEndC3.ClinicaOdontologica.security;

import BackEndC3.ClinicaOdontologica.entity.*;
import BackEndC3.ClinicaOdontologica.repository.PacienteRepository;
import BackEndC3.ClinicaOdontologica.repository.TurnoRepository;
import BackEndC3.ClinicaOdontologica.repository.UsuarioRepository;
import BackEndC3.ClinicaOdontologica.repository.OdontologoRepository;
import BackEndC3.ClinicaOdontologica.service.OdontologoService;
import BackEndC3.ClinicaOdontologica.service.PacienteService;
import BackEndC3.ClinicaOdontologica.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatosIniciales implements ApplicationRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OdontologoService odontologoService;

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Crear y guardar un administrador
        String passSinCifrar= "user";
        String passCifrado=  passwordEncoder.encode(passSinCifrar);
        Usuario usuario= new Usuario("jorgito","jpereryradh","user@user.com",passCifrado, UsuarioRole.ROLE_USER);
        usuarioRepository.save(usuario);


        // Crear y guardar un administrador
        String passAdminSinCifrar = "admin";
        String passAdminCifrado = passwordEncoder.encode(passAdminSinCifrar);
        Usuario admin = new Usuario("jurany","Ramirez", "admin@admin.com", passAdminCifrado, UsuarioRole.ROLE_ADMIN);
        usuarioRepository.save(admin);
    }
}