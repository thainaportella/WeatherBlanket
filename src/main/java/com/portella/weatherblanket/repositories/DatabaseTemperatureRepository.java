package com.portella.weatherblanket.repositories;

import com.portella.weatherblanket.entities.ColorsEnum;
import com.portella.weatherblanket.entities.RegistroDTO;
import com.portella.weatherblanket.entities.TemperaturaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Transactional
public class DatabaseTemperatureRepository implements TemperatureRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void salvar(double temperatura) {

        ColorsEnum corEnum = ColorsEnum.fromTemperatura(temperatura);

        TemperaturaEntity registro = new TemperaturaEntity();
        registro.setData(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        registro.setHora(LocalTime.now(ZoneId.of("America/Sao_Paulo")));
        registro.setTemp_celsius(temperatura);
        registro.setCor(corEnum.name());
        registro.setCor_oficial(corEnum.getNomeOficial());

        em.persist(registro);
    }

    @Override
    public LocalDate buscarUltimaData() {

        TypedQuery<LocalDate> query = em.createQuery(
                "SELECT MAX(t.data) FROM TemperaturaEntity t",
                LocalDate.class
        );

        return query.getSingleResult();
    }



    @Override
    public List<RegistroDTO> listarRegistros(Integer mes, Integer ano, String order, Integer limit) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM TemperaturaEntity t");

        boolean hasWhere = false;

        if (ano != null) {
            jpql.append(" WHERE EXTRACT(YEAR FROM t.data) = :ano");
            hasWhere = true;
        }

        if (mes != null) {
            jpql.append(hasWhere ? " AND " : " WHERE ");
            jpql.append("EXTRACT(MONTH FROM t.data) = :mes");
        }


        jpql.append(" ORDER BY t.data ");
        jpql.append("asc".equalsIgnoreCase(order) ? "ASC" : "DESC");

        TypedQuery<TemperaturaEntity> query = em.createQuery(jpql.toString(), TemperaturaEntity.class);

        if (ano != null) {
            query.setParameter("ano", ano);
        }

        if (mes != null) {
            query.setParameter("mes", mes);
        }


        if (limit != null && limit > 0) {
            query.setMaxResults(limit);
        }

        List<TemperaturaEntity> results = query.getResultList();

        return results.stream()
                .map(t -> new RegistroDTO(
                        t.getData().toString(),
                        t.getHora().format(DateTimeFormatter.ofPattern("HH:mm")),
                        String.valueOf(t.getTemp_celsius()),
                        t.getCor(),
                        t.getCor_oficial()
                ))
                .toList();
    }




}

