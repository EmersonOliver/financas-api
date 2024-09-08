package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.ComprasEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ComprasRepository implements PanacheRepositoryBase<ComprasEntity, Long> {

}
