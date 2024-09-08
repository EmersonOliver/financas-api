package br.com.emerson.core.dataprovider;

import br.com.emerson.core.entity.CarteiraEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarteiraRepository implements PanacheRepositoryBase<CarteiraEntity, Long> {
}
