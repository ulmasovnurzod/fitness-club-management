package uz.codelog.fitnessclubmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.codelog.fitnessclubmanagement.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
