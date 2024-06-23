package BackEndC3.ClinicaOdontologica.repository;

import BackEndC3.ClinicaOdontologica.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno,Long> {

}
