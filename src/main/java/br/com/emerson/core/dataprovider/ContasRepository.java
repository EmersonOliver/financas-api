package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.ContasEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContasRepository implements PanacheRepositoryBase<ContasEntity, Long> {
}
