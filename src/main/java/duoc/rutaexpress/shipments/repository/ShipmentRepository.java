package duoc.rutaexpress.shipments.repository;

import duoc.rutaexpress.shipments.domain.Shipment;
import duoc.rutaexpress.shipments.domain.ShipmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    boolean existsByCodigoSeguimiento(String codigoSeguimiento);

    @Query("""
            select shipment from Shipment shipment
            where (:status is null or shipment.estado = :status)
              and (:from is null or shipment.fechaCreacion >= :from)
              and (:to is null or shipment.fechaCreacion <= :to)
            order by shipment.fechaCreacion desc
            """)
    List<Shipment> findByFilters(@Param("status") ShipmentStatus status,
                                 @Param("from") LocalDateTime from,
                                 @Param("to") LocalDateTime to);
}
