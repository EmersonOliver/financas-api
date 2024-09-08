package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.FaturaEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FaturaRepository implements PanacheRepositoryBase<FaturaEntity, Long> {
}
