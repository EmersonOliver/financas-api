package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.ParcelaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ParcelaRepository implements PanacheRepositoryBase<ParcelaEntity, Long> {
}
