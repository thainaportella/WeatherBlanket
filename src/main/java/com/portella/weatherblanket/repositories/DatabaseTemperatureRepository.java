package com.portella.weatherblanket.repositories;

import com.portella.weatherblanket.entities.enums.ColorsEnum;
import com.portella.weatherblanket.entities.DTOs.RegistroDTO;
import com.portella.weatherblanket.entities.TemperatureEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class DatabaseTemperatureRepository implements TemperatureRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void persistData(double temperature) {

        ColorsEnum color = ColorsEnum.fromTemperature(temperature);

        TemperatureEntity record = new TemperatureEntity();
        record.setDate(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        record.setTime(LocalTime.now(ZoneId.of("America/Sao_Paulo")));
        record.setTemperature(temperature);
        record.setColor(color.name());
        record.setYarn_color(color.getYarnColor());

        em.persist(record);
    }

    @Override
    public LocalDate getLastRecordedDate() {

        TypedQuery<LocalDate> query = em.createQuery(
                "SELECT MAX(t.date) FROM TemperatureEntity t",
                LocalDate.class
        );

        return query.getSingleResult();
    }



    @Override
    public List<RegistroDTO> listRecords(Integer month, Integer year, String order, Integer limit) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM TemperatureEntity t");

        boolean hasWhere = false;

        if (year != null) {
            jpql.append(" WHERE EXTRACT(YEAR FROM t.date) = :year");
            hasWhere = true;
        }

        if (month != null) {
            jpql.append(hasWhere ? " AND " : " WHERE ");
            jpql.append("EXTRACT(MONTH FROM t.date) = :month");
        }


        jpql.append(" ORDER BY t.date ");
        jpql.append("asc".equalsIgnoreCase(order) ? "ASC" : "DESC");

        TypedQuery<TemperatureEntity> query = em.createQuery(jpql.toString(), TemperatureEntity.class);

        if (year != null) {
            query.setParameter("year", year);
        }

        if (month != null) {
            query.setParameter("month", month);
        }


        if (limit != null && limit > 0) {
            query.setMaxResults(limit);
        }

        List<TemperatureEntity> results = query.getResultList();

        return results.stream()
                .map(t -> new RegistroDTO(
                        t.getDate().toString(),
                        t.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        String.valueOf(t.getTemperature()),
                        t.getColor(),
                        t.getYarn_color(),
                        t.getStatus().getValue()
                ))
                .toList();
    }

    public Optional<TemperatureEntity> getRecordByDate(LocalDate date) {
        TypedQuery<TemperatureEntity> query = em.createQuery(
                "SELECT t FROM TemperatureEntity t WHERE t.date = :date",
                TemperatureEntity.class
        );

        query.setParameter("date", date);

        List<TemperatureEntity> result = query.getResultList();

        return result.isEmpty()
                ? Optional.empty()
                : Optional.of(result.get(0));
    }

    @Override
    public void updateStatus(TemperatureEntity record) {
        em.merge(record);
    }
}

