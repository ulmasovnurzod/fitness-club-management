package uz.codelog.fitnessclubmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.codelog.fitnessclubmanagement.entity.Device;
import uz.codelog.fitnessclubmanagement.entity.User;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceIdAndUser(String deviceId, User user);
}
